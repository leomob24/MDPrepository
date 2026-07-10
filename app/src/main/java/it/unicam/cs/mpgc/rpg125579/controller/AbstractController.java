package it.unicam.cs.mpgc.rpg125579.controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * Implementazione base che realizza l'interfaccia {@link Controller} fornendo
 * le operazioni CRUD (Create, Read, Update, Delete) con supporto Hibernate.
 * Questa classe rappresenta il template comune per tutti i controller che sfruttano
 * Hibernate come framework ORM per la persistenza.
 *
 * @param <T> Tipo dell'entità gestita da questo controller.
 */
public abstract class AbstractController<T> implements Controller<T> {

    protected static SessionFactory sessionFactory;

    private final Class<T> entityClass;

    /**
     * Inizializza il controller con il tipo di entità da gestire.
     * Configura la {@link SessionFactory} di Hibernate al primo istanziamento
     * caricando le impostazioni dal file di configurazione.
     *
     * @param entityClass Classe dell'entità (es. Character.class, Superpower.class).
     */
    protected AbstractController(Class<T> entityClass) {
        this.entityClass = entityClass;
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
    }

    /**
     * Inserisce una nuova entità nel database entro una transazione controllata
     * per assicurare l'atomicità dell'operazione.
     *
     * @param entity L'oggetto da salvare nel database.
     */
    @Override
    public void add(T entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
        }
    }

    /**
     * Elimina dal database un'entità identificata dal valore ID fornito.
     *
     * @param id Chiave primaria dell'entità da eliminare.
     */
    @Override
    public void remove(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            T entity = session.find(entityClass, id);
            if (entity != null) {
                session.remove(entity);
            }
            tx.commit();
        }
    }

    /**
     * Sincronizza lo stato di un'entità tra l'oggetto Java e il database
     * utilizzando il meccanismo di merge fornito da Hibernate.
     *
     * @param entity L'entità con i dati aggiornati da sincronizzare.
     */
    @Override
    public void update(T entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
        }
    }

    /**
     * Esegue una query generica per ottenere tutte le istanze della classe
     * di entità gestita da questo controller.
     *
     * @return Lista di tutte le entità presenti nel database.
     */
    @Override
    public List<T> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from " + entityClass.getSimpleName(), entityClass).list();
        }
    }
}

