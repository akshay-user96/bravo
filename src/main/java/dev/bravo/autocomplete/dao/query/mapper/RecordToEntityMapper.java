package dev.bravo.autocomplete.dao.query.mapper;

import dev.bravo.autocomplete.dao.entity.node.TrieNodeEntity;
import dev.bravo.helper.Constants;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class RecordToEntityMapper {
    private final Logger logger = Logger.getLogger("RecordToEntityMapper");
    public TrieNodeEntity mapRecordToTrieNodeEntity(Map<String, Object> recordMap) {
        logger.info("mapRecordToTrieNodeEntity invoked with recordMap: " + recordMap);
        if (recordMap != null) {
            Map<String, Object> valueMap = (Map<String, Object>) recordMap.get("t");
            return TrieNodeEntity.builder()
                .id((Long)valueMap.get(Constants.id))
                    //.recommendationScores(valueMap.get(Constants.recommendationScores))
                    .build();
        } else {
            return null;
        }
    }
}
