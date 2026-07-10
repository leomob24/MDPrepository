package it.unicam.cs.mpgc.rpg125579.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class BattleView {

    // --- Stats Eroe ---
    @FXML private Label lblHeroName;
    @FXML private Label lblHeroHp;
    @FXML private Label lblHeroAtk;
    @FXML private Label lblHeroDef;

    // --- Stats Nemico ---
    @FXML private Label lblEnemyName;
    @FXML private Label lblEnemyHp;
    @FXML private Label lblEnemyAtk;
    @FXML private Label lblEnemyDef;

    // --- Controlli Battaglia ---
    @FXML private TextArea txtBattleLog;
    @FXML private Button btnAtk, btnDef, btnCura, btnScappa;

    /**
     * Metodo per inizializzare i dati della battaglia.
     * Da chiamare dal MainViewController prima di mostrare questa schermata.
     * (Nota: puoi sostituire i parametri String/int con i tuoi oggetti Hero/Enemy veri)
     */
    public void initData(String heroName, int heroHp, int heroAtk, int heroDef,
                         String enemyName, int enemyHp, int enemyAtk, int enemyDef) {

        // Popola i dati dell'Eroe
        lblHeroName.setText(heroName);
        lblHeroHp.setText(String.valueOf(heroHp));
        lblHeroAtk.setText(String.valueOf(heroAtk));
        lblHeroDef.setText(String.valueOf(heroDef));

        // Popola i dati del Nemico
        lblEnemyName.setText(enemyName);
        lblEnemyHp.setText(String.valueOf(enemyHp));
        lblEnemyAtk.setText(String.valueOf(enemyAtk));
        lblEnemyDef.setText(String.valueOf(enemyDef));

        // Messaggio iniziale nel log
        txtBattleLog.appendText("La battaglia ha inizio: " + heroName + " contro " + enemyName + "!\n");
    }

    @FXML void handleAttack(ActionEvent event) {
        txtBattleLog.appendText("Hai attaccato il nemico!\n");
        // Calcola il danno, sottrai gli HP del nemico, aggiorna lblEnemyHp.setText(...)
        // Esegui il turno del nemico
    }

    @FXML void handleDefend(ActionEvent event) {
        txtBattleLog.appendText("Ti stai difendendo! Difesa aumentata per questo turno.\n");
        // Applica bonus difesa, esegui il turno del nemico
    }

    @FXML void handleHeal(ActionEvent event) {
        txtBattleLog.appendText("Hai usato Cura! HP ripristinati.\n");
        // Ripristina HP, aggiorna lblHeroHp, inizia cooldown di 3 turni (disabilita bottone), esegui turno nemico
    }

    @FXML void handleEscape(ActionEvent event) {
        txtBattleLog.appendText("Sei scappato dalla battaglia!\n");
        // Ritorna alla MainView
    }
}
