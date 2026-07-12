package it.unicam.cs.mpgc.rpg125579.view;

import it.unicam.cs.mpgc.rpg125579.controller.*;
import it.unicam.cs.mpgc.rpg125579.model.power.Superpower;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mostra il catalogo prefissato dei superpoteri disponibili (sola lettura).
 * La creazione libera non è più prevista: i valori sono definiti in
 * {@link it.unicam.cs.mpgc.rpg125579.model.power.SuperpowerCatalog}.
 */
public class SuperpowerCatalogView {

    @FXML private ListView<String> listSuperpowers;
    @FXML private Label lblAtk, lblDef, lblHp, lblBonusAtk, lblDescription;
    @FXML private Button btnBack;

    private final Controller<Superpower> superpowerController = new BasicController<>(Superpower.class);
    private List<Superpower> superpowers;

    @FXML
    public void initialize() {
        superpowers = superpowerController.getAll();
        listSuperpowers.setItems(FXCollections.observableArrayList(
                superpowers.stream().map(Superpower::getPowerName).collect(Collectors.toList())
        ));
        listSuperpowers.getSelectionModel().selectedIndexProperty()
                .addListener((obs, oldIdx, newIdx) -> showDetails(newIdx.intValue()));
        if (!superpowers.isEmpty()) {
            listSuperpowers.getSelectionModel().selectFirst();
        }
    }

    private void showDetails(int index) {
        if (index < 0 || index >= superpowers.size()) return;
        Superpower sp = superpowers.get(index);
        lblAtk.setText("ATK: " + sp.getAtk());
        lblDef.setText("DEF: " + sp.getDef());
        lblHp.setText("HP: " + sp.getHp());
        lblBonusAtk.setText("Bonus ATK: " + sp.getBonusAtk());
        lblDescription.setText(sp.getDescription());
    }

    @FXML
    void handleBack(ActionEvent event) {
        ViewNavigator.switchTo("/DashboardView.fxml", "Dashboard");
    }
}