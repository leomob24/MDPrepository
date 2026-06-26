package it.unicam.cs.mpgc.rpg125579.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class Superpower {
    private String powerName;
    private int atk;
    private int hp;
    private float damageMultiplier;

    public Superpower(String powerName, int atk, int hp, float damageMultiplier){
        if (powerName == null || powerName.isEmpty()) {
            throw new IllegalArgumentException("Power name cannot be null or empty");
        }
        if (atk < 0) {
            throw new IllegalArgumentException("Attack value cannot be negative");
        }
        if (hp < 0) {
            throw new IllegalArgumentException("Health value cannot be negative");
        }
        if (damageMultiplier < 0) {
            throw new IllegalArgumentException("Damage multiplier cannot be negative");
        }
        this.powerName = powerName;
        this.atk = atk;
        this.hp = hp;
        this.damageMultiplier = damageMultiplier;
    }
}
