package it.unicam.cs.mpgc.rpg125579.controller;

import java.util.List;

/**
 * Contratto generico che specifica le operazioni fondamentali di persistenza (CRUD)
 * per la gestione delle entità nel sistema.
 * Implementatori di questa interfaccia devono fornire una soluzione omogenea
 * per le operazioni di creazione, lettura, aggiornamento ed eliminazione di dati,
 * garantendo consistenza e prevedibilità nel layer di accesso ai dati.
 *
 * @param <T> Tipo parametrico dell'entità da gestire.
 */
public interface Controller<T> {

    /**
     * Persiste una nuova entità nel data layer sottostante.
     *
     * @param element L'oggetto di tipo {@code T} da persistere.
     */
    void add(T element);

    /**
     * Cancella dall'archivio dati una specifica entità identificata dal suo ID.
     *
     * @param id Identificatore univoco dell'entità da rimuovere.
     */
    void remove(Long id);

    /**
     * Modifica le informazioni di un'entità già memorizzata nel data layer.
     *
     * @param entity L'entità con i campi aggiornati.
     */
    void update(T entity);

    /**
     * Recupera dalla sorgente dati la raccolta completa di tutte le istanze
     * del tipo {@code T}.
     *
     * @return {@link List} di tutte le entità di tipo {@code T} presenti in archivio.
     */
    List<T> getAll();
}

