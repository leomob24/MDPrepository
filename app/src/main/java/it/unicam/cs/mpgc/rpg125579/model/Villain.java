package it.unicam.cs.mpgc.rpg125579.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Random;

@Getter
@NoArgsConstructor
public class Villain extends Character {

    /**
     * Pool di nomi per il Villain da creare
     */
    private static final String[] VILLAIN_NAMES = {
            "Doctor Bad Mood", "Galactose Intolerant", "Doc Hawk", "Low-Key",
            "Choko Kraven", "Rhinoplasty", "K.O.M.O.D.O.", "Bronze Skater"
    };

    /**
     * Costruttore per la classe villain. E' privato perchè l'unico modo per costruire un oggetto Villain è tramite il metodo statico generateVillain().
     * @param name Nome assegnato
     * @param atk Punti attacco assegnati
     * @param hp Punti vita assegnati
     * @param damageMultiplier Moltiplicatore di danni
     * @param bonusAtk Punti bonus per l'attacco
     */
    private Villain(String name, int atk, int hp, float damageMultiplier, int bonusAtk) {
        super(name, atk, hp, damageMultiplier, bonusAtk);
    }

    /**
     * Metodo statico per la creazione di un nuovo Villain Random
     * @return Villain
     */
    public static Villain generateVillain() {
        Random random = new Random();
        String name = VILLAIN_NAMES[random.nextInt(VILLAIN_NAMES.length)];
        int atk = random.nextInt(20) + 30;
        int hp = random.nextInt(100) + 100;
        float damageMultiplier = (random.nextInt(10) + 11) / 10.0f;
        int bonusAtk = random.nextInt(5) + 1;
        return new Villain(name, atk, hp, damageMultiplier, bonusAtk);
    }
}