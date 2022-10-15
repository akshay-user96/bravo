package dev.bravo.autocomplete.dao.query.mapper;

import dev.bravo.autocomplete.dao.entity.TrieNodeEntity;
import dev.bravo.helper.Constants;
import org.neo4j.driver.Record;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class RecordToEntityMapper {
    private final Logger logger = Logger.getLogger("RecordToEntityMapper");
    public TrieNodeEntity mapRecordToTrieNodeEntity(Record record) {
        if (record != null) {
            return TrieNodeEntity.builder()
//                .id(record.get("t").get("id").asLong())     // TODO: How to fetch Id?
                    .topRecommendations(record.get("t").get(Constants.topRecommendations).asList().stream()
                            .map(Objects::toString)
                            .collect(Collectors.toList()))
                    .build();
        } else {
            return null;
        }
    }
}
