package it.unicam.cs.mpgc.rpg125579.view;

import it.unicam.cs.mpgc.rpg125579.controller.BasicController;
import it.unicam.cs.mpgc.rpg125579.controller.*;
import it.unicam.cs.mpgc.rpg125579.model.entity.Character;
import it.unicam.cs.mpgc.rpg125579.model.entity.Mostro;
import it.unicam.cs.mpgc.rpg125579.model.entity.Superhero;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller della Dashboard: mostra tutti i Superhero esistenti come
 * "Recent Games" e permette di crearne uno nuovo, eliminarlo o riprenderlo.
 */
public class DashboardView {

    @FXML private TableView<Superhero> gamesTable;
    @FXML private TableColumn<Superhero, String> colSuperName;
    @FXML private TableColumn<Superhero, Integer> colHp;
    @FXML private TableColumn<Superhero, String> colSuperpower;
    @FXML private TableColumn<Superhero, Integer> colAtk;
    @FXML private TableColumn<Superhero, Integer> colDef;
    @FXML private TableColumn<Superhero, Integer> colBonus;
    @FXML private Button btnSuperpowers, btnRules, btnNewGame, btnDeleteGame, btnResumeSelected;

    private final Controller<Character> characterController = new BasicController<>(Character.class);
    @FXML
    public void initialize() {
        colSuperName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colHp.setCellValueFactory(new PropertyValueFactory<>("hpAttuali"));
        colSuperpower.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getSuperpower().getPowerName()));
        colAtk.setCellValueFactory(new PropertyValueFactory<>("atk"));
        colDef.setCellValueFactory(new PropertyValueFactory<>("def"));
        colBonus.setCellValueFactory(new PropertyValueFactory<>("bonusAtk"));

        refreshGamesTable();
    }

    private void refreshGamesTable() {
        List<Superhero> heroes = characterController.getAll().stream()
                .filter(c -> c instanceof Superhero)
                .map(c -> (Superhero) c)
                .collect(Collectors.toList());
        gamesTable.setItems(FXCollections.observableArrayList(heroes));
    }

    @FXML
    void handleManageSuperpowers(ActionEvent event) {
        ViewNavigator.switchTo("/fxml/SuperpowerCreationView.fxml", "Manage Superpowers");
    }

    @FXML
    void handleShowRules(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rules");
        alert.setHeaderText("Come si gioca");
        alert.setContentText("""
                1. Crea un Superhero scegliendo un Superpower.
                2. Alla creazione, il gioco genera automaticamente i nemici (Villain + Minion).
                3. Sfida un nemico dalla lista per iniziare la battaglia.
                4. In battaglia scegli ogni turno: Attacca, Difendi, Cura o Scappa.
                """);
        alert.showAndWait();
    }

    @FXML
    void handleNewGame(ActionEvent event) {
        ViewNavigator.switchTo("/fxml/GameCreationView.fxml", "Create your Superhero");
    }

    @FXML
    void handleDeleteGame(ActionEvent event) {
        Superhero selected = gamesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Seleziona una partita da eliminare.");
            return;
        }

        // Elimina anche tutti i nemici (Mostro) di proprietà di questo eroe,
        // altrimenti resterebbero "orfani" nel database.
        characterController.getAll().stream()
                .filter(c -> c instanceof Mostro)
                .map(c -> (Mostro) c)
                .filter(m -> selected.equals(m.getOwner()))
                .forEach(m -> characterController.remove(m.getId()));

        characterController.remove(selected.getId());
        refreshGamesTable();
    }

    @FXML
    void handleResumeGame(ActionEvent event) {
        Superhero selected = gamesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Seleziona una partita da riprendere.");
            return;
        }
        MainView controller = ViewNavigator.switchTo("/fxml/MainView.fxml", "Superbattles - " + selected.getName());
        controller.initHero(selected);
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
