package com.primecult.stateful;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        assertEquals(initialState, machine.getCurrentState());
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

        Assert.assertEquals(oneState, machine.getCurrentState());
    }

    @Test
    public void triggeringConsecutivelyResultsCorrectState() {
        String initialState = "Zero";
        String oneState = "One";
        String twoState = "Two";
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
                .transition(oneState, 2, twoState)
                .transition(twoState, 0, initialState)
                .transition(twoState, 1, oneState)
                .build();

        machine.trigger(1);
        Assert.assertEquals(oneState, machine.getCurrentState());

        machine.trigger(2);
        Assert.assertEquals(twoState, machine.getCurrentState());

        machine.trigger(0);
        Assert.assertEquals(initialState, machine.getCurrentState());

        machine.trigger(1);
        Assert.assertEquals(oneState, machine.getCurrentState());

        machine.trigger(2);
        Assert.assertEquals(twoState, machine.getCurrentState());

        machine.trigger(1);
        Assert.assertEquals(oneState, machine.getCurrentState());
    }

    @Test
    public void illegalTransitionHandlerCalled() {
        String initialState = "Zero";
        String oneState = "One";
        String twoState = "Two";
        final List<TransitionInput<String, Integer>> inputs = new ArrayList<>();
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
                        inputs.add(new TransitionInput<>(from, integer));
                    }
                })
                .transition(initialState, 1, oneState)
                .transition(oneState, 2, twoState)
                .build();

        // Triggering 2 from initial state is not legal.
        machine.trigger(2);

        Assert.assertEquals(1, inputs.size());
        Assert.assertEquals(new TransitionInput<String, Integer>(initialState, 2), inputs.get(0));
    }

    @Test
    public void preTransitionHandlerCalled() {
        String initialState = "Zero";
        String oneState = "One";
        String twoState = "Two";
        final List<Transition<String, Integer>> transitions = new ArrayList<>();
        StateMachine<String, Integer> machine = new StateMachine.Builder<>(initialState,
                new StateTransitionHandler<String, Integer>() {
                    @Override
                    public void preTransition(String from, Integer trigger, String to) {
                        transitions.add(new Transition<String, Integer>(from, trigger, to));
                    }

                    @Override
                    public void postTransition(String from, Integer trigger, String to) {

                    }

                    @Override
                    public void illegalTransition(String from, Integer trigger) {
                    }
                })
                .transition(initialState, 1, oneState)
                .transition(oneState, 2, twoState)
                .build();

        machine.trigger(1);

        Assert.assertEquals(1, transitions.size());
        Assert.assertEquals(initialState, transitions.get(0).getOriginalState());
        Assert.assertEquals(1, (int) transitions.get(0).getTrigger());
        Assert.assertEquals(oneState, transitions.get(0).getNewState());
    }

    @Test
    public void postTransitionHandlerCalled() {
        String initialState = "Zero";
        String oneState = "One";
        String twoState = "Two";
        final List<Transition<String, Integer>> transitions = new ArrayList<>();
        StateMachine<String, Integer> machine = new StateMachine.Builder<>(initialState,
                new StateTransitionHandler<String, Integer>() {
                    @Override
                    public void preTransition(String from, Integer trigger, String to) {
                    }

                    @Override
                    public void postTransition(String from, Integer trigger, String to) {
                        transitions.add(new Transition<String, Integer>(from, trigger, to));
                    }

                    @Override
                    public void illegalTransition(String from, Integer trigger) {
                    }
                })
                .transition(initialState, 1, oneState)
                .transition(oneState, 2, twoState)
                .build();

        machine.trigger(1);

        Assert.assertEquals(1, transitions.size());
        Assert.assertEquals(initialState, transitions.get(0).getOriginalState());
        Assert.assertEquals(1, (int) transitions.get(0).getTrigger());
        Assert.assertEquals(oneState, transitions.get(0).getNewState());
    }

    @Test
    public void ignoredTransitionDoesNotChangeCurrentState() {
        String initialState = "Zero";
        String oneState = "One";
        String twoState = "Two";
        final List<Transition<String, Integer>> transitions = new ArrayList<>();
        StateMachine<String, Integer> machine = new StateMachine.Builder<>(initialState,
                new StateTransitionHandler<String, Integer>() {
                    @Override
                    public void preTransition(String from, Integer trigger, String to) {
                    }

                    @Override
                    public void postTransition(String from, Integer trigger, String to) {
                    }

                    @Override
                    public void illegalTransition(String from, Integer trigger) {
                    }
                })
                .transition(initialState, 1, oneState)
                .transition(oneState, 2, twoState)
                .transition(twoState, 0, initialState)
                .ignore(twoState, 1)
                .build();

        machine.trigger(1); // To oneState
        machine.trigger(2); // To twoState

        Assert.assertEquals(twoState, machine.getCurrentState());
        machine.trigger(1); // Ignored
        Assert.assertEquals(twoState, machine.getCurrentState());
    }

    @Test
    public void ignoredTransitionDoesNotCallHandlers() {
        String initialState = "Zero";
        String oneState = "One";
        final String twoState = "Two";
        final List<Transition<String, Integer>> transitions = new ArrayList<>();
        StateMachine<String, Integer> machine = new StateMachine.Builder<>(initialState,
                new StateTransitionHandler<String, Integer>() {
                    @Override
                    public void preTransition(String from, Integer trigger, String to) {
                        if (from == twoState && trigger == 1) {
                            throw new AssertionError("Should not call preTransition().");
                        }
                    }

                    @Override
                    public void postTransition(String from, Integer trigger, String to) {
                        if (from == twoState && trigger == 1) {
                            throw new AssertionError("Should not call postTransition().");
                        }
                    }

                    @Override
                    public void illegalTransition(String from, Integer trigger) {
                        if (from == twoState && trigger == 1) {
                            throw new AssertionError("Should not call illegalTransition().");
                        }
                    }
                })
                .transition(initialState, 1, oneState)
                .transition(oneState, 2, twoState)
                .transition(twoState, 0, initialState)
                .ignore(twoState, 1)
                .build();

        machine.trigger(1); // To oneState
        machine.trigger(2); // To twoState

        Assert.assertEquals(twoState, machine.getCurrentState());
        machine.trigger(1); // Ignored
        Assert.assertEquals(twoState, machine.getCurrentState());
    }

    @Test
    public void reentrantTransitionCallsHandlers() {
        String initialState = "Zero";
        final String oneState = "One";
        final boolean[] handlerCalls = new boolean[2];
        Arrays.fill(handlerCalls, false);
        StateMachine<String, Integer> machine = new StateMachine.Builder<>(initialState,
                new StateTransitionHandler<String, Integer>() {
                    @Override
                    public void preTransition(String from, Integer trigger, String to) {
                        if (from == oneState && trigger == 1 && to == oneState) {
                            handlerCalls[0] = true;
                        }
                    }

                    @Override
                    public void postTransition(String from, Integer trigger, String to) {
                        if (from == oneState && trigger == 1 && to == oneState) {
                            handlerCalls[1] = true;
                        }
                    }

                    @Override
                    public void illegalTransition(String from, Integer trigger) {
                    }
                })
                .transition(initialState, 1, oneState)
                .transition(oneState, 1, oneState) // Reentrant transition
                .build();

        machine.trigger(1);
        Assert.assertFalse(handlerCalls[0]);
        Assert.assertFalse(handlerCalls[1]);

        machine.trigger(1);
        Assert.assertTrue(handlerCalls[0]);
        Assert.assertTrue(handlerCalls[1]);
    }

    @Test
    public void reentrantTransitionDoesNotCauseIllegalTransition() {
        String initialState = "Zero";
        final String oneState = "One";
        final boolean[] illegalTransitions = new boolean[]{false};
        StateMachine<String, Integer> machine = new StateMachine.Builder<>(initialState,
                new StateTransitionHandler<String, Integer>() {
                    @Override
                    public void preTransition(String from, Integer trigger, String to) {

                    }

                    @Override
                    public void postTransition(String from, Integer trigger, String to) {

                    }

                    @Override
                    public void illegalTransition(String from, Integer trigger) {
                        if (from == oneState && trigger == 1) {
                            illegalTransitions[0] = true;
                        }
                    }
                })
                .transition(initialState, 1, oneState)
                .transition(oneState, 1, oneState) // Reentrant transition
                .build();

        machine.trigger(1);
        Assert.assertFalse(illegalTransitions[0]);

        // Trigger reentrant transition
        machine.trigger(1);
        Assert.assertFalse(illegalTransitions[0]);
    }
}
