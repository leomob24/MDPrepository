package it.unicam.cs.mpgc.rpg125579.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Superhero {
    private String name;
    private Superpower superpower;
    private int atk;
    private int hp;
    private int damageMultiplier;

    public Superhero(String name, Superpower superpower) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (superpower == null) {
            throw new IllegalArgumentException("Superpower cannot be null");
        }
        this.name = name;
        this.superpower = superpower;
        this.atk = superpower.getAtk();
        this.hp = superpower.getHp();
        this.damageMultiplier = superpower.getDamageMultiplier();
    }
}
