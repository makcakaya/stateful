package com.primecult.stateful;

public interface StateTransitionHandler<TState, TTrigger> {
    void preTransition(TState from, TTrigger trigger, TState to);

    void postTransition(TState from, TTrigger trigger, TState to);

    void illegalTransition(TState from, TTrigger trigger);
}
