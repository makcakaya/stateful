package com.primecult.stateful;

import java.util.ArrayList;

public class StateMachine<TState, TTrigger> {
    private final TState _initialState;
    private final ArrayList<Transition> _legalTransitions;
    private final StateTransitionHandler<TState, TTrigger> _transitionHandler;
    private final ArrayList<Transition<TState, TTrigger>> _transitions = new ArrayList<>();

    private StateMachine(TState initialState, ArrayList<Transition> legalTransitions,
                         StateTransitionHandler<TState, TTrigger> transitionHandler) {
        _initialState = initialState;
        _legalTransitions = legalTransitions;
        _transitionHandler = transitionHandler;
    }

    public void trigger(TTrigger trigger) {
        if (trigger == null) {
            throw new IllegalArgumentException("trigger");
        }
    }

    public TState currentState() {
        return _transitions.isEmpty() ? _initialState : _transitions.get(_transitions.size() - 1).getNewState();
    }

    public static class Builder<TState, TTrigger> {
        private TState _initialState;
        private final ArrayList<Transition> _transitions = new ArrayList<>();
        private StateTransitionHandler<TState, TTrigger> _transitionHandler;

        public Builder(TState initialState, StateTransitionHandler<TState, TTrigger> handler) {
            _initialState = initialState;
            if (_transitionHandler != null) {
                throw new IllegalArgumentException("Transition handler is already set.");
            }
            _transitionHandler = handler;
        }

        public Builder<TState, TTrigger> transition(TState from, TTrigger trigger, TState to) {
            Transition<TState, TTrigger> transition = new Transition<>(from, trigger, to);
            if (_transitions.contains(transition)) {
                throw new IllegalArgumentException("Transition already added.");
            }
            _transitions.add(transition);
            return this;
        }

        public Builder<TState, TTrigger> ignore(TState state, TTrigger trigger) {
            _transitions.add(new Transition(state, trigger));
            return this;
        }

        public StateMachine<TState, TTrigger> build() {
            return new StateMachine<>(_initialState, _transitions, _transitionHandler);
        }
    }
}
