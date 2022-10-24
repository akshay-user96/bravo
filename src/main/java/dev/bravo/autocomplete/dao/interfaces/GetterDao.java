package dev.bravo.autocomplete.dao.interfaces;

import dev.bravo.autocomplete.dao.entity.node.TrieNodeEntity;
import dev.bravo.autocomplete.dao.exception.UserDoesNotExistException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GetterDao {
    /**
     * Fetch the TrieNode for the user post traversing the prefix word
     * @param userId
     * @param prefix
     * @return TrieNode
     */
    Optional<TrieNodeEntity> traverseGraph(String userId, String prefix) throws UserDoesNotExistException;

    Optional<TrieNodeEntity> traverseGraph(TrieNodeEntity trieNode, char c);

    /**
     * Runs any query in Neo4j
     * @param query
     * @return Query result - List of nodes
     */
    List<Map<String, Object>> runQuery(String query);
}
