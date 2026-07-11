package it.unicam.cs.mpgc.rpg125579.view;

import it.unicam.cs.mpgc.rpg125579.controller.*;
import it.unicam.cs.mpgc.rpg125579.model.power.Superpower;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller per la creazione di Superpower custom. Ogni Superpower creato
 * è riutilizzabile tra più partite (come discusso: i superpoteri sono
 * cross-partita, a differenza di Villain/Minion che sono legati a un eroe).
 */
public class SuperpowerCreationView {

    @FXML private TextField txtName, txtDescription, txtHp, txtAtk, txtDef, txtBonusAtk;
    @FXML private ListView<String> listSuperpowers;
    @FXML private Button btnCreatePower;
    @FXML private Button btnBack;

    private final Controller<Superpower> superpowerController = new BasicController<>(Superpower.class);

    @FXML
    public void initialize() {
        refreshList();
    }

    private void refreshList() {
        List<String> nomi = superpowerController.getAll().stream()
                .map(Superpower::getPowerName)
                .collect(Collectors.toList());
        listSuperpowers.setItems(FXCollections.observableArrayList(nomi));
    }

    @FXML
    void handleCreatePower(ActionEvent event) {
        Integer hp = parseIntField(txtHp.getText(), "HP");
        Integer atk = parseIntField(txtAtk.getText(), "ATK");
        Integer def = parseIntField(txtDef.getText(), "DEF");
        Integer bonusAtk = parseIntField(txtBonusAtk.getText(), "Bonus ATK");

        if (hp == null || atk == null || def == null || bonusAtk == null) {
            return; // errore già mostrato da parseIntField
        }

        try {
            Superpower superpower = new Superpower(
                    txtName.getText(), atk, def, hp, bonusAtk, txtDescription.getText()
            );
            superpowerController.add(superpower);
            refreshList();
            clearFields();
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private Integer parseIntField(String text, String fieldLabel) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException | NullPointerException ex) {
            showError("Il campo \"" + fieldLabel + "\" deve essere un numero intero valido.");
            return null;
        }
    }

    private void clearFields() {
        txtName.clear();
        txtDescription.clear();
        txtHp.clear();
        txtAtk.clear();
        txtDef.clear();
        txtBonusAtk.clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Dati non validi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void handleBack(ActionEvent event) {
        ViewNavigator.switchTo("/DashboardView.fxml", "Dashboard");
    }
}
