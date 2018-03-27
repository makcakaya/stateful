package com.primecult.stateful;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class TransitionLogger<TState, TTrigger> {
    private final LoggingMode _loggingMode;
    private final LinkedList<Transition<TState, TTrigger>> _transitions;

    public void log(Transition<TState, TTrigger> transition) {
        if (transition == null || transition.isIgnored()) {
            return; // Ignore nulls
        }

        switch (_loggingMode) {
            case None:
                break;
            case IllegalTransitionsOnly:
                if (transition.getOutput().isIllegal()) {
                    _transitions.add(transition);
                }
                break;
            case LegalTransitionsOnly:
                if (!transition.getOutput().isIllegal()) {
                    _transitions.add(transition);
                }
                break;
            case LegalAndIllegalTransitions:
                if (!transition.isIgnored()) {
                    _transitions.add(transition);
                }
                break;
        }
    }

    public List<Transition<TState, TTrigger>> getLogs() {
        return Collections.unmodifiableList(_transitions);
    }

    public Transition<TState, TTrigger> getFirstIllegalTransition() {
        for (Transition<TState, TTrigger> transition : _transitions) {
            if (transition.isIllegal()) {
                return transition;
            }
        }
        return null;
    }

    public TransitionLogger(LoggingMode loggingMode) {
        _loggingMode = loggingMode;
        _transitions = new LinkedList<>();
    }

    public enum LoggingMode {
        None,
        IllegalTransitionsOnly,
        LegalTransitionsOnly,
        LegalAndIllegalTransitions
    }
}
