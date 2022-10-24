package dev.bravo.autocomplete.dao.repository;

import dev.bravo.autocomplete.dao.entity.node.TrieNodeEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

public interface TrieNodeRepository extends ReactiveNeo4jRepository<TrieNodeEntity, Long> {
}
