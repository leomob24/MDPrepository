package it.unicam.cs.mpgc.rpg125579.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DashboardView {
    @FXML private TableView<?> gamesTable;
    @FXML private TableColumn<?, ?> colSuperName, colHp, colSuperpower, colAtk, colDef, colBonus;
    @FXML private Button btnSuperpowers, btnRules, btnNewGame, btnDeleteGame, btnResumeSelected;

    @FXML void handleManageSuperpowers(ActionEvent event) { /* Route to SuperpowerCreationView */ }
    @FXML void handleShowRules(ActionEvent event) { /* Show Rules popup or view */ }
    @FXML void handleNewGame(ActionEvent event) { /* Route to GameCreationView */ }
    @FXML void handleDeleteGame(ActionEvent event) { /* Delete selected game */ }
    @FXML void handleResumeGame(ActionEvent event) { /* Route to MainView with selected game */ }
}