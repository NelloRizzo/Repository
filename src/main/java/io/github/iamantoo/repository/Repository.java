package io.github.iamantoo.repository;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Concetto di repository quale elemento in grado di immagazzinare e restituire insiemi di oggetti.
 *
 * @param <T> tipo di dato gestito.
 */
public interface Repository<T> {

    /**
     * Aggiunge un nuovo elemento o modifica un elemento esistente.
     * La ricerca avviene attraverso il metodo <code>equals()</code> sull'entità passata come parametro.
     *
     * @param entity elemento da aggiungere o modificare.
     * @return l'oggetto dopo la modifica apportata o <code>Optional.empty()</code> se l'operazione non è andata a buon fine.
     */
    Optional<T> addOrUpdate(T entity);

    /**
     * Elimina un elemento esistente.
     * La ricerca avviene attraverso il metodo <code>equals()</code> sull'entità passata come parametro.
     *
     * @param entity elemento da eliminare.
     * @return l'oggetto eliminato (oppure <code>Optional.empty()</code> se l'elemento non è stato trovato).
     */
    Optional<T> delete(T entity);

    /**
     * Elimina tutti gli elementi dall'elenco.
     */
    void clear();
    /**
     * Recupera un insieme di elementi.
     * @param searchFor predicato per filtrare i risultati.
     * @return l'elenco degli elementi che soddisfano il predicato.
     */
    Stream<T> get(Predicate<T> searchFor);

    /**
     * Recupera tutti gli elementi.
     * @return l'elenco degli elementi gestiti.
     */
    Stream<T> getAll();
}
