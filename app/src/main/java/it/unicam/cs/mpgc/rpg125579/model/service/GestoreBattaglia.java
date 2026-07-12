package it.unicam.cs.mpgc.rpg125579.model.service;

import it.unicam.cs.mpgc.rpg125579.model.entity.Battaglia;
import it.unicam.cs.mpgc.rpg125579.model.entity.Character;
import it.unicam.cs.mpgc.rpg125579.model.entity.Superhero;

/**
 * Applica le regole di un combattimento (cooldown cura, tetto massimo cure,
 * bonus difesa, contrattacco) a una {@link Battaglia} esistente, orchestrando
 * {@link GestoreCombattimento} (danno) e {@link GestoreLivelli} (progressione).
 * <p>
 * {@code hero} ed {@code enemy} vengono passati esplicitamente ad ogni metodo
 * invece di essere derivati da {@code Battaglia} (che li referenzia solo
 * tramite relazioni JPA lazy): il chiamante li possiede già come entità
 * pienamente caricate, evitando così di dover inizializzare proxy Hibernate
 * fuori da una sessione attiva.
 */
public class GestoreBattaglia {

    public static final int HEAL_AMOUNT = 25;
    public static final int HEAL_COOLDOWN_TURNS = 3;
    public static final int MAX_CURE_PER_BATTLE = 3;
    private static final double DEFEND_BONUS_MULTIPLIER = 1.5;

    private final GestoreCombattimento gestoreCombattimento = new GestoreCombattimento();
    private final GestoreLivelli gestoreLivelli = new GestoreLivelli();

    public boolean isCuraDisponibile(Battaglia battaglia) {
        return battaglia.getCureUsate() < MAX_CURE_PER_BATTLE
                && battaglia.getTurnsSinceHeal() >= HEAL_COOLDOWN_TURNS;
    }

    public int getCureRimaste(Battaglia battaglia) {
        return MAX_CURE_PER_BATTLE - battaglia.getCureUsate();
    }

    public int esperienzaRichiesta(Superhero hero) {
        return gestoreLivelli.esperienzaRichiesta(hero.getLivello());
    }

    public RisultatoAttacco eseguiAttacco(Battaglia battaglia, Superhero hero, Character enemy) {
        gestoreCombattimento.eseguiAttacco(hero, enemy);
        if (enemy.getHpAttuali() <= 0) {
            return new RisultatoAttacco(true, false);
        }

        boolean eroeSconfitto = eseguiTurnoNemico(battaglia, hero, enemy);
        return new RisultatoAttacco(false, eroeSconfitto);
    }

    public RisultatoAttacco eseguiDifesa(Battaglia battaglia, Superhero hero, Character enemy) {
        int defOriginale = hero.getDef();
        hero.setDef((int) Math.round(defOriginale * DEFEND_BONUS_MULTIPLIER));

        boolean eroeSconfitto = eseguiTurnoNemico(battaglia, hero, enemy);

        hero.setDef(defOriginale);
        return new RisultatoAttacco(false, eroeSconfitto);
    }

    public RisultatoCura eseguiCura(Battaglia battaglia, Superhero hero, Character enemy) {
        if (!isCuraDisponibile(battaglia)) {
            String motivo = battaglia.getCureUsate() >= MAX_CURE_PER_BATTLE
                    ? "Hai già usato tutte le cure disponibili per questo combattimento."
                    : "Cura non ancora disponibile.";
            return RisultatoCura.rifiutata(motivo);
        }

        int nuoviHp = Math.min(hero.getHpAttuali() + HEAL_AMOUNT, hero.getHp());
        int recuperati = nuoviHp - hero.getHpAttuali();
        hero.setHpAttuali(nuoviHp);
        battaglia.setTurnsSinceHeal(0);
        battaglia.setCureUsate(battaglia.getCureUsate() + 1);

        boolean eroeSconfitto = eseguiTurnoNemico(battaglia, hero, enemy);
        return RisultatoCura.eseguita(recuperati, getCureRimaste(battaglia), eroeSconfitto);
    }

    public RisultatoVittoria assegnaEsperienzaVittoria(Superhero hero, Character enemy) {
        int xpOttenuta = gestoreLivelli.calcolaEsperienzaOttenuta(enemy);
        int livelliGuadagnati = gestoreLivelli.assegnaEsperienza(hero, xpOttenuta);
        return new RisultatoVittoria(xpOttenuta, livelliGuadagnati);
    }

    private boolean eseguiTurnoNemico(Battaglia battaglia, Superhero hero, Character enemy) {
        battaglia.setTurnsSinceHeal(battaglia.getTurnsSinceHeal() + 1);
        gestoreCombattimento.eseguiAttacco(enemy, hero);
        return hero.getHpAttuali() <= 0;
    }
}