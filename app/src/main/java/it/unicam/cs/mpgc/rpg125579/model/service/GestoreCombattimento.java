package it.unicam.cs.mpgc.rpg125579.model.service;

import it.unicam.cs.mpgc.rpg125579.model.entity.Character;

import java.util.Random;

/**
 * Gestisce il calcolo del danno durante i combattimenti.
 * Implementa la logica di attacco base e bonus, aplicando la probabilità di critico.
 * <p>
 * Responsabilità singola: questa classe calcola solo il danno.
 * La gestione completa della battaglia (turni, cooldown, esperienza) è delegata a {@link GestoreBattaglia}.
 */
public class GestoreCombattimento {

    /**
     * Probabilità percentuale di applicare il bonus attack (12%).
     */
    private static final int BONUS_CHANCE = 12;

    /**
     * Danno minimo garantito anche se la difesa supera l'attacco.
     */
    private static final int DANNO_MINIMO = 1;

    /**
     * Generatore casuale per le probabilità di bonus attack.
     */
    private static final Random random = new Random();

    /**
     * Esegue un attacco da un attaccante verso un difensore.
     * Calcola il danno e lo applica direttamente al difensore.
     *
     * @param attaccante Il personaggio che attacca
     * @param difensore Il personaggio che subisce l'attacco
     */
    public void eseguiAttacco(Character attaccante, Character difensore) {
        int danno = calcolaDanno(attaccante, difensore);
        int nuoviHp = difensore.getHpAttuali() - danno;
        difensore.setHpAttuali(nuoviHp);
    }

    /**
     * Calcola il danno infligibile da un attaccante verso un difensore.
     * Applica il bonus di attacco con probabilità del 12%.
     * Garantisce un danno minimo di 1, anche se la difesa supera l'attacco base.
     *
     * @param attaccante Il personaggio che attacca
     * @param difensore Il personaggio che subisce l'attacco
     * @return Il danno totale da infliggere
     */
    private int calcolaDanno(Character attaccante, Character difensore) {
        int atkBase = attaccante.getAtk();

        // Applica il bonus attack con il 12% di probabilità
        if (random.nextInt(100) < BONUS_CHANCE) {
            atkBase += attaccante.getBonusAtk();
        }

        return Math.max(atkBase - difensore.getDef(), DANNO_MINIMO);
    }
}