package com.kenneth;

import java.io.InputStream;
import java.util.HashMap;

public class EdgeWeightedDigraph {

    private final int V; //Number of vertices
    private int E; //Number of Edges
    private Bag<DirectedEdge>[] adj;

    //Construct empty V-vertex digraph
    public EdgeWeightedDigraph(int V) {
        this.V = V;
        this.E = 0;
    }

    //Construct from In
    public EdgeWeightedDigraph(InputStream in) {
        this.V = 0;
        this.E = 0;
    }

    //Number of vertices
    public int V() { return V; }

    //Number of edges
    public int E() { return E; }

    //Add edge e to this digraph
    private void addEdge(DirectedEdge e) {

    }

    //Edges pointing from v
    public Iterable<DirectedEdge> adj(int v) {
        return adj[v];
    }


}
