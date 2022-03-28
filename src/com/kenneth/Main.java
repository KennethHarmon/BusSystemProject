package com.kenneth;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static EdgeWeightedDigraph graph;

    public static void main(String[] args) throws IOException {
        graph = new EdgeWeightedDigraph("resources/stop_times.txt", "resources/transfers.txt");
        startInterface();
    }

    private static void startInterface() {
        boolean hasQuit = false;
        Scanner input = new Scanner(System.in);
        System.out.println("Hello! Welcome to the bus app!");
        ShortestPath currentPath = new ShortestPath(graph, 646);
        System.out.println(String.format("Graph has %s vertices and %s edges.", graph.V(), graph.E()));
        System.out.println(currentPath.pathTo(64));
        while (!hasQuit) {
            System.out.println("Which option would you like (or press q to quit): ");
            System.out.println("1: Find a path between two stops of your choosing.");
            System.out.println("2: Search for a particular stop.");
            System.out.println("3: Search for a trip with a given arrival time.");
            if (input.hasNextInt()) {
                int option = input.nextInt();
                switch (option) {
                    case 1:
                        System.out.println("You chose option 1");
                        break;
                    case 2:
                        System.out.println("You chose option 2");
                        break;
                    case 3:
                        System.out.println("You chose option 3");
                        break;
                    default:
                        System.out.println("Hmm.. that's not one of the options, please choose again.");
                }
            }
            else if (input.hasNext()){
                String option = input.next();
                if (option.equalsIgnoreCase("q")) {
                    System.out.println("Hope you found what you were looking for!");
                    hasQuit = true;
                }
                else {
                    System.out.println("Hmm, that's not one of the options. Please select again!");
                }
            }
        }
    }

    private static void findShortestPath() {

    }

    private static void searchStops() {

    }

    private static void searchByArrivalTime() {

    }
}
