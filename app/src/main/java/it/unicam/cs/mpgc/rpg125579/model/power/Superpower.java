package it.unicam.cs.mpgc.rpg125579.model.power;

import lombok.*;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entità che rappresenta un Superpotere nel gioco.
 * Questa classe è mappata come entità JPA per gestire la persistenza dei dati sul database.
 */
@Entity
@Table(name = "superpowers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Superpower implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Identificatore univoco del superpotere nel database (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String powerName;

    private int atk;
    private int def;
    private int hp;
    private int bonusAtk;

    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Costruttore validato per Superpower.
     *
     * @param powerName Nome del superpotere
     * @param atk Punti di attacco bonus
     * @param def Punti di difesa bonus
     * @param hp Punti vita bonus
     * @param bonusAtk Bonus di attacco aggiuntivo
     * @param description Descrizione del superpotere
     * @throws IllegalArgumentException se i parametri non sono validi
     */
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

    @Override
    public String toString() {
        return "Superpower{" +
                "id=" + id +
                ", powerName='" + powerName + '\'' +
                ", atk=" + atk +
                ", def=" + def +
                ", hp=" + hp +
                ", bonusAtk=" + bonusAtk +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Superpower that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
