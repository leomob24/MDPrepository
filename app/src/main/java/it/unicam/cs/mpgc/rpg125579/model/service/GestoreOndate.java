package it.unicam.cs.mpgc.rpg125579.model.service;

import it.unicam.cs.mpgc.rpg125579.model.entity.Minion;
import it.unicam.cs.mpgc.rpg125579.model.entity.Mostro;
import it.unicam.cs.mpgc.rpg125579.model.entity.Partita;
import it.unicam.cs.mpgc.rpg125579.model.entity.Villain;

import java.util.List;

/**
 * Gestisce la generazione di nuove ondate di nemici quando la partita
 * corrente ha visto sconfitti tutti i nemici dell'ondata precedente.
 * <p>
 * Ogni ondata è composta da 1 Villain e 2 Minion, scalati in difficoltà
 * in base al numero dell'ondata.
 */
public class GestoreOndate {

    /**
     * Genera la prossima ondata di nemici e incrementa il contatore ondata.
     * Restituisce una lista di 1 Villain e 2 Minion generati casualmente
     * con statistiche scalate all'ondata corrente.
     *
     * @param partita La partita di cui generare l'ondata
     * @return Una lista di {@link Mostro} (1 Villain + 2 Minion) per la nuova ondata
     */
    public List<Mostro> generaProssimaOndata(Partita partita) {
        partita.setOndataAttuale(partita.getOndataAttuale() + 1);

        return List.of(
                Villain.generateVillain(partita),
                Minion.generateMinion(partita),
                Minion.generateMinion(partita)
        );
    }
}