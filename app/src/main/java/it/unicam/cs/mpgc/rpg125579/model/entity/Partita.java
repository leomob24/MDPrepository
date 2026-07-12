package it.unicam.cs.mpgc.rpg125579.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Rappresenta una sessione di gioco: collega un {@link Superhero} al suo
 * stato di avanzamento nella run corrente (ondata attuale, date di
 * creazione/salvataggio). Separata da {@code Superhero} per rispettare la
 * singola responsabilità: l'eroe descrive "quanto è forte il personaggio",
 * la Partita descrive "a che punto è arrivato il giocatore in questa run".
 */
@Entity
@Table(name = "partite")
@Getter
@Setter
@NoArgsConstructor
public class Partita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "superhero_id", nullable = false, unique = true)
    private Superhero superhero;

    private int ondataAttuale;

    private LocalDateTime dataCreazione;
    private LocalDateTime dataUltimoSalvataggio;

    public Partita(Superhero superhero) {
        if (superhero == null) {
            throw new IllegalArgumentException("Superhero cannot be null");
        }
        this.superhero = superhero;
        this.ondataAttuale = 1;
        this.dataCreazione = LocalDateTime.now();
        this.dataUltimoSalvataggio = LocalDateTime.now();
    }

    /**
     * Aggiorna il timestamp dell'ultimo salvataggio, da chiamare ogni volta
     * che lo stato della partita viene persistito (es. dopo un turno di
     * battaglia o al termine di uno scontro).
     */
    public void aggiornaSalvataggio() {
        this.dataUltimoSalvataggio = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Partita partita)) return false;
        return id != null && id.equals(partita.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}