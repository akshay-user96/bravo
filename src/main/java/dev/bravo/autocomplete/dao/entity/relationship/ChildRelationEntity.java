package dev.bravo.autocomplete.dao.entity.relationship;

import dev.bravo.autocomplete.dao.entity.node.TrieNodeEntity;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@Builder
@RelationshipProperties
public class ChildRelationEntity {
    @RelationshipId @GeneratedValue
    private Long id;
    @TargetNode
    private TrieNodeEntity endNode;
    private char character;
}
