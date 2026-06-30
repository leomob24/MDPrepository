package it.unicam.cs.mpgc.rpg125579.model.entity;

import lombok.*;

@Getter
@NoArgsConstructor
public abstract class Character {

    private String name;
    private int atk;
    private int def;
    private int hp;
    private int hpAttuali;
    private int bonusAtk;

    protected Character(String name, int atk, int def, int hp, int bonusAtk) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
        this.atk = atk;
        this.def = def;
        this.hp = hp;
        this.hpAttuali = hp;
        this.bonusAtk = bonusAtk;
    }


    public void setHpAttuali(int hpAttuali) {
        this.hpAttuali = Math.max(hpAttuali, 0);
    }
}