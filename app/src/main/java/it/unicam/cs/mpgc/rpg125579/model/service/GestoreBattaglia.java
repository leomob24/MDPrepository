package it.unicam.cs.mpgc.rpg125579.model.service;

import it.unicam.cs.mpgc.rpg125579.model.entity.Battaglia;
import it.unicam.cs.mpgc.rpg125579.model.entity.Character;
import it.unicam.cs.mpgc.rpg125579.model.entity.Superhero;

/**
 * Applica le regole di un combattimento (cooldown cura, tetto massimo cure,
 * bonus difesa, contrattacco) a una {@link Battaglia} esistente, orchestrando
 * {@link GestoreCombattimento} (danno) e {@link GestoreLivelli} (progressione).
 * <p>
 * {@code hero} ed {@code enemy} vengono passati esplicitamente ad ogni metodo
 * invece di essere derivati da {@code Battaglia} (che li referenzia solo
 * tramite relazioni JPA lazy): il chiamante li possiede già come entità
 * pienamente caricate, evitando così di dover inizializzare proxy Hibernate
 * fuori da una sessione attiva.
 */
public class GestoreBattaglia {

    public static final int HEAL_AMOUNT = 12;
    public static final int HEAL_COOLDOWN_TURNS = 3;
    public static final int MAX_CURE_PER_BATTLE = 3;
    private static final double DEFEND_BONUS_MULTIPLIER = 1.5;

    private final GestoreCombattimento gestoreCombattimento = new GestoreCombattimento();
    private final GestoreLivelli gestoreLivelli = new GestoreLivelli();

    /**
     * Verifica se una cura è disponibile per il turno corrente della battaglia.
     * Una cura è disponibile se: il numero di cure usate è inferiore al massimo
     * AND il cooldown (turnsSinceHeal) è trascorso.
     *
     * @param battaglia La battaglia in corso
     * @return true se una cura può essere usata, false altrimenti
     */
    public boolean isCuraDisponibile(Battaglia battaglia) {
        return battaglia.getCureUsate() < MAX_CURE_PER_BATTLE
                && battaglia.getTurnsSinceHeal() >= HEAL_COOLDOWN_TURNS;
    }

    /**
     * Restituisce il numero di cure rimanenti disponibili in questa battaglia.
     *
     * @param battaglia La battaglia in corso
     * @return Il numero di cure ancora utilizzabili
     */
    public int getCureRimaste(Battaglia battaglia) {
        return MAX_CURE_PER_BATTLE - battaglia.getCureUsate();
    }

    /**
     * Restituisce l'esperienza totale richiesta per passare dal livello attuale al successivo.
     *
     * @param hero L'eroe di cui calcolare l'esperienza richiesta
     * @return Il totale di XP necessario per il livello successivo
     */
    public int esperienzaRichiesta(Superhero hero) {
        return gestoreLivelli.esperienzaRichiesta(hero.getLivello());
    }

    /**
     * Esegue un attacco dell'eroe verso il nemico, gestendo il turno successivo del nemico.
     * Se il nemico viene sconfitto, la battaglia termina con vittoria dell'eroe.
     * Altrimenti, il nemico contrattacca.
     *
     * @param battaglia La battaglia in corso
     * @param hero L'eroe attaccante
     * @param enemy Il nemico difensore
     * @return RisultatoAttacco indicante se il nemico è stato sconfitto o l'eroe
     */
    public RisultatoAttacco eseguiAttacco(Battaglia battaglia, Superhero hero, Character enemy) {
        gestoreCombattimento.eseguiAttacco(hero, enemy);
        if (enemy.getHpAttuali() <= 0) {
            return new RisultatoAttacco(true, false);
        }

        boolean eroeSconfitto = eseguiTurnoNemico(battaglia, hero, enemy);
        return new RisultatoAttacco(false, eroeSconfitto);
    }

    /**
     * Esegue una difesa dell'eroe (aumenta temporaneamente la difesa del 50%),
     * seguito dal turno di contrattacco del nemico.
     *
     * @param battaglia La battaglia in corso
     * @param hero L'eroe che si difende
     * @param enemy Il nemico che contrattacca
     * @return RisultatoAttacco indicante il risultato del contrattacco
     */
    public RisultatoAttacco eseguiDifesa(Battaglia battaglia, Superhero hero, Character enemy) {
        int defOriginale = hero.getDef();
        hero.setDef((int) Math.round(defOriginale * DEFEND_BONUS_MULTIPLIER));

        boolean eroeSconfitto = eseguiTurnoNemico(battaglia, hero, enemy);

        hero.setDef(defOriginale);
        return new RisultatoAttacco(false, eroeSconfitto);
    }

    /**
     * Applica una cura all'eroe se disponibile, seguita dal turno di contrattacco del nemico.
     * Se la cura non è disponibile, restituisce il motivo del rifiuto.
     *
     * @param battaglia La battaglia in corso
     * @param hero L'eroe da curare
     * @param enemy Il nemico che potrebbe contrattaccare
     * @return RisultatoCura con dettagli del risultato (accettata/rifiutata, HP recuperati, ecc.)
     */
    public RisultatoCura eseguiCura(Battaglia battaglia, Superhero hero, Character enemy) {
        if (!isCuraDisponibile(battaglia)) {
            String motivo = battaglia.getCureUsate() >= MAX_CURE_PER_BATTLE
                    ? "Hai già usato tutte le cure disponibili per questo combattimento."
                    : "Cura non ancora disponibile.";
            return RisultatoCura.rifiutata(motivo);
        }

        int nuoviHp = Math.min(hero.getHpAttuali() + (HEAL_AMOUNT*hero.getHp())/100, hero.getHp());
        int recuperati = nuoviHp - hero.getHpAttuali();
        hero.setHpAttuali(nuoviHp);
        battaglia.setTurnsSinceHeal(0);
        battaglia.setCureUsate(battaglia.getCureUsate() + 1);

        boolean eroeSconfitto = eseguiTurnoNemico(battaglia, hero, enemy);
        return RisultatoCura.eseguita(recuperati, getCureRimaste(battaglia), eroeSconfitto);
    }

    /**
     * Assegna l'esperienza ottenuta dalla vittoria e calcola eventuali level-up.
     * Delega a {@link GestoreLivelli} il calcolo dell'esperienza e del level-up.
     *
     * @param hero L'eroe che ha vinto
     * @param enemy Il nemico sconfitto
     * @return RisultatoVittoria contenente XP ottenuta e livelli guadagnati
     */
    public RisultatoVittoria assegnaEsperienzaVittoria(Superhero hero, Character enemy) {
        int xpOttenuta = gestoreLivelli.calcolaEsperienzaOttenuta(enemy);
        int livelliGuadagnati = gestoreLivelli.assegnaEsperienza(hero, xpOttenuta);
        return new RisultatoVittoria(xpOttenuta, livelliGuadagnati);
    }

    /**
     * Esegue il turno di combattimento del nemico (contrattacco).
     * Incrementa il cooldown delle cure e applica il danno del nemico all'eroe.
     *
     * @param battaglia La battaglia in corso
     * @param hero L'eroe che subisce il contrattacco
     * @param enemy Il nemico che contrattacca
     * @return true se l'eroe è stato sconfitto, false altrimenti
     */
    private boolean eseguiTurnoNemico(Battaglia battaglia, Superhero hero, Character enemy) {
        battaglia.setTurnsSinceHeal(battaglia.getTurnsSinceHeal() + 1);
        gestoreCombattimento.eseguiAttacco(enemy, hero);
        return hero.getHpAttuali() <= 0;
    }
}