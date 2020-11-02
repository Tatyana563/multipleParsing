package ua.tpetrenko.esp.core.factories;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.tpetrenko.esp.api.handlers.CityHandler;
import ua.tpetrenko.esp.core.components.CityHandlerImpl;

/**
 * @author Roman Zdoronok
 */
@Component
@RequiredArgsConstructor
public class CityHandlerFactory {

    public CityHandler getCityHandler() {
        return new CityHandlerImpl();
    }

}
