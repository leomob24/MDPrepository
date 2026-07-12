package it.unicam.cs.mpgc.rpg125579.utility;

import it.unicam.cs.mpgc.rpg125579.controller.BasicController;
import it.unicam.cs.mpgc.rpg125579.controller.Controller;
import it.unicam.cs.mpgc.rpg125579.model.power.Superpower;
import it.unicam.cs.mpgc.rpg125579.model.power.SuperpowerCatalog;

public final class SuperpowerSeeder {

    private SuperpowerSeeder() {
    }

    public static void seedIfEmpty() {
        Controller<Superpower> controller = new BasicController<>(Superpower.class);
        if (controller.getAll().isEmpty()) {
            SuperpowerCatalog.PREDEFINED.forEach(controller::add);
        }
    }
}