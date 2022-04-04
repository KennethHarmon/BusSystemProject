package com.kenneth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.HashMap;

public class Main {

    private static EdgeWeightedDigraph graph;
    private static TST<Integer> tst;
    private static HashMap<String, String> stopMap;
    private static HashMap<String, ArrayList<StopTime>> stopTimes;

    public static void main(String[] args) throws IOException {
        graph = new EdgeWeightedDigraph("resources/stop_times.txt", "resources/transfers.txt");
        tst = new TST<Integer>();
        stopMap = new HashMap<String, String>();
        stopTimes = loadStopTimes();
        loadTST();
        startInterface();
    }

    private static void startInterface() {
        boolean hasQuit = false;
        Scanner input = new Scanner(System.in);
        System.out.println("Hello! Welcome to the bus app!");
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
                        findShortestPath(input);
                        break;
                    case 2:
                        System.out.println("You chose option 2");
                        searchStops(input);
                        break;
                    case 3:
                        System.out.println("You chose option 3");
                        searchByArrivalTime(input);
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

    private static void findShortestPath(Scanner input) {
        boolean inputRecieved = false;
        int from;
        int to;
        while (!inputRecieved) {
            System.out.println("What stop would you like to start from: ");
            if (input.hasNextInt()) {
                from = input.nextInt();
                System.out.println("Where would you like to go: ");
                if (input.hasNextInt()) {
                    to = input.nextInt();
                    if (checkGraphInputs(from, to)) {
                        inputRecieved = true;
                        ShortestPath shortestPath = new ShortestPath(graph, from);
                        if (shortestPath.hasPathTo(to)) {
                            System.out.println("The shortest path is :");
                            Iterable<DirectedEdge> currentPath = shortestPath.pathTo(to);
                            System.out.println(currentPath);
                        }
                        else {
                            System.out.println("Sorry, there doesn't seem to be a path between those two stops.");
                        }
                    }
                    else {
                        System.out.println("Your stops must be greater than 0 and be a valid stop number.");
                    }
                }
                else if (input.hasNext()) {
                    System.out.println("Error, please enter a valid stop number!");
                }
            }
            else if (input.hasNext()) {
                System.out.println("Error, please enter a valid stop number!");
            }
        }
    }

    private static boolean checkGraphInputs(int from, int to) {
        return (from < graph.V() && from > 0 && to < graph.V() && to > 0);
    }

    private static void loadTST() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("resources/stops.txt"));
            String line;
            boolean skippedHeadings = false;
            int counter = 0;
            while ((line = in.readLine()) != null) {
                if (skippedHeadings) {
                    String[] stopData = line.split(",");
                    StringBuilder newName = new StringBuilder();
                    int stopID = Integer.parseInt(stopData[0]);
                    int stopCode = -1;
                    if (!stopData[1].equals(" ")) {
                        stopCode = Integer.parseInt(stopData[1]);
                    }
                    String stopName = stopData[2];;
                    String desc = stopData[3];
                    String lat = stopData[4];
                    String lon = stopData[5];
                    String zoneID = stopData[6];
                    String url = stopData[7];
                    String locationType = stopData[8];
                    //String parentStation = stopData[9];
                    String currentStop;
                    String directionPrefix = stopName.substring(0,2);
                    String flagstopPrefix = stopName.substring(0,8);
                    if (flagstopPrefix.equalsIgnoreCase("flagstop")) {
                        newName.append(stopName.substring(9));
                        newName.append(" ").append(flagstopPrefix);
                    }
                    else if (directionPrefix.equalsIgnoreCase("wb") || directionPrefix.equalsIgnoreCase("nb") | directionPrefix.equalsIgnoreCase("eb") || directionPrefix.equalsIgnoreCase("sb")) {
                        newName.append(stopName.substring(3));
                        newName.append(" ").append(directionPrefix);
                    }
                    else {
                        newName.append(stopName);
                    }
                    //System.out.println(newName);
                     currentStop = newName + " : " +
                                "stopID=" + stopID +
                                ", stopCode=" + stopCode +
                                ", desc='" + desc + '\'' +
                                ", lat='" + lat + '\'' +
                                ", lon='" + lon + '\'' +
                                ", zoneID='" + zoneID + '\'' +
                                ", url='" + url + '\'' +
                                ", locationType='" + locationType + '\'' +
                                ", parentStation='" + "" + '\'';
                    stopMap.put(newName.toString(), currentStop);
                    tst.put(newName.toString(), counter);
                    counter++;
                }
                else {
                    skippedHeadings = true;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private static void searchStops(Scanner input) {
        System.out.println("Please enter your search: ");
        if (input.hasNext()) {
            String search = input.next();
            Iterable<String> results = tst.keysWithPrefix(search.toUpperCase());
            if (!results.iterator().hasNext()) {
                System.out.println("Sorry, no results were found for your search.");
                return;
            }
            System.out.println("These are the stops that were found: ");
            for (String result : results) {
                String stop = stopMap.get(result);
                System.out.println(stop);
            }
        }
    }

    private static HashMap<String, ArrayList<StopTime>> loadStopTimes() {
        //Reading stops file
        HashMap<String, ArrayList<StopTime>> stopTimesList = new HashMap<String, ArrayList<StopTime>>();
        try {
            BufferedReader in = new BufferedReader(new FileReader("resources/stop_times.txt"));
            String line;
            boolean skippedHeadings = false;
            while ((line = in.readLine()) != null) {
                if (skippedHeadings) {
                    String[] stopData = line.split(",");
                    StopTime currentStopTime = new StopTime(stopData);
                    ArrayList<StopTime> stopTimes;
                    String key = stopData[1].trim();
                    try {
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:m:s");
                        timeFormatter.parse(key);
                        try {
                            Integer.parseInt(key.substring(0, 2));
                        }
                        catch (NumberFormatException ignored) {
                            key = "0" + key;
                        }
                        stopTimes = stopTimesList.get(stopData[1]);
                        if (stopTimes == null) {
                            stopTimes = new ArrayList<StopTime>();
                        }
                        stopTimes.add(currentStopTime);
                        stopTimesList.put(key, stopTimes);
                    }
                    catch (DateTimeException ignored) {};
                }
                else {
                    skippedHeadings = true;
                }
            }
            return stopTimesList;
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return stopTimesList;
    }

    private static void searchByArrivalTime(Scanner input) {
        System.out.println("What time would you like to search for, please input it in the format HH:MM:SS");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:m:s");
        String search;
        boolean validInput = false;
        while ((search = input.next()) != null && !validInput) {
            try {
                timeFormatter.parse(search);
                validInput = true;
                ArrayList<StopTime> results = stopTimes.get(search);
                if (results == null) {
                    System.out.println("Sorry, no results were found for your search.");
                    return;
                }
                else {
                    Collections.sort(results);
                    System.out.println("These are the stops that were found (Sorted by TripID): ");
                    for (StopTime result : results) {
                        System.out.println(result);
                    }
                    break;
                }
            } catch (DateTimeParseException | NullPointerException e) {
                System.out.println("Sorry, your input seems to be formatted wrong. Please try again.");
                System.out.println("Make sure your input is in the format HH:MM:SS");
            }
        }
    }
}

class StopTime implements Comparable<StopTime>{
    public int tripID;
    public String arrivalTime;
    public String departureTime;
    public String stopID;
    public String stopSequence;
    public String stopHeadsign;
    public String pickupType;
    public String dropoffType;
    public String shapeDistTravelled;

    public StopTime(int tripID, String arrivalTime, String departureTime, String stopID, String stopSequence, String stopHeadsign, String pickupType, String dropoffType, String shapeDistTravelled) {
        this.tripID = tripID;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.stopID = stopID;
        this.stopSequence = stopSequence;
        this.stopHeadsign = stopHeadsign;
        this.pickupType = pickupType;
        this.dropoffType = dropoffType;
        this.shapeDistTravelled = shapeDistTravelled;
    }

    public StopTime(String[] stopTimeData) {
        this.tripID = Integer.parseInt(stopTimeData[0]);
        this.arrivalTime = stopTimeData[1];
        this.departureTime = stopTimeData[2];
        this.stopID = stopTimeData[3];
        this.stopSequence = stopTimeData[4];
        this.stopHeadsign = "";
        this.pickupType = stopTimeData[5];
        this.dropoffType = stopTimeData[6];
        this.shapeDistTravelled = stopTimeData[7];
    }

    @Override
    public int compareTo(StopTime o) {
        return this.tripID - o.tripID;
    }

    @Override
    public String toString() {
        return "Trip {" +
                "tripID=" + tripID +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", stopID='" + stopID + '\'' +
                ", stopSequence='" + stopSequence + '\'' +
                ", stopHeadsign='" + stopHeadsign + '\'' +
                ", pickupType='" + pickupType + '\'' +
                ", dropoffType='" + dropoffType + '\'' +
                ", shapeDistTravelled='" + shapeDistTravelled + '\'' +
                '}';
    }
}
