# Friends
A utility for processing friends in a social network. 

Particularly: 
1. Find friends of each person
2. Find common/mutual friends of each person
3. Count number of triangles each person appear in

A social network is an undirected graph where the nodes represent people and an edge between
two nodes represents that the corresponding two people are friends. (Loops and multi-edges do not
exist in social graphs.) 

### 1. Friends list
The input data to the utility would be all pairs of people in the social network that are friends with each other.
E.g. Alice and bob are friends then our expression would be (Alice, Eve).
The goal is to compute the friends list for every person in the social network.

IO example: Given input `(a,b),(a,c),(d,a)` then output `a:b,c,d`

### 2. Common Friends
Two people Alice and Bob in a social network have a common friend Eve whenever
edges between (Alice, Eve) and (Bob, Eve) exist. Note that this does not require that Alice and Bob are friends.
The goal is to compute the common friends for all pairs of people in the social network.

IO example: Given input `(a,c),(b,c)` then output `a,b:c`

### 3. Counting Number of Triangles in a Social Network
A triangle in a undirected graph is a collection of 3 nodes that are each connected to the other two.
For example, Alice, Bob, Eve form a triangle if (Alice, Bob), (Eve, Alice) and (Eve, Bob) are edges in the graph. 
(A triangle is called a 3-clique in graph theoretic terms).
The goal is to calculate the number of triangles that each person in the social network appears in.

IO example: Given input `(a,b),(c,a),(c,b)` then output `a:1,b:1,c:1`

## Code style
* The implementation heavily uses Java Streams. 
* Great principles from functional programming such as immutability and recursion are adopted when possible.  
* The processing technique MapReduce was also used for its efficiency.
* Every function contains documentation and unit tests.

## Build
```
$ mvn clean test
```
