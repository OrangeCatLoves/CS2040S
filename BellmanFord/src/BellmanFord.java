
import java.util.ArrayList;
public class BellmanFord {
    // DO NOT MODIFY THE TWO STATIC VARIABLES BELOW
    public static int INF = 20000000;
    public static int NEGINF = -20000000;

    // TODO: add additional attributes and/or variables needed here, if any
    public ArrayList<ArrayList<IntPair>> arrList;
    public int[] shortestDistArr;

    public BellmanFord(ArrayList<ArrayList<IntPair>> arrList) {
        // TODO: initialize your attributes here, if any
        // Arraylist captures all the edge information in the graph
        // Shortestdistarr stores the shortestdist between 2 nodes
        this.arrList = arrList;
        this.shortestDistArr = new int[arrList.size()];
    }

    public void computeShortestPaths(int source) {
        // Initialisation of the arr. Initial shortest dist is
        // INFINITY except for the starting node
        this.shortestDistArr[source] = 0;
        for (int i = 0; i < shortestDistArr.length; i++) {
            if (i != source) {
                shortestDistArr[i] = BellmanFord.INF;
            }
        }

        // BellmanFord runs here. V-1 iterations and then for each iteration
        // Consider all the edges around the node v
        for (int i = 0; i < arrList.size() - 1; i++) { // Nodes
            // Consider all edges for the node.
            for (int j = 0; j < arrList.size(); j++) { // Edge

                for (IntPair pair : arrList.get(j)) {

                    if (shortestDistArr[j] != BellmanFord.INF) {
                        if (shortestDistArr[pair.first] > pair.second + shortestDistArr[j] && shortestDistArr[pair.first] != BellmanFord.NEGINF) {
                            shortestDistArr[pair.first] = pair.second + shortestDistArr[j];
                        }
                    }
                }
            }
        }


        // Checking of negative weight cycle.
        for (int i = 0; i < arrList.size(); i++) {

            for (IntPair pair : arrList.get(i)) {

                if (shortestDistArr[pair.first] > pair.second + shortestDistArr[i] && shortestDistArr[pair.first] != BellmanFord.NEGINF) {
                    // Detecting changes in the distance even though BellmanFord has already ran V iterations
                    // Suggest negative weight cycle. Assign NEGINF
                    for (int j = 0; j < arrList.size(); j++) {

                        for (int k = 0; k < arrList.size(); k++) {

                            for (IntPair p : arrList.get(k)) {

                                if (shortestDistArr[k] != BellmanFord.INF && shortestDistArr[p.first] > p.second + shortestDistArr[k] && shortestDistArr[p.first] != BellmanFord.NEGINF) {
                                    shortestDistArr[p.first] = NEGINF;
                                }
                            }
                        }
                    }
                    return;
                }
            }
        }
    }

    // TODO: add additional methods here, if any
    public int getDistance(int node) {
        // TODO: implement your getDistance operation here
        if (node >= 0 && node < arrList.size()) {
            return shortestDistArr[node];
        } else {
            return INF;
        }
    }
    public static void main(String[] args) {
        // Given example
        ArrayList<ArrayList<IntPair>> adjList = new ArrayList<>();
        ArrayList zero = new ArrayList<>();
        zero.add(new IntPair(3, 2));
        ArrayList one = new ArrayList<>();
        one.add(new IntPair(2, -2));
        ArrayList two = new ArrayList<>();
        two.add(new IntPair(1, 1));
        ArrayList three = new ArrayList<>();
        ArrayList four = new ArrayList<>();


        adjList.add(zero);
        adjList.add(one);
        adjList.add(two);
        adjList.add(three);
        adjList.add(four);
        BellmanFord graph = new BellmanFord(adjList);
        graph.computeShortestPaths(0);
        int a = graph.getDistance(0);
        int b = graph.getDistance(1);
        int c = graph.getDistance(2);
        int d = graph.getDistance(3);
        int e = graph.getDistance(4);
    }
}