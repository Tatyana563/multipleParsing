package ua.tpetrenko.esp.core.components.iterators;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.function.Consumer;
import ua.tpetrenko.esp.api.iterators.Iterator;

/**
 * @author Roman Zdoronok
 */
public abstract class AbstractIterator<E, D, H> implements Iterator<D, H> {
    private static final int DEFAULT_PAGE_SIZE = 1000;

    protected abstract Page<E> getPage(Pageable pageable);
    protected abstract List<Pair<D, H>> map(Page<E> page);

    @Override
    public void forEach(Consumer<Pair<D, H>> consumer) {
        forEachPage(DEFAULT_PAGE_SIZE, items -> items.forEach(consumer));
    }

    @Override
    public void forEachPage(Consumer<List<Pair<D, H>>> consumer) {
        forEachPage(DEFAULT_PAGE_SIZE, consumer);
    }

    @Override
    public void forEachPage(int pageSize, Consumer<List<Pair<D, H>>> consumer) {
        Page<E> entities;
        Pageable pageRequest = PageRequest.of(0, pageSize);
        do {
            entities = getPage(pageRequest);
            consumer.accept(map(entities));
            pageRequest = pageRequest.next();
        } while (entities.hasNext());
    }
}
