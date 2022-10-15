package dev.bravo.autocomplete.dao.entity;

import dev.bravo.helper.Constants;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.util.List;

@Node(Constants.trieNodeEntity)
@Builder
@Data
public class TrieNodeEntity {
    @Id @GeneratedValue
    private Long id;
    @Property(name = Constants.topRecommendations)
    private List<String> topRecommendations;
}
