package it.unicam.cs.mpgc.rpg125579.model.power;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class Superpower {
    private String powerName;
    private int atk;
    private int def;
    private int hp;
    private int bonusAtk;
    private String description;

    public Superpower(String powerName, int atk, int def, int hp, int bonusAtk, String description){
        if (powerName == null || powerName.isEmpty()) {
            throw new IllegalArgumentException("Power name cannot be null or empty");
        }
        if (atk < 0) {
            throw new IllegalArgumentException("Attack value cannot be negative");
        }
        if (def < 0) {
            throw new IllegalArgumentException("Defense value cannot be negative");
        }
        if (hp < 0) {
            throw new IllegalArgumentException("Health value cannot be negative");
        }
        if (bonusAtk < 0) {
            throw new IllegalArgumentException("Bonus attack value cannot be negative");
        }
        this.powerName = powerName;
        this.atk = atk;
        this.def = def;
        this.hp = hp;
        this.bonusAtk = bonusAtk;
        this.description = description;
    }
}
