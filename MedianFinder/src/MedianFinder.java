import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Collections;
import java.util.PriorityQueue;

public class MedianFinder {

    // Median maintenance algorithm - the use of 2 priority queues where the 2 priorityqueues store the smaller half
    // and larger half of the total elements
    // TODO: Include your data structures here

    public MedianFinder() {
        // TODO: Construct/Initialise your data structures here
    }

    private PriorityQueue<Integer> small = new PriorityQueue<>(Collections.reverseOrder());
    private PriorityQueue<Integer> large = new PriorityQueue<>();
    private boolean evenTotalEle = true;

    public void insert(int num) {
        // TODO: Implement your insertion operation here
        if (evenTotalEle) {
            large.offer(num);
            small.offer(large.poll());
        } else {
            small.offer(num);
            large.offer(small.poll());
        }
        evenTotalEle = !evenTotalEle ;
    }

    public int getMedian() {
        // TODO: Implement your getMedian operation here
        int medianEle = evenTotalEle  ? large.peek() : small.peek();
        if (evenTotalEle) large.poll();
        else small.poll();

        evenTotalEle  = !evenTotalEle;
        return medianEle;
    }


}