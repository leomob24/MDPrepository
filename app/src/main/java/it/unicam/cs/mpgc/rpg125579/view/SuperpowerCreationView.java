package it.unicam.cs.mpgc.rpg125579.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SuperpowerCreationView {
    @FXML private TextField txtName, txtDescription, txtHp, txtAtk, txtDef, txtBonusAtk;
    @FXML private ListView<String> listSuperpowers;
    @FXML private Button btnCreatePower;

    @FXML void handleCreatePower(ActionEvent event) {
        // Read TextFields, save to database/memory, update ListView, redirect
    }
}