package it.unicam.cs.mpgc.rpg125579.view;

import it.unicam.cs.mpgc.rpg125579.controller.*;
import it.unicam.cs.mpgc.rpg125579.model.entity.*;
import it.unicam.cs.mpgc.rpg125579.model.entity.Character;
import it.unicam.cs.mpgc.rpg125579.model.service.GestoreBattaglia;
import it.unicam.cs.mpgc.rpg125579.model.service.RisultatoAttacco;
import it.unicam.cs.mpgc.rpg125579.model.service.RisultatoCura;
import it.unicam.cs.mpgc.rpg125579.model.service.RisultatoVittoria;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Controller della schermata di battaglia. Delega le regole di combattimento
 * a {@link GestoreBattaglia}; si occupa di tradurre eventi UI in chiamate al
 * dominio, aggiornare le label/log e decidere quando persistere lo stato.
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
    private final Controller<Battaglia> battagliaController = new BasicController<>(Battaglia.class);
    private final GestoreBattaglia gestoreBattaglia = new GestoreBattaglia();

    private Partita partita;
    private Battaglia battaglia;
    private Superhero hero;
    private Mostro enemy;

    private boolean battagliaConclusa = false;

    public void initBattle(Partita partita, Character enemyCharacter) {
        this.partita = partita;
        this.hero = partita.getSuperhero();
        this.enemy = (Mostro) enemyCharacter;
        this.battaglia = trovaOCreaBattaglia(this.enemy);
        this.battagliaConclusa = false;

        refreshLabels();
        refreshCuraInfo();
        txtBattleLog.clear();
        txtBattleLog.appendText("La battaglia continua: " + hero.getName() + " contro " + enemy.getName() + "!\n");
    }

    /**
     * Recupera la battaglia già in corso contro questo nemico (persistita da
     * un tentativo precedente, es. dopo una fuga), oppure ne crea una nuova.
     */
    private Battaglia trovaOCreaBattaglia(Mostro enemy) {
        return battagliaController.getAll().stream()
                .filter(b -> b.getEnemy() != null && enemy.getId().equals(b.getEnemy().getId()))
                .findFirst()
                .orElseGet(() -> {
                    Battaglia nuova = new Battaglia(enemy);
                    battagliaController.add(nuova);
                    return nuova;
                });
    }

    private void refreshLabels() {
        lblHeroName.setText(hero.getName());
        lblHeroHp.setText(hero.getHpAttuali() + "/" + hero.getHp());
        lblHeroAtk.setText(String.valueOf(hero.getAtk()));
        lblHeroDef.setText(String.valueOf(hero.getDef()));
        lblHeroLevel.setText("Lv. " + hero.getLivello());
        lblHeroXp.setText(hero.getEsperienza() + "/" + gestoreBattaglia.esperienzaRichiesta(hero) + " XP");

        lblEnemyName.setText(enemy.getName());
        lblEnemyHp.setText(enemy.getHpAttuali() + "/" + enemy.getHp());
        lblEnemyAtk.setText(String.valueOf(enemy.getAtk()));
        lblEnemyDef.setText(String.valueOf(enemy.getDef()));
    }

    private void refreshCuraInfo() {
        lblCuraInfo.setText("Cura: +" + GestoreBattaglia.HEAL_AMOUNT + " HP — "
                + gestoreBattaglia.getCureRimaste(battaglia) + "/" + GestoreBattaglia.MAX_CURE_PER_BATTLE + " rimaste");
        btnCura.setDisable(!gestoreBattaglia.isCuraDisponibile(battaglia));
    }

    @FXML
    void handleAttack(ActionEvent event) {
        RisultatoAttacco esito = gestoreBattaglia.eseguiAttacco(battaglia, hero, enemy);
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
        txtBattleLog.appendText("Ti stai difendendo! Difesa aumentata per questo turno.\n");
        RisultatoAttacco esito = gestoreBattaglia.eseguiDifesa(battaglia, hero, enemy);
        registraContrattacco(esito.eroeSconfitto());
    }

    @FXML
    void handleHeal(ActionEvent event) {
        RisultatoCura esito = gestoreBattaglia.eseguiCura(battaglia, hero, enemy);
        if (!esito.eseguita()) {
            txtBattleLog.appendText(esito.motivoRifiuto() + "\n");
            return;
        }

        txtBattleLog.appendText("Hai usato Cura! Hai recuperato " + esito.hpRecuperati() + " HP ("
                + hero.getHpAttuali() + "/" + hero.getHp() + "). Cure rimaste: "
                + esito.cureRimaste() + "/" + GestoreBattaglia.MAX_CURE_PER_BATTLE + ".\n");
        lblHeroHp.setText(hero.getHpAttuali() + "/" + hero.getHp());
        refreshCuraInfo();

        registraContrattacco(esito.eroeSconfitto());
    }

    @FXML
    void handleEscape(ActionEvent event) {
        if (battagliaConclusa) {
            // La battaglia è già finita (vittoria o sconfitta) e già persistita
            // in endBattle(): qui il bottone serve solo a tornare alla base,
            // niente da salvare di nuovo (enemy e Battaglia potrebbero non
            // esistere più nel DB).
            tornaAllaBase();
            return;
        }

        txtBattleLog.appendText("Sei scappato dalla battaglia!\n");
        salvaProgressi();
        tornaAllaBase();
    }

    private void tornaAllaBase() {
        MainView controller = ViewNavigator.switchTo("/MainView.fxml", "Superbattles - " + hero.getName());
        controller.initPartita(partita);
    }

    private void registraContrattacco(boolean eroeSconfitto) {
        txtBattleLog.appendText(enemy.getName() + " contrattacca! " + hero.getName()
                + " scende a " + hero.getHpAttuali() + " HP.\n");
        lblHeroHp.setText(hero.getHpAttuali() + "/" + hero.getHp());
        refreshCuraInfo();

        if (eroeSconfitto) {
            txtBattleLog.appendText("\n💀 Sei stato sconfitto da " + enemy.getName() + "...\n");
            endBattle(true);
        } else {
            salvaProgressi();
        }
    }

    private void gestisciVittoria() {
        txtBattleLog.appendText("\n🏆 Hai sconfitto " + enemy.getName() + "!\n");

        RisultatoVittoria vittoria = gestoreBattaglia.assegnaEsperienzaVittoria(hero, enemy);
        txtBattleLog.appendText("Hai guadagnato " + vittoria.xpOttenuta() + " punti esperienza!\n");
        if (vittoria.livelliGuadagnati() > 0) {
            txtBattleLog.appendText("⭐ LEVEL UP! Ora sei livello " + hero.getLivello()
                    + " (" + (vittoria.livelliGuadagnati() > 1 ? vittoria.livelliGuadagnati() + " livelli guadagnati, " : "")
                    + "statistiche massime aumentate)!\n");
        }
        refreshLabels();
        endBattle(false);
    }

    /**
     * Conclude definitivamente lo scontro. Se {@code eroeSconfitto} è vero,
     * marca la partita come terminata (Game Over): resta persistita per
     * eventuale classifica, ma non sarà più possibile affrontare battaglie.
     */
    private void endBattle(boolean eroeSconfitto) {
        btnAtk.setDisable(true);
        btnDef.setDisable(true);
        btnCura.setDisable(true);
        btnScappa.setText("Torna alla base");
        battagliaConclusa = true;

        battagliaController.remove(battaglia.getId());
        characterController.update(hero);
        if (enemy.getHpAttuali() <= 0) {
            characterController.remove(enemy.getId());
        } else {
            characterController.update(enemy);
        }

        if (eroeSconfitto) {
            partita.setGameOver(true);
        }
        partita.aggiornaSalvataggio();
        partitaController.update(partita);
    }

    /**
     * Salva lo stato corrente (eroe, nemico, contatori della battaglia) senza
     * concludere lo scontro: permette di riprenderlo esattamente da dove era
     * rimasto, incluse cure già usate e cooldown attivo.
     */
    private void salvaProgressi() {
        characterController.update(hero);
        characterController.update(enemy);
        battagliaController.update(battaglia);
        partita.aggiornaSalvataggio();
        partitaController.update(partita);
    }
}