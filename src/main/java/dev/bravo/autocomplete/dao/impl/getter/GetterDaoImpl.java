package dev.bravo.autocomplete.dao.impl.getter;

import dev.bravo.autocomplete.dao.entity.node.RootNodeEntity;
import dev.bravo.autocomplete.dao.entity.node.TrieNodeEntity;
import dev.bravo.autocomplete.dao.exception.UserDoesNotExistException;
import dev.bravo.autocomplete.dao.interfaces.GetterDao;
import dev.bravo.autocomplete.dao.query.builder.QueryBuilder;
import dev.bravo.autocomplete.dao.query.mapper.RecordToEntityMapper;
import dev.bravo.autocomplete.dao.repository.RootNodeRepository;
import dev.bravo.autocomplete.dao.repository.TrieNodeRepository;
import dev.bravo.helper.Constants;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Primary
public class GetterDaoImpl implements GetterDao, AutoCloseable {
    @Autowired
    private Driver driver;
    @Autowired
    private RootNodeRepository rootNodeRepository;
    @Autowired
    private TrieNodeRepository trieNodeRepository;
    @Autowired
    private RecordToEntityMapper recordToEntityMapper;

    @Override
    public Optional<TrieNodeEntity> traverseGraph(String userId, String prefix) throws UserDoesNotExistException {
        RootNodeEntity rootNode = rootNodeRepository.findByUserId(userId).block();
        if (rootNode == null) {
            throw new UserDoesNotExistException("User with userId: '" + userId + "' doesn't exist");
        } else {
            String query = getGraphTraversalQuery(userId, prefix);
            List<TrieNodeEntity> trieNodeEntities = runQuery(query).stream()
                    .map(record -> recordToEntityMapper.mapRecordToTrieNodeEntity(record))
                    .map(childNode -> trieNodeRepository.findById(childNode.getId()).block())
                    .collect(Collectors.toList());
            assert trieNodeEntities.size() <= 1;
            return trieNodeEntities.size() == 1 ? Optional.ofNullable(trieNodeEntities.get(0)) : Optional.empty();
        }
    }

    @Override
    public Optional<TrieNodeEntity> traverseGraph(TrieNodeEntity trieNode, char currentChar) {
        QueryBuilder queryBuilder = new QueryBuilder();
        String query = queryBuilder.match("(s:" + Constants.trieNodeEntity
                                + ")", "[:CHILD {character:'"
                                + currentChar + "'}]",
                        "(t:TrieNode)")
                .where("id(s)=" + trieNode.getId())
                .return_("t{.*, id:ID(t)}")
                .build();
        List<Map<String, Object>> queryResult = runQuery(query);
        Optional<TrieNodeEntity> childNodeOptional = queryResult.stream()
                .findFirst()
                .map(record -> recordToEntityMapper.mapRecordToTrieNodeEntity(record))
                .map(childNode -> trieNodeRepository.findById(childNode.getId()).block());
        return childNodeOptional;
    }

    @Override
    public List<Map<String, Object>> runQuery(String query) {
        try (Session session = driver.session()) {
            Result result = session.run(query);
            List<Map<String, Object>> recordList = new ArrayList<>();
            while (result.hasNext()) {
                Record record = result.next();
                if (record != null) {
                    recordList.add(record.asMap());
                }
            }
            return recordList;
        }
    }

    private String getGraphTraversalQuery(String userId, String prefix) {
        QueryBuilder queryBuilder = new QueryBuilder();
        for (int i = 0; i < prefix.length(); i++) {
            char currentChar = prefix.charAt(i);
            if (i == 0) {
                queryBuilder.match("(r:" + Constants.rootNodeEntity + " {userId:'" + userId + "'})",
                        "[:CHILD {character:'" + currentChar + "'}]",
                        i == prefix.length()-1 ? "(t:TrieNode)" : "()");
            } else {
                queryBuilder.mapTo("[:CHILD {character:'" + currentChar + "'}]",
                        i == prefix.length()-1 ? "(t:TrieNode)" : "()");
            }
        }
        queryBuilder.return_("t{.*, " + Constants.id + ":ID(t)}");
        return queryBuilder.build();
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }
}
