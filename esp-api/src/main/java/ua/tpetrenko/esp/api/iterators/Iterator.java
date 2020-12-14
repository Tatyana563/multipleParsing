package ua.tpetrenko.esp.api.iterators;

import java.util.List;
import java.util.function.Consumer;
import lombok.Value;


/**
 * @author Roman Zdoronok
 */
public interface Iterator<D, H> {

    void forEach(Consumer<Pair<D, H>> consumer);

    void forEachPage(Consumer<List<Pair<D, H>>> consumer);

    void forEachPage(int pageSize, Consumer<List<Pair<D, H>>> consumer);


    @Value(staticConstructor = "of")
    class Pair<D, H> {
        D item;
        H handler;
    }
}
