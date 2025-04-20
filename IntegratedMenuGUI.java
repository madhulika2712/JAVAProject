import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class IntegratedMenuGUI extends JFrame {
    private static final int SIZE = 512;
    private JTextArea textArea;
    private int[] UnSortedArrayData;
    private int[] sortedArrayData;
    private int[] storedData;
    private LinkedList<Integer> listUnsortedLL;


    public IntegratedMenuGUI() {
        setTitle("CS401 Final Project - Mandakini");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();

        // First Menu Item - Data Preparation
        // (1) Generate Random Data
        // (2) Load Data from File
        JMenu dataPreparationMenu = new JMenu("Data Preparation");
        JMenuItem generateDataItem = new JMenuItem("Generate Random Data");
        JMenuItem loadDataItem = new JMenuItem("Load Data from File");
        dataPreparationMenu.add(generateDataItem);
        dataPreparationMenu.add(loadDataItem);

        // Second Menu Item - Create Unsorted List
        // (1) Store Data in Array
        // (2) Store Data in Linked List
        JMenu createUnsortedListMenu = new JMenu("Create Unsorted List");
        JMenuItem arrayItem = new JMenuItem("Store Data in Array");
        JMenuItem linkedListItem = new JMenuItem("Store Data in Linked List");
        createUnsortedListMenu.add(arrayItem);
        createUnsortedListMenu.add(linkedListItem);

        // Third Menu Item - Create Sorted List
        // (1) Sort Array using Merge Sort
        JMenu createSortedListMenu = new JMenu("Create Sorted List");
        JMenuItem sortArrayItem = new JMenuItem("Sort Array using Merge Sort");
        createSortedListMenu.add(sortArrayItem);

        // Fourth Menu Item - Binary Search Tree
        // (1) Create and Display BST
        JMenu binarySearchTreeMenu = new JMenu("Binary Search Tree");
        JMenuItem createBSTItem = new JMenuItem("Create and Display BST");
        binarySearchTreeMenu.add(createBSTItem);

        // Fifth Menu Item - Search Implementation
        // (1) Search Unsorted Array
        // (2) Search Unsorted Linked List
        // (3) Search Sorted Array
        // (4) Search Sorted Linked List
        // (5) Search BST
        JMenu searchImplementation = new JMenu("Search Implementation");
        JMenuItem searchUnsortedArray = new JMenuItem("Search Unsorted Array");
        JMenuItem searchUnsortedLinkedList = new JMenuItem("Search Unsorted Linked List");
        JMenuItem searchSortedArray = new JMenuItem("Search Sorted Array");
        JMenuItem searchSortedLinkedList = new JMenuItem("Search Sorted Linked List");
        JMenuItem searchBST = new JMenuItem("Search BST");
        searchImplementation.add(searchUnsortedArray);
        searchImplementation.add(searchUnsortedLinkedList);
        searchImplementation.add(searchSortedArray);
        searchImplementation.add(searchSortedLinkedList);
        searchImplementation.add(searchBST);

        menuBar.add(dataPreparationMenu);
        menuBar.add(createUnsortedListMenu);
        menuBar.add(createSortedListMenu);
        menuBar.add(binarySearchTreeMenu);
        menuBar.add(searchImplementation);

        setJMenuBar(menuBar);

        // Generate Random data, store in array and display on screen
        generateDataItem.addActionListener(e -> {
            UnSortedArrayData = generateRandomData();
            displayData(UnSortedArrayData);
        });

        // Load data from file, store in Array, then display
        loadDataItem.addActionListener(e -> {
            UnSortedArrayData = loadDataFromFile("src/main/java/data.txt");
            displayData(UnSortedArrayData);
        });

        // Store data in Array, it was done in previous step, just displaying here again
        arrayItem.addActionListener(e -> displayArray(UnSortedArrayData));

        // Store data in Linked List, then display it on the screen
        linkedListItem.addActionListener(e -> {
            listUnsortedLL = createLinkedList(UnSortedArrayData);
            displayLinkedList(listUnsortedLL);
        });

        // take unsorted array data and sorts them using merge sort then display the same on screen
        sortArrayItem.addActionListener(e -> {
            //int[] sortedArrayData = Arrays.copyOf(UnSortedArrayData, UnSortedArrayData.length);
            sortedArrayData = Arrays.copyOf(UnSortedArrayData, UnSortedArrayData.length);
            mergeSort(sortedArrayData, 0, sortedArrayData.length - 1);
            displayArray(sortedArrayData);
        });

        // working on it now MMM
        createBSTItem.addActionListener(e -> {
            BST bst = new BST();
            for (int num : UnSortedArrayData) {
                bst.insert(num);
            }
            displayBST(bst);
        });
        /* new code */
                searchUnsortedArray.addActionListener( Act -> {
                    int target = getSearchTarget();
                    SearchResult result = linearSearch(UnSortedArrayData, target);

                    textArea.setText("Search in Unsorted Array (Linear Search)\n"
                            + "Value " + target + (result.found ? " found." : " not found.") + "\n"
                            + "Time: " + result.timeNano + " ns\n"
                            + "Theoretical Complexity: O(n)");
                });

        searchUnsortedLinkedList.addActionListener(e -> {
            int target = getSearchTarget();
            LinkedList<Integer> list = new LinkedList<>();
            for (int num : UnSortedArrayData) list.add(num);
            long start = System.nanoTime();
            boolean found = list.contains(target);
            long end = System.nanoTime();
            long duration = end - start;
            //textArea.setText("Search in Unsorted Linked List:\nValue " + target + (found ? " found." : " not found."));
            textArea.setText("Search in Unsorted Linked List:\n"
                    + "Value " + target + (found ? " found." : " not found.") + "\n"
                    + "Time: " + duration + " ns\n"
                    + "Theoretical Complexity: O(n)");
        });

        searchSortedArray.addActionListener(e -> {
           int[] sorted = Arrays.copyOf(sortedArrayData, sortedArrayData.length);
           mergeSort(sorted, 0, sorted.length - 1);
            int target = getSearchTarget();
            SearchResult result  = binarySearch(sorted, target);
            textArea.setText("Search in Sorted Array (Binary Search)\n"
                    + "Value " + target + (result.found ? " found." : " not found.") + "\n"
                    + "Time: " + result.timeNano + " ns\n"
                    + "Theoretical Complexity: O(log n)");
        });
        searchSortedLinkedList.addActionListener(e -> {
            int target = getSearchTarget();
            LinkedList<Integer> list = new LinkedList<>();
            for (int num : sortedArrayData) list.add(num);
            list.sort(Comparator.naturalOrder());
            boolean found = false;
            long start = System.nanoTime();
            for (int num : list) {
                if (num > target) break;
                if (num == target) {
                    found = true;
                    break;
                }
            }
            long end = System.nanoTime();
            SearchResult result = new SearchResult(found, end - start);
            //textArea.setText("Search in Sorted Linked List:\nValue " + target + (found ? " found." : " not found."));
            textArea.setText("Search in Sorted Linked List (Linear Search)\n"
                    + "Value " + target + (result.found ? " found." : " not found.") + "\n"
                    + "Time: " + result.timeNano + " ns\n"
                    + "Theoretical Complexity: O(n)");
        });


        searchBST.addActionListener(e -> {
            int target = getSearchTarget();
            BST bst = new BST();
            /*
            for (int num : UnSortedArrayData) bst.insert(num);
            boolean found = bst.search(target);
            textArea.setText("Search in BST:\nValue " + target + (found ? " found." : " not found."));*/
            SearchResult result = bstSearchWithTiming(bst, target);
            textArea.setText("Search in BST\n"
                    + "Value " + target + (result.found ? " found." : " not found.") + "\n"
                    + "Time: " + result.timeNano + " ns\n"
                    + "Theoretical Complexity: O(log n) average, O(n) worst");
        });

    }

    private int[] generateRandomData() {
        Random rand = new Random();
        int[] data = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            data[i] = rand.nextInt(1000); // Random numbers between 0 and 999
        }
        return data;
    }

    private int[] loadDataFromFile(String filename) {
        int[] data = new int[SIZE];
        try {
            Scanner fileScanner = new Scanner(new File(filename));
            for (int i = 0; i < SIZE; i++) {
                if (fileScanner.hasNextInt()) {
                    data[i] = fileScanner.nextInt();
                }
            }
        } catch (FileNotFoundException e) {
            textArea.setText("File not found");
        }
        return data;
    }

    private void displayData(int[] data) {
        textArea.setText("");
        for (int i = 0; i < data.length; i++) {
            textArea.append(data[i] + " ");
            if ((i + 1) % 20 == 0) {
                textArea.append("\n");
            }
        }
    }

    private void displayArray(int[] data) {
        textArea.setText("");
        for (int i = 0; i < data.length; i++) {
            textArea.append(data[i] + " ");
            if ((i + 1) % 20 == 0) {
                textArea.append("\n");
            }
        }
    }

    private LinkedList<Integer> createLinkedList(int[] data) {
        LinkedList<Integer> list = new LinkedList<>();
        for (int num : data) {
            list.add(num);
        }
        return list;
    }

    private void displayLinkedList(LinkedList<Integer> list) {
        textArea.setText("");
        int count = 0;
        for (int num : list) {
            textArea.append(num + " ");
            count++;
            if (count % 20 == 0) {
                textArea.append("\n");
            }
        }
    }


//    private void displayLinkedList(int[] data) {
//        LinkedList<Integer> listUnsortedLL = new LinkedList<>();
//        for (int num : data) {
//            listUnsortedLL.add(num);
//        }
//        textArea.setText("");
//        int count = 0;
//        for (int num : listUnsortedLL) {
//            textArea.append(num + " ");
//            count++;
//            if (count % 20 == 0) {
//                textArea.append("\n");
//            }
//        }
//    }

    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    private void merge(int[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
        }
        for (int i = 0; i < n2; i++) {
            rightArray[i] = array[mid + 1 + i];
        }

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
/* new code */
    private int getSearchTarget() {
        String input = JOptionPane.showInputDialog(this, "Enter value to search:");
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private SearchResult linearSearch(int[] data, int target) {
        long start = System.nanoTime();
        boolean found = false;
        for (int val : data) {
            if (val == target)
                found = true;
            break;
        }
        long end = System.nanoTime();
        return new SearchResult(found, end - start);
    }
    private SearchResult binarySearch(int[] data, int target) {
        long start = System.nanoTime();
        int left = 0, right = data.length - 1;
        boolean found = false;
       // int left = 0, right = data.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (data[mid] == target)
            {
                found = true;
                break;
            }
            if (data[mid] < target) left = mid + 1;
            else right = mid - 1;
        }
        long end = System.nanoTime();
        return new SearchResult(found, end - start);
    }
    private SearchResult bstSearchWithTiming(BST bst, int target) {
        long start = System.nanoTime();
        boolean found = bst.search(target);
        long end = System.nanoTime();
        return new SearchResult(found, end - start);
    }
    private void displayBST(BST bst) {
        textArea.setText("");
        displayBSTRec(bst.root, "", true);
    }

    private void displayBSTRec(BST.Node node, String prefix, boolean isTail) {
        if (node != null) {
            textArea.append(prefix + (isTail ? "└── " : "├── ") + node.data + "\n");
            displayBSTRec(node.left, prefix + (isTail ? "    " : "│   "), false);
            displayBSTRec(node.right, prefix + (isTail ? "    " : "│   "), true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IntegratedMenuGUI frame = new IntegratedMenuGUI();

            frame.setVisible(true);
        });
    }
}
class SearchResult {
    boolean found;
    long timeNano;

    SearchResult(boolean found, long timeNano) {
        this.found = found;
        this.timeNano = timeNano;
    }
}
class BST {
    class Node {
        int data;
        Node left, right;

        Node(int data) {
            this.data = data;
            left = right = null;
        }

    }

    Node root;

    BST() {
        root = null;
    }

    void insert(int data) {
        root = insertRec(root, data);
    }

    Node insertRec(Node root, int data) {
        if (root == null) {
            root = new Node(data);
            return root;
        }
        if (data < root.data) {
            root.left = insertRec(root.left, data);
        } else if (data > root.data) {
            root.right = insertRec(root.right, data);
        }
        return root;
    }
    boolean search(int target) {
        return searchRec(root, target);
    }

    private boolean searchRec(Node node, int target) {
        if (node == null) return false;
        if (node.data == target) return true;
        if (target < node.data) return searchRec(node.left, target);
        else return searchRec(node.right, target);
    }


}

