package com.primecult.stateful;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StateMachineTests {
    @Test
    public void canGetCurrentStateInitially() {
        String initialState = "Zero";
        StateMachine<String, Integer> machine = new StateMachine.Builder<>(initialState, new StateTransitionHandler<String, Integer>() {
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

        assertEquals(initialState, machine.currentState());
    }

    @Test
    public void canTriggerInitially() {
        String initialState = "Zero";
        String oneState = "One";
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
                })
                .transition(initialState, 1, oneState)
                .build();
        machine.trigger(1);
    }
}
