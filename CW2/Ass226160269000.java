/*
    * =====================================================================
    * Course:  CO2226, Software Engineering, Algorithm Design and Analysis
    *          AYA 2017-18, UOL International Programmes
    * Title:   Coursework Assignment 2
    * Student: Phone Thant Ko
    * SRN:     160269000
    * 
    * Please type the following to run the program
    * java Ass226160269000 (airportID-airportName data file) (airportID-longtitude-latitude data file) (airportID1-airportID2-adjacencyValue data file)
    * =====================================================================
*/

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Ass226160269000 {
    
    static int N = 3000;                                                            //change the value if the id of the airports get more than 3000
    static double edges[][] = new double[N][N];                                     //adjacency matrix for graph with normal weight
    static double edgesDistance[][] = new double[N][N];                             //different adjacency matrix for graph with km weight
    static TreeMap <Integer, String> airportNames = new TreeMap <Integer, String> ();
    static Set <Integer> airportKeys = airportNames.keySet();
    static HashMap <Integer, double[]> airportLonLat = new HashMap <Integer, double[]>();
    static Graph g;
    static ArrayList<Integer> maxSSP = new ArrayList<Integer>();
    static long startingTime, endingTime;
    
    public static void main(String[] args) throws FileNotFoundException {
        /* Record the Starting Time */
        startingTime = System.nanoTime();
        
        for(int i = 0; i < N; i++)
        {
            for(int j = 0; j < N; j++)
            {
                edges[i][j] = 0.0;
            }
        }
        /* 
        File Names
        ---------------
        Please note that the program assumes the user to enter the respective file names with the extensions INCLUDED. 
        */
        String filename1 = args[0];
        String filename2 = args[1];
        String filename3 = args[2];
        String z;
        
        /* Scanning the Airport Names */
        Scanner s1 = new Scanner(new FileReader(filename1));
        z = s1.nextLine();
        while(s1.hasNext())
        {
            z = s1.nextLine();
            String[] results = z.split(",");
            airportNames.put(Integer.parseInt(results[0]), results[1]);//code, ATA
        }
        
        s1 = new Scanner(new FileReader(filename2));
        while(s1.hasNext()){
            z = s1.nextLine();
            String[] results = z.split(",");
            double[] lon_lat = new double [2];
            lon_lat[0] = Double.parseDouble(results[1]);//longtitude
            lon_lat[1] = Double.parseDouble(results[2]);//latitude
            airportLonLat.put(Integer.parseInt(results[0]), lon_lat);
        }
        
        /* Scanning the adjency matrix with respective weights*/
        s1 = new Scanner(new FileReader(filename3));
        while(s1.hasNext())
        {
            z = s1.nextLine();
            String[] results = z.split(",");
            edges[Integer.parseInt(results[0])][Integer.parseInt(results[1])] = Double.parseDouble(results[2]); //code, code, weight
            edgesDistance[Integer.parseInt(results[0])][Integer.parseInt(results[1])] = realDistance(
                    airportLonLat.get(Integer.parseInt(results[0]))[1], airportLonLat.get(Integer.parseInt(results[0]))[0],
                    airportLonLat.get(Integer.parseInt(results[1]))[1], airportLonLat.get(Integer.parseInt(results[1]))[0]);
        }
        
        /* Create Graph objects */
        g = new Graph(edges);
        
        /* Output as instructed */
        printAnswers();
    }
    
    static void printAnswers()
    {
        System.out.println("Name: Phone Thant Ko");
        System.out.println("Student ID: 160269000");
        
        System.out.println("");
        System.out.println("Question 1: " + g.shortestPaths(getKeyUsingValue("ATH"), getKeyUsingValue("LHR")).size());
        calculateMaxSSP();
        System.out.println("Question 2: " + maxSSP.get(0) + " " + maxSSP.get(1));
        System.out.println("Question 3: " + maxSSP.get(2));
        System.out.println("Question 4: " + firstElement(g.shortestPaths(maxSSP.get(0), maxSSP.get(1))).size());
        System.out.println("Question 5: " + furthestAirport(getKeyUsingValue("LAX")));
        System.out.println("Question 6: " + findLengthInSum(getKeyUsingValue("LGA"), getKeyUsingValue("CDG")));
        System.out.println("Question 7: " + findLengthInKM(getKeyUsingValue("SIN"), getKeyUsingValue("FCO")));
        System.out.println("");
        
        endingTime = System.nanoTime();
        System.out.println("Execution Time: " + (endingTime - startingTime)/1000000 + " milliseconds");
    }
    
    static void calculateMaxSSP()
    {
        int startingAirport = 0, endingAirport = 0, maxNum = 0;
        HashSet <ArrayList <Integer>> shortestPaths;
        
        for(int startingKey: airportKeys)
        {
            for(int endingKey: airportKeys)
            {
                shortestPaths = g.shortestPaths(startingKey, endingKey);
                int currentSize = 0;
                if(!shortestPaths.isEmpty())
                {
                    currentSize = shortestPaths.size();
                    if(maxNum < currentSize)
                    {
                        maxNum = currentSize;
                        startingAirport = startingKey;
                        endingAirport = endingKey;
                    }
                }
            }
        }
        maxSSP.add(startingAirport);
        maxSSP.add(endingAirport);
        maxSSP.add(maxNum);
    }
    
    static ArrayList<Integer> furthestAirport(int startingAirport)
    {
        int max = 0, currentSize = 0;
        ArrayList<Integer> result = new ArrayList<Integer> ();
        ArrayList<Integer> tempList = new ArrayList<Integer> ();
        for(int endKey: airportKeys)
        {
            if(startingAirport != endKey)
            {
                HashSet<ArrayList<Integer>> tempHash = g.shortestPaths(startingAirport, endKey);
                if(!tempHash.isEmpty())
                {
                    for(ArrayList temp: tempHash)
                    {
                        currentSize = temp.size();
                        if(max <=  currentSize)
                        {
                            max = currentSize;
                            if(!tempList.contains(endKey)) tempList.add(endKey);
                        }
                    }
                }
            }
        }
        for(int endKey: tempList)
        {
            if(startingAirport != endKey)
            {
                HashSet<ArrayList<Integer>> tempHash = g.shortestPaths(startingAirport, endKey);
                if(!tempHash.isEmpty())
                {
                    for(ArrayList temp: tempHash)
                    {
                        currentSize = temp.size();
                        if(max ==  currentSize)
                        {
                            if(!result.contains(endKey)) result.add(endKey);
                        }
                    }
                }
            }
        }
        return result;
    }
    
    static double findLengthInSum(int start, int end)
    {
        double distance = 0.0, temp = 0.0;
        ArrayList<Integer> path = g.dijkstra(start, end);
        if(!path.isEmpty())
        {
            if(path.size() == 2)
            {
                temp = edges[path.get(0)][path.get(1)];
                edges[path.get(0)][path.get(1)] = 0.0;
                ArrayList<Integer> pathFinal = g.dijkstra(start, end);
                edges[path.get(0)][path.get(1)] = temp;
                distance = g.Q.get(pathFinal.get(pathFinal.size() - 1));
            }
            else
            {
                distance = g.Q.get(path.get(path.size() - 1));
            }
        }
        return distance;
    }
    
    static double realDistance(double lat1, double lon1, double lat2, double lon2)
    {
        int R = 6371;
        // km (change this constant to get miles)
        double dLat = (lat2 - lat1) * Math.PI/180;
        double dLon = (lon2 - lon1) * Math.PI/180;
        double a =  Math.sin(dLat/2) * Math.sin(dLat/2) + 
                    Math.cos(lat1 * Math.PI/180) * Math.cos(lat2 * Math.PI/180) * 
                    Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d;
    }

    static double findLengthInKM(int start, int end)
    {
        double[] firstAirport = null, secondAirport = null;
        double distance = 0.0, temp = 0.0;
        edges = edgesDistance;
        g = new Graph(edges);
        ArrayList<Integer> path = g.dijkstra(start, end);
        if(!path.isEmpty())
        {   
            if(path.size() == 2)
            {
                temp = edges[path.get(0)][path.get(1)];
                edges[path.get(0)][path.get(1)] = 0.0;
                ArrayList<Integer> pathFinal = g.dijkstra(start, end);
                edges[path.get(0)][path.get(1)] = temp;
                for(int i = 0; i < path.size(); i++)
                {
                    if(secondAirport == null)
                    {
                        secondAirport = airportLonLat.get(pathFinal.get(i));
                    }
                    else
                    {
                        firstAirport = airportLonLat.get(pathFinal.get(i));
                        distance += realDistance(firstAirport[1], firstAirport[0], secondAirport[1], secondAirport[0]);
                        secondAirport = firstAirport;
                    }
                }
            }
            else
            {
                for(int i = 0; i < path.size(); i++)
                {
                    if(secondAirport == null)
                    {
                        secondAirport = airportLonLat.get(path.get(i));
                    }
                    else
                    {
                        firstAirport = airportLonLat.get(path.get(i));
                        distance += realDistance(firstAirport[1], firstAirport[0], secondAirport[1], secondAirport[0]);
                        secondAirport = firstAirport;
                    }
                }
            }
        }
        return distance;
    }

    static int getKeyUsingValue(String value)
    {
        int key = 0;
        for(Entry<Integer, String> entry: airportNames.entrySet())
        {
            if(entry.getValue().equals(value))
            {
                key = entry.getKey();
            }
        }
        return key;
    }
    
    @SuppressWarnings("unchecked")
    static ArrayList<Integer> firstElement (HashSet <ArrayList <Integer>> s)
    {
        return ( ArrayList<Integer>)s.toArray()[0];
    }
    
    static ArrayList<String> convert (ArrayList<Integer> m)
    {
        ArrayList<String> z= new ArrayList<String>();
        for (Integer i:m)
            z.add(airportNames.get(i));
        return z;
    }
    
    static HashSet<ArrayList<String>> convert (HashSet<ArrayList<Integer>> paths)
    {
        HashSet <ArrayList <String>> k= new HashSet <ArrayList<String>>();
        for (ArrayList <Integer> p:paths)
            k.add(convert(p));
        return k;
    }
    
    
    /*===================================================================
    *Graph Class 
    =====================================================================*/
    static class Graph
    {
        double [] [] adj;
        static HashMap<Integer, Double> Q = new HashMap<Integer, Double>();
        double sum = 0.0;
            
        Graph (double [] [] a)
        //constructor for Graph class
        {
            adj = new double [a.length][a.length];
            for (int i=0;i<a.length;i++)
            {
                for (int j=0;j<a.length;j++) 
                {
                    adj[i][j]=a[i][j];
                }
            }
        }
        
        public HashSet <Integer> neighbours(int v)
        {
            HashSet <Integer> h = new HashSet <Integer> ();
            for (int i=0; i<adj.length; i++)
            {
                if (adj[v][i]!=0) h.add(i);
            }
            return h;
        }

        public HashSet <Integer> vertices()
        {
            HashSet <Integer> h = new HashSet <Integer>(); 
            for (int i=0;i<adj.length;i++) 
                h.add(i); 
            return h;
        }
        
        @SuppressWarnings("unchecked")
        ArrayList <Integer> addToEnd (int i, ArrayList <Integer> path)
        // returns a new path with i at the end of path
        {
            ArrayList <Integer> k; 
            k=(ArrayList<Integer>)path.clone(); 
            k.add(i);
            return k;
        }
        
        @SuppressWarnings("unchecked")
        public HashSet <ArrayList <Integer>> shortestPathsRecur(HashSet <ArrayList <Integer>> sofar, HashSet <Integer> visited, int end, boolean firstAttempt)
        {
            HashSet <ArrayList <Integer>> more = new HashSet <ArrayList<Integer>>();
            HashSet <ArrayList <Integer>> result = new HashSet <ArrayList<Integer>>();
            HashSet <Integer> newVisited = (HashSet <Integer>) visited.clone();
            boolean done = false;
            boolean carryon = false;
            
            
            for (ArrayList <Integer> p: sofar)
            {   
                for (Integer z: neighbours(p.get(p.size()-1)))
                {
                    if (!visited.contains(z))
                    {
                        carryon=true; newVisited.add(z);
                        if (z==end) 
                        {   
                            if(!firstAttempt){
                                done=true;
                                result.add(addToEnd(z,p));
                            }
                            else
                            {
                                firstAttempt = false;
                                more.add(addToEnd(z,p));
                                newVisited.remove(z);
                            }
                        }
                        else
                        {
                            more.add(addToEnd(z,p));
                        }
                    }
                }
            }
            
            if (done) 
            {
                return result;
            } 
            else
            {
                if (carryon)
                {
                    return shortestPathsRecur(more, newVisited, end, firstAttempt);
                }
                else
                {
                    return new HashSet <ArrayList <Integer>>();
                }
            }
        }
        
        public HashSet <ArrayList <Integer>> shortestPaths(int first, int end)
        {
            HashSet <ArrayList <Integer>> sofar = new HashSet <ArrayList<Integer>>();
            HashSet <Integer> visited = new HashSet<Integer>();
            ArrayList <Integer> starting = new ArrayList<Integer>();
            boolean firstAttempt = true;
            
            starting.add(first);
            sofar.add(starting);
            
            if (first==end)
            {
                return sofar;
            }
            else
            {
                return shortestPathsRecur(sofar, visited, end, firstAttempt);
            }
        }
        
        int findSmallest(HashMap <Integer, Double> t)
        {
            Object[] things = t.keySet().toArray();
            double val = t.get(things[0]);
            int least = (int) things[0];
            Set <Integer> k = t.keySet();
            for(Integer i: k)
            {
                if(t.get(i) < val)
                {
                    least = i;
                    val = t.get(i);
                }
            }
            return least;
        }
        
        @SuppressWarnings("unchecked")
        public ArrayList<Integer> dijkstra (int start, int end) 
        {   
            int N = adj.length;
            final double INFINITY = Double.POSITIVE_INFINITY;
		
            ArrayList<Integer>[] paths = new ArrayList[N]; // Path
            for (int i = 0; i < N; i++) {
                    Q.put(i, INFINITY);
                    paths[i] = new ArrayList<Integer>();
                    paths[i].add(start);
            }

            HashSet<Integer> S = new HashSet<Integer>();
            S.add(start);
            Q.put(start, 0.0);
            while (!Q.isEmpty()) {
                int v = findSmallest(Q);
                if (v == end && (Q.get(v) != INFINITY)) {
                        return paths[end];
                }
                double w = Q.get(v);
                S.add(v);
                for (int u : neighbours(v))
                        if (!S.contains(u)) {
                                double w1 = w + adj[v][u];
                                if (w1 < Q.get(u)) {
                                        Q.put(u, w1);
                                        paths[u] = addToEnd(u, paths[v]);
                                }
                        }
                Q.remove(v);
            }
            return new ArrayList<Integer>();
        }
    }  
}
