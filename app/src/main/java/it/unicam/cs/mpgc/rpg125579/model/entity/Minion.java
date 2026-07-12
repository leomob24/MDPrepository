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

    private static final String[] MINION_NAMES = {
            "The Tickler", "Justin Case", "Barry Cades", "Friendly-Fire Frank",
            "The Distraction", "Red Shirt Rick", "Poison-Taster Paul", "Glow-in-the-Dark Greg"
    };

    private Minion(String name, int atk, int def, int hp, int bonusAtk, Partita owner) {
        super(name, atk, def, hp, bonusAtk, owner);
    }

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