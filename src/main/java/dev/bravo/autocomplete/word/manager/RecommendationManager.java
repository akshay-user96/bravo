package dev.bravo.autocomplete.word.manager;

import dev.bravo.autocomplete.dao.entity.node.TrieNodeEntity;
import dev.bravo.autocomplete.dao.exception.UserDoesNotExistException;
import dev.bravo.autocomplete.dao.interfaces.GetterDao;
import dev.bravo.autocomplete.dao.interfaces.UpsertDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class RecommendationManager {
    @Autowired
    private GetterDao getterDao;
    @Autowired
    private UpsertDao upsertDao;
    private final Logger logger = Logger.getLogger("RecommendationManager");

    public Set<String> getRecommendation(String userId, String prefixWord, Integer numOfRecommendations)
            throws UserDoesNotExistException {
        logger.info("Inside getRecommendations with userId: " + userId + " prefixWord: " + prefixWord);
        Optional<TrieNodeEntity> trieNode = getterDao.traverseGraph(userId, prefixWord);
        Set<String> recommendations = trieNode.map(TrieNodeEntity::getRecommendationScores)
                .orElse(Collections.emptyMap())
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed())
                .limit(numOfRecommendations)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        logger.info("Fetched recommendations: " + recommendations);
        return recommendations;
    }

    public void updateFrequency(String userId, String word) throws UserDoesNotExistException {
        logger.info("Inside updateFrequency with userId: " + userId + " prefixWord: " + word);
        upsertDao.upsertGraph(userId, word);
    }
}
