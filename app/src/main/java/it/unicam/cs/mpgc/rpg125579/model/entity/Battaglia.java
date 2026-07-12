package it.unicam.cs.mpgc.rpg125579.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Stato persistito di un combattimento in corso contro un nemico specifico.
 * Un solo record per nemico (constraint unique su enemy_id): se l'eroe
 * scappa e poi risfida lo stesso nemico, la battaglia riprende da qui,
 * con cure già usate e cooldown conservati.
 * <p>
 * Nessuna logica di combattimento qui dentro: è un'entità di sola gestione
 * dello stato, esattamente come {@link Partita}. Le regole vivono in
 * {@link it.unicam.cs.mpgc.rpg125579.model.service.GestoreBattaglia}.
 * <p>
 * Nota di design: questa classe NON espone metodi che attraversano {@code enemy}
 * per risalire all'eroe (es. un ex {@code getHero()} via {@code enemy.getOwner().getSuperhero()}).
 * Farlo richiederebbe inizializzare un proxy Hibernate lazy fuori da una
 * sessione attiva, dato che ogni {@code Controller} apre/chiude una sessione
 * per singola operazione. Eroe e nemico vanno quindi passati esplicitamente
 * dal chiamante, che li possiede già come entità pienamente caricate.
 */
@Entity
@Table(name = "battaglie")
@Getter
@Setter
@NoArgsConstructor
public class Battaglia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enemy_id", nullable = false, unique = true)
    private Mostro enemy;

    private int turnsSinceHeal;
    private int cureUsate;

    public Battaglia(Mostro enemy) {
        this.enemy = enemy;
        this.turnsSinceHeal = 3; // GestoreBattaglia.HEAL_COOLDOWN_TURNS: cura subito disponibile
        this.cureUsate = 0;
    }
}