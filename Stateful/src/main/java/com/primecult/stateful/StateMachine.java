package com.primecult.stateful;

import java.util.HashMap;
import java.util.Map;

/**
 * State machine tracks state of your code.
 *
 * @param <TState>
 * @param <TTrigger>
 */
public class StateMachine<TState, TTrigger> {
    private static final String CONSTANT_VALUE = "Constant Value";
    private TState _currentState;
    private final Map<TransitionInput<TState, TTrigger>, TransitionOutput<TState>> _legalTransitions;
    private final StateTransitionHandler<TState, TTrigger> _transitionHandler;
    private final TransitionLogger<TState, TTrigger> _transitionLogger;

    private StateMachine(TState initialState, HashMap<TransitionInput<TState, TTrigger>, TransitionOutput<TState>> legalTransitions,
                         StateTransitionHandler<TState, TTrigger> transitionHandler, TransitionLogger<TState, TTrigger> transitionLogger) {
        if (initialState == null) {
            throw new IllegalArgumentException("initialState");
        }
        if (legalTransitions == null) {
            throw new IllegalArgumentException("legalTransitions");
        }
        if (transitionLogger == null) {
            throw new IllegalArgumentException("transitionLogger");
        }

        _currentState = initialState;
        _legalTransitions = new HashMap<>(legalTransitions);
        _transitionHandler = transitionHandler;
        _transitionLogger = transitionLogger;
    }

    public void trigger(TTrigger trigger) {
        if (trigger == null) {
            throw new IllegalArgumentException("trigger");
        }

        TransitionInput<TState, TTrigger> input = new TransitionInput<>(getCurrentState(), trigger);
        if (!_legalTransitions.containsKey(input)) {
            if (_transitionHandler != null) {
                _transitionLogger.log(Transition.illegal(input));
                _transitionHandler.illegalTransition(input.getState(), trigger);
            }
            return;
        }
        TransitionOutput<TState> output = _legalTransitions.get(input);
        if (output.isIgnored()) {
            return;
        }

        _transitionLogger.log(Transition.legal(input, output));
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

    public Transition<TState, TTrigger> getFirstIllegalTransition() {
        return _transitionLogger.getFirstIllegalTransition();
    }

    public static class Builder<TState, TTrigger> {
        private final HashMap<TransitionInput<TState, TTrigger>, TransitionOutput<TState>> _transitions = new HashMap<>();
        private TState _initialState;
        private StateTransitionHandler<TState, TTrigger> _transitionHandler;
        private TransitionLogger<TState, TTrigger> _transitionLogger;

        public Builder(TState initialState, StateTransitionHandler<TState, TTrigger> handler) {
            _initialState = initialState;
            if (_transitionHandler != null) {
                throw new IllegalArgumentException("Transition handler is already set.");
            }
            _transitionHandler = handler;
        }

        public Builder<TState, TTrigger> transition(TState from, TTrigger trigger, TState to) {
            registerInputToOutputMap(new TransitionInput<>(from, trigger), TransitionOutput.legal(to));
            return this;
        }

        public Builder<TState, TTrigger> ignore(TState state, TTrigger trigger) {
            registerInputToOutputMap(new TransitionInput<TState, TTrigger>(state, trigger), null);
            return this;
        }

        public Builder<TState, TTrigger> loggingMode(TransitionLogger.LoggingMode mode) {
            _transitionLogger = new TransitionLogger<>(mode);
            return this;
        }

        public StateMachine<TState, TTrigger> build() {
            return new StateMachine<>(_initialState, _transitions, _transitionHandler,
                    _transitionLogger != null ? _transitionLogger : getDefaultTransitionLogger());
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

        private TransitionLogger<TState, TTrigger> getDefaultTransitionLogger() {
            return new TransitionLogger<>(TransitionLogger.LoggingMode.IllegalTransitionsOnly);
        }
    }
}
