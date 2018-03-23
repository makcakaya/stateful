package com.primecult.stateful;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TransitionTests {
    @Test
    public void equalsReturnsFalseForNull() {
        Transition<String, String> transition = new Transition<>("from", "trigger", "to");
        assertNotEquals(transition, null);
        assertFalse(transition.equals(null));
    }

    @Test
    public void equalsReturnsFalseForNotDifferentValues() {
        Transition<String, String> transition = new Transition<>("from", "trigger", "to");
        Transition<String, String> other = new Transition<>("different", "values", "here");
        assertNotEquals(transition, other);
        assertFalse(transition.equals(other));
    }

    @Test
    public void equalsReturnsFalseForDifferentType() {
        Transition<String, String> transition = new Transition<>("from", "trigger", "to");
        Integer differentType = 123;
        assertNotEquals(transition, differentType);
        assertFalse(transition.equals(differentType));
    }

    @Test
    public void equalsReturnsTrueIfSameValues() {
        Transition<String, String> transition = new Transition<>("from", "trigger", "to");
        Transition<String, String> other = new Transition<>("from", "trigger", "to");
        assertEquals(transition, other);
        assertTrue(transition.equals(other));
    }

    @Test
    public void mapMatchesTransitionsWithSameValues() {
        HashMap<Transition, String> transitions = new HashMap<Transition, String>();
        String value = "The value";
        Transition<String, String> transition = new Transition<>("from", "trigger", "to");
        transitions.put(transition, value);
        Transition<String, String> other = new Transition<>("from", "trigger", "to");
        assertTrue(transitions.containsKey(other));
    }

    @Test
    public void sameStateTriggerIgnoredEquals() {
        Transition<String, String> one = new Transition<>("state", "trigger");
        Transition<String, String> two = new Transition<>("state", "trigger");
        assertEquals(one, two);
    }

    @Test
    public void differentStateTriggerIgnoredEquals() {
        Transition<String, String> one = new Transition<>("state1", "trigger");
        Transition<String, String> two = new Transition<>("state2", "trigger");
        assertNotEquals(one, two);

        Transition<String, String> three = new Transition<>("state", "trigger1");
        Transition<String, String> four = new Transition<>("state", "trigger3");
        assertNotEquals(one, two);
    }
}
