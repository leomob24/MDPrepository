package it.unicam.cs.mpgc.rpg125579.model.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.Random;

/**
 * Entità che rappresenta un Minion nel gioco.
 * Eredita da Character e aggiunge la generazione casuale dei parametri.
 */
@Entity
@DiscriminatorValue("MINION")
@Getter
@Setter
@NoArgsConstructor
public class Minion extends Mostro {

    /**
     * Pool di nomi per il Minion da creare
     */
    private static final String[] MINION_NAMES = {
            "The Tickler", "Justin Case", "Barry Cades", "Friendly-Fire Frank",
            "The Distraction", "Red Shirt Rick", "Poison-Taster Paul", "Glow-in-the-Dark Greg"
    };

    /**
     * Costruttore per la classe Minion. È privato perché l'unico modo per costruire un oggetto Minion è tramite il metodo statico generateMinion().
     * @param name Nome assegnato
     * @param atk Punti attacco assegnati
     * @param def Punti difesa assegnati
     * @param hp Punti vita assegnati
     * @param bonusAtk Punti bonus per l'attacco
     * @param owner Supereroe proprietario del minion
     */
    private Minion(String name, int atk, int def, int hp, int bonusAtk, Superhero owner) {
        super(name, atk, def, hp, bonusAtk, owner);
    }
    /**
     * Metodo statico per la creazione di un nuovo Minion Random
     * @return Minion
     */
    public static Minion generateMinion(Superhero owner) {
        Random random = new Random();
        String name = MINION_NAMES[random.nextInt(MINION_NAMES.length)];
        int atk = random.nextInt(8) + 8;        // 8-15
        int def = random.nextInt(4) + 4;        // 4-7
        int hp = random.nextInt(30) + 15;       // 15-44
        int bonusAtk = random.nextInt(3);       // 0-2
        return new Minion(name, atk, def, hp, bonusAtk, owner);
    }
}
