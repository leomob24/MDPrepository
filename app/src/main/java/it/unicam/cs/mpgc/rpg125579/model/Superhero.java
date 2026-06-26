package it.unicam.cs.mpgc.rpg125579.model;

import lombok.*;

@Getter
@NoArgsConstructor
public class Superhero extends Character {

    private Superpower superpower;

    public Superhero(String name, Superpower superpower) {
        super(name, validate(superpower).getAtk(), superpower.getHp(),
                superpower.getDamageMultiplier(), superpower.getBonusAtk());
        this.superpower = superpower;
    }

    private static Superpower validate(Superpower superpower) {
        if (superpower == null) {
            throw new IllegalArgumentException("Superpower cannot be null");
        }
        return superpower;
    }
}
