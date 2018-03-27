package com.primecult.stateful;

import org.junit.Assert;
import org.junit.Test;

public class TransitionLoggerTests {
    @Test
    public void doesNotLogInNoneMode() {
        TransitionLogger<String, Integer> logger = new TransitionLogger<>(TransitionLogger.LoggingMode.None);
        logger.log(getLegalTransition());
        logger.log(getIllegalTransition());
        logger.log(getIgnoredTransition());
        Assert.assertTrue(logger.getLogs().size() == 0);
    }

    @Test
    public void logsOnlyIllegalTransitionsInTheMode() {
        TransitionLogger<String, Integer> logger = new TransitionLogger<>(TransitionLogger.LoggingMode.IllegalTransitionsOnly);
        logger.log(getLegalTransition());
        logger.log(getIllegalTransition());
        logger.log(getIgnoredTransition());
        Assert.assertTrue(logger.getLogs().size() == 1);
        Assert.assertTrue(logger.getLogs().get(0).equals(getIllegalTransition()));
    }

    @Test
    public void logsOnlyLegalTransitionsInTheMode() {
        TransitionLogger<String, Integer> logger = new TransitionLogger<>(TransitionLogger.LoggingMode.LegalTransitionsOnly);
        logger.log(getLegalTransition());
        logger.log(getIllegalTransition());
        logger.log(getIgnoredTransition());
        Assert.assertTrue(logger.getLogs().size() == 1);
        Assert.assertTrue(logger.getLogs().get(0).equals(getLegalTransition()));
    }

    @Test
    public void logsLegalAndIllegalTransitionsInTheMode() {
        TransitionLogger<String, Integer> logger = new TransitionLogger<>(TransitionLogger.LoggingMode.LegalAndIllegalTransitions);
        logger.log(getLegalTransition());
        logger.log(getIllegalTransition());
        logger.log(getIgnoredTransition());
        Assert.assertTrue(logger.getLogs().size() == 2);
        Assert.assertTrue(logger.getLogs().get(0).equals(getLegalTransition()));
        Assert.assertTrue(logger.getLogs().get(1).equals(getIllegalTransition()));
    }

    private Transition<String, Integer> getLegalTransition() {
        return Transition.legal(new TransitionInput<>("Zero", 1), TransitionOutput.legal("One"));
    }

    private Transition<String, Integer> getIllegalTransition() {
        return Transition.illegal(new TransitionInput<String, Integer>("Zero", 3));
    }

    private Transition<String, Integer> getIgnoredTransition() {
        return Transition.ignored(new TransitionInput<String, Integer>("Zero", 0));
    }
}

