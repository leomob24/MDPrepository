package it.unicam.cs.mpgc.rpg125579.model;

import lombok.*;

@Getter
@NoArgsConstructor
public abstract class Character {

    private String name;
    private int atk;
    private int hp;
    private float damageMultiplier;
    private int bonusAtk;

    protected Character(String name, int atk, int hp, float damageMultiplier, int bonusAtk) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
        this.atk = atk;
        this.hp = hp;
        this.damageMultiplier = damageMultiplier;
        this.bonusAtk = bonusAtk;
    }

    void setHp(int hp) {
        this.hp = Math.max(hp, 0);
    }
}