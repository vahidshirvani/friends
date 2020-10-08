package com.vahidshirvani;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static com.vahidshirvani.Friends.*;
import static java.util.Collections.singletonList;
import static java.util.Map.Entry.comparingByKey;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

@SuppressWarnings({"rawtypes", "unchecked"})
public class FriendsTest {

    @Test
    public void countTrianglesTest() {
        List connections = List.of(
                new Connection("alice", "bob"),
                new Connection("alice", "eve"),
                new Connection("bob", "eve"));

        Map expected = Map.ofEntries(
                entry("alice", 1),
                entry("bob", 1),
                entry("eve", 1));
        Map actual = countTriangles(connections);
        assertEquals(expected, actual);
    }

    @Test
    public void triangleTest() {
        Entry entry = entry(Set.of("bob", "eve"), Set.of("alice"));
        Map map = Map.ofEntries(
                entry("alice", Set.of("bob", "eve")),
                entry("bob", Set.of("eve", "alice")),
                entry("eve", Set.of("bob", "alice")));

        List expected = singletonList(Set.of("alice", "bob", "eve"));
        List actual = triangles(entry, map);
        assertEquals(expected, actual);
    }


    @Test
    public void triangleToPeopleTest() {
        Set<String> triangle = Set.of("alice", "bob", "eve");
        List expected = List.of(
                entry("alice", 1),
                entry("bob", 1),
                entry("eve", 1)).stream().sorted(comparingByKey()).collect(toList());
        List actual = triangleToPeople(triangle).stream().sorted(comparingByKey()).collect(toList());
        assertEquals(expected, actual);
    }

    @Test
    public void commonFriendsTest() {
        List connections = List.of(
                new Connection("alice", "bob"),
                new Connection("alice", "eve"),
                new Connection("dave", "alice"));

        Map expected = Map.ofEntries(
                entry(Set.of("bob", "eve"), Set.of("alice")),
                entry(Set.of("bob", "dave"), Set.of("alice")),
                entry(Set.of("eve", "dave"), Set.of("alice")));
        Map actual = commonFriends(connections);
        assertEquals(expected, actual);
    }

    @Test
    public void commonFriendsForAPersonTest() {
        Entry entry = entry("alice", Set.of("bob", "eve"));
        Map expected = Map.ofEntries(entry(Set.of("bob", "eve"), singletonList("alice")));
        Map actual = commonFriendsForAPerson(entry);
        assertEquals(expected, actual);
    }

    @Test
    public void pairsTest() {
        Set<String> people = Set.of("alice", "bob", "eve");
        Set expected = List.of(
                Set.of("eve", "alice"),
                Set.of("bob", "eve"),
                Set.of("bob", "alice")).stream().collect(toSet());
        Set actual = pairs(people).stream().collect(toSet());
        assertEquals(expected, actual);
    }

    @Test
    public void friendsOfEachPersonTest() {
        List connections = List.of(
                new Connection("alice", "bob"),
                new Connection("alice", "eve"),
                new Connection("dave", "alice"));

        Map expected = Map.ofEntries(
                entry("alice", Set.of("bob", "eve", "dave")),
                entry("bob", Set.of("alice")),
                entry("eve", Set.of("alice")),
                entry("dave", Set.of("alice")));
        Map actual = friendsOfEachPerson(connections);
        assertEquals(expected, actual);
    }

    @Test
    public void combineTest() {
        List expected = List.of("alice", "bob", "bob", "eve");
        List actual = combine(List.of("alice", "bob"), List.of("bob", "eve"));
        assertEquals(expected, actual);
    }
}
