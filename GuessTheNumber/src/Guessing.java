public class Guessing {

    // Your local variables here
    private int low = 0;
    private int high = 1000;

    /**
     * Implement how your algorithm should make a guess here
     */
    public int guess() {
        int mid = low + (high - low) / 2;
        return mid;
    }

    /**
     * Implement how your algorithm should update its guess here
     */
    public void update(int answer) {
        if (answer == 1) {
            high = (low + high) / 2;
        } else if (answer == -1) {
            low = 1 + (low + high) / 2;
        }
    }
}
