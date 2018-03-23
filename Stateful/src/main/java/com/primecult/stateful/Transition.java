package com.primecult.stateful;

final class Transition<TState, TTrigger> {
    private final TransitionInput<TState, TTrigger> _input;
    private final TransitionOutput<TState> _output;

    public Transition(TState from, TTrigger trigger, TState to) {
        if (from == null) {
            throw new IllegalArgumentException("from");
        }
        if (trigger == null) {
            throw new IllegalArgumentException("trigger");
        }
        if (to == null) {
            throw new IllegalArgumentException("to");
        }
        _input = new TransitionInput<>(from, trigger);
        _output = TransitionOutput.newState(to);
    }

    public Transition(TState state, TTrigger trigger) {
        _input = new TransitionInput<>(state, trigger);
        _output = TransitionOutput.ignore();
    }

    public TState getOriginalState() {
        return _input.getState();
    }

    public TTrigger getTrigger() {
        return _input.getTrigger();
    }

    public TState getNewState() {
        return _output.getState();
    }

    public boolean isIgnored() {
        return _output.isIgnored();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!this.getClass().isInstance(other)) {
            return false;
        }

        Transition<TState, TTrigger> otherCasted = (Transition<TState, TTrigger>) other;
        return _input == otherCasted._input && _output == otherCasted._output;
    }

    @Override
    public int hashCode() {
        int hash = 11;
        hash = 31 * hash + _input.hashCode();
        hash = 37 * hash + _output.hashCode();
        return hash;
    }
}
