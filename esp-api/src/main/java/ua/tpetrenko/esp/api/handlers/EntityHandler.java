package ua.tpetrenko.esp.api.handlers;

/**
 * @author Roman Zdoronok
 */
public interface EntityHandler<D> {
    void handle(D entity);
}
