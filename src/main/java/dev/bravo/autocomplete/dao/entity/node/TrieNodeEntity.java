package dev.bravo.autocomplete.dao.entity.node;

import dev.bravo.autocomplete.dao.converter.ScoreRecommendationMapConverter;
import dev.bravo.autocomplete.dao.entity.relationship.ChildRelationEntity;
import dev.bravo.helper.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Node(Constants.trieNodeEntity)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrieNodeEntity {
    @Id @GeneratedValue
    private Long id;
    @Property(name = Constants.recommendationScores)
    @CompositeProperty(converter = ScoreRecommendationMapConverter.class)
    private Map<String, Integer> recommendationScores = new HashMap<>();
    @Relationship(type = "CHILD", direction = Relationship.Direction.OUTGOING)
    private List<ChildRelationEntity> children = new ArrayList<>();

    public void addChild(ChildRelationEntity childRelation) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(childRelation);
    }

    public void addFrequency(String word, int scoreToAdd) {
        if (recommendationScores == null) {
            recommendationScores = new HashMap<>();
        }
        Integer existingScore = recommendationScores.getOrDefault(word, 0);
        recommendationScores.put(word, existingScore + scoreToAdd);
    }
}
