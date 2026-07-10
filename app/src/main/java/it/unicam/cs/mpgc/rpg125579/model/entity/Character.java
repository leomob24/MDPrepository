package it.unicam.cs.mpgc.rpg125579.model.entity;

import lombok.*;
import jakarta.persistence.*;

/**
 * Classe base astratta che rappresenta un personaggio nel gioco.
 * Implementa l'ereditarietà single-table per tutte le sottoclassi (Superhero, Villain, Minion).
 */
@Entity
@Table(name = "characters")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "character_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public abstract class Character {

    /**
     * Identificatore univoco del personaggio nel database (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int atk;
    private int def;
    private int hp;
    private int hpAttuali;
    private int bonusAtk;

    /**
     * Costruttore protetto per la creazione di un Character.
     *
     * @param name Nome del personaggio
     * @param atk Punti di attacco
     * @param def Punti di difesa
     * @param hp Punti vita massimi
     * @param bonusAtk Bonus di attacco
     * @throws IllegalArgumentException se il nome è null o vuoto
     */
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

    /**
     * Imposta i punti vita attuali, garantendo che non scendano sotto lo zero.
     *
     * @param hpAttuali I nuovi punti vita
     */
    public void setHpAttuali(int hpAttuali) {
        this.hpAttuali = Math.max(hpAttuali, 0);
    }

    @Override
    public String toString() {
        return "Character{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", atk=" + atk +
                ", def=" + def +
                ", hp=" + hp +
                ", hpAttuali=" + hpAttuali +
                ", bonusAtk=" + bonusAtk +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Character character)) return false;
        return id != null && id.equals(character.id);
    }
}