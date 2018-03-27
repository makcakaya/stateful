package com.primecult.stateful;

public final class TransitionOutput<TState> {
    private final TState _state;
    private final boolean _ignored;
    private final boolean _illegal;

    private TransitionOutput(TState state, boolean ignored, boolean illegal) {
        _state = state;
        _ignored = ignored;
        _illegal = illegal;
    }

    public TState getState() {
        return _state;
    }

    public boolean isIgnored() {
        return _ignored;
    }

    public boolean isIllegal() {
        return _illegal;
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
        return _state == casted._state && _ignored == casted._ignored && _illegal == casted._illegal;
    }

    @Override
    public int hashCode() {
        int hash = 11;
        hash = 31 * hash + (_state == null ? 0 : _state.hashCode());
        hash = 37 * hash + (_ignored ? 0 : 1);
        hash = 41 * hash + (_illegal ? 0 : 1);
        return hash;
    }

    public static <TState> TransitionOutput<TState> legal(TState state) {
        if (state == null) {
            throw new IllegalArgumentException("state");
        }
        return new TransitionOutput<>(state, false, false);
    }

    public static <TState> TransitionOutput<TState> ignore() {
        return new TransitionOutput<>(null, true, false);
    }

    public static <TState> TransitionOutput<TState> illegal() {
        return new TransitionOutput<>(null, false, true);
    }
}
