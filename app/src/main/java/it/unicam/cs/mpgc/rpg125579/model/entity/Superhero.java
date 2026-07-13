package it.unicam.cs.mpgc.rpg125579.model.entity;

import it.unicam.cs.mpgc.rpg125579.model.power.Superpower;
import lombok.*;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@DiscriminatorValue("SUPERHERO")
@Getter
@Setter
@NoArgsConstructor
public class Superhero extends Character implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Superpotere associato a questo eroe. Determina i bonus statistici iniziali.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "superpower_id", referencedColumnName = "id")
    private Superpower superpower;

    /**
     * Livello attuale dell'eroe. Parte da 1 e cresce sconfiggendo nemici.
     */
    private int livello;

    /**
     * Punti esperienza accumulati nel livello corrente. Gestito da {@link it.unicam.cs.mpgc.rpg125579.model.service.GestoreLivelli}.
     */
    private int esperienza;


    public Superhero(String name, Superpower superpower) {
        super(name, validate(superpower).getAtk(), superpower.getDef(), superpower.getHp(), superpower.getBonusAtk());
        this.superpower = superpower;
        this.livello = 1;
        this.esperienza = 0;
    }

    private static Superpower validate(Superpower superpower) {
        if (superpower == null) {
            throw new IllegalArgumentException("Superpower cannot be null");
        }
        return superpower;
    }
}