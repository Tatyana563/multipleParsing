package ua.tpetrenko.esp.core.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.handlers.ProductItemHandler;
import ua.tpetrenko.esp.core.components.ProductItemHandlerImpl;

/**
 * @author Roman Zdoronok
 */
@Component
@RequiredArgsConstructor
public class ProductItemHandlerFactory {

    public ProductItemHandler getProductItemHandler() {
        return new ProductItemHandlerImpl();
    }
}
