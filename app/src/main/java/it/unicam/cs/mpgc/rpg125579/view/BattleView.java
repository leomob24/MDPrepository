package it.unicam.cs.mpgc.rpg125579.view;

import it.unicam.cs.mpgc.rpg125579.controller.*;
import it.unicam.cs.mpgc.rpg125579.model.entity.Character;
import it.unicam.cs.mpgc.rpg125579.model.entity.Partita;
import it.unicam.cs.mpgc.rpg125579.model.entity.Superhero;
import it.unicam.cs.mpgc.rpg125579.model.service.GestoreCombattimento;
import it.unicam.cs.mpgc.rpg125579.model.service.GestoreLivelli;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class BattleView {

    @FXML private Label lblHeroName;
    @FXML private Label lblHeroHp;
    @FXML private Label lblHeroAtk;
    @FXML private Label lblHeroDef;
    @FXML private Label lblHeroLevel;
    @FXML private Label lblHeroXp;

    @FXML private Label lblEnemyName;
    @FXML private Label lblEnemyHp;
    @FXML private Label lblEnemyAtk;
    @FXML private Label lblEnemyDef;

    @FXML private Label lblCuraInfo;

    @FXML private TextArea txtBattleLog;
    @FXML private Button btnAtk, btnDef, btnCura, btnScappa;

    private final GestoreCombattimento gestoreCombattimento = new GestoreCombattimento();
    private final GestoreLivelli gestoreLivelli = new GestoreLivelli();
    private final Controller<Character> characterController = new BasicController<>(Character.class);
    private final Controller<Partita> partitaController = new BasicController<>(Partita.class);

    private Partita partita;
    private Superhero hero;
    private Character enemy;

    private static final int HEAL_AMOUNT = 8;
    private static final int HEAL_COOLDOWN_TURNS = 3;
    private static final int MAX_CURE_PER_BATTLE = 3;
    private static final double DEFEND_BONUS_MULTIPLIER = 1.5;

    private int turnsSinceHeal = HEAL_COOLDOWN_TURNS;
    private int cureUsate = 0;
    private boolean battleOver = false;

    public void initBattle(Partita partita, Character enemy) {
        this.partita = partita;
        this.hero = partita.getSuperhero();
        this.enemy = enemy;
        this.battleOver = false;
        this.turnsSinceHeal = HEAL_COOLDOWN_TURNS;
        this.cureUsate = 0;

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
        lblHeroLevel.setText("Lv. " + hero.getLivello());
        lblHeroXp.setText(hero.getEsperienza() + "/" + gestoreLivelli.esperienzaRichiesta(hero.getLivello()) + " XP");

        lblEnemyName.setText(enemy.getName());
        lblEnemyHp.setText(enemy.getHpAttuali() + "/" + enemy.getHp());
        lblEnemyAtk.setText(String.valueOf(enemy.getAtk()));
        lblEnemyDef.setText(String.valueOf(enemy.getDef()));
    }

    @FXML
    void handleAttack(ActionEvent event) {
        if (battleOver) return;

        gestoreCombattimento.eseguiAttacco(hero, enemy);
        txtBattleLog.appendText(hero.getName() + " attacca! " + enemy.getName()
                + " scende a " + enemy.getHpAttuali() + " HP.\n");

        if (checkEnemyDefeated()) return;

        eseguiTurnoNemico();
    }

    @FXML
    void handleDefend(ActionEvent event) {
        if (battleOver) return;

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

        if (cureUsate >= MAX_CURE_PER_BATTLE) {
            txtBattleLog.appendText("Hai già usato tutte le cure disponibili per questo combattimento.\n");
            return;
        }
        if (turnsSinceHeal < HEAL_COOLDOWN_TURNS) {
            txtBattleLog.appendText("Cura non ancora disponibile.\n");
            return;
        }

        int nuoviHp = Math.min(hero.getHpAttuali() + (HEAL_AMOUNT*hero.getHp()/100), hero.getHp());
        int recuperati = nuoviHp - hero.getHpAttuali();
        hero.setHpAttuali(nuoviHp);
        turnsSinceHeal = 0;
        cureUsate++;

        txtBattleLog.appendText("Hai usato Cura! Hai recuperato " + recuperati + " HP ("
                + hero.getHpAttuali() + "/" + hero.getHp() + "). Cure rimaste: "
                + (MAX_CURE_PER_BATTLE - cureUsate) + "/" + MAX_CURE_PER_BATTLE + ".\n");
        lblHeroHp.setText(hero.getHpAttuali() + "/" + hero.getHp());
        updateHealButtonAvailability();

        eseguiTurnoNemico();
    }

    @FXML
    void handleEscape(ActionEvent event) {
        txtBattleLog.appendText("Sei scappato dalla battaglia!\n");
        persistState();

        MainView controller = ViewNavigator.switchTo("/MainView.fxml", "Superbattles - " + hero.getName());
        controller.initPartita(partita);
    }

    private void eseguiTurnoNemico() {
        turnsSinceHeal++;
        updateHealButtonAvailability();

        gestoreCombattimento.eseguiAttacco(enemy, hero);
        txtBattleLog.appendText(enemy.getName() + " contrattacca! " + hero.getName()
                + " scende a " + hero.getHpAttuali() + " HP.\n");
        lblHeroHp.setText(hero.getHpAttuali() + "/" + hero.getHp());

        checkHeroDefeated();
    }

    private boolean checkEnemyDefeated() {
        lblEnemyHp.setText(enemy.getHpAttuali() + "/" + enemy.getHp());
        if (enemy.getHpAttuali() <= 0) {
            txtBattleLog.appendText("\n🏆 Hai sconfitto " + enemy.getName() + "!\n");
            assegnaEsperienzaESegnala();
            endBattle();
            return true;
        }
        return false;
    }

    private void assegnaEsperienzaESegnala() {
        int xpOttenuta = gestoreLivelli.calcolaEsperienzaOttenuta(enemy);
        int livelliGuadagnati = gestoreLivelli.assegnaEsperienza(hero, xpOttenuta);

        txtBattleLog.appendText("Hai guadagnato " + xpOttenuta + " punti esperienza!\n");
        if (livelliGuadagnati > 0) {
            txtBattleLog.appendText("⭐ LEVEL UP! Ora sei livello " + hero.getLivello()
                    + " (" + (livelliGuadagnati > 1 ? livelliGuadagnati + " livelli guadagnati, " : "")
                    + "statistiche massime aumentate)!\n");
        }
        lblHeroLevel.setText("Lv. " + hero.getLivello());
        lblHeroXp.setText(hero.getEsperienza() + "/" + gestoreLivelli.esperienzaRichiesta(hero.getLivello()) + " XP");
        lblHeroAtk.setText(String.valueOf(hero.getAtk()));
        lblHeroDef.setText(String.valueOf(hero.getDef()));
        lblHeroHp.setText(hero.getHpAttuali() + "/" + hero.getHp());
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

    private void persistState() {
        characterController.update(hero);
        if (enemy.getHpAttuali() <= 0) {
            characterController.remove(enemy.getId());
        } else {
            characterController.update(enemy);
        }
        partita.aggiornaSalvataggio();
        partitaController.update(partita);
    }

    /**
     * Aggiorna sia la disponibilità del bottone Cura (cooldown + tetto massimo)
     * sia la label informativa che mostra quanto si curerà e quante cure restano.
     */
    private void updateHealButtonAvailability() {
        boolean cooldownOk = turnsSinceHeal >= HEAL_COOLDOWN_TURNS;
        boolean cureResidue = cureUsate < MAX_CURE_PER_BATTLE;
        btnCura.setDisable(!cooldownOk || !cureResidue || battleOver);

        lblCuraInfo.setText("Cura: +" + (HEAL_AMOUNT*hero.getHp()/100) + " HP — "
                + (MAX_CURE_PER_BATTLE - cureUsate) + "/" + MAX_CURE_PER_BATTLE + " rimaste");
    }
}