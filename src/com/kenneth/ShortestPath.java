package com.kenneth;
import com.kenneth.DirectedEdge;
import com.kenneth.EdgeWeightedDigraph;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Stack;

public class ShortestPath {

    private final EdgeWeightedDigraph graph;
    private final int s; //Source
    private final double[] distTo;
    private DirectedEdge[] edgeTo;
    private PriorityQueue<VertexDistance> pq;
    private HashSet<Integer> visited;

    public ShortestPath(EdgeWeightedDigraph G, int s) {
        this.graph = G;                                         //Graph used for finding shortest path
        this.s = s;                                             //Source vertex for dijkstra
        distTo = new double[G.V()];                             //Shortest distance found to [index] vertex
        edgeTo = new DirectedEdge[G.V()];                       //Final edge on shortest path to [index] vertex
        pq = new PriorityQueue<VertexDistance>(graph.V());      //Queue for choosing which vertex to work on next
        visited = new HashSet<Integer>();                       //Used to ensure we don't visit a vertex more than once
        runDijkstra();
    }

    private void runDijkstra() {
        //Initialise distances to "infinity"
        for (int i = 0; i < graph.V(); i++) {
            distTo[i] = Integer.MAX_VALUE;
        }

        //Distance is 0
        distTo[s] = 0;

        //Add the source node to the queue
        pq.add( new VertexDistance(s, distTo[s]));

        //While we still have nodes to check
        while(!pq.isEmpty()) {
            //Get the vertex with the current lowest distance
            int v = pq.remove().vertex;

            //Get that vertex's edges
            Iterable<DirectedEdge> edges = graph.adj(v);

            //Avoid nullpointer on vertex with no outgoing edges
            if (edges != null) {
                for (DirectedEdge edge : edges) {
                    //Relax all edges pointing from this vertex
                    relax(edge);

                    //Only add a vertex to the queue if we haven't already visited it
                    if (!visited.contains(edge.to())) {
                        pq.add(new VertexDistance(edge.to(), distTo[edge.to()]));
                        visited.add(edge.to());
                    }
                }
            }
        }
    }

    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if(distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
        }
    }

    double distTo(int v) {
        return distTo[v];
    }

    boolean hasPathTo(int v) {
        if (v == s) {
            return true;
        }
        else return edgeTo[v] != null;
    }

    Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        DirectedEdge e = edgeTo[v];
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        while(e != null) {
            path.push(e);
            e = edgeTo[e.from()];
        }
        return path;
    }

    private class VertexDistance implements Comparable<VertexDistance>{
        /**
         * Helper class which provides a mapping between a vertex's number and current distance
         */
        private int vertex;
        private double distance;

        public VertexDistance(int v, double w) {
            this.vertex = v;
            this.distance = w;
        }

        public int vertex(){ return this.vertex; }
        public double weight(){ return this.distance; }

        @Override
        public int compareTo(VertexDistance e) {
            return Double.compare(this.distance, e.weight());
        }
    }
}
