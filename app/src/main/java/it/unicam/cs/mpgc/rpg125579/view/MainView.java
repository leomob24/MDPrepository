package it.unicam.cs.mpgc.rpg125579.view;

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
 * Controller della MainView: mostra le statistiche dell'eroe e la lista
 * dei nemici associati alla sua partita (tramite {@link Mostro#getOwner()}).
 */
public class MainView {

    @FXML private Label lblHeroName, lblAtk, lblDef, lblHp, lblBonusAtk, lblSuperpowerName, lblSuperpowerDesc;
    @FXML private TableView<Character> enemyTable;
    @FXML private TableColumn<Character, String> colEnemyName;
    @FXML private TableColumn<Character, Integer> colEnemyAtk, colEnemyDef, colEnemyHp;
    @FXML private Button btnSfida;

    private final Controller<Character> characterController = new BasicController<>(Character.class);
    private Superhero hero;

    @FXML
    public void initialize() {
        colEnemyName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEnemyAtk.setCellValueFactory(new PropertyValueFactory<>("atk"));
        colEnemyDef.setCellValueFactory(new PropertyValueFactory<>("def"));
        colEnemyHp.setCellValueFactory(new PropertyValueFactory<>("hpAttuali"));
    }

    /**
     * Inizializza la view con l'eroe della partita corrente.
     * Da chiamare subito dopo aver caricato questo FXML (vedi {@link ViewNavigator}).
     */
    public void initHero(Superhero hero) {
        this.hero = hero;
        refreshHeroLabels();
        refreshEnemyTable();
    }

    private void refreshHeroLabels() {
        lblHeroName.setText("Hero Name: " + hero.getName());
        lblAtk.setText("ATK: " + hero.getAtk());
        lblDef.setText("DEF: " + hero.getDef());
        lblHp.setText("HP: " + hero.getHpAttuali() + "/" + hero.getHp());
        lblBonusAtk.setText("Bonus ATK: " + hero.getBonusAtk());
        lblSuperpowerName.setText("Superpower: " + hero.getSuperpower().getPowerName());
        lblSuperpowerDesc.setText("Description: " + hero.getSuperpower().getDescription());
    }

    private void refreshEnemyTable() {
        List<Character> enemies = characterController.getAll().stream()
                .filter(c -> c instanceof Mostro)
                .map(c -> (Mostro) c)
                .filter(m -> hero.equals(m.getOwner()) && m.getHpAttuali() > 0)
                .map(m -> (Character) m)
                .collect(Collectors.toList());
        enemyTable.setItems(FXCollections.observableArrayList(enemies));
    }

    @FXML
    void handleChallengeEnemy(ActionEvent event) {
        Character selected = enemyTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Seleziona un nemico dalla lista prima di sfidarlo.");
            return;
        }

        BattleView controller = ViewNavigator.switchTo("/fxml/BattleVIew.fxml", "Battaglia!");
        controller.initBattle(hero, selected);
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
