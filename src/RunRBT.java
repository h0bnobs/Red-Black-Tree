import java.util.*;

public class RunRBT {
    public static void main(String[] args) {
        //custom test 1
        LinkedList<Integer> l4 = new LinkedList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int randomNumber = random.nextInt(101);
            if (!l4.contains(randomNumber)) {
                l4.add(randomNumber);
            }
        }
        System.out.println("first list: " + l4);
        buildAndPrintTree(l4);

        LinkedList<Integer> l5 = new LinkedList<>();
        l5.add(10);
        l5.add(12);
        l5.add(11);
        buildAndPrintTree(l5);

    }

    public static void buildAndPrintTree(List<Integer> inputs) {
        RBT tree = new RBT();
        for (int i : inputs) {
            tree.insert(i);
        }
        List<Integer> output = tree.serializeTree();
        System.out.println(output);
        System.out.println("Max tree height: " + tree.maxHeight());
        System.out.println("Maintained all values: " + (output.containsAll(inputs) && inputs.containsAll(output)));
        List<Integer> tmp = new LinkedList<>(output);
        Collections.sort(tmp);
        System.out.println("Output is sorted: " + (tmp.equals(output)));
    }


}
