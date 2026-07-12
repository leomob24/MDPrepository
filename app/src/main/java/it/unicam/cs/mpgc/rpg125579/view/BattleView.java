package it.unicam.cs.mpgc.rpg125579.view;

import it.unicam.cs.mpgc.rpg125579.controller.*;
import it.unicam.cs.mpgc.rpg125579.model.entity.Character;
import it.unicam.cs.mpgc.rpg125579.model.entity.Partita;
import it.unicam.cs.mpgc.rpg125579.model.entity.Superhero;
import it.unicam.cs.mpgc.rpg125579.model.service.Battaglia;
import it.unicam.cs.mpgc.rpg125579.model.service.RisultatoAttacco;
import it.unicam.cs.mpgc.rpg125579.model.service.RisultatoCura;
import it.unicam.cs.mpgc.rpg125579.model.service.RisultatoVittoria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Controller della schermata di battaglia. Delega tutta la logica di
 * combattimento a {@link Battaglia}: si occupa solo di tradurre gli eventi
 * dei bottoni in chiamate al dominio, aggiornare le label e scrivere il log.
 */
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

    private final Controller<Character> characterController = new BasicController<>(Character.class);
    private final Controller<Partita> partitaController = new BasicController<>(Partita.class);

    private Partita partita;
    private Battaglia battaglia;
    private Superhero hero;
    private Character enemy;

    public void initBattle(Partita partita, Character enemy) {
        this.partita = partita;
        this.hero = partita.getSuperhero();
        this.enemy = enemy;
        this.battaglia = new Battaglia(hero, enemy);

        refreshLabels();
        refreshCuraInfo();
        txtBattleLog.clear();
        txtBattleLog.appendText("La battaglia ha inizio: " + hero.getName() + " contro " + enemy.getName() + "!\n");
    }

    private void refreshLabels() {
        lblHeroName.setText(hero.getName());
        lblHeroHp.setText(hero.getHpAttuali() + "/" + hero.getHp());
        lblHeroAtk.setText(String.valueOf(hero.getAtk()));
        lblHeroDef.setText(String.valueOf(hero.getDef()));
        lblHeroLevel.setText("Lv. " + hero.getLivello());
        lblHeroXp.setText(hero.getEsperienza() + "/" + battaglia.esperienzaRichiesta() + " XP");

        lblEnemyName.setText(enemy.getName());
        lblEnemyHp.setText(enemy.getHpAttuali() + "/" + enemy.getHp());
        lblEnemyAtk.setText(String.valueOf(enemy.getAtk()));
        lblEnemyDef.setText(String.valueOf(enemy.getDef()));
    }

    private void refreshCuraInfo() {
        lblCuraInfo.setText("Cura: +" + battaglia.getHealAmount() + " HP — "
                + battaglia.getCureRimaste() + "/" + battaglia.getMaxCurePerBattle() + " rimaste");
        btnCura.setDisable(!battaglia.isCuraDisponibile());
    }

    @FXML
    void handleAttack(ActionEvent event) {
        if (battaglia.isTerminata()) return;

        RisultatoAttacco esito = battaglia.eseguiAttacco();
        txtBattleLog.appendText(hero.getName() + " attacca! " + enemy.getName()
                + " scende a " + enemy.getHpAttuali() + " HP.\n");
        lblEnemyHp.setText(enemy.getHpAttuali() + "/" + enemy.getHp());

        if (esito.nemicoSconfitto()) {
            gestisciVittoria();
            return;
        }

        registraContrattacco(esito.eroeSconfitto());
    }

    @FXML
    void handleDefend(ActionEvent event) {
        if (battaglia.isTerminata()) return;

        txtBattleLog.appendText("Ti stai difendendo! Difesa aumentata per questo turno.\n");
        RisultatoAttacco esito = battaglia.eseguiDifesa();

        registraContrattacco(esito.eroeSconfitto());
    }

    @FXML
    void handleHeal(ActionEvent event) {
        if (battaglia.isTerminata()) return;

        RisultatoCura esito = battaglia.eseguiCura();
        if (!esito.eseguita()) {
            txtBattleLog.appendText(esito.motivoRifiuto() + "\n");
            return;
        }

        txtBattleLog.appendText("Hai usato Cura! Hai recuperato " + esito.hpRecuperati() + " HP ("
                + hero.getHpAttuali() + "/" + hero.getHp() + "). Cure rimaste: "
                + esito.cureRimaste() + "/" + battaglia.getMaxCurePerBattle() + ".\n");
        lblHeroHp.setText(hero.getHpAttuali() + "/" + hero.getHp());
        refreshCuraInfo();

        registraContrattacco(esito.eroeSconfitto());
    }

    @FXML
    void handleEscape(ActionEvent event) {
        txtBattleLog.appendText("Sei scappato dalla battaglia!\n");
        persistState();

        MainView controller = ViewNavigator.switchTo("/MainView.fxml", "Superbattles - " + hero.getName());
        controller.initPartita(partita);
    }

    /**
     * Scrive nel log l'esito del contrattacco del nemico (comune ad attacco,
     * difesa e cura) e conclude la battaglia se l'eroe è stato sconfitto.
     */
    private void registraContrattacco(boolean eroeSconfitto) {
        txtBattleLog.appendText(enemy.getName() + " contrattacca! " + hero.getName()
                + " scende a " + hero.getHpAttuali() + " HP.\n");
        lblHeroHp.setText(hero.getHpAttuali() + "/" + hero.getHp());
        refreshCuraInfo();

        if (eroeSconfitto) {
            txtBattleLog.appendText("\n💀 Sei stato sconfitto da " + enemy.getName() + "...\n");
            endBattle();
        }
    }

    private void gestisciVittoria() {
        txtBattleLog.appendText("\n🏆 Hai sconfitto " + enemy.getName() + "!\n");

        RisultatoVittoria vittoria = battaglia.assegnaEsperienzaVittoria();
        txtBattleLog.appendText("Hai guadagnato " + vittoria.xpOttenuta() + " punti esperienza!\n");
        if (vittoria.livelliGuadagnati() > 0) {
            txtBattleLog.appendText("⭐ LEVEL UP! Ora sei livello " + hero.getLivello()
                    + " (" + (vittoria.livelliGuadagnati() > 1 ? vittoria.livelliGuadagnati() + " livelli guadagnati, " : "")
                    + "statistiche massime aumentate)!\n");
        }
        refreshLabels();
        endBattle();
    }

    private void endBattle() {
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
}