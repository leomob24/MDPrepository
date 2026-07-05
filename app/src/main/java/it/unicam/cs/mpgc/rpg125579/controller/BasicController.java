package it.unicam.cs.mpgc.rpg125579.controller;

/**
 * Realizzazione concreta del pattern Template Method tramite estensione
 * di {@link AbstractController}, fornendo un'istanza pratica e utilizzabile
 * per le operazioni CRUD su qualsiasi entità.
 * Questo controller eredita tutta la logica di persistenza dalla superclasse
 * senza necessità di reimplementazioni specifiche.
 *
 * @param <T> Tipo dell'entità da gestire.
 */
public class BasicController<T> extends AbstractController<T> {

    /**
     * Configura questa istanza del controller per lavorare con l'entità specificata.
     * Delega la configurazione della sessione Hibernate al costruttore della superclasse.
     *
     * @param entityClass Token della classe dell'entità (es. Character.class, Superpower.class).
     */
    public BasicController(Class<T> entityClass) {
        super(entityClass);
    }
}

