package it.unicam.cs.mpgc.rpg125579.view;

import it.unicam.cs.mpgc.rpg125579.controller.*;
import it.unicam.cs.mpgc.rpg125579.model.entity.Minion;
import it.unicam.cs.mpgc.rpg125579.model.entity.Partita;
import it.unicam.cs.mpgc.rpg125579.model.entity.Superhero;
import it.unicam.cs.mpgc.rpg125579.model.entity.Villain;
import it.unicam.cs.mpgc.rpg125579.model.entity.Character;
import it.unicam.cs.mpgc.rpg125579.model.power.Superpower;
import it.unicam.cs.mpgc.rpg125579.utility.AlertFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameCreationView {

    @FXML private TextField txtName;
    @FXML private ComboBox<String> comboSuperpower;
    @FXML private Button btnCreate;
    @FXML private Button btnBack;

    private final Controller<Character> characterController = new BasicController<>(Character.class);
    private final Controller<Superpower> superpowerController = new BasicController<>(Superpower.class);
    private final Controller<Partita> partitaController = new BasicController<>(Partita.class);

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

        // Cascade ALL su Partita.superhero: persiste anche l'eroe automaticamente.
        Partita partita = new Partita(hero);
        partitaController.add(partita);

        characterController.add(Villain.generateVillain(partita));
        characterController.add(Minion.generateMinion(partita));
        characterController.add(Minion.generateMinion(partita));

        MainView controller = ViewNavigator.switchTo("/MainView.fxml", "Superbattles - " + hero.getName());
        controller.initPartita(partita);
    }

    private void showError(String message) {
        AlertFactory.mostraAvviso(txtName.getScene().getWindow(), "Dati incompleti", message);
    }

    @FXML
    void handleBack(ActionEvent event) {
        ViewNavigator.switchTo("/DashboardView.fxml", "Dashboard");
    }
}