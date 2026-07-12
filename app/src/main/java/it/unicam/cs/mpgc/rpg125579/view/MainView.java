package it.unicam.cs.mpgc.rpg125579.view;

import it.unicam.cs.mpgc.rpg125579.controller.*;
import it.unicam.cs.mpgc.rpg125579.model.entity.Character;
import it.unicam.cs.mpgc.rpg125579.model.entity.Mostro;
import it.unicam.cs.mpgc.rpg125579.model.entity.Partita;
import it.unicam.cs.mpgc.rpg125579.model.entity.Superhero;
import it.unicam.cs.mpgc.rpg125579.model.service.GestoreLivelli;
import it.unicam.cs.mpgc.rpg125579.model.service.GestoreOndate;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class MainView {

    @FXML private Label lblHeroName, lblAtk, lblDef, lblHp, lblBonusAtk, lblSuperpowerName, lblSuperpowerDesc;
    @FXML private Label lblLevel, lblXp, lblOndata;
    @FXML private TableView<Character> enemyTable;
    @FXML private TableColumn<Character, String> colEnemyName;
    @FXML private TableColumn<Character, Integer> colEnemyAtk, colEnemyDef, colEnemyHp;
    @FXML private Button btnSfida, btnBack;

    private final Controller<Character> characterController = new BasicController<>(Character.class);
    private final Controller<Partita> partitaController = new BasicController<>(Partita.class);
    private final GestoreLivelli gestoreLivelli = new GestoreLivelli();
    private final GestoreOndate gestoreOndate = new GestoreOndate();

    private Partita partita;
    private Superhero hero;

    @FXML
    public void initialize() {
        colEnemyName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEnemyAtk.setCellValueFactory(new PropertyValueFactory<>("atk"));
        colEnemyDef.setCellValueFactory(new PropertyValueFactory<>("def"));
        colEnemyHp.setCellValueFactory(new PropertyValueFactory<>("hpAttuali"));
    }

    /**
     * Inizializza la view con la partita corrente. Da chiamare subito dopo
     * aver caricato questo FXML (vedi {@link ViewNavigator}).
     */
    public void initPartita(Partita partita) {
        this.partita = partita;
        this.hero = partita.getSuperhero();
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
        lblLevel.setText("Livello: " + hero.getLivello());
        lblXp.setText("XP: " + hero.getEsperienza() + "/" + gestoreLivelli.esperienzaRichiesta(hero.getLivello()));
        lblOndata.setText("Ondata: " + partita.getOndataAttuale());
    }

    /**
     * Aggiorna la tabella dei nemici vivi. Se l'ondata corrente è stata
     * completamente sconfitta, ne genera automaticamente una nuova, più
     * difficile, prima di mostrare la tabella.
     */
    private void refreshEnemyTable() {
        List<Character> nemiciVivi = getNemiciViviDellaPartita();

        if (nemiciVivi.isEmpty()) {
            gestoreOndate.generaProssimaOndata(partita).forEach(characterController::add);
            partita.aggiornaSalvataggio();
            partitaController.update(partita);
            refreshHeroLabels();

            showInfo("Hai sconfitto tutti i nemici! Inizia l'ondata " + partita.getOndataAttuale() + ".");
            nemiciVivi = getNemiciViviDellaPartita();
        }

        enemyTable.setItems(FXCollections.observableArrayList(nemiciVivi));
    }

    private List<Character> getNemiciViviDellaPartita() {
        Long partitaId = partita.getId();
        return characterController.getAll().stream()
                .filter(c -> c instanceof Mostro)
                .map(c -> (Mostro) c)
                .filter(m -> m.getOwner() != null
                        && partitaId.equals(m.getOwner().getId())
                        && m.getHpAttuali() > 0)
                .map(m -> (Character) m)
                .collect(Collectors.toList());
    }

    @FXML
    void handleChallengeEnemy(ActionEvent event) {
        Character selected = enemyTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Seleziona un nemico dalla lista prima di sfidarlo.");
            return;
        }

        BattleView controller = ViewNavigator.switchTo("/BattleView.fxml", "Battaglia!");
        controller.initBattle(partita, selected);
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void handleBack(ActionEvent event) {
        ViewNavigator.switchTo("/DashboardView.fxml", "Dashboard");
    }
}