package dev.bravo.autocomplete.dao.entity.node;

import dev.bravo.autocomplete.dao.entity.relationship.ChildRelationEntity;
import dev.bravo.helper.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.List;

@Node(Constants.rootNodeEntity)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RootNodeEntity {
    @Id @GeneratedValue
    private Long id;
    @Property(name = "userId")
    private String userId;
    @Relationship(type = "CHILD", direction = Relationship.Direction.OUTGOING)
    private List<ChildRelationEntity> children = new ArrayList<>();

    public void addChild(ChildRelationEntity childRelation) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(childRelation);
    }
}
