# What is Bravo?

Bravo provides personalised auto complete recommendations as you type on any digital platform. It will be your google assistance for typing out your thoughts.
It is a recommendation engine for word and sentence completion.

# How does it work?
## Backend
### Word Completion
The backend server is implemented using Spring. It uses Neo4j which is a graph database for storing the typed words.
Below couple of APIs help us achieve the goal of autocompletion.
1. `autocompleteWord` - Takes in a prefixWord and returns the top K recommendations
2. `addFrequency` - Updates the score of the word(taken as input) and recalculates the top K recommendations for all prefixWords

### API Performance
I used [Apache Jmeter](https://jmeter.apache.org/) for calculating the API performance.

| API               | input prefix(length) | TPS | Concurrency/#threads | Avg Latency | P90 Latency | P99 Latency | P100 Latency |
|-------------------|----------------------|-----|----------------------|-------------|-------------|-------------|--------------|
| autocompleteWord  | `capt`(4)            | 36  | 1                    | 27.42       | 30          | 36          | 128          |
 | autocompleteWord  | `capt`(4)            | 94  | 10                   | 159.41      | 191         | 777.3       | 34766        |