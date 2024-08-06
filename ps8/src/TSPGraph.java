import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.*;

public class TSPGraph implements IApproximateTSP {
    private double[][] initialiseAdjacencyMatrix(TSPMap map, int n) {
        // Stores the distance represented as the weight of the edges between the points
        double[][] adjMatrix = new double[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                adjMatrix[i][j] = map.pointDistance(i, j);

        return adjMatrix;
    }

    private boolean[] initialiseVisitedArr(int n) {
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) visited[i] = false;
        return visited;
    }

    @Override
    public void MST(TSPMap map) {

        int numPoints = map.getCount(); // Total number of points in the graph.

        // Adjacency matrix so store the shortest distance from point i to j
        // Initially initialised with the distance between each and every point in the graph
        // Distance being the edge, representing a clique graph
        double[][] adj = initialiseAdjacencyMatrix(map, numPoints);

        // Initialisation of priorityQueue for prims, adding each node into the priorityQueue
        // Key is the node represented by an Integer and the priority is the edge weights represented
        // as a Double
        TreeMapPriorityQueue<Double, Integer> pq = new TreeMapPriorityQueue<>();
        for (int i = 0; i < numPoints; i++) pq.add(i, Double.MAX_VALUE);

        // Starting node
        pq.decreasePriority(0, 0.0);

        // To keep track of whether node (i), index of array is visited
        boolean[] visited = initialiseVisitedArr(numPoints);
        visited[0] = true;

        // A hashmap to keep track of the parent nodes of each node.
        HashMap<Integer, Integer> hashTableparent = new HashMap<>();
        // Starting root node does not have a parent
        hashTableparent.put(0, null);

        // Prims algorithm
        while (!pq.isEmpty()) { // Populates the parent hashTable for processing the links that should be set to produce MST
            // Lowest weight edge in this case distance
            int curr = pq.extractMin();
            visited[curr] = true; // To keep track of the visited nodes.
            // visited array would represent the set of finished visited nodes which
            // will keep increasing as we relax more and more edges

            // Considering every neighbouring edge since each point is connected to v-1 other points.
            for (int nextPoint = 0; nextPoint < numPoints; nextPoint++) {
                // Initial shortest distance assigned previously between currPoint and nextPoint
                double dist = adj[curr][nextPoint];
                // When curr = nextPoint distance is 0.0, basically you're considering the same point
                if (dist == 0.0) continue;

                if (!visited[nextPoint]) {
                    pq.decreasePriority(nextPoint, dist);
                    if (!hashTableparent.containsKey(nextPoint) || dist < adj[nextPoint][hashTableparent.get(nextPoint)])
                        hashTableparent.put(nextPoint, curr); // Reassign parent since there exists an shorter edge to the point
                }
            }
        }

        // Sets the directed edges for MST to be produced
        // Entry<Integer, Integer> => Entry<Child, Parent>
        for (Map.Entry<Integer, Integer> entry : hashTableparent.entrySet())
            if (entry.getValue() != null) map.setLink(entry.getKey(), entry.getValue(), false);

        map.redraw();
    }

    /*@Override
    public void MST(TSPMap map) {
        // TODO: implement this method
        // Initialisation process for Prims algorithm
        // Priority depends on the edge of weight => smaller weight means higher priority
        TreeMapPriorityQueue<Double, Integer> priorityQueue = new TreeMapPriorityQueue<>();

        for (int i = 0; i < map.getCount(); i++) { // Add every node into pq
            // Node(key, priority) key is the Points, priority is the weight of incoming edge
            // Pair pair = new Pair(i, Integer.MAX_VALUE);
            priorityQueue.add(i, Double.MAX_VALUE);
        }
        priorityQueue.decreasePriority(0, 0.0); // Starting point, with 0 weight (Lowest priority)
        HashSet hSet = new HashSet();
        hSet.add(0); // Adding starting node to hashSet

        // Initialise parent hashTable
        HashMap<Integer, Integer> hashTableParents = new HashMap<>();
        hashTableParents.put(0, 0); // Starting source node parent is null (There's no parent)
        // Initialised parent as itself
        //Integer parent = -1; // Initial parent is null for root node, will be reassigned

        // parent = firstPoint;

        while (!priorityQueue.isEmpty()) {
            Integer currPoint = priorityQueue.extractMin(); // Get Integer that is used to represent a point
            hSet.add(currPoint); // Dequeued node gets added to hashSet
            hashTableParents.put(currPoint, hashTableParents.get(currPoint));
            System.out.println(currPoint);
            map.setLink(currPoint, hashTableParents.get(currPoint));
            // Consider the neighbours of each point, each points is connected to every other points
            for (int i = 0; i < map.getCount(); i++) { // Every iteration, there's an attempt to update every
                // node's distance
                Double calculatedDistance = map.pointDistance(currPoint, i);
                if (i != currPoint && !hSet.contains(i) && priorityQueue.lookup(i) > calculatedDistance) {
                    map.eraseLink(i);
                    priorityQueue.decreasePriority(i, calculatedDistance);
                    hashTableParents.put(i, currPoint);
                }
            }
            // parent = currPoint;
        }
        map.redraw();
    }*/

    @Override
    public void TSP(TSPMap map) {
        MST(map);

        // Calculate the number of points
        int numPoints = map.getCount();

        //Stack stackforDFS = new Stack<>();
        int[] parent = new int[numPoints]; // Stores edge information, point i (arr index) to point (element in arr)
        boolean[] visited = initialiseVisitedArr(numPoints);
        HashMap<Integer, ArrayList<Integer>> hashmap = new HashMap<>();

        // Initialisation of a hashMap with ArrayList to store
        for (int i = -1 ; i < numPoints; i++) {
            hashmap.put(i, new ArrayList<Integer>());
        }

        // Parent arr and hashMap keeps track of incoming and outgoing edges between points i and j

        for (int i = 0 ; i < numPoints; i++) {
            parent[i] = map.getLink(i); // Retrieves the outgoing link from point i to point that is connected to
            // and stores it in parent array
            Integer key = map.getLink(i); // Point at the end of outgoing edge
            // Preprocess the hashMap with
            hashmap.get(key).add(i);
            // The links in present MST to be erased soon after to be replaced by TSP graph
            // Links to be refreshed
            map.eraseLink(i, false);
        }

        int curr = 0; // Starting point, defaulted to point 0
        int tempPoint = -1; // To be reassigned

        // Depth-first search
        while (curr != 0 || !hashmap.get(0).isEmpty()) {
            // Terminates when we reach the node that we started from again
            if (hashmap.get(curr).isEmpty()) { // Leaf node, no incoming edge

                // Current Point is a leaf hence you backtrack
                // An empty hashmap.get(i) => there's no incoming edges toward point i
                int prevPoint = parent[curr];
                if (!visited[curr]) {
                    visited[curr] = true;
                    tempPoint = curr;
                }
                curr = prevPoint;
            } else {
                int next = hashmap.get(curr).remove(0);
                if (!visited[curr]) {
                    visited[curr] = true;
                    map.setLink(curr, next, false);
                } else {
                    map.setLink(tempPoint, next, false);
                }
                curr = next;
            }
        }
        map.setLink(tempPoint, 0, false);

        map.redraw();
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        int next = map.getLink(0);
        int parent = 0;
        int num = 1; // root
        int numPoints = map.getCount();

        if (map != null) {

            if (numPoints == 1) return true;

            while (next != parent) {
                if (next == -1) return false;
                if (numPoints == num && next == parent) return true;
                if (numPoints == num && next != parent) return false;
                num++;
                next = map.getLink(next);
            }
            return num == map.getCount();
        }
        return false;
    }

    @Override
    public double tourDistance(TSPMap map) {
        if (isValidTour(map)) {
            return calculateDist(map, 0.0, 0, 0);
        }
        return -1;
    }

    public double calculateDist(TSPMap map, double distance, int source, int start) {

        if (map.getLink(source) == start) {
            return distance + map.pointDistance(source, start);
        }
        double newDist = distance + map.pointDistance(map.getLink(source), source);
        return calculateDist(map, newDist, map.getLink(source), start);
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "fiftypoints.txt");
        TSPGraph graph = new TSPGraph();

         //graph.MST(map);
        graph.TSP(map);
        //System.out.println(graph.isValidTour(map));
        //System.out.println(graph.tourDistance(map));
    }
}