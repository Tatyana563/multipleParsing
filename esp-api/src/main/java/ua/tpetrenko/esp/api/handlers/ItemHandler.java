package ua.tpetrenko.esp.api.handlers;

/**
 * @author Roman Zdoronok
 */
public interface ItemHandler<D> {
    void handle(D itemDto);
}
