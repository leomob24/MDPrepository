package it.unicam.cs.mpgc.rpg125579.model.service;

import it.unicam.cs.mpgc.rpg125579.model.entity.Minion;
import it.unicam.cs.mpgc.rpg125579.model.entity.Mostro;
import it.unicam.cs.mpgc.rpg125579.model.entity.Superhero;
import it.unicam.cs.mpgc.rpg125579.model.entity.Villain;

import java.util.List;

/**
 * Gestisce la generazione di nuove ondate di nemici quando l'eroe
 * ha sconfitto tutti i nemici dell'ondata corrente. Ogni nuova ondata
 * è più difficile della precedente, incentivando la progressione
 * tramite level-up prima di affrontarla.
 */
public class GestoreOndate {

    /**
     * Genera i nemici (1 Villain + 2 Minion) della prossima ondata per l'eroe
     * indicato, incrementando il suo contatore di ondate.
     *
     * @return la lista dei nuovi nemici generati, da persistere.
     */
    public List<Mostro> generaProssimaOndata(Superhero hero) {
        hero.setOndataAttuale(hero.getOndataAttuale() + 1);
        int ondata = hero.getOndataAttuale();

        return List.of(
                Villain.generateVillain(hero, ondata),
                Minion.generateMinion(hero, ondata),
                Minion.generateMinion(hero, ondata)
        );
    }
}