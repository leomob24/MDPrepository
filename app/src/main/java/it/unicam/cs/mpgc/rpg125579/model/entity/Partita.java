package it.unicam.cs.mpgc.rpg125579.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    /**
     * Indica se la partita è terminata a causa della sconfitta dell'eroe.
     * Una volta a {@code true}, la partita resta visibile (es. per una
     * classifica in Dashboard) ma non è più possibile affrontare battaglie.
     */
    private boolean gameOver;

    private LocalDateTime dataCreazione;
    private LocalDateTime dataUltimoSalvataggio;

    public Partita(Superhero superhero) {
        if (superhero == null) {
            throw new IllegalArgumentException("Superhero cannot be null");
        }
        this.superhero = superhero;
        this.ondataAttuale = 1;
        this.gameOver = false;
        this.dataCreazione = LocalDateTime.now();
        this.dataUltimoSalvataggio = LocalDateTime.now();
    }

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