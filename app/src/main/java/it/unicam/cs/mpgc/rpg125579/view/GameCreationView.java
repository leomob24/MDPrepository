package it.unicam.cs.mpgc.rpg125579.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class GameCreationView {
    @FXML private TextField txtName;
    @FXML private ComboBox<String> comboSuperpower;
    @FXML private Button btnCreate;

    @FXML void handleCreateHero(ActionEvent event) {
        // Save hero, generate enemies, route to MainView
    }
}