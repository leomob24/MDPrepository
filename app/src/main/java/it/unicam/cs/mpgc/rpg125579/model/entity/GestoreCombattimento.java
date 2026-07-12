package it.unicam.cs.mpgc.rpg125579.model.entity;

import java.util.Random;

public class GestoreCombattimento {

    private static final Random random = new Random();
    private static final int BONUS_CHANCE = 20; // 20% di probabilità
    private static final int DANNO_MINIMO = 1; // Danno minimo garantito

    public void eseguiAttacco(Character attaccante, Character difensore) {
        int danno = calcolaDanno(attaccante, difensore);
        int nuoviHp = difensore.getHpAttuali() - danno;
        difensore.setHpAttuali(nuoviHp);
    }

    private int calcolaDanno(Character a, Character d) {
        int atkBase = a.getAtk();

        // Applica il bonus attack con il 20% di probabilità
        if (random.nextInt(100) < BONUS_CHANCE) {
            atkBase += a.getBonusAtk();
        }
        
        return Math.max(atkBase - d.getDef(), DANNO_MINIMO);
    }
}