package dev.bravo.autocomplete.dao.repository;

import dev.bravo.autocomplete.dao.entity.node.RootNodeEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RootNodeRepository extends ReactiveNeo4jRepository<RootNodeEntity, Long> {
    public Mono<RootNodeEntity> findByUserId(String userId);
}
