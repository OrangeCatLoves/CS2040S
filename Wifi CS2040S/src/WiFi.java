import java.util.Arrays;

class WiFi {

    /**
     * Implement your solution here
     */
    public static double computeDistance(int[] houses, int numOfAccessPoints) {
        Arrays.sort(houses);
        int start = 0;
        int end = houses[houses.length - 1] - houses[0];
        int[] possibleWifiRange = new int[houses[houses.length - 1] - houses[0]];
        for (int i = 0; i < possibleWifiRange.length; i++) {
            possibleWifiRange[i] = i;
        }
        int mid = 0;
        while (start < end) {
            mid = (start + end) / 2;
            if (coverable(houses, numOfAccessPoints, mid)) {
                end = mid;
            } else {
                start = mid + 1;
            }
        }
        return end;
    }

    /**
     * Implement your solution here
     */
    public static boolean coverable(int[] houses, int numOfAccessPoints, double distance) {
        Arrays.sort(houses);
        int numOfWifi = numOfAccessPoints - 1;
        int Reachable = 0;
        if (houses.length == 1 && numOfAccessPoints == 1) { // Special 1 house and 1 wifi case
            return true;
        } else if (houses == null) { // Filter null houses array
            return false;
        } else if (houses.length == 0) { // Filter empty array
            return false;
        } else if (numOfAccessPoints <= 0) { // Checks for invalid input of numOfAccessPoints
            return false;
        }

        for (int i = 0; i < houses.length - 1; i++) {
            if (houses[i + 1] - houses[i] + Reachable <= distance) {
                Reachable = Reachable + (houses[i + 1] - houses[i]);
            } else if (houses[i + 1] - houses[i] + Reachable <= distance * 2) {
                Reachable = Reachable + (houses[i + 1] - houses[i]);
            } else {
                Reachable = 0;
                numOfWifi--;
            }
        }
        if (numOfWifi < 0) {
            return false;
        } else {
            return true;
        }

    }
}
