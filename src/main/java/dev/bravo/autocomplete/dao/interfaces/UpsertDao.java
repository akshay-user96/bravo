package dev.bravo.autocomplete.dao.interfaces;

import dev.bravo.autocomplete.dao.exception.UserDoesNotExistException;

public interface UpsertDao {
    /**
     * Updates the graph/trie for the userId with the word
     * @param userId
     * @param word
     */
    void upsertGraph(String userId, String word) throws UserDoesNotExistException;
}
