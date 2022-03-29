package com.kenneth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashMap;

public class Main {

    private static EdgeWeightedDigraph graph;
    private static TST<Integer> tst;
    private static HashMap<String, Stop> stopMap;

    public static void main(String[] args) throws IOException {
        graph = new EdgeWeightedDigraph("resources/stop_times.txt", "resources/transfers.txt");
        tst = new TST<Integer>();
        stopMap = new HashMap<String, Stop>();
        loadTST();
        startInterface();
    }

    private static void startInterface() {
        boolean hasQuit = false;
        Scanner input = new Scanner(System.in);
        System.out.println("Hello! Welcome to the bus app!");
        System.out.println(String.format("Graph has %s vertices and %s edges.", graph.V(), graph.E()));
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
                    inputRecieved = true;
                    ShortestPath shortestPath = new ShortestPath(graph, from);
                    System.out.println("The shortest path is :");
                    Iterable<DirectedEdge> currentPath = shortestPath.pathTo(to);
                    System.out.println(currentPath);
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
                    Stop currentStop = new Stop(stopID, stopCode, stopName, desc, lat, lon, zoneID, url, locationType);
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
            Iterable<String> results = tst.keysWithPrefix(search);
            if (!results.iterator().hasNext()) {
                System.out.println("Sorry, no results were found for your search.");
                return;
            }
            System.out.println("These are the stops that were found: ");
            for (String result : results) {
                Stop stop = stopMap.get(result);
                System.out.println(stop);
            }
        }
    }

    private static void searchByArrivalTime() {

    }
}

class Stop{
    //stop_id,stop_code,stop_name,stop_desc,stop_lat,stop_lon,zone_id,stop_url,location_type,parent_station
    public int stopID;
    public int stopCode;
    public String stopName;
    public String desc;
    public String lat;
    public String lon;
    public String zoneID;
    public String url;
    public String locationType;
    public String parentStation;

    public Stop(int stopID, int stopCode, String stopName, String desc, String lat, String lon, String zoneID, String url, String locationType) {
        this.stopID = stopID;
        this.stopCode = stopCode;
        this.stopName = stopName;
        this.desc = desc;
        this.lat = lat;
        this.lon = lon;
        this.zoneID = zoneID;
        this.url = url;
        this.locationType = locationType;
        this.parentStation = "";
    }

    @Override
    public String toString() {
        return "Stop{" +
                "stopID=" + stopID +
                ", stopCode=" + stopCode +
                ", desc='" + desc + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", zoneID='" + zoneID + '\'' +
                ", url='" + url + '\'' +
                ", locationType='" + locationType + '\'' +
                ", parentStation='" + parentStation + '\'' +
                '}';
    }
}
