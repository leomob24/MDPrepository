package it.unicam.cs.mpgc.rpg125579.model.entity;

import it.unicam.cs.mpgc.rpg125579.model.power.Superpower;
import lombok.*;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entità che rappresenta un Eroe dotato di un superpotere.
 * Eredita da Character e aggiunge la possibilità di possedere un Superpower.
 */
@Entity
@DiscriminatorValue("SUPERHERO")
@Getter
@Setter
@NoArgsConstructor
public class Superhero extends Character implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "superpower_id", referencedColumnName = "id")
    private Superpower superpower;

    /**
     * Costruttore per Superhero
     *
     * @param name Nome del supereroe
     * @param superpower Il superpotere associato
     * @throws IllegalArgumentException se il superpotere è null
     */
    public Superhero(String name, Superpower superpower) {
        super(name, validate(superpower).getAtk(), superpower.getDef(), superpower.getHp(), superpower.getBonusAtk());
        this.superpower = superpower;
    }

    private static Superpower validate(Superpower superpower) {
        if (superpower == null) {
            throw new IllegalArgumentException("Superpower cannot be null");
        }
        return superpower;
    }
}
