package dev.bravo.autocomplete.dao;

import dev.bravo.autocomplete.dao.entity.TrieNodeEntity;
import dev.bravo.autocomplete.dao.exception.UserDoesNotExistException;

public interface CommonDAO {
    /**
     * Fetch the TrieNode for the user post traversing the prefix word
     * @param userId
     * @param prefix
     * @return TrieNode
     */
    TrieNodeEntity traverseGraph(String userId, String prefix) throws UserDoesNotExistException;
}
