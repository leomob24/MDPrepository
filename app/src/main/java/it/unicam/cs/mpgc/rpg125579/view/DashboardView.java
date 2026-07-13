package it.unicam.cs.mpgc.rpg125579.view;

import it.unicam.cs.mpgc.rpg125579.controller.*;
import it.unicam.cs.mpgc.rpg125579.model.entity.Character;
import it.unicam.cs.mpgc.rpg125579.model.entity.Mostro;
import it.unicam.cs.mpgc.rpg125579.model.entity.Partita;
import it.unicam.cs.mpgc.rpg125579.utility.AlertFactory;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Objects;

/**
 * Controller FXML per la schermata iniziale (Dashboard) dell'applicazione.
 * Gestisce la visualizzazione della lista delle partite salvate con accesso rapido
 * alle operazioni principali: creare nuova partita, continuare una partita, eliminare,
 * visualizzare il catalogo dei superpoteri e le regole del gioco.
 */
public class DashboardView {

    @FXML private TableView<Partita> gamesTable;
    @FXML private TableColumn<Partita, String> colSuperName;
    @FXML private TableColumn<Partita, Number> colHp;
    @FXML private TableColumn<Partita, String> colSuperpower;
    @FXML private TableColumn<Partita, Number> colAtk;
    @FXML private TableColumn<Partita, Number> colDef;
    @FXML private TableColumn<Partita, Number> colBonus;
    @FXML private TableColumn<Partita, Number> colLevel;
    @FXML private TableColumn<Partita, Number> colOndata;
    @FXML private Button btnSuperpowers, btnRules, btnNewGame, btnDeleteGame, btnResumeSelected;
    @FXML private TableColumn<Partita, String> colStato;

    private final Controller<Character> characterController = new BasicController<>(Character.class);
    private final Controller<Partita> partitaController = new BasicController<>(Partita.class);

    @FXML
    public void initialize() {
        colSuperName.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSuperhero().getName()));
        colHp.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getSuperhero().getHpAttuali()));
        colSuperpower.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSuperhero().getSuperpower().getPowerName()));
        colAtk.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getSuperhero().getAtk()));
        colDef.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getSuperhero().getDef()));
        colBonus.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getSuperhero().getBonusAtk()));
        colLevel.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getSuperhero().getLivello()));
        colOndata.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getOndataAttuale()));
        colStato.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().isGameOver() ? "Game Over" : "In corso"));

        refreshGamesTable();
    }

    private void refreshGamesTable() {
        List<Partita> partite = partitaController.getAll();
        gamesTable.setItems(FXCollections.observableArrayList(partite));
    }

    @FXML
    void handleManageSuperpowers(ActionEvent event) {
        ViewNavigator.switchTo("/SuperpowerCatalogView.fxml", "Manage Superpowers");
    }

    @FXML
    void handleShowRules(ActionEvent event) {
        AlertFactory.mostraInfoConTestoScorrevole(
                gamesTable.getScene().getWindow(),
                "Rules",
                "Come si gioca",
                """
                1. Crea un Superhero scegliendo un Superpower.
                2. Alla creazione, il gioco genera automaticamente i nemici (Villain + Minion).
                3. Sfida un nemico dalla lista per iniziare la battaglia.
                4. In battaglia scegli ogni turno: Attacca, Difendi, Cura o Scappa.
                5. Sconfiggi tutti i nemici di un'ondata per affrontarne una nuova, più difficile.
                """
        );
    }

    @FXML
    void handleNewGame(ActionEvent event) {
        ViewNavigator.switchTo("/GameCreationView.fxml", "Create your Superhero");
    }

    @FXML
    void handleDeleteGame(ActionEvent event) {
        Partita selected = gamesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Seleziona una partita da eliminare.");
            return;
        }

        characterController.getAll().stream()
                .filter(c -> c instanceof Mostro)
                .map(c -> (Mostro) c)
                .filter(m -> m.getOwner() != null && selected.getId().equals(m.getOwner().getId()))
                .forEach(m -> characterController.remove(m.getId()));

        partitaController.remove(selected.getId());
        refreshGamesTable();
    }

    @FXML
    void handleResumeGame(ActionEvent event) {
        Partita selected = gamesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Seleziona una partita da riprendere.");
            return;
        }
        MainView controller = ViewNavigator.switchTo("/MainView.fxml",
                "Superbattles - " + selected.getSuperhero().getName());
        controller.initPartita(selected);
    }

    private void showInfo(String message) {
        AlertFactory.mostraInfo(gamesTable.getScene().getWindow(), message);
    }
}