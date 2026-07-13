package it.unicam.cs.mpgc.rpg125579.model.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.Random;

@Entity
@DiscriminatorValue("MINION")
@Getter
@Setter
@NoArgsConstructor
public class Minion extends Mostro {

    /**
     * Nomi predefiniti per i Minion generati casualmente.
     */
    private static final String[] MINION_NAMES = {
            "The Tickler", "Justin Case", "Barry Cades", "Friendly-Fire Frank",
            "The Distraction", "Red Shirt Rick", "Poison-Taster Paul", "Glow-in-the-Dark Greg"
    };

    /**
     * Costruttore privato per la creazione di un Minion.
     *
     * @param name Nome del minion
     * @param atk Punti di attacco
     * @param def Punti di difesa
     * @param hp Punti vita massimi
     * @param bonusAtk Bonus di attacco
     * @param owner Partita proprietaria del minion
     */
    private Minion(String name, int atk, int def, int hp, int bonusAtk, Partita owner) {
        super(name, atk, def, hp, bonusAtk, owner);
    }

    /**
     * Genera un Minion scalato in base all'ondata attuale della partita.
     * Più debole di un Villain, ma rappresenta una minaccia scalata.
     *
     * @param partita La partita corrente
     * @return Un nuovo Minion con statistiche scalate secondo l'ondata attuale
     */
    public static Minion generateMinion(Partita partita) {
        Random random = new Random();
        String name = MINION_NAMES[random.nextInt(MINION_NAMES.length)];
        int scala = partita.getOndataAttuale() - 1;
        int atk = random.nextInt(8) + 8 + (scala * 2);
        int def = random.nextInt(4) + 4 + scala;
        int hp = random.nextInt(30) + 15 + (scala * 8);
        int bonusAtk = random.nextInt(3) + scala;
        return new Minion(name, atk, def, hp, bonusAtk, partita);
    }
}