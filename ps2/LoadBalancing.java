/**
 * Contains static routines for solving the problem of balancing m jobs on p processors
 * with the constraint that each processor can only perform consecutive jobs.
 */
public class LoadBalancing {

    /**
     * Checks if it is possible to assign the specified jobs to the specified number of processors such that no
     * processor's load is higher than the specified query load.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param queryLoad the maximum load allowed for any processor
     * @param p the number of processors
     * @return true iff it is possible to assign the jobs to p processors so that no processor has more than queryLoad load.
     */
    public static boolean isFeasibleLoad(int[] jobSizes, int queryLoad, int p) {
        // TODO: Implement this

        int load = 0;
        int total = 0;
        int totalLoadCapacity = p - 1;
        for (int i = 0; i < jobSizes.length; i++) { // Calculates total load in jobSizes
            total = total + jobSizes[i];
        }
        if (queryLoad * p < total) { // Filter out jobSizes that exceed total load
            return false;
        } else if (jobSizes.length == 0) { // Filter out empty array
            return false;
        } else if (jobSizes == null) { // Null array filter
            return false;
        } else if (jobSizes.length == 1) { // Special 1 jobSize case
            if (jobSizes[0] > queryLoad) {
                return false;
            }
        }

        for (int i = 0; i < jobSizes.length; i++) {
            load = load + jobSizes[i];
            if (jobSizes[i] > queryLoad) { // Checks if any individual jobs exceed the queryLoad
                return false;
            } else if (load > queryLoad) {
                load = load - jobSizes[i];
                totalLoadCapacity--;
                load = 0;
                i--;
            }
        }

        if (totalLoadCapacity < 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns the minimum achievable load given the specified jobs and number of processors.
     *
     * @param jobSizes the sizes of the jobs to be performed
     * @param p the number of processors
     * @return the maximum load for a job assignment that minimizes the maximum load
     */
    public static int findLoad(int[] jobSizes, int p) {
        // TODO: Implement this
        int smallest = -Integer.MAX_VALUE;
        int totalLoad = 0;
        if (p <= 0) {
            return -1;
        } else if (jobSizes == null) {
            return -1;
        } else if (jobSizes.length <= 0) {
            return -1;
        } else if (jobSizes.length <= p) { // Return value of largest jobSize O(n)
            for (int i = 0; i < jobSizes.length; i++) {
                if (jobSizes[i] > smallest) {
                    smallest = jobSizes[i];
                }
            }
            return smallest;
        } else {
            for (int i = 0; i < jobSizes.length; i++) { // Calculate largest job in jobSizes
                if (jobSizes[i] > smallest) {
                    smallest = jobSizes[i];
                }
            }
            for (int j = 0; j < jobSizes.length; j++) { // Calculate totalLoad
                totalLoad = totalLoad + jobSizes[j];
            }
        }

        System.out.println(smallest);
        System.out.println(totalLoad);

        int begin = 0; // Starting index
        int end = totalLoad - smallest; // Ending index
        int[] rangeOfPossibleLoads = new int[totalLoad - smallest + 1]; // Initialising array with size rangeOfLoads
        for (int j = 0; j < rangeOfPossibleLoads.length; j++) { // Populating the rangeOfPossibleLoads array size O(n*m)
            rangeOfPossibleLoads[j] = smallest + j;
        }
        int mid = 0;
        while(begin < end) {
            mid = (end + begin) / 2;
            if (isFeasibleLoad(jobSizes, rangeOfPossibleLoads[mid], p)) { // load may not be the smallest
                //mid = (end + begin) / 2;
                end = mid;
            } else {
                //mid = (end + begin) / 2;
                begin = mid + 1; // Current load is too big, hence more load needs to be added, min largest load increases
            }
        }
        return rangeOfPossibleLoads[end];
    }

    // These are some arbitrary testcases.
    public static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, 100, 80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83},
            {7}
    };

    /**
     * Some simple tests for the findLoad routine.
     */
    public static void main(String[] args) {
        for (int p = 1; p < 30; p++) {
            System.out.println("Processors: " + p);
            for (int[] testCase : testCases) {
                System.out.println(findLoad(testCase, p));
            }
        }
    }
}
