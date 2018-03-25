package com.primecult.stateful;

import java.util.HashMap;
import java.util.Map;

public class StateMachine<TState, TTrigger> {
    private TState _currentState;
    private final Map<TransitionInput<TState, TTrigger>, TransitionOutput<TState>> _legalTransitions;
    private final StateTransitionHandler<TState, TTrigger> _transitionHandler;

    private StateMachine(TState initialState, HashMap<TransitionInput<TState, TTrigger>, TransitionOutput<TState>> legalTransitions,
                         StateTransitionHandler<TState, TTrigger> transitionHandler) {
        if (initialState == null) {
            throw new IllegalArgumentException("initialState");
        }
        if (legalTransitions == null) {
            throw new IllegalArgumentException("legalTransitions");
        }

        _currentState = initialState;
        _legalTransitions = new HashMap<>(legalTransitions);
        _transitionHandler = transitionHandler;
    }

    public void trigger(TTrigger trigger) {
        if (trigger == null) {
            throw new IllegalArgumentException("trigger");
        }

        TransitionInput<TState, TTrigger> input = new TransitionInput<>(getCurrentState(), trigger);
        if (!_legalTransitions.containsKey(input)) {
            if (_transitionHandler != null) {
                _transitionHandler.illegalTransition(input.getState(), trigger);
            }
            return;
        }
        TransitionOutput<TState> output = _legalTransitions.get(input);
        if (output.isIgnored()) {
            return;
        }

        if (_transitionHandler != null) {
            _transitionHandler.preTransition(input.getState(), input.getTrigger(), output.getState());
        }
        _currentState = output.getState();
        if (_transitionHandler != null) {
            _transitionHandler.postTransition(input.getState(), input.getTrigger(), output.getState());
        }
    }

    public TState getCurrentState() {
        return _currentState;
    }

    public static class Builder<TState, TTrigger> {
        private TState _initialState;
        private final HashMap<TransitionInput<TState, TTrigger>, TransitionOutput<TState>> _transitions = new HashMap<>();
        private StateTransitionHandler<TState, TTrigger> _transitionHandler;

        public Builder(TState initialState, StateTransitionHandler<TState, TTrigger> handler) {
            _initialState = initialState;
            if (_transitionHandler != null) {
                throw new IllegalArgumentException("Transition handler is already set.");
            }
            _transitionHandler = handler;
        }

        public Builder<TState, TTrigger> transition(TState from, TTrigger trigger, TState to) {
            registerInputToOutputMap(new TransitionInput<>(from, trigger), TransitionOutput.newState(to));
            return this;
        }

        public Builder<TState, TTrigger> ignore(TState state, TTrigger trigger) {
            registerInputToOutputMap(new TransitionInput<TState, TTrigger>(state, trigger), null);
            return this;
        }

        public StateMachine<TState, TTrigger> build() {
            return new StateMachine<>(_initialState, _transitions, _transitionHandler);
        }

        private void registerInputToOutputMap(TransitionInput<TState, TTrigger> input, TransitionOutput<TState> output) {
            if (input == null) {
                throw new IllegalArgumentException("input");
            }
            if (_transitions.containsKey(input)) {
                throw new IllegalArgumentException("Transition already added.");
            }
            output = output == null ? TransitionOutput.<TState>ignore() : output;
            _transitions.put(input, output);
        }
    }
}
