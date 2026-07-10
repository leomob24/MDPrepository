package it.unicam.cs.mpgc.rpg125579.view;

import it.unicam.cs.mpgc.rpg125579.controller.*;
import it.unicam.cs.mpgc.rpg125579.model.entity.Character;
import it.unicam.cs.mpgc.rpg125579.model.entity.GestoreCombattimento;
import it.unicam.cs.mpgc.rpg125579.model.entity.Superhero;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Controller della battaglia: il giocatore sceglie un'azione ogni turno
 * (Attacca, Difendi, Cura, Scappa). Ogni calcolo di danno passa da
 * {@link GestoreCombattimento} — questa classe si limita a orchestrare
 * il turno e aggiornare la UI, senza calcolare nulla direttamente.
 */
public class BattleView {

    @FXML private Label lblHeroName;
    @FXML private Label lblHeroHp;
    @FXML private Label lblHeroAtk;
    @FXML private Label lblHeroDef;

    @FXML private Label lblEnemyName;
    @FXML private Label lblEnemyHp;
    @FXML private Label lblEnemyAtk;
    @FXML private Label lblEnemyDef;

    @FXML private TextArea txtBattleLog;
    @FXML private Button btnAtk, btnDef, btnCura, btnScappa;

    private final GestoreCombattimento gestore = new GestoreCombattimento();
    private final Controller<Character> characterController = new BasicController<>(Character.class);
    private Superhero hero;
    private Character enemy;

    private static final int HEAL_AMOUNT = 25;
    private static final int HEAL_COOLDOWN_TURNS = 3;
    private static final double DEFEND_BONUS_MULTIPLIER = 1.5;

    private int turnsSinceHeal = HEAL_COOLDOWN_TURNS; // curabile fin da subito
    private boolean battleOver = false;

    /**
     * Inizializza la battaglia. Da chiamare subito dopo aver caricato questo FXML.
     */
    public void initBattle(Superhero hero, Character enemy) {
        this.hero = hero;
        this.enemy = enemy;
        this.battleOver = false;
        this.turnsSinceHeal = HEAL_COOLDOWN_TURNS;

        refreshLabels();
        txtBattleLog.clear();
        txtBattleLog.appendText("La battaglia ha inizio: " + hero.getName() + " contro " + enemy.getName() + "!\n");
        updateHealButtonAvailability();
    }

    private void refreshLabels() {
        lblHeroName.setText(hero.getName());
        lblHeroHp.setText(hero.getHpAttuali() + "/" + hero.getHp());
        lblHeroAtk.setText(String.valueOf(hero.getAtk()));
        lblHeroDef.setText(String.valueOf(hero.getDef()));

        lblEnemyName.setText(enemy.getName());
        lblEnemyHp.setText(enemy.getHpAttuali() + "/" + enemy.getHp());
        lblEnemyAtk.setText(String.valueOf(enemy.getAtk()));
        lblEnemyDef.setText(String.valueOf(enemy.getDef()));
    }

    @FXML
    void handleAttack(ActionEvent event) {
        if (battleOver) return;

        gestore.eseguiAttacco(hero, enemy);
        txtBattleLog.appendText(hero.getName() + " attacca! " + enemy.getName()
                + " scende a " + enemy.getHpAttuali() + " HP.\n");

        if (checkEnemyDefeated()) return;

        eseguiTurnoNemico();
    }

    @FXML
    void handleDefend(ActionEvent event) {
        if (battleOver) return;

        // Bonus difesa temporaneo solo per l'attacco nemico di questo turno:
        // alziamo def, eseguiamo l'attacco nemico, poi ripristiniamo il valore originale.
        int defOriginale = hero.getDef();
        hero.setDef((int) Math.round(defOriginale * DEFEND_BONUS_MULTIPLIER));
        txtBattleLog.appendText("Ti stai difendendo! Difesa aumentata per questo turno.\n");

        eseguiTurnoNemico();

        hero.setDef(defOriginale);
        lblHeroDef.setText(String.valueOf(hero.getDef()));
    }

    @FXML
    void handleHeal(ActionEvent event) {
        if (battleOver) return;
        if (turnsSinceHeal < HEAL_COOLDOWN_TURNS) {
            txtBattleLog.appendText("Cura non ancora disponibile.\n");
            return;
        }

        int nuoviHp = Math.min(hero.getHpAttuali() + HEAL_AMOUNT, hero.getHp());
        hero.setHpAttuali(nuoviHp);
        turnsSinceHeal = 0;
        txtBattleLog.appendText("Hai usato Cura! HP ripristinati a " + hero.getHpAttuali() + "/" + hero.getHp() + ".\n");
        lblHeroHp.setText(hero.getHpAttuali() + "/" + hero.getHp());
        updateHealButtonAvailability();

        eseguiTurnoNemico();
    }

    @FXML
    void handleEscape(ActionEvent event) {
        txtBattleLog.appendText("Sei scappato dalla battaglia!\n");
        persistState();

        MainView controller = ViewNavigator.switchTo("/fxml/MainView.fxml", "Superbattles - " + hero.getName());
        controller.initHero(hero);
    }

    /**
     * Esegue il turno del nemico, se ancora vivo, e verifica lo stato di fine battaglia.
     * Incrementa anche il contatore per il cooldown di Cura.
     */
    private void eseguiTurnoNemico() {
        turnsSinceHeal++;
        updateHealButtonAvailability();

        gestore.eseguiAttacco(enemy, hero);
        txtBattleLog.appendText(enemy.getName() + " contrattacca! " + hero.getName()
                + " scende a " + hero.getHpAttuali() + " HP.\n");
        lblHeroHp.setText(hero.getHpAttuali() + "/" + hero.getHp());

        checkHeroDefeated();
    }

    private boolean checkEnemyDefeated() {
        lblEnemyHp.setText(enemy.getHpAttuali() + "/" + enemy.getHp());
        if (enemy.getHpAttuali() <= 0) {
            txtBattleLog.appendText("\n🏆 Hai sconfitto " + enemy.getName() + "!\n");
            endBattle();
            return true;
        }
        return false;
    }

    private void checkHeroDefeated() {
        if (hero.getHpAttuali() <= 0) {
            txtBattleLog.appendText("\n💀 Sei stato sconfitto da " + enemy.getName() + "...\n");
            endBattle();
        }
    }

    private void endBattle() {
        battleOver = true;
        btnAtk.setDisable(true);
        btnDef.setDisable(true);
        btnCura.setDisable(true);
        btnScappa.setText("Torna alla base");
        persistState();
    }

    /**
     * Salva lo stato aggiornato di eroe e nemico. Se il nemico è stato
     * sconfitto (HP a 0), lo rimuove dal database: non serve più.
     */
    private void persistState() {
        characterController.update(hero);
        if (enemy.getHpAttuali() <= 0) {
            characterController.remove(enemy.getId());
        } else {
            characterController.update(enemy);
        }
    }

    private void updateHealButtonAvailability() {
        boolean disponibile = turnsSinceHeal >= HEAL_COOLDOWN_TURNS;
        btnCura.setDisable(!disponibile || battleOver);
    }
}
