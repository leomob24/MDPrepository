package it.unicam.cs.mpgc.rpg125579.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe astratta condivisa da {@link Villain} e {@link Minion}.
 * Entrambe le sottoclassi hanno bisogno dello stesso campo: il riferimento
 * alla {@link Partita} a cui il nemico appartiene, per sapere quali nemici
 * sono associati a quale sessione di gioco.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public abstract class Mostro extends Character {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partita_id")
    private Partita owner;

    protected Mostro(String name, int atk, int def, int hp, int bonusAtk, Partita owner) {
        super(name, atk, def, hp, bonusAtk);
        this.owner = owner;
    }
}