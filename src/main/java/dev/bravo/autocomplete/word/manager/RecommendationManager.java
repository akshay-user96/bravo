package dev.bravo.autocomplete.word.manager;

import dev.bravo.autocomplete.dao.entity.node.TrieNodeEntity;
import dev.bravo.autocomplete.dao.exception.UserDoesNotExistException;
import dev.bravo.autocomplete.dao.interfaces.GetterDao;
import dev.bravo.autocomplete.dao.interfaces.UpsertDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Component
public class RecommendationManager {
    @Autowired
    private GetterDao getterDao;
    @Autowired
    private UpsertDao upsertDao;
    private final Logger logger = Logger.getLogger("RecommendationManager");

    public Set<String> getRecommendation(String userId, String prefixWord)
            throws UserDoesNotExistException {
        logger.info("Inside getRecommendations with userId: " + userId + " prefixWord: " + prefixWord);
        Optional<TrieNodeEntity> trieNode = getterDao.traverseGraph(userId, prefixWord);
        Set<String> recommendations = trieNode.map(trieNodeEntity -> trieNodeEntity.getRecommendationScores().keySet())
                .orElse(Collections.emptySet());
        logger.info("Fetched recommendations: " + recommendations);
        return recommendations;
    }

    public void updateFrequency(String userId, String word) throws UserDoesNotExistException {
        logger.info("Inside updateFrequency with userId: " + userId + " prefixWord: " + word);
        upsertDao.upsertGraph(userId, word);
    }
}
