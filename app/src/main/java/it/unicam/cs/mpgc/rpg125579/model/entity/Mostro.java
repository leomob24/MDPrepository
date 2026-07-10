package it.unicam.cs.mpgc.rpg125579.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe astratta condivisa da {@link Villain} e {@link Minion}.
 * <p>
 * Entrambe le sottoclassi concrete hanno
 * bisogno dello stesso campo: il riferimento al {@link Superhero} "proprietario"
 * della partita a cui il nemico appartiene. Questo permette di sapere, quando
 * si riprende una partita, quali nemici sono associati a quale eroe.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public abstract class Mostro extends Character {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Superhero owner;

    protected Mostro(String name, int atk, int def, int hp, int bonusAtk, Superhero owner) {
        super(name, atk, def, hp, bonusAtk);
        this.owner = owner;
    }
}
