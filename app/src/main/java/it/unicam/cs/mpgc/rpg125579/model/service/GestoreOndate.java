package it.unicam.cs.mpgc.rpg125579.model.service;

import it.unicam.cs.mpgc.rpg125579.model.entity.Minion;
import it.unicam.cs.mpgc.rpg125579.model.entity.Mostro;
import it.unicam.cs.mpgc.rpg125579.model.entity.Partita;
import it.unicam.cs.mpgc.rpg125579.model.entity.Villain;

import java.util.List;

/**
 * Gestisce la generazione di nuove ondate di nemici quando la partita
 * corrente ha visto sconfitti tutti i nemici dell'ondata precedente.
 */
public class GestoreOndate {

    public List<Mostro> generaProssimaOndata(Partita partita) {
        partita.setOndataAttuale(partita.getOndataAttuale() + 1);

        return List.of(
                Villain.generateVillain(partita),
                Minion.generateMinion(partita),
                Minion.generateMinion(partita)
        );
    }
}