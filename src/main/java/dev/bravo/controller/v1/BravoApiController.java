package dev.bravo.controller.v1;

import java.util.List;
import java.util.logging.Logger;

import dev.bravo.autocomplete.dao.exception.UserDoesNotExistException;
import dev.bravo.autocomplete.word.manager.RecommendationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class BravoApiController {
    @Autowired
    private RecommendationManager recommendationManager;
    private Logger logger = Logger.getLogger("BravoApiController");

    @GetMapping("/autocomplete/word/{userId}/{prefixWord}")
    public List<String> autocompleteWord(@PathVariable("userId") String userId,
                                         @PathVariable("prefixWord") String prefixWord)
            throws UserDoesNotExistException {
        logger.info("autoCompleteWord API called for userId: " + userId + " and prefixWord: " + prefixWord);
        return recommendationManager.getRecommendation(userId, prefixWord);
    }

}
