package com.primecult.stateful;

public final class TransitionOutput<TState> {
    private final TState _state;
    private final boolean _ignored;

    private TransitionOutput(TState state, boolean ignored) {
        _state = state;
        _ignored = ignored;
    }

    public TState getState() {
        return _state;
    }

    public boolean isIgnored() {
        return _ignored;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!this.getClass().isInstance(o)) {
            return false;
        }

        TransitionOutput<TState> casted = (TransitionOutput<TState>) o;
        return _state == casted._state && _ignored == casted._ignored;
    }

    @Override
    public int hashCode() {
        int hash = 11;
        hash = 31 * hash + (_state == null ? 0 : _state.hashCode());
        hash = 37 * hash + (_ignored ? 0 : 1);
        return hash;
    }

    public static <TState> TransitionOutput<TState> newState(TState state) {
        if (state == null) {
            throw new IllegalArgumentException("state");
        }
        return new TransitionOutput<>(state, false);
    }

    public static <TState> TransitionOutput<TState> ignore() {
        return new TransitionOutput<>(null, true);
    }
}
