package it.unicam.cs.mpgc.rpg125579.model.service;

/**
 * Esito della richiesta di cura durante una {@link Battaglia}: può essere
 * rifiutata (cooldown non scaduto o tetto massimo raggiunto) oppure eseguita,
 * riportando quanti HP sono stati recuperati e se il contrattacco del nemico
 * ha sconfitto l'eroe.
 */
public record RisultatoCura(boolean eseguita, String motivoRifiuto, int hpRecuperati,
                            int cureRimaste, boolean eroeSconfitto) {

    public static RisultatoCura rifiutata(String motivo) {
        return new RisultatoCura(false, motivo, 0, 0, false);
    }

    public static RisultatoCura eseguita(int hpRecuperati, int cureRimaste, boolean eroeSconfitto) {
        return new RisultatoCura(true, null, hpRecuperati, cureRimaste, eroeSconfitto);
    }
}