package it.unicam.cs.mpgc.rpg125579.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.Random;

@Entity
@DiscriminatorValue("VILLAIN")
@Getter
@Setter
@NoArgsConstructor
public class Villain extends Mostro {

    /**
     * Nomi predefiniti per i Villain generati casualmente.
     */
    private static final String[] VILLAIN_NAMES = {
            "Doctor Bad Mood", "Galactose Intolerant", "Doc Hawk", "Low-Key",
            "Choko Kraven", "Rhinoplasty", "K.O.M.O.D.O.", "Bronze Skater"
    };

    /**
     * Costruttore privato per la creazione di un Villain.
     *
     * @param name Nome del villain
     * @param atk Punti di attacco
     * @param hp Punti vita massimi
     * @param def Punti di difesa
     * @param bonusAtk Bonus di attacco
     * @param owner Partita proprietaria del villain
     */
    private Villain(String name, int atk, int hp, int def, int bonusAtk, Partita owner) {
        super(name, atk, def, hp, bonusAtk, owner);
    }

    /**
     * Genera un Villain scalato in base all'ondata attuale della partita.
     * La difficoltà aumenta con il numero di ondate superate (statistiche più alte).
     *
     * @param partita La partita corrente
     * @return Un nuovo Villain con statistiche scalate secondo l'ondata attuale
     */
    public static Villain generateVillain(Partita partita) {
        Random random = new Random();
        String name = VILLAIN_NAMES[random.nextInt(VILLAIN_NAMES.length)];
        int scala = partita.getOndataAttuale() - 1;
        int atk = random.nextInt(15) + 20 + (scala * 4);
        int hp = random.nextInt(60) + 80 + (scala * 20);
        int def = random.nextInt(8) + 8 + (scala * 2);
        int bonusAtk = random.nextInt(4) + 2 + scala;
        return new Villain(name, atk, hp, def, bonusAtk, partita);
    }
}