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

    /**
     * Identificatore univoco della partita nel database (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * L'eroe giocabile della partita, con relazione uno-a-uno.
     * La cascata ALL e orphanRemoval assicurano che l'eroe sia eliminato quando la partita è rimossa.
     */
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "superhero_id", nullable = false, unique = true)
    private Superhero superhero;

    /**
     * Numero dell'ondata corrente. Incrementato ogni volta che tutti i nemici sono sconfitti.
     */
    private int ondataAttuale;

    /**
     * Indica se la partita è terminata a causa della sconfitta dell'eroe.
     * Una volta a {@code true}, la partita resta visibile (es. per una
     * classifica in Dashboard) ma non è più possibile affrontare battaglie.
     */
    private boolean gameOver;

    /**
     * Timestamp di creazione della partita.
     */
    private LocalDateTime dataCreazione;

    /**
     * Timestamp dell'ultimo salvataggio della partita.
     */
    private LocalDateTime dataUltimoSalvataggio;

    /**
     * Crea una nuova partita con l'eroe specificato.
     *
     * @param superhero L'eroe giocabile della partita
     * @throws IllegalArgumentException se superhero è null
     */
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

    /**
     * Aggiorna il timestamp dell'ultimo salvataggio al momento corrente.
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