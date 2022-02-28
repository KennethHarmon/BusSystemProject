package com.kenneth;

public class ShortestPath {

    //Source
    private final int S;

    public ShortestPath(EdgeWeightedDigraph G, int s) {
        this.S = s;
    }

    //Distance from s to v, infinite if no path
    public double distTo(int v) {
        return 0;
    }

    //Path from s to v?
    boolean hasPathTo(int v) {
        return false;
    }

    //Path trace from s to v, null if none
    public Iterable<DirectedEdge> pathTo(int v) {
        return null;
    }
}
