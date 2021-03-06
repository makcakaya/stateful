package com.primecult.stateful;

import org.junit.Assert;
import org.junit.Test;

public class TransitionOutputTests {
    @Test
    public void equalsToNullReturnsFalse() {
        TransitionOutput<String> output = TransitionOutput.legal("Zero");
        Assert.assertNotEquals(output, null);
        Assert.assertFalse(output.equals(null));
    }

    @Test
    public void equalsToDifferentValuesReturnsFalse() {
        TransitionOutput<String> one = TransitionOutput.legal("Zero");
        TransitionOutput<String> two = TransitionOutput.legal("One");
        Assert.assertNotEquals(one, two);
        Assert.assertFalse(one.equals(two));
    }

    @Test
    public void equalsToSameValuesReturnsTrue() {
        TransitionOutput<String> one = TransitionOutput.legal("Zero");
        TransitionOutput<String> two = TransitionOutput.legal("Zero");
        Assert.assertEquals(one, two);
        Assert.assertTrue(one.equals(two));

        TransitionOutput<String> three = TransitionOutput.ignore();
        TransitionOutput<String> four = TransitionOutput.ignore();
        Assert.assertEquals(three, four);
        Assert.assertTrue(three.equals(four));
    }
}
