package it.unicam.cs.mpgc.rpg125579.model.entity;

import lombok.*;
import java.util.Random;

@Getter
@NoArgsConstructor
public class Minion extends Character {

    /**
     * Pool di nomi per il Minion da creare
     */
    private static final String[] MINION_NAMES = {
            "The Tickler", "Justin Case", "Barry Cades", "Friendly-Fire Frank",
            "The Distraction", "Red Shirt Rick", "Poison-Taster Paul", "Glow-in-the-Dark Greg"
    };
    /**
     * Costruttore per la classe Minion. E' privato perchè l'unico modo per costruire un oggetto Minion è tramite il metodo statico generateMinion().
     * @param name Nome assegnato
     * @param atk Punti attacco assegnati
     * @param hp Punti vita assegnati
     */
    private Minion(String name, int atk, int hp) {
        super(name, atk, hp, 1.0f, 0);
    }

    /**
     * Metodo statico per la creazione di un nuovo Minion Random
     * @return Minion
     */
    public static Minion generateMinion() {
        Random random = new Random();
        String name = MINION_NAMES[random.nextInt(MINION_NAMES.length)];
        int atk = random.nextInt(10) + 10;
        int hp = random.nextInt(50) + 20;
        return new Minion(name, atk, hp);
    }
}
