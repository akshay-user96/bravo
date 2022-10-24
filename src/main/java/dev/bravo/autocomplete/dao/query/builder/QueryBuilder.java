package dev.bravo.autocomplete.dao.query.builder;

import java.util.logging.Logger;

public class QueryBuilder {
    private String query = "";
    private static String SPACE = " ";
    private final Logger logger = Logger.getLogger("QueryBuilder");

    public QueryBuilder match(String param) {
        query += "MATCH " + param;
        return this;
    }

    public QueryBuilder match(String sourceNode, String relation, String toNode) {
        query += "MATCH " + sourceNode + "-" + relation + "->" + toNode;
        return this;
    }

    public QueryBuilder mapTo(String sourceNode, String relation, String toNode) {
        query += SPACE + sourceNode + "-" + relation + "->" + toNode;
        return this;
    }

    public QueryBuilder mapTo(String relation, String toNode) {
        query += "-" + relation + "->" + toNode;
        return this;
    }

    public QueryBuilder where(String param) {
        query += " WHERE " + param;
        return this;
    }

    public QueryBuilder return_(String param) {
        query += " RETURN " + param;
        return this;
    }

    public String build() {
        logger.info("Built query '" + query + "'");
        return query;
    }
}
