/**
 * ScapeGoat Tree class
 * <p>
 * This class contains some basic code for implementing a ScapeGoat tree. This version does not include any of the
 * functionality for choosing which node to scapegoat. It includes only code for inserting a node, and the code for
 * rebuilding a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     * <p>
     * This class holds the data for a node in a binary tree.
     * <p>
     * Note: we have made things public here to facilitate problem set grading/testing. In general, making everything
     * public like this is a bad idea!
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;
        public float weight;

        TreeNode(int k) {
            key = k;
            this.weight = 1;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree.
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
     * Builds an array of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    private static int index = 0;
    TreeNode[] enumerateNodes(TreeNode node, Child child) {
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
     * Builds a tree from the list of nodes Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    TreeNode buildTree(TreeNode[] nodeList) {
        // TODO: Implement this
        return helperBuildTree(nodeList, 0, nodeList.length - 1);
    }

    public TreeNode helperBuildTree(TreeNode[] arr, int start, int end) {
        if (start > end) {
            return null;
        }
        int midIdx = (start + end) / 2;
        // In-Order traversal result taken from enumerate nodes. The sequence is taken from a tree that fulfills the
        // BST search property. The middle value taken would then be the root, given that it's an inorder traversal
        int Value = arr[midIdx].key;
        TreeNode root = new TreeNode(Value); // Please modify the weight to be thrown in
        // Deploy wishful thinking
        TreeNode leftChildroot = helperBuildTree(arr, start, midIdx - 1);
        TreeNode rightChildroot = helperBuildTree(arr, midIdx + 1, end);
        root.left = leftChildroot;
        root.right = rightChildroot;
        return root;
    }
    /**
     * Determines if a node is balanced. If the node is balanced, this should return true. Otherwise, it should return
     * false. A node is unbalanced if either of its children has weight greater than 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {
        // TODO: Implement this
        if (node == null) {
            return true;
        }

        float twothirdofCurrWeight = 2 * node.weight / 3;

        if (node.left == null && node.right == null) {
            return true;
        } else if (node.left == null && node.right != null) {
            //return node.right.weight <= twothirdofCurrWeight;
            if (node.right.weight > twothirdofCurrWeight) {
                return false;
            } else {
                return true;
            }
        } else if (node.left != null && node.right == null) {
            //return node.left.weight <= twothirdofCurrWeight;
            if (node.left.weight > twothirdofCurrWeight) {
                return false;
            } else {
                return true;
            }
        } else { //else if (node.left != null && node.right != null)
            //return node.left.weight <= twothirdofCurrWeight && node.right.weight <= twothirdofCurrWeight;
            if (node.left.weight > twothirdofCurrWeight || node.right.weight > twothirdofCurrWeight) {
                return false;
            } else {
                return true;
            }
        }
    }


    /**
     * Rebuilds the specified subtree of a node.
     *
     * @param node  the part of the subtree to rebuild
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
            fixWeights(node, child.LEFT);
        } else if (child == Child.RIGHT) {
            node.right = newChild;
            fixWeights(node, child.RIGHT);
        }
    }

    public void fixWeights(TreeNode u, Child child) {
        if (child == child.RIGHT) {
            fixWeightHelper(u.right);
        } else if (child == child.LEFT) {
            fixWeightHelper(u.left);
        }
    }

    public void fixWeightHelper(TreeNode node) {
        TreeNode currNode = node;
        if (node == null) {
            return;
        } else if (node.left == null && node.right == null) {
            currNode.weight += 1;
            return;
        } else if (node.left == null && node.right != null) {
            fixWeightHelper(currNode.right);
            currNode.weight = 1 + currNode.right.weight;
        } else if (node.left != null && node.right == null) {
            fixWeightHelper(currNode.left);
            currNode.weight = 1 + currNode.left.weight;
        } else if (node.left != null && node.right != null) {
            fixWeightHelper(currNode.left);
            fixWeightHelper(currNode.right);
            currNode.weight = 1 + currNode.left.weight + currNode.right.weight;
        }
        return;
    }

    /**
     * Inserts a key into the tree.
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
            //node.weight += 1; //Since we're inserting weight += 1
            if (key <= node.key) { // Traverse through the tree to find where to insert new node
                node.weight += 1;
                if (node.left == null) break;
                node = node.left;
            } else {
                node.weight += 1;
                if (node.right == null) break;
                node = node.right;
            }
        }

        if (key <= node.key) { // node insertion
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }

        node = root;

        while (node.key != key) {
            if (key <= node.key) {
                if (node.left == null) break;
                if (!checkBalance(node.left)) {
                    rebuild(node, Child.LEFT);
                    break;
                } else if (checkBalance(node.left)) {
                    node = node.left;
                }
            } else {
                if (node.right == null) break;
                if (!checkBalance(node.right)) {
                    rebuild(node, Child.RIGHT);
                    break;
                } else if (checkBalance(node.right)) {
                    node = node.right;
                }
            }
        }
    }




        /*TreeNode node = root; // Pointer to keep track of insertion
        TreeNode unbalancedNode = null; // To remember the first highest node that's unbalanced
        int condition = 0;
        int direction = 0; // 0 signals left subtree while 1 signals right subtree

        while (true) {
            node.weight += 1; // Since the node is inserted at leaf, we update the weights as we go down
            if (!checkBalance(node) && condition == 0) { // Checks for the first highest unbalanced node
                unbalancedNode = node; // To remember first highest node that's unbalanced
                condition = 1; // So that thus if condition will not be entered into again
                float twothirdofCurrWeight = 2 * node.weight / 3; // Find out is it the right or left subtree that's unbalanced
                if (node.right.weight > twothirdofCurrWeight) {
                    direction = 1;
                } else if (node.left.weight > twothirdofCurrWeight) {
                    direction = 0;
                }
            }

            if (key <= node.key) { // Traverse through the tree to find where to insert new node
                if (node.left == null) break;
                node = node.left;
            } else {
                if (node.right == null) break;
                node = node.right;
            }
        }

        if (key <= node.key) { // node insertion
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }

        if (unbalancedNode != null) {
            if (direction == 1) {
                rebuild(unbalancedNode, Child.RIGHT);
            } else if (direction == 0) {
                rebuild(unbalancedNode, Child.LEFT);
            }
        } else if (unbalancedNode == null) {
            return;
        }*/



    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 100; i++) {
            tree.insert(i);
        }
        tree.rebuild(tree.root, Child.RIGHT);
    }
}
