package it.unicam.cs.mpgc.rpg125579.model.service;

import it.unicam.cs.mpgc.rpg125579.model.entity.Character;
import it.unicam.cs.mpgc.rpg125579.model.entity.Superhero;

/**
 * Gestisce la progressione di livello del {@link Superhero}: calcolo dell'esperienza
 * ottenuta sconfiggendo un nemico e applicazione del level-up quando la soglia
 * richiesta viene raggiunta o superata (anche con più livelli in un colpo solo).
 * <p>
 * Isolata da {@link GestoreCombattimento} per rispettare la singola responsabilità:
 * uno calcola il danno, l'altro la progressione del personaggio.
 */
public class GestoreLivelli {

    private static final int XP_BASE = 50;
    private static final int XP_INCREMENTO_PER_LIVELLO = 25;

    private static final int ATK_PER_LIVELLO = 3;
    private static final int DEF_PER_LIVELLO = 2;
    private static final int HP_PER_LIVELLO = 15;
    private static final int BONUS_ATK_OGNI_DUE_LIVELLI = 1;

    /**
     * Calcola l'esperienza ottenuta sconfiggendo un nemico, proporzionale
     * alla sua potenza complessiva. Un Villain forte rende quindi più
     * esperienza dei Minion, incentivando strategie diverse.
     *
     * @param nemicoSconfitto Il nemico da cui calcolare l'XP
     * @return L'esperienza ottenuta dalla sconfitta
     */
    public int calcolaEsperienzaOttenuta(Character nemicoSconfitto) {
        return nemicoSconfitto.getAtk()
                + nemicoSconfitto.getDef()
                + (nemicoSconfitto.getHp() / 5)
                + (nemicoSconfitto.getBonusAtk() * 2);
    }

    /**
     * Assegna l'esperienza guadagnata all'eroe, applicando eventuali
     * level-up (anche multipli) in cascata fino a che l'eroe non ha
     * esperienza sufficiente per il prossimo livello.
     *
     * @param hero L'eroe a cui assegnare l'esperienza
     * @param xpOttenuta L'ammontare di XP da assegnare
     * @return Numero di livelli guadagnati con questa assegnazione (0 se nessuno)
     */
    public int assegnaEsperienza(Superhero hero, int xpOttenuta) {
        hero.setEsperienza(hero.getEsperienza() + xpOttenuta);
        int livelliGuadagnati = 0;

        while (hero.getEsperienza() >= esperienzaRichiesta(hero.getLivello())) {
            levelUp(hero);
            livelliGuadagnati++;
        }
        return livelliGuadagnati;
    }

    /**
     * Restituisce l'esperienza totale necessaria per passare dal livello
     * indicato al livello successivo (incrementale, non cumulativa).
     *
     * @param livelloAttuale Il livello corrente
     * @return L'esperienza richiesta per raggiungere il prossimo livello
     */
    public int esperienzaRichiesta(int livelloAttuale) {
        return XP_BASE + (livelloAttuale - 1) * XP_INCREMENTO_PER_LIVELLO;
    }

    /**
     * Esegue un level-up sull'eroe: decrementa l'XP, incrementa il livello,
     * e applica i bonus statistici corrispondenti.
     * Nota: HP attuali NON vengono ripristinati per mantenere il rischio.
     *
     * @param hero L'eroe da potenziare
     */
    private void levelUp(Superhero hero) {
        int xpRichiesta = esperienzaRichiesta(hero.getLivello());
        hero.setEsperienza(hero.getEsperienza() - xpRichiesta);
        hero.setLivello(hero.getLivello() + 1);

        hero.setAtk(hero.getAtk() + ATK_PER_LIVELLO);
        hero.setDef(hero.getDef() + DEF_PER_LIVELLO);
        hero.setHp(hero.getHp() + HP_PER_LIVELLO);
        if (hero.getLivello() % 2 == 0) {
            hero.setBonusAtk(hero.getBonusAtk() + BONUS_ATK_OGNI_DUE_LIVELLI);
        }
        // Il level-up aumenta le statistiche massime (compreso l'HP massimo),
        // ma NON ripristina la vita attuale: l'eroe resta ferito quanto era
        // prima della vittoria, per mantenere un minimo di rischio anche
        // dopo un level-up.
    }
}