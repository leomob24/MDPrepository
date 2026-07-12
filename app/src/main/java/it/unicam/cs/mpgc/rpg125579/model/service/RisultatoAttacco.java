package it.unicam.cs.mpgc.rpg125579.model.service;

/**
 * Esito di un'azione offensiva (attacco o difesa) eseguita durante una {@link Battaglia}.
 */
public record RisultatoAttacco(boolean nemicoSconfitto, boolean eroeSconfitto) {
}