package dev.bravo.autocomplete.dao.impl;

import dev.bravo.autocomplete.dao.CommonDAO;
import dev.bravo.autocomplete.dao.entity.RootNodeEntity;
import dev.bravo.autocomplete.dao.entity.TrieNodeEntity;
import dev.bravo.autocomplete.dao.exception.UserDoesNotExistException;
import dev.bravo.autocomplete.dao.query.builder.QueryBuilder;
import dev.bravo.autocomplete.dao.query.mapper.RecordToEntityMapper;
import dev.bravo.autocomplete.dao.repository.RootNodeRepository;
import dev.bravo.helper.Constants;
import org.neo4j.driver.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class Dao implements CommonDAO, AutoCloseable {
    @Autowired
    private Driver driver;
    @Autowired
    private RootNodeRepository rootNodeRepository;
    @Autowired
    private RecordToEntityMapper recordToEntityMapper;

    @Override
    public TrieNodeEntity traverseGraph(String userId, String prefix) throws UserDoesNotExistException {
        Mono<RootNodeEntity> rootNodeEntityMono = rootNodeRepository.findById(userId);
        RootNodeEntity rootNode = rootNodeEntityMono.block();
        if (rootNode == null) {
            throw new UserDoesNotExistException("User with userId: '" + userId + "' doesn't exist");
        } else {
            String query = getGraphTraversalQuery(userId, prefix);
            try (Session session = driver.session()) {
                Result result = session.run(query);
                List<TrieNodeEntity> trieNodeEntities = new ArrayList<>();
                while (result.hasNext()) {
                    Record record = result.next();
                    TrieNodeEntity trieNode = recordToEntityMapper.mapRecordToTrieNodeEntity(record);
                    if (trieNode != null) {
                        trieNodeEntities.add(trieNode);
                    }
                }
                assert trieNodeEntities.size() <= 1;
                return trieNodeEntities.isEmpty() ? null : trieNodeEntities.get(0);
            }
        }
    }

    private String getGraphTraversalQuery(String userId, String prefix) {
        QueryBuilder queryBuilder = new QueryBuilder();
        for (int i = 0; i < prefix.length(); i++) {
            char currentChar = prefix.charAt(i);
            if (i == 0) {
                queryBuilder.match("(r:" + Constants.rootNodeEntity + " {userId:'" + userId + "'})",
                        "[:CHILD {character:'" + currentChar + "'}]",
                        i == prefix.length()-1 ? "(t:TrieNode)" : "(TrieNode)");
            } else {
                queryBuilder.mapTo("[:CHILD {character:'" + currentChar + "'}]",
                        i == prefix.length()-1 ? "(t:TrieNode)" : "(TrieNode)");
            }
        }
        queryBuilder.return_("t");
        return queryBuilder.build();
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }
}
