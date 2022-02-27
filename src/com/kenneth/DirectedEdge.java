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

    public double getWeight() {
        return weight;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    @Override
    public String toString() {
        return this.from + " -> " + this.to + " " + this.weight;
    }
}
