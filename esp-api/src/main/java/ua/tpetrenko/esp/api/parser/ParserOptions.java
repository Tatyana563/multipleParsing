package ua.tpetrenko.esp.api.parser;

import java.util.Set;

/**
 * @author Roman Zdoronok
 */
public interface ParserOptions {
    Set<String> getIncludedCategories();
    Set<String> getExcludedCategories();
}
