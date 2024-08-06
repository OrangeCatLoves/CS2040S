import java.util.Arrays;
class InversionCounter {

    public static long countSwaps(int[] arr) {
        return merge(arr, 0, arr.length - 1);
    }

    public static long merge(int[] arr, int low, int high) {
        // Merging an unsorted array
        int mid;
        long count = 0;
        if (high > low) {
            mid = (high + low) / 2;
            // Wishful thinking => Assuming that you already sorted the 2 halves and then add the
            // the cost of merging the 2 sorted halves into one
            count = count + merge(arr, low, mid);
            count = count + merge(arr, mid + 1, high);
            count = count + mergeAndCount(arr, low, mid, mid + 1, high);
        }
        return count;
    }

    /**
     * Given an input array so that arr[left1] to arr[right1] is sorted and arr[left2] to arr[right2] is sorted
     * (also left2 = right1 + 1), merges the two so that arr[left1] to arr[right2] is sorted, and returns the
     * minimum amount of adjacent swaps needed to do so.
     */
    public static long mergeAndCount(int[] arr, int left1, int right1, int left2, int right2) {
        if (arr == null) {
            return 0;
        }
        if (arr.length == 1) {
            return 0;
        }

        if (arr.length == 2) {
            if (arr[0] < arr[1]) {
                return 0;
            } else {
                return 1;
            }
        }

        // Create new copies of array
        int[] leftArr = Arrays.copyOfRange(arr, left1, right1 + 1);
        int[] rightArr = Arrays.copyOfRange(arr, left2, right2 + 1);

        int i = 0;
        int j = 0;
        int k = left1;
        long swapCount = 0;

        while (i < leftArr.length && j < rightArr.length) { // Count number of swaps here
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
                swapCount += right1 + 1 - left1 - i;
                // You only need to care about the number of position
                // that the elements in the second half of the array
                // by how much that they have to shift, which is
                // right1 - left + 1
            }
        }

        while (j < rightArr.length) { // When all elements in left arr is copied over
            arr[k++] = rightArr[j++];
        }

        while (i < leftArr.length) { // When all elements in the right arr is copied over
            arr[k++] = leftArr[i++];
        }

        return swapCount;
    }
    /*public static long countSwaps(int[] arr) {

        int numOfSwaps = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    numOfSwaps++;
                }
            }
        }
        return numOfSwaps;
    }

    /**
     * Given an input array so that arr[left1] to arr[right1] is sorted and arr[left2] to arr[right2] is sorted
     * (also left2 = right1 + 1), merges the two so that arr[left1] to arr[right2] is sorted, and returns the
     * minimum amount of adjacent swaps needed to do so.

    public static long mergeAndCount(int[] arr, int left1, int right1, int left2, int right2) {
        int numOfAdjSwaps = 0; // Try bubblesort ?
        int totalElements = right2 - left1 + 1;
        for (int i = 0; i < totalElements; i++) {
            for (int j = 0; j < totalElements - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    numOfAdjSwaps++;
                }
            }
        }
        return numOfAdjSwaps;
    }*/

}
