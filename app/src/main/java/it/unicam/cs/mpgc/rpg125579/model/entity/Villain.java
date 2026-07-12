package it.unicam.cs.mpgc.rpg125579.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.Random;

/**
 * Entità che rappresenta un antagonista (Villain) nel gioco.
 * Eredita da Character e aggiunge la generazione casuale dei parametri,
 * oltre al riferimento al {@link Superhero} "proprietario".
 */
@Entity
@DiscriminatorValue("VILLAIN")
@Getter
@Setter
@NoArgsConstructor
public class Villain extends Mostro {

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
     * @param def Punti difesa assegnati
     * @param bonusAtk Punti bonus per l'attacco
     * @param owner Supereroe proprietario del villain
     */
    private Villain(String name, int atk, int hp, int def, int bonusAtk, Superhero owner) {
        super(name, atk, def, hp, bonusAtk, owner);
    }
    /**
     * Metodo statico per la creazione di un nuovo Villain Random
     * @return Villain
     */
    public static Villain generateVillain(Superhero owner) {
        Random random = new Random();
        String name = VILLAIN_NAMES[random.nextInt(VILLAIN_NAMES.length)];
        int atk = random.nextInt(15) + 20;      // 20-34
        int hp = random.nextInt(60) + 80;       // 80-139
        int def = random.nextInt(8) + 8;        // 8-15
        int bonusAtk = random.nextInt(4) + 2;   // 2-5
        return new Villain(name, atk, hp, def, bonusAtk, owner);
    }
}