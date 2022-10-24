package dev.bravo.autocomplete.dao.repository;

import dev.bravo.autocomplete.dao.entity.relationship.ChildRelationEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

public interface ChildRelationshipRepository extends ReactiveNeo4jRepository<ChildRelationEntity, Long> {
}
