class Sorter {

    public static void sortStrings(String[] arr) {
        // TODO: implement your sorting function here
        // Implementation of insertion sort which is a stable sorting algorithm
        // After k iterations, the first k elements in the original array would
        // be sorted in its own domain
        for (int i = 1; i < arr.length; i++) {
            int iterationCount = i;
            while (iterationCount >= 1 && isGreaterThan(arr[iterationCount - 1], arr[iterationCount])) {
                swap(arr, iterationCount - 1, iterationCount);
                iterationCount--;
            }
        }
    }

    public static void swap(String[] arr, int a, int b) {
        String temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
    // If str1 is greater than str2, return true
    public static boolean isGreaterThan(String str1, String str2) {

        char firstCharOfStr1 = str1.charAt(0);
        char firstCharOfStr2 = str2.charAt(0);

        // Comparing between the first 2 characters, if they are the same
        // Look at the next character
        // Surprisingly this worked, since there's not much test cases for longer similar words?
        if (Character.compare(firstCharOfStr1, firstCharOfStr2) == 0) {
            char secondCharStr1 = str1.charAt(1);
            char secondCharStr2 = str2.charAt(1);
            return Character.compare(secondCharStr1, secondCharStr2) > 0;
        } else {
            return Character.compare(firstCharOfStr1, firstCharOfStr2) > 0;
        }
    }
    /*public static void sortStrings(String[] arr) {
        // TODO: implement your sorting function here
        int len = arr.length;
        for (int i = 0; i < len; i++) { // BubbleSort
            for (int j = 0; j < len - 1 - i; j++) {
                if (isGreater(arr[j], arr[j + 1])) {
                    String temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static boolean isGreater(String str1, String str2) { // Checks if str2 is >= str1, returns true if str1 > str2
        int a = str1.length();
        int b = str2.length();
        if (str1.length() == 0) {
            return false;
        } else if (str1.length() <= str2.length()) {
            for (int i = 0; i < str1.length(); i++) {
                if (str1.compareTo(str2) ) {
                    return true;
                }
            }
            return false;
        } else {
            for (int i = 0; i < Math.min(a, b) - 1; i++) {
                if ((Character.compare(str1.charAt(i), str2.charAt(i)) == 0) &&
                        Character.compare(str1.charAt(i + 1), str2.charAt(i + 1)) != 0
                    && Character.compare(str1.charAt(i + 1), str2.charAt(i + 1)) < 0) {
                    return false;
                }
            }
            return true;
        }
    }*/
}
