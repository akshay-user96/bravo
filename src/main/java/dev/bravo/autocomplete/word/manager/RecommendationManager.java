package dev.bravo.autocomplete.word.manager;

import dev.bravo.autocomplete.dao.CommonDAO;
import dev.bravo.autocomplete.dao.entity.TrieNodeEntity;
import dev.bravo.autocomplete.dao.exception.UserDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class RecommendationManager {
    @Autowired
    private CommonDAO dao;
    private final Logger logger = Logger.getLogger("RecommendationManager");

    public List<String> getRecommendation(String userId, String prefixWord)
            throws UserDoesNotExistException {
        logger.info("Inside getRecommendations with userId: " + userId + " prefixWord: " + prefixWord);
        TrieNodeEntity trieNode = dao.traverseGraph(userId, prefixWord);
        List<String> recommendations = trieNode != null ? trieNode.getTopRecommendations() : List.of();
        logger.info("Fetched recommendations: " + recommendations);
        return recommendations;
    }
}
