package dev.bravo.autocomplete.dao.converter;

import org.neo4j.driver.Value;
import org.neo4j.driver.Values;
import org.springframework.data.neo4j.core.convert.Neo4jConversionService;
import org.springframework.data.neo4j.core.convert.Neo4jPersistentPropertyToMapConverter;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class ScoreRecommendationMapConverter implements
        Neo4jPersistentPropertyToMapConverter<String, Map<String, Integer>> {
    /**
     * @param property
     * @param neo4jConversionService
     * @return
     */
    @Override
    public Map<String, Value> decompose(Map<String, Integer> property, Neo4jConversionService neo4jConversionService) {
        Map<String, Value> finalMap = new HashMap<>();
        if (property != null) {
            for (Map.Entry<String, Integer> entry : property.entrySet()) {
                finalMap.put(entry.getKey(), Values.value(entry.getValue()));
            }
        }
        return finalMap;
    }

    /**
     * @param source
     * @param neo4jConversionService
     * @return
     */
    @Override
    public Map<String, Integer> compose(Map<String, Value> source, Neo4jConversionService neo4jConversionService) {
        Map<String, Integer> finalMap = new HashMap<>();
        for (Map.Entry<String, Value> entry : source.entrySet()) {
            finalMap.put(entry.getKey(), entry.getValue().asInt());
        }
        return finalMap;
    }
}
