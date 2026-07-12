package it.unicam.cs.mpgc.rpg125579.model.service;

import it.unicam.cs.mpgc.rpg125579.model.entity.Character;
import it.unicam.cs.mpgc.rpg125579.model.entity.Superhero;
import lombok.Getter;

/**
 * Rappresenta una singola sessione di combattimento tra un {@link Superhero}
 * e un nemico. Incapsula lo stato (turni dalla cura, cure usate, esito) e le
 * regole del combattimento (cooldown cura, tetto massimo cure, bonus difesa),
 * orchestrando {@link GestoreCombattimento} (danno) e {@link GestoreLivelli}
 * (progressione) senza che la View debba conoscerne i dettagli.
 * <p>
 * Nessun setter esposto: lo stato interno (turni, cure usate) può cambiare
 * solo tramite i metodi di dominio, mai da chiamate esterne dirette.
 */
public class Battaglia {

    private static final int HEAL_AMOUNT = 25;
    private static final int HEAL_COOLDOWN_TURNS = 3;
    private static final int MAX_CURE_PER_BATTLE = 3;
    private static final double DEFEND_BONUS_MULTIPLIER = 1.5;

    private final GestoreCombattimento gestoreCombattimento = new GestoreCombattimento();
    private final GestoreLivelli gestoreLivelli = new GestoreLivelli();

    @Getter
    private final Superhero hero;

    @Getter
    private final Character enemy;

    @Getter
    private boolean terminata = false;

    // Dettagli implementativi: NIENTE @Getter, restano privati.
    private int turnsSinceHeal = HEAL_COOLDOWN_TURNS;
    private int cureUsate = 0;

    public Battaglia(Superhero hero, Character enemy) {
        this.hero = hero;
        this.enemy = enemy;
    }

    public int getHealAmount() {
        return HEAL_AMOUNT;
    }

    public int getMaxCurePerBattle() {
        return MAX_CURE_PER_BATTLE;
    }

    public int getCureRimaste() {
        return MAX_CURE_PER_BATTLE - cureUsate;
    }

    public boolean isCuraDisponibile() {
        return !terminata && cureUsate < MAX_CURE_PER_BATTLE && turnsSinceHeal >= HEAL_COOLDOWN_TURNS;
    }

    public int esperienzaRichiesta() {
        return gestoreLivelli.esperienzaRichiesta(hero.getLivello());
    }

    public RisultatoAttacco eseguiAttacco() {
        verificaBattagliaInCorso();

        gestoreCombattimento.eseguiAttacco(hero, enemy);
        if (enemy.getHpAttuali() <= 0) {
            terminata = true;
            return new RisultatoAttacco(true, false);
        }

        boolean eroeSconfitto = eseguiTurnoNemico();
        return new RisultatoAttacco(false, eroeSconfitto);
    }

    public RisultatoAttacco eseguiDifesa() {
        verificaBattagliaInCorso();

        int defOriginale = hero.getDef();
        hero.setDef((int) Math.round(defOriginale * DEFEND_BONUS_MULTIPLIER));

        boolean eroeSconfitto = eseguiTurnoNemico();

        hero.setDef(defOriginale);
        return new RisultatoAttacco(false, eroeSconfitto);
    }

    public RisultatoCura eseguiCura() {
        verificaBattagliaInCorso();

        if (cureUsate >= MAX_CURE_PER_BATTLE) {
            return RisultatoCura.rifiutata("Hai già usato tutte le cure disponibili per questo combattimento.");
        }
        if (turnsSinceHeal < HEAL_COOLDOWN_TURNS) {
            return RisultatoCura.rifiutata("Cura non ancora disponibile.");
        }

        int nuoviHp = Math.min(hero.getHpAttuali() + HEAL_AMOUNT, hero.getHp());
        int recuperati = nuoviHp - hero.getHpAttuali();
        hero.setHpAttuali(nuoviHp);
        turnsSinceHeal = 0;
        cureUsate++;

        boolean eroeSconfitto = eseguiTurnoNemico();
        return RisultatoCura.eseguita(recuperati, getCureRimaste(), eroeSconfitto);
    }

    public RisultatoVittoria assegnaEsperienzaVittoria() {
        int xpOttenuta = gestoreLivelli.calcolaEsperienzaOttenuta(enemy);
        int livelliGuadagnati = gestoreLivelli.assegnaEsperienza(hero, xpOttenuta);
        return new RisultatoVittoria(xpOttenuta, livelliGuadagnati);
    }

    private boolean eseguiTurnoNemico() {
        turnsSinceHeal++;
        gestoreCombattimento.eseguiAttacco(enemy, hero);
        if (hero.getHpAttuali() <= 0) {
            terminata = true;
            return true;
        }
        return false;
    }

    private void verificaBattagliaInCorso() {
        if (terminata) {
            throw new IllegalStateException("La battaglia è già terminata");
        }
    }
}