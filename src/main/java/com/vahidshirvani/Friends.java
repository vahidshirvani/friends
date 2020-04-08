package com.vahidshirvani;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.Map.entry;
import static java.util.stream.Collectors.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Friends {

    /**
     * Count number of triangles every person occurs in given a list of friends.
     *
     * @param friends list of all connections
     * @return a map with person as key and number of triangles he/she have been a member of
     */
    static Map<String, Integer> countTriangles(List<Connection> friends) {
        return commonFriends(friends)
                .entrySet()
                .stream()
                .flatMap(e -> triangles(e, friendsOfEachPerson(friends)).stream())
                .distinct()
                .flatMap(t -> triangleToPeople(t).stream())
                .collect(toMap(
                        Entry::getKey,
                        Entry::getValue,
                        Integer::sum));
    }

    /**
     * Find triangles from mutual friends.
     * E.g. if alice and bob both are friends with eve (their mutual friend)
     * and it turns out that alice and bob are also friends then we have found a triangle.
     * Given mutual friends [alice bob]=eve and friends list alice=[bob eve] then triangle [alice bob eve]
     *
     * @return a list of triangles
     */
    static List<Set<String>> triangles(Entry<Set<String>, Set<String>> e, Map<String, Set<String>> friendsOfEachPerson) {
        Iterator<String> iterator = e.getKey().iterator(); // exact size two
        String a = iterator.next();
        String b = iterator.next();

        return friendsOfEachPerson.get(a).contains(b) ?
                e.getValue().stream().map(c -> asSet(a, b, c)).collect(toList()) :
                Collections.emptyList();
    }

    /**
     * Split triangle to individuals member in that triangle.
     * E.g. A triangle [alice, bob, eve] means all three are friends with each other.
     *
     * @param triangle a set containing exactly three people
     * @return a list of entry where key is a person and value is number of triangles he/she is a member of
     */
    static List<Entry<String, Integer>> triangleToPeople(Set<String> triangle) {
        return triangle
                .stream()
                .map(p -> entry(p, 1))
                .collect(toList());
    }

    /**
     * Find mutual friends of all pairs from all of the connections using MapReduce.
     *
     * @param friends list of all connections
     * @return a map containing a pair (set of size two) as key and their mutual friends as value.
     */
    static Map<Set<String>, Set<String>> commonFriends(List<Connection> friends) {
        return friendsOfEachPerson(friends)
                .entrySet()
                .stream()
                .flatMap(f -> commonFriendsForAPerson(f).entrySet().stream())
                .collect(toMap(                                                         // Map phase in MapReduce
                        Entry::getKey,
                        Entry::getValue,
                        Friends::combine))                                              // Higher complexity to reduce here
                .entrySet()                                                             // Reduce phase in MapReduce
                .stream()
                .map(e -> entry(e.getKey(), e.getValue().stream().collect(toSet())))    // Actual reduce
                .collect(toMap(
                        Entry::getKey,                                                  // No collision
                        Entry::getValue));
    }

    /**
     * Two individuals that have one person as a common/mutual friend.
     * E.g. alice is friend with bob and eve then bob and eve has alice as a common/mutual friend.
     * Given alice's friends list alice=[bob, eve] then result is [bob, eve]=alice
     *
     * @param e entry with person as key and friends list as value
     * @return a map containing a pair (set of size two) as key and their mutual friend as value.
     */
    static Map<Set<String>, List<String>> commonFriendsForAPerson(Entry<String, Set<String>> e) {
        return pairs(e.getValue())
                .stream()
                .collect(toMap(
                        Function.identity(),
                        t -> singletonList(e.getKey()),
                        Friends::combine));
    }

    /**
     * Given a list of individuals calculate all pair combinations.
     * A pair is an unordered set of size two which helps us avoid duplicates.
     * E.g. given [alice, bob, eve] then result is [[alice, bob], [alice, eve], [bob, eve]]
     *
     * @param people list of individuals
     * @return list of pairs (set of size two)
     */
    static List<Set<String>> pairs(Set<String> people) {
        return pairs(
                people.stream().findFirst().orElse(""),
                people.stream().skip(1).collect(toSet()),
                Collections.emptyList());
    }

    /**
     * Helper function to {@link Friends#pairs(Set)} that does the actual job with recursion.
     *
     * Note: Java does not support tail recursion which leads to stack growth despite
     * the fact that end result is passed as input parameter and last call is the recursive call.
     *
     * @param person first person from people list
     * @param people the rest of people list
     * @param result accumulated results
     * @return the list of all pairs (set of size two)
     */
    private static List<Set<String>> pairs(String person, Set<String> people, List<Set<String>> result) {
        // Base case
        if (people.isEmpty()) {
            return result;
        }

        // Current case
        List<Set<String>> tuples = people
                .stream()
                .map(p -> asSet(person, p))
                .collect(toList());

        // N - 1 case
        return pairs(
                people.stream().findFirst().orElse(""),
                people.stream().skip(1).collect(toSet()),
                combine(tuples, result));
    }

    /**
     * Calculate the friends of each person from all of the connections using MapReduce.
     * E.g. Alice has Bob and Eve as friends then alice=[bob, eve]
     *
     * @param friends list of all connections
     * @return a map with an individual as key and friends as value
     */
    static Map<String, Set<String>> friendsOfEachPerson(List<Connection> friends) {
        return friends
                .stream()
                .flatMap(c -> Stream.of(c, new Connection(c.getB(), c.getA())))         // Reverse connection as well
                .collect(toMap(                                                         // Map phase in MapReduce
                        Connection::getA,
                        c -> singletonList(c.getB()),
                        Friends::combine))                                              // Higher complexity to reduce here
                .entrySet()                                                             // Reduce phase in MapReduce
                .stream()
                .map(e -> entry(e.getKey(), e.getValue().stream().collect(toSet())))    // Actual reduce
                .collect(toMap(
                        Entry::getKey,                                                  // No collision
                        Entry::getValue));
    }

    /**
     * Combines two {@link List}s into one.
     * Does not reduce with {@link Stream#distinct} on purpose in order to keep complexity low.
     *
     * @param a First list
     * @param b Second list
     * @return Combined list
     */
    static <T> List<T> combine(List<T> a, List<T> b) {
        return Stream
                .of(a, b)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    /**
     * A helper function similar to {@link Arrays#asList}.
     * Sets are unordered collection without duplicates.
     *
     * @param <T> the class of the objects in the array
     * @param a the array by which the set will be backed
     * @return a set view of the specified array
     */
    @SafeVarargs
    static <T> Set<T> asSet(T... a) {
        return Stream.of(a).collect(toSet());
    }
}
