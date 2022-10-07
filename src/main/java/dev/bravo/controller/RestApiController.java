package dev.bravo.controller;

import java.util.List;

import dev.bravo.autocomplete.word.manager.RecommendationManager;
import dev.bravo.helper.Constants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {
    RecommendationManager recommendationManager;

    @GetMapping("/autoCompleteWord/{userid}/{prefixWord}")
    public List<String> autoCompleteWord(@RequestParam String userId, @RequestParam String prefixWord) {
        return recommendationManager.getRecommendation(userId, prefixWord, Constants.NUM_RECOMMENDATION);
    }

}
