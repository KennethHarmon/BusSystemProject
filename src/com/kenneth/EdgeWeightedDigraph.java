package com.kenneth;
import com.kenneth.Bag;
import com.kenneth.DirectedEdge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.Buffer;

public class EdgeWeightedDigraph {

    private int V; //Number of vertices
    private int E; //Number of Edges
    private Bag<DirectedEdge>[] adj;

    //Construct empty V-vertex digraph
    public EdgeWeightedDigraph(int V) {
        this.V = V;
        this.E = 0;
    }

    //Construct from In
    @SuppressWarnings("unchecked")
    public EdgeWeightedDigraph(String stopsFile, String transfersFile) throws IOException {
        //Reading stops file
        try {
            BufferedReader in = new BufferedReader(new FileReader(stopsFile));
            String line;
            String nextLine;
            boolean skippedHeadings = false;
            this.adj = (Bag<DirectedEdge>[]) new Bag[13500];
            this.V = 13500;
            double weight = 1;
            while ((line = in.readLine()) != null) {
                if (skippedHeadings) {
                    if ((nextLine = in.readLine()) != null) {
                        String[] fromEdgeData = line.split(",");
                        String[] toEdgeData = nextLine.split(",");
                        int fromTripID = Integer.parseInt(fromEdgeData[0]);
                        int toTripID = Integer.parseInt(toEdgeData[0]);
                        if (fromTripID == toTripID) {
                            int from = Integer.parseInt(fromEdgeData[3]);
                            int to = Integer.parseInt(toEdgeData[3]);
                            DirectedEdge edge = new DirectedEdge(from, to, weight);
                            if (adj[from] == null) {
                                Bag<DirectedEdge> edges = new Bag<DirectedEdge>();
                                edges.add(edge);
                                adj[from] = edges;
                            } else {
                                adj[from].add(edge);
                            }
                            this.E++;
                        }
                    }
                }
                else {
                    skippedHeadings = true;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        //Reading transfers file
        try {
            BufferedReader in = new BufferedReader(new FileReader(transfersFile));
            String line;
            boolean skippedHeadings = false;
            double weight;
            while ((line = in.readLine()) != null) {
                if (skippedHeadings) {
                    String[] transferData = line.split(",");
                    int from = Integer.parseInt(transferData[0]);
                    int to = Integer.parseInt(transferData[1]);
                    int transferType = Integer.parseInt(transferData[2]);
                    weight = 2;
                    if (transferType == 2) {
                        weight = Float.parseFloat(transferData[3]) / 100;
                    }
                    DirectedEdge edge = new DirectedEdge(from, to, weight);
                    if (adj[from] == null) {
                        Bag<DirectedEdge> edges = new Bag<DirectedEdge>();
                        edges.add(edge);
                        adj[from] = edges;
                    } else {
                        adj[from].add(edge);
                    }
                    this.E++;
                }
                else {
                    skippedHeadings = true;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
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

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Iterable<DirectedEdge> vertex:adj) {
            for (DirectedEdge edge: vertex) {
                output.append(edge.toString()).append("\n");
            }
        }
        return output.toString();
    }
}
