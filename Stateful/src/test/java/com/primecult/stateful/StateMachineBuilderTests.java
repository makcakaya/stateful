package com.primecult.stateful;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

public class StateMachineBuilderTests {
    @Test
    public void fluentApi() {
        StateMachine<String, Integer> machine = new StateMachine.Builder<>("Zero",
                new StateTransitionHandler<String, Integer>() {
                    @Override
                    public void preTransition(String from, Integer integer, String to) {

                    }

                    @Override
                    public void postTransition(String from, Integer integer, String to) {

                    }

                    @Override
                    public void illegalTransition(String from, Integer integer) {

                    }
                }).ignore("Zero", -1)
                .transition("Zero", 1, "One")
                .transition("One", 1, "Two")
                .transition("One", -1, "Zero")
                .build();
    }

    @Test
    public void canBuild() {
        String initialState = "Zero";
        StateMachine<String, Integer> machine = new StateMachine.Builder<>(initialState,
                new StateTransitionHandler<String, Integer>() {
                    @Override
                    public void preTransition(String from, Integer integer, String to) {

                    }

                    @Override
                    public void postTransition(String from, Integer integer, String to) {

                    }

                    @Override
                    public void illegalTransition(String from, Integer integer) {

                    }
                }).build();
        assertNotNull(machine);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registeringSameTransitionInputToMultipleTransitionOutputsThrows() {
        String initialState = "Zero";
        String oneState = "One";
        String twoState = "Two";
        Integer oneTrigger = 1;
        Integer oneDifferentTrigger = 11;
        StateMachine.Builder<String, Integer> builder = new StateMachine.Builder<>(initialState,
                new StateTransitionHandler<String, Integer>() {
                    @Override
                    public void preTransition(String from, Integer integer, String to) {

                    }

                    @Override
                    public void postTransition(String from, Integer integer, String to) {

                    }

                    @Override
                    public void illegalTransition(String from, Integer integer) {

                    }
                })
                .transition(initialState, oneTrigger, oneState)
                .transition(initialState, oneTrigger, twoState);
    }
}
