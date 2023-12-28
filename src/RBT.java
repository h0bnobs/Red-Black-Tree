/**
 * RBT
 * Red-Black Tree Insert
 *
 * @author mb2153
 */

import java.util.*;
public class RBT {
    private Node root;

    public RBT() {}

    public boolean isRed(Node x) {
        if (x == null) return false;
        return x.getColor() == Node.Color.RED;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean contains(int x) {
        return nodeContainsData(root, x);
    }

    private boolean nodeContainsData(Node r, int x) {
        while (r != null) {
            if (r.getData() - x < 0) {
                r = r.getLeft();
            } else if (r.getData() - x > 0) {
                r = r.getRight();
            } else {
                return true;
            }
        }
        return false;
    }

    public List<Integer> serializeTree() {
        return serializeTree(root);
    }

    private List<Integer> serializeTree(Node r) {
        if (r == null) return new LinkedList<>();
        int data = r.getData();
        List<Integer> left = serializeTree(r.getLeft());
        List<Integer> right = serializeTree(r.getRight());
        left.add(data);
        left.addAll(right);
        return left;
    }

    public int maxHeight() {
        return maxHeight(root);
    }

    private int maxHeight(Node r) {
        if (r==null) return 0;
        return 1 + Math.max(maxHeight(r.getLeft()), maxHeight(r.getRight()));
    }

    // ************************************************************************
    // * INSERT INTO RED-BLACK TREE
    // ************************************************************************

    public void insert(int x) {
        root = nodeInsertData(root, x);
        root.setColor(Node.Color.BLACK);
    }

    // RBT NOTES:
    // RBTs are a type of self-balancing BSTs that have certain properties:
    // 1: a node is either red or black.
    // 2: the ROOT and LEAVES (null) on the tree are black.
    // 3: if a node is red then the children are black.
    // 4: all paths from any node to its null descendants contain the same number of black nodes.

    // INSERTION NOTES:
    // 1: insert the node and colour it red.
    // 2: then recolour and rotate the nodes to fix any violations.
    // the node is coloured red because it breaks properties that are the easiest to fix (2 and 3)
    // for step 2 there will be 4 main scenarios that will need addressing/fixing.
    // scenario 1: the node aka Z = root.
    // scenario 2: Z.uncle = red.
    // scenario 3: Z.uncle = black (triangle).
    // scenario 4: Z.uncle = black (line).

    // TRIANGLE NOTES:
    // a triangle is formed when Z and Z.parent are opposite children, meaning if Z is a left child and Z.parent is a right child,
    // a triangle will be formed on z, z.parent and z.grandparent. You can also have a triangle case that is symmetrical to this, where
    // Z is a right child and Z.parent is a left child of Z.grandparent.

    // LINE NOTES:
    // a line is formed when Z and Z.parent are the same side children, meaning if Z is a right child, Z.parent is also a right child of Z.grandparent.
    // the symmetrical case to this is when Z is a left child, and Z.parent is a left child of Z.grandparent.

    // scenario 1 fix: colour Z black.
    // scenario 2 fix: recolour Z's parent, uncle and grandparent.
    // scenario 3 fix: rotate Z.parent in the opposite direction of Z (if Z is a left child, rotateRight(Z.parent)), so that Z takes the place of its parent.
    // scenario 4 fix: rotate Z.grandparent in the opposite direction of Z (if Z is a right child, rotateLeft(Z.grandparent)), so that Z.parent takes the place
    // of Z.grandparent. Also recolour the original Z.parent and grandparent after the rotation.

    //            Node nodeInsertData(Node r, int x) {
//            * ordinary binary tree insertion with recursion:
//            if r is null then make new root node with x
//            if x is less than r's data recur on left
//            if x is greater than r's data recur on right
//            * fix up the tree by looking at children/grandchildren:
//            if r.right is red and r.left is black then r=rotateLeft(r)
//            if r.left and r.left.left are red then r=rotateRight(r)
//            if r.left and r.right are red then flipColors(r)
//            return r;
//        }
//         Because the recursive function will visit the nodes on a path from the root to
//         the newly inserted value (as discussed in the lecture), the "fix up" conditions
//         will work on all the nodes in that path from the bottom up, so there is no need for a loop.
    private Node nodeInsertData(Node r, int x) {
        //case 0
        if (r == null) {
            return new Node(x, Node.Color.RED);
        }
        if (r == root) {
            r.setColor(Node.Color.BLACK);
        }

        //insert into tree
        if (x < r.getData()) {
            r.setLeft(nodeInsertData(r.getLeft(), x));
        } else if (x > r.getData()) {
            r.setRight(nodeInsertData(r.getRight(), x));
        } else {
            return r;
        }

        //case 1
        //if r.right is red and r.left is black then r=rotateLeft(r)
        if (isRed(r.getRight()) && !isRed(r.getLeft())) {
            //System.out.println("case 1 hit");
            r.getRight().setColor(r.getColor());
            r.setColor(Node.Color.RED);
            r = rotateLeft(r);
        }
        // if r.left is red and r.right is black then r=rotateRight(r)
        if (isRed(r.getLeft()) && !isRed(r.getRight())) {
            //System.out.println("case 1 hit");
            r.getLeft().setColor(r.getColor());
            r.setColor(Node.Color.RED);
            r = rotateRight(r);
        }
        //case 2
        //if r.left and r.left.left are red then r=rotateRight(r)
        if (isRed(r.getLeft()) && isRed(r.getLeft().getLeft())) {
            r.getLeft().setColor(r.getColor());
            r.setColor(Node.Color.RED);
            r = rotateRight(r);
        }
        //if r.right and r.right.right are red then r=rotateLeft(r)
        if (isRed(r.getRight()) && isRed(r.getRight().getRight())) {
            r.getRight().setColor(r.getColor());
            r.setColor(Node.Color.RED);
            r = rotateLeft(r);
        }
        //case 3
        //if r.left and r.right are red then flipColors(r)
        if (isRed(r.getLeft()) && isRed(r.getRight())) {
            flipColors(r);
        }
        return r;
    }

    // ROTATION NOTES:
    // rotations rearrange subtrees and alter the structure of the tree.
    // the goal of any rotation is to decrease the height of the tree.
    // this is done by moving larger subtrees UP the tree and smaller subtrees DOWN the tree.
    // rotations do not affect the order of elements. smaller elements left larger right.
    // think of it as changing pointers in the tree.

    private Node rotateRight(Node h) {
        Node temp = h.getLeft();
        h.setLeft(temp.getRight());
        temp.setRight(h);
        if (h == root) {
            root = temp;
        }
        return temp;
    }

    private Node rotateLeft(Node h) {
        Node temp = h.getRight();
        h.setRight(temp.getLeft());
        temp.setLeft(h);
        if (h == root) {
            root = temp;
        }
        return temp;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        //store the current colours
        Node.Color currentColour = h.getColor();
        Node.Color leftColour = h.getLeft().getColor();
        Node.Color rightColour = h.getRight().getColor();

        //flip those current colours
        h.setColor(Node.flipColor(currentColour));
        if (h.getRight() != null) {
            h.getRight().setColor(Node.flipColor(rightColour));
        }
        if (h.getLeft() != null) {
            h.getLeft().setColor(Node.flipColor(leftColour));
        }
    }
}
