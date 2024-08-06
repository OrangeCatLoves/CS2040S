import java.util.Arrays;

/**
 * ScapeGoat Tree class
 *
 * This class contains some of the basic code for implementing a ScapeGoat tree.
 * This version does not include any of the functionality for choosing which node
 * to scapegoat.  It includes only code for inserting a node, and the code for rebuilding
 * a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     *
     * This class holds the data for a node in a binary tree.
     *
     * Note: we have made things public here to facilitate problem set grading/testing.
     * In general, making everything public like this is a bad idea!
     *
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;

        TreeNode(int k) {
            key = k;
        }

        /*@Override
        public String toString() {
            return Integer.toString(this.key);
        }*/
    }

    // Root of the binary tree
    public TreeNode root = null;



    /**
     * Counts the number of nodes in the specified subtree
     *
     * @param node  the parent node, not to be counted
     * @param child the specified subtree
     * @return number of nodes
     */

    public int countNodes(TreeNode node, Child child) {
        // TODO: Implement this
        if (node.left == null && node.right == null) {
            return 0;
        } else if (child == Child.RIGHT) {
            if (node.right == null) {
                    return 0;
            } else if (node.right != null) {
                    return countNodes(node.right, Child.RIGHT) + countNodes(node.right, Child.LEFT) + 1;
            }
        } else if (child == Child.LEFT) {
            if (node.left == null) {
                    return 0;
            } else if (node.left != null) {
                    return countNodes(node.left, Child.RIGHT) + countNodes(node.left, Child.LEFT) + 1;
            }
        }
        return 0;
    }

    /**
     * Builds an array of nodes in the specified subtree
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    private static int index = 0;
    public TreeNode[] enumerateNodes(TreeNode node, Child child) {
        // TODO: Implement this
        // Create array only once
        int size = countNodes(node, child);
        TreeNode[] inOrderArray = new TreeNode[size];
        if (child == Child.RIGHT) {
            inOrderHelper(node.right, inOrderArray); // Assign values in InOrder sequence
        } else if (child == Child.LEFT) {
            inOrderHelper(node.left, inOrderArray); // Assign values in InOrder sequence
        }
        // System.out.println(Arrays.toString(inOrderArray));
        index = 0; // Reset to 0 because index is static or else it'll fail for subsequent test cases after the first
        return inOrderArray;
    }

    public void inOrderHelper(TreeNode node, TreeNode[] arr) {
        if (node == null) {
            return;
        } else {
            inOrderHelper(node.left, arr);
            arr[index] = node;
            index++;
            inOrderHelper(node.right, arr);
        }
    }

    /**
     * Builds a tree from the list of nodes
     * Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */

    // Helper function to return the root

    public TreeNode buildTree(TreeNode[] nodeList) {
        // TODO: Implement this
        return helperBuildTree(nodeList, 0, nodeList.length - 1);
        //return helperBuildTree(nodeList, 0, nodeList.length - 1);

        // Failed 1st attempt that only builds a balanced tree where each sibling node would differ by 1 node at most,
        // but did not take into account the BST search property
        /*int len = nodeList.length;
        TreeNode firstNode = nodeList[0]; // The root
        for (int i = 0; i < len; i++) {
            nodeList[i].left = null;
            nodeList[i].right = null;
        }
        for (int i = 0; i < len; i++) {
            FindCorrectInsert(firstNode, nodeList[i]);
        }
        return firstNode;*/
    }

    public TreeNode helperBuildTree(TreeNode[] arr, int start, int end) {
        if (start > end) {
            return null;
        }
        int midIdx = (start + end) / 2;
        // In-Order traversal result taken from enumerate nodes. The sequence is taken from a tree that fulfills the
        // BST search property. The middle value taken would then be the root, given that it's an inorder traversal
        int Value = arr[midIdx].key;
        TreeNode root = new TreeNode(Value);
        // Deploy wishful thinking
        TreeNode leftChildroot = helperBuildTree(arr, start, midIdx - 1);
        TreeNode rightChildroot = helperBuildTree(arr, midIdx + 1, end);
        root.left = leftChildroot;
        root.right = rightChildroot;
        return root;
    }

    /* TreeNode builder(TreeNode[] arr, int lower, int higher) {
        if (lower > higher) {
            return null;
        }
        int mid = (lower + higher) / 2;

        int midNum = arr[mid].key;
        TreeNode root = new TreeNode(midNum);
        TreeNode left = builder(arr, lower, mid - 1);
        TreeNode right = builder(arr, mid + 1, higher);

        root.left = left;
        root.right = right;

        return root;
    }*/

    // First attempt's helper function
    /*public void FindCorrectInsert(TreeNode node, TreeNode nodetoInsert) { // NODE STARTS AT THE ROOT
        if (node.left == null) {
            node.left = nodetoInsert;
        } else if (node.right == null) {
            node.right = nodetoInsert;
        } else if (((countNodes(node.left, Child.LEFT) + countNodes(node.left, Child.RIGHT) + 1) ==
                (countNodes(node.right, Child.LEFT) + countNodes(node.right, Child.RIGHT) + 1)) && node.left != null) {
            FindCorrectInsert(node.left, nodetoInsert);
        } else if (((countNodes(node.left, Child.LEFT) + countNodes(node.left, Child.RIGHT) + 1) >
                (countNodes(node.right, Child.LEFT) + countNodes(node.right, Child.RIGHT) + 1)) && node.right != null) {
            FindCorrectInsert(node.right, nodetoInsert);
        }
    }*/

    /**
    * Rebuilds the specified subtree of a node
    * 
    * @param node the part of the subtree to rebuild
    * @param child specifies which child is the root of the subtree to rebuild
    */
    public void rebuild(TreeNode node, Child child) {
        // Error checking: cannot rebuild null tree
        if (node == null) return;
        // First, retrieve a list of all the nodes of the subtree rooted at child
        TreeNode[] nodeList = enumerateNodes(node, child);
        // Then, build a new subtree from that list
        TreeNode newChild = buildTree(nodeList);
        // Finally, replace the specified child with the new subtree
        if (child == Child.LEFT) {
            node.left = newChild;
        } else if (child == Child.RIGHT) {
            node.right = newChild;
        }
    }

    /**
    * Inserts a key into the tree
    *
    * @param key the key to insert
    */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        TreeNode node = root;

        while (true) {
            if (key <= node.key) {
                if (node.left == null) break;
                node = node.left;
            } else {
                if (node.right == null) break;
                node = node.right;
            }
        }

        if (key <= node.key) {
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }
    }


    // Simple main function for debugging purposes
public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 100; i++) {
            tree.insert(i);
        }
        tree.rebuild(tree.root, Child.RIGHT);
        // tree.enumerateNodes(tree.root, Child.RIGHT);
    }
}
