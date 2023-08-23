package io.github.iamantoo.repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Repository che mantiene gli elementi in una lista sincronizzata con un file.
 * Il tipo T deve implementare l'interfaccia Serializable.
 * Uso:
 * <pre>
 * var item = new MyItem(); // tipo di dato gestito
 * Repository<MyItem> fr = new FileRepository<>("nome del file");
 * fr.addOrUpdate(item);
 * fr.delete(item);
 * fr.get(i -> i.property == filtro);
 * </pre>
 *
 * @param <T> il tipo di dato gestito.
 */
public class FileRepository<T extends Serializable> implements Repository<T> {

    private final List<T> items = new ArrayList<>();
    private final String fileName;

    public FileRepository(String fileName) {
        this.fileName = fileName;
        load();
    }

    protected boolean load() {
        try {
            try (var is = new ObjectInputStream(new FileInputStream(fileName))) {
                @SuppressWarnings("unchecked") var list = (List<T>) is.readObject();
                items.clear();
                items.addAll(list);
                return true;
            } catch (ClassNotFoundException e) {
                System.err.println(e);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return false;
    }

    protected boolean save() {
        try {
            try (var os = new ObjectOutputStream(new FileOutputStream(fileName))) {
                os.writeObject(items);
                return true;
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return false;
    }

    @Override
    public Optional<T> addOrUpdate(T entity) {
        var item = items.stream().filter(i -> items.equals(entity)).findFirst();
        item.ifPresent(items::remove);
        items.add(entity);
        if (save())
            return item;
        return Optional.empty();
    }

    @Override
    public Optional<T> delete(T entity) {
        if (items.remove(entity) && save())
            return Optional.of(entity);

        return Optional.empty();
    }

    @Override
    public void clear() {
        items.clear();
        save();
    }

    @Override
    public Stream<T> get(Predicate<T> searchFor) {
        return items.stream().filter(searchFor);
    }

    @Override
    public Stream<T> getAll() {
        return items.stream();
    }
}
