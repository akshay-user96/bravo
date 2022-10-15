package dev.bravo.autocomplete.dao.repository;

import dev.bravo.autocomplete.dao.entity.RootNodeEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

public interface RootNodeRepository extends ReactiveNeo4jRepository<RootNodeEntity, String> {
}
