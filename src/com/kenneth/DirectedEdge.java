package com.kenneth;

public class DirectedEdge {

    private final double weight;
    private final int from;
    private final int to;

    public DirectedEdge(int from, int to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public double weight() {
        return weight;
    }

    public int from() {
        return from;
    }

    public int to() {
        return to;
    }

    @Override
    public String toString() {
        return this.from + " -> " + this.to + " " + this.weight;
    }
}
