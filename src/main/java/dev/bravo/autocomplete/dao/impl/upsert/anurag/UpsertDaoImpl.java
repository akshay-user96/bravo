package dev.bravo.autocomplete.dao.impl.upsert.anurag;

import dev.bravo.autocomplete.dao.entity.node.RootNodeEntity;
import dev.bravo.autocomplete.dao.entity.node.TrieNodeEntity;
import dev.bravo.autocomplete.dao.entity.relationship.ChildRelationEntity;
import dev.bravo.autocomplete.dao.exception.UserDoesNotExistException;
import dev.bravo.autocomplete.dao.interfaces.GetterDao;
import dev.bravo.autocomplete.dao.interfaces.UpsertDao;
import dev.bravo.autocomplete.dao.query.mapper.RecordToEntityMapper;
import dev.bravo.autocomplete.dao.repository.ChildRelationshipRepository;
import dev.bravo.autocomplete.dao.repository.RootNodeRepository;
import dev.bravo.autocomplete.dao.repository.TrieNodeRepository;
import lombok.NonNull;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Primary
public class UpsertDaoImpl implements UpsertDao, AutoCloseable {
    @Autowired
    private Driver driver;
    @Autowired
    private RootNodeRepository rootNodeRepository;
    @Autowired
    private TrieNodeRepository trieNodeRepository;
    @Autowired
    private ChildRelationshipRepository childRelationshipRepository;
    @Autowired
    private RecordToEntityMapper recordToEntityMapper;
    @Autowired
    private GetterDao getterDao;

    @Override
    public void upsertGraph(String userId, String word) throws UserDoesNotExistException {
        Mono<RootNodeEntity> rootNodeEntityMono = rootNodeRepository.findByUserId(userId);
        RootNodeEntity rootNode = rootNodeEntityMono.block();
        if (rootNode == null) {
            throw new UserDoesNotExistException("User with userId: '" + userId + "' doesn't exist");
        } else {
            try (Session session = driver.session(); Transaction transaction = session.beginTransaction()) {
                TrieNodeEntity previousNode = null;
                for (int i = 0; i < word.length(); i++) {
                    char currentChar = word.charAt(i);
                    Optional<TrieNodeEntity> childNodeOptional = i == 0 ?
                            getterDao.traverseGraph(userId, String.valueOf(currentChar)) :
                            getterDao.traverseGraph(previousNode, currentChar);
                    TrieNodeEntity childNode = childNodeOptional.orElse(new TrieNodeEntity());
                    childNode = updateTopRecommendation(childNode, word);
                    if (childNodeOptional.isEmpty()) {
                        if (i == 0) {
                            rootNode = updateRelationshipsWithRootNodeParent(rootNode, childNode, currentChar);
                        } else {
                            previousNode = updateRelationShipsWithTrieNodeParent(previousNode, childNode, currentChar);
                        }
                    }
                    previousNode = childNode;
                }
                transaction.commit();
            }
        }
    }

    private TrieNodeEntity updateTopRecommendation(TrieNodeEntity trieNode, String word) {
        trieNode.addFrequency(word, 1);
        trieNode = trieNodeRepository.save(trieNode).block();
        return trieNode;
    }

    private RootNodeEntity updateRelationshipsWithRootNodeParent(@NonNull RootNodeEntity rootNode,
                                                       @NonNull TrieNodeEntity childNode, char currentChar) {
        ChildRelationEntity childRelation = rootNode.getChildren().stream()
                .filter(childEdge -> currentChar == childEdge.getCharacter())
                .findFirst().orElse(
                        ChildRelationEntity.builder()
                                .character(currentChar)
                                .endNode(childNode)
                                .build());
        rootNode.addChild(childRelation);
        rootNode = rootNodeRepository.save(rootNode).block();
        return rootNode;
    }

    private TrieNodeEntity updateRelationShipsWithTrieNodeParent(@NonNull TrieNodeEntity parentNode,
                                                       @NonNull TrieNodeEntity childNode, char currentChar) {
        ChildRelationEntity childRelation = parentNode.getChildren().stream()
                .filter(childEdge -> currentChar == childEdge.getCharacter())
                .findFirst().orElse(
                        ChildRelationEntity.builder()
                                .character(currentChar)
                                .endNode(childNode)
                                .build());
        parentNode.addChild(childRelation);
        parentNode = trieNodeRepository.save(parentNode).block();
        return parentNode;
    }

    /**
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        driver.close();
    }
}
