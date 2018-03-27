package com.primecult.stateful;

final class Transition<TState, TTrigger> {
    private final TransitionInput<TState, TTrigger> _input;
    private final TransitionOutput<TState> _output;

    private Transition(TransitionInput<TState, TTrigger> input, TransitionOutput<TState> output) {
        if (input == null) {
            throw new IllegalArgumentException("input");
        }
        if (output == null) {
            throw new IllegalArgumentException("output");
        }
        _input = input;
        _output = output;
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

    public boolean isIllegal() {
        return _output.isIllegal();
    }

    public TransitionInput<TState, TTrigger> getInput() {
        return _input;
    }

    public TransitionOutput<TState> getOutput() {
        return _output;
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
        return _input.equals(otherCasted._input) && _output.equals(otherCasted._output);
    }

    @Override
    public int hashCode() {
        int hash = 11;
        hash = 31 * hash + _input.hashCode();
        hash = 37 * hash + _output.hashCode();
        return hash;
    }

    public static <TState, TTrigger> Transition<TState, TTrigger> legal(TState originalState, TTrigger trigger, TState newState) {
        if (originalState == null) {
            throw new IllegalArgumentException("originalState");
        }
        if (trigger == null) {
            throw new IllegalArgumentException("trigger");
        }
        if (newState == null) {
            throw new IllegalArgumentException("newState");
        }
        return new Transition<>(new TransitionInput<>(originalState, trigger), TransitionOutput.legal(newState));
    }

    public static <TState, TTrigger> Transition<TState, TTrigger> legal(TransitionInput<TState, TTrigger> input, TransitionOutput<TState> output) {
        return new Transition<>(input, output);
    }

    public static <TState, TTrigger> Transition<TState, TTrigger> illegal(TransitionInput<TState, TTrigger> input) {
        return new Transition<>(input, TransitionOutput.<TState>illegal());
    }

    public static <TState, TTrigger> Transition<TState, TTrigger> ignored(TransitionInput<TState, TTrigger> input) {
        return new Transition<>(input, TransitionOutput.<TState>ignore());
    }
}
