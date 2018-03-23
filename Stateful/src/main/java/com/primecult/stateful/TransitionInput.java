package com.primecult.stateful;

public final class TransitionInput<TState, TTrigger> {
    private final TState _state;
    private final TTrigger _trigger;

    public TransitionInput(TState state, TTrigger trigger) {
        if (state == null) {
            throw new IllegalArgumentException("state");
        }
        if (trigger == null) {
            throw new IllegalArgumentException("trigger");
        }
        _state = state;
        _trigger = trigger;
    }

    public TState getState() {
        return _state;
    }

    public TTrigger getTrigger() {
        return _trigger;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!this.getClass().isInstance(o)) {
            return false;
        }

        TransitionInput<TState, TTrigger> casted = (TransitionInput<TState, TTrigger>) o;
        return _state == casted._state && _trigger == casted._trigger;
    }

    @Override
    public int hashCode() {
        int hash = 11;
        hash = hash * 31 + _state.hashCode();
        hash = hash * 37 + _trigger.hashCode();
        return hash;
    }
}
