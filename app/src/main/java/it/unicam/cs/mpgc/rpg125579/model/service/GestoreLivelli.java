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
     */
    public int calcolaEsperienzaOttenuta(Character nemicoSconfitto) {
        return nemicoSconfitto.getAtk()
                + nemicoSconfitto.getDef()
                + (nemicoSconfitto.getHp() / 5)
                + (nemicoSconfitto.getBonusAtk() * 2);
    }

    /**
     * Assegna l'esperienza guadagnata all'eroe, applicando eventuali
     * level-up (anche multipli) in cascata.
     *
     * @return numero di livelli guadagnati con questa assegnazione (0 se nessuno)
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
     * indicato al successivo.
     */
    public int esperienzaRichiesta(int livelloAttuale) {
        return XP_BASE + (livelloAttuale - 1) * XP_INCREMENTO_PER_LIVELLO;
    }

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