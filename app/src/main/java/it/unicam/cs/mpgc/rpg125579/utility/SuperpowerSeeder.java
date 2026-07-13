package it.unicam.cs.mpgc.rpg125579.utility;

import it.unicam.cs.mpgc.rpg125579.controller.BasicController;
import it.unicam.cs.mpgc.rpg125579.controller.Controller;
import it.unicam.cs.mpgc.rpg125579.model.power.Superpower;
import it.unicam.cs.mpgc.rpg125579.model.power.SuperpowerCatalog;

/**
 * Utility che popola il database con i superpoteri predefiniti al primo avvio dell'applicazione.
 * Se la tabella dei superpoteri è vuota, carica automaticamente il catalogo
 * {@link SuperpowerCatalog#PREDEFINED} nel database, consentendo così il gioco
 * anche in caso di database fresco.
 */
public final class SuperpowerSeeder {

    private SuperpowerSeeder() {
    }

    /**
     * Popola il database con i superpoteri predefiniti, se la tabella è ancora vuota.
     * Viene invocato automaticamente all'avvio dell'applicazione via {@link App}.
     */
    public static void seedIfEmpty() {
        Controller<Superpower> controller = new BasicController<>(Superpower.class);
        if (controller.getAll().isEmpty()) {
            SuperpowerCatalog.PREDEFINED.forEach(controller::add);
        }
    }
}