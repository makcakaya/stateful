package com.primecult.stateful;

import org.junit.Assert;
import org.junit.Test;

public final class TransitionInputTests {
    @Test
    public void equalsToNullReturnsFalse() {
        TransitionInput<String, Integer> input = new TransitionInput<>("Zero", 1);
        Assert.assertNotEquals(input, null);
        Assert.assertFalse(input.equals(null));
    }

    @Test
    public void equalsToDifferentValuesReturnsFalse() {
        TransitionInput<String, Integer> one = new TransitionInput<>("Zero", 1);
        TransitionInput<String, Integer> two = new TransitionInput<>("One", 1);
        Assert.assertNotEquals(one, two);
        Assert.assertFalse(one.equals(two));

        TransitionInput<String, Integer> three = new TransitionInput<>("Zero", 1);
        TransitionInput<String, Integer> four = new TransitionInput<>("Zero", 2);
        Assert.assertNotEquals(three, four);
        Assert.assertFalse(three.equals(four));
    }

    @Test
    public void equalsToSameValuesReturnsTrue() {
        TransitionInput<String, Integer> one = new TransitionInput<>("Zero", 1);
        TransitionInput<String, Integer> two = new TransitionInput<>("Zero", 1);
        Assert.assertEquals(one, two);
        Assert.assertTrue(one.equals(two));
    }
}
