import java.util.ArrayList;
import java.util.Arrays;


public class Trie {

    // Wildcards
    final char WILDCARD = '.';
    public TrieNode root;
    public TrieNode[] presentChars = new TrieNode[62];
    private class TrieNode {
        // TODO: Create your TrieNode class here.
        private char character;
        TrieNode[] presentChars = new TrieNode[62];
        boolean endWord;
        public TrieNode(char character) {
            this.character = character;
            this.endWord = false;
            for (int i = 0; i < this.presentChars.length; i++) {
                this.presentChars[i] = null;
            }
        }

        boolean hasChild() {
            TrieNode[] allChild = this.presentChars;
            for (int i = 0; i < 62; i++) {
                if (allChild[i] != null) {
                    return true;
                }
            }
            return false;
        }
    }

    public int getIndex(char c) { // Find index to search or to insert trienode
        char character = c;
        if (Character.isDigit(character)) {
            int asciiValue = (int) character;
            return asciiValue - 48;
        } else if (!Character.isDigit(character)) {
            if (Character.isLowerCase(character)) {
                int asciiValue = (int) character;
                return asciiValue - 97 + 10 + 26;
            } else if (Character.isUpperCase(character)) {
                int asciiValue = (int) character;
                return asciiValue - 65 + 10;
            }

        }
        return -1; // Should never reach here
        /*char[] alphanum = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
                'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
                'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
                'V', 'W', 'X', 'Y', 'Z'};
        for (int i = 0; i < alphanum.length; i++) {
            if (character == alphanum[i]) {
                return i;
            }
        }
        return -1; // Should never reach here*/
    }

    public Trie() {
        // TODO: Initialise a trie class here.
        this.root = new TrieNode(WILDCARD); // Starting node
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        // TODO
        String word = s;
        int len = s.length();
        TrieNode currNode = this.root;
        for (int i = 0; i < len; i++) {
            int index = getIndex(word.charAt(i));
            if (currNode.presentChars[index] == null) {
                TrieNode newNode = new TrieNode(word.charAt(i));
                currNode.presentChars[index] = newNode;
                currNode = newNode;
            } else if (currNode.presentChars[index] != null) {
                currNode = currNode.presentChars[index];
            }
        }
        currNode.endWord = true; // To signify the end of a word at the last node;
    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        // TODO
        String word = s;
        int len = s.length();
        TrieNode currNode = this.root;
        for (int i = 0 ; i < len; i++) {
            int index = getIndex(word.charAt(i));
            if (currNode.presentChars[index] == null) {
                return false;
            } else if (currNode.presentChars[index] != null) {
                currNode = currNode.presentChars[index];
            }
        }
        if (currNode.endWord) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        // TODO
        TrieNode currNode = this.root;
        if (currNode == null) {
            return;
        }

        int len = s.length();
        String word = s;
        StringBuilder sb = new StringBuilder();
        /*if (!containDots(s)) {
            for (int i = 0; i < len; i++) { // Traverse through all the nodes to the last before recursion
                int index = getIndex(word.charAt(i));
                if (currNode.presentChars[index] != null) {
                    System.out.println(1);
                    currNode = currNode.presentChars[index];
                }
            }
            prefixRecurseHelperNoDots(word, currNode, results, sb.append(s), limit); // Fill in temp arraylist with all strings relevant
        } else if (containDots(s)) {
            prefixRecurseHelperWithDots(word, currNode, results, 0, sb, limit); // Fill in temp arraylist with all strings relevant
        }*/
        prefixRecurseHelperWithDots(word, currNode, results, 0, sb, limit); // Fill in temp arraylist with all strings relevant
    }

    void prefixRecurseHelperNoDots(TrieNode node, ArrayList<String> arrlist, StringBuilder sb, int limit) { // Should fill in arrlist

        if (arrlist.size() >= limit) {
            return;
        }

        if (node.endWord) { // Base Case

            arrlist.add(String.valueOf(sb));

        }

        for (int i = 0; i < 62; i++) {
            if (node.presentChars[i] != null) {
                StringBuilder sbnew = new StringBuilder(sb);
                prefixRecurseHelperNoDots(node.presentChars[i], arrlist, sbnew.append(node.presentChars[i].character), limit);
            }
        }
    }

    void prefixRecurseHelperWithDots(String s, TrieNode node, ArrayList<String> arrlist, int counter, StringBuilder sb, int lim) { // Should fill in arrlist
        int len = s.length();
        String word = s;

        if (counter == len) { // Base case
            prefixRecurseHelperNoDots(node, arrlist, sb, lim);
            return;
        }
        //abcd
        //abcd.
        if (word.charAt(counter) == '.') {
            for (int i = 0; i < 62; i++) { // Recurse on each existing trienode in the array
                if (node.presentChars[i] != null) {
                    StringBuilder sbnew = new StringBuilder(sb);
                    prefixRecurseHelperWithDots(s, node.presentChars[i], arrlist, counter + 1, sbnew.append(node.presentChars[i].character), lim);
                }
            }
        } else { // Recurse only on the specific trienode
            int index = getIndex(word.charAt(counter));
            if (node.presentChars[index] != null) {
                //System.out.println(1);
                prefixRecurseHelperWithDots(s, node.presentChars[index], arrlist, counter + 1, sb.append(node.presentChars[index].character), lim);
            }
        }
    }

    boolean containDots(String s) {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) == '.') {
                return true;
            }
        }
        return false;
    }



    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }


    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("a");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("pik");

        String[] result1 = t.prefixSearch("pe", 10);
        String[] result2 = t.prefixSearch("pe.", 10);
        System.out.println(Arrays.toString(result1));
        System.out.println(Arrays.toString(result2));

        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}
