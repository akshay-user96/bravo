package dev.bravo.autocomplete.dao.entity;

import dev.bravo.helper.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.util.List;

@Node(Constants.rootNodeEntity)
@AllArgsConstructor
@Data
public class RootNodeEntity {
    @Id
    private String userId;

    @Relationship(type = "CHILD", direction = Relationship.Direction.OUTGOING)
    private List<TrieNodeEntity> children;
}
