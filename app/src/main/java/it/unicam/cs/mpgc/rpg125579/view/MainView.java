package it.unicam.cs.mpgc.rpg125579.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainView {
    @FXML private Label lblHeroName, lblAtk, lblDef, lblHp, lblBonusAtk, lblSuperpowerName, lblSuperpowerDesc;
    @FXML private TableView<?> enemyTable;
    @FXML private TableColumn<?, ?> colEnemyName, colEnemyAtk, colEnemyDef, colEnemyHp;
    @FXML private Button btnSfida;

    @FXML void handleChallengeEnemy(ActionEvent event) {
        // Get selected enemy from table, route to BattleView
    }
}