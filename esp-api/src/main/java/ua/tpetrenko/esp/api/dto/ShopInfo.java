package ua.tpetrenko.esp.api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Roman Zdoronok
 */
//@RequiredArgsConstructor
//@Getter
public class ShopInfo {
    private final String name;
    private final  String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public ShopInfo(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
