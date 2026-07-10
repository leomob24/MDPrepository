package it.unicam.cs.mpgc.rpg125579.view;

import it.unicam.cs.mpgc.rpg125579.controller.*;
import it.unicam.cs.mpgc.rpg125579.model.entity.Character;
import it.unicam.cs.mpgc.rpg125579.model.entity.Minion;
import it.unicam.cs.mpgc.rpg125579.model.entity.Superhero;
import it.unicam.cs.mpgc.rpg125579.model.entity.Villain;
import it.unicam.cs.mpgc.rpg125579.model.power.Superpower;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller per la creazione di un nuovo Superhero. Alla conferma,
 * genera automaticamente i nemici della partita (1 Villain + 2 Minion),
 * tutti legati al nuovo eroe tramite il campo owner di {@link it.unicam.cs.mpgc.rpg125579.model.entity.Mostro}.
 */
public class GameCreationView {

    @FXML private TextField txtName;
    @FXML private ComboBox<String> comboSuperpower;
    @FXML private Button btnCreate;

    private final Controller<Character> characterController = new BasicController<>(Character.class);
    private final Controller<Superpower> superpowerController = new BasicController<>(Superpower.class);

    private final Map<String, Superpower> superpowersByName = new HashMap<>();

    @FXML
    public void initialize() {
        List<Superpower> superpowers = superpowerController.getAll();
        superpowersByName.clear();
        for (Superpower sp : superpowers) {
            superpowersByName.put(sp.getPowerName(), sp);
        }
        comboSuperpower.getItems().setAll(superpowersByName.keySet());
    }

    @FXML
    void handleCreateHero(ActionEvent event) {
        String name = txtName.getText();
        String powerName = comboSuperpower.getValue();

        if (name == null || name.isBlank()) {
            showError("Inserisci un nome per il tuo eroe.");
            return;
        }
        if (powerName == null || !superpowersByName.containsKey(powerName)) {
            showError("Seleziona un superpotere.");
            return;
        }

        Superpower superpower = superpowersByName.get(powerName);
        Superhero hero = new Superhero(name.trim(), superpower);
        characterController.add(hero);

        // Generazione automatica dei nemici della partita, legati all'eroe.
        characterController.add(Villain.generateVillain(hero));
        characterController.add(Minion.generateMinion(hero));
        characterController.add(Minion.generateMinion(hero));

        MainView controller = ViewNavigator.switchTo("/fxml/MainView.fxml", "Superbattles - " + hero.getName());
        controller.initHero(hero);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Dati incompleti");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
