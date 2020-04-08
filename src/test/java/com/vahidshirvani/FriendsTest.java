package com.vahidshirvani;

import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import static com.vahidshirvani.Friends.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

@SuppressWarnings({"rawtypes", "unchecked"})
public class FriendsTest {

    @Test
    public void countTrianglesTest() {
        List connections = asList(
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
        Entry entry = entry(asSet("bob", "eve"), asSet("alice"));
        Map map = Map.ofEntries(
                entry("alice", asSet("bob", "eve")),
                entry("bob", asSet("eve", "alice")),
                entry("eve", asSet("bob", "alice")));

        List expected = singletonList(asSet("alice", "bob", "eve"));
        List actual = triangles(entry, map);
        assertEquals(expected, actual);
    }


    @Test
    public void triangleToPeopleTest() {
        Set triangle = asSet("alice", "bob", "eve");
        List expected = asList(
                entry("bob", 1),
                entry("eve", 1),
                entry("alice", 1));
        List actual = triangleToPeople(triangle);
        assertEquals(expected, actual);
    }

    @Test
    public void commonFriendsTest() {
        List connections = asList(
                new Connection("alice", "bob"),
                new Connection("alice", "eve"),
                new Connection("dave", "alice"));

        Map expected = Map.ofEntries(
                entry(asSet("bob", "eve"), asSet("alice")),
                entry(asSet("bob", "dave"), asSet("alice")),
                entry(asSet("eve", "dave"), asSet("alice")));
        Map actual = commonFriends(connections);
        assertEquals(expected, actual);
    }

    @Test
    public void commonFriendsForAPersonTest() {
        Entry entry = entry("alice", asSet("bob", "eve"));
        Map expected = Map.ofEntries(entry(asSet("bob", "eve"), singletonList("alice")));
        Map actual = commonFriendsForAPerson(entry);
        assertEquals(expected, actual);
    }

    @Test
    public void pairsTest() {
        Set people = asSet("alice", "bob", "eve");
        List expected = asList(
                asSet("eve", "alice"),
                asSet("bob", "eve"),
                asSet("bob", "alice"));
        List actual = pairs(people);
        assertEquals(expected, actual);
    }

    @Test
    public void friendsOfEachPersonTest() {
        List connections = asList(
                new Connection("alice", "bob"),
                new Connection("alice", "eve"),
                new Connection("dave", "alice"));

        Map expected = Map.ofEntries(
                entry("alice", asSet("bob", "eve", "dave")),
                entry("bob", asSet("alice")),
                entry("eve", asSet("alice")),
                entry("dave", asSet("alice")));
        Map actual = friendsOfEachPerson(connections);
        assertEquals(expected, actual);
    }

    @Test
    public void combineTest() {
        List expected = asList("alice", "bob", "bob", "eve");
        List actual = combine(asList("alice", "bob"), asList("bob", "eve"));
        assertEquals(expected, actual);
    }

    @Test
    public void asSetTest() {
        Set expected = Stream.of("alice", "bob", "bob", "eve").collect(toSet());
        Set actual = asSet("alice", "bob", "eve");
        assertEquals(expected, actual);
    }
}
