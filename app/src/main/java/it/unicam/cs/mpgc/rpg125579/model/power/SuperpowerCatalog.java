package it.unicam.cs.mpgc.rpg125579.model.power;

import java.util.List;

/**
 * Catalogo statico dei superpoteri disponibili nel gioco.
 * I valori sono calibrati per essere bilanciati tra loro
 * e rispetto ai nemici generati da {@code Villain}/{@code Minion}.
 */
public final class SuperpowerCatalog {

    private SuperpowerCatalog() {
    }

    public static final List<Superpower> PREDEFINED = List.of(
            new Superpower("Super Forza", 30, 10, 100, 8,
                    "Forza sovrumana che permette colpi devastanti."),
            new Superpower("Scudo Impenetrabile", 18, 25, 110, 3,
                    "Una difesa quasi impenetrabile, ideale per resistere a lungo."),
            new Superpower("Frecciarossa", 25, 12, 90, 10,
                    "Riflessi e rapidità fuori dal comune."),
            new Superpower("Corpo di Ferro", 20, 18, 140, 4,
                    "Pelle dura come il ferro, incassa ogni colpo senza vacillare."),
            new Superpower("Controllo mentale", 26, 13, 100, 6,
                    "Entra nei pensieri piú viscerali dei nemici e falli rivoltare contro se stessi."),
            new Superpower("Furia Berserk", 34, 8, 85, 12,
                    "Scatena la tua rabbia in un attacco potentissimo.")
    );
}