package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        int sizeOfBoard = (int) Math.sqrt(n + 1);
//        int n = 15;
        int[][] initialBoard = new int[sizeOfBoard][sizeOfBoard];
//
//        int[][] initialBoard = {{1, 2, 3, 4},
//                {5, 6, 7, 8},
//                {9, 10, 11, 12},
//                {0, 13, 14, 15}};

        int[][] goalBoard = new int[sizeOfBoard][sizeOfBoard];

//        int[][] goalBoard = {{1, 2, 3, 4},
//                {5, 6, 7, 8},
//                {9, 10, 11, 12},
//                {13, 14, 15, 0}};

        int indexOfWhiteSpace = scanner.nextInt();
//
//        int indexOfWhiteSpace = -1;
        if (indexOfWhiteSpace == -1) {
            indexOfWhiteSpace = n + 1;
        }

        initializeBoards(scanner, initialBoard, goalBoard, sizeOfBoard, indexOfWhiteSpace);

//        printMatrix(initialBoard, sizeOfBoard);
//        System.out.println("----------------------------");
//        printMatrix(goalBoard, sizeOfBoard);

        Node initialNode = new Node();
        initialNode.setBoard(initialBoard);
        initialNode.setIndexOfZero();

        ManhattanDistance manhattanDistance = new ManhattanDistance();
        NPuzzle nPuzzle = new NPuzzle(initialNode, goalBoard, sizeOfBoard, manhattanDistance);

        Node result = nPuzzle.IDAStarSearch();
        List<Node> path = nPuzzle.getFinalPath(result);

        int step = 0;
        ArrayList<String> directions = new ArrayList<>();
        for (Node node : path) {
            if (node.getDirection() != null) {
                directions.add(node.getDirection().toString());
                step++;
            }
        }
        System.out.println(step);
        directions.forEach(System.out::println);
    }

    private static void initializeBoards(Scanner scanner, int[][] initialBoard, int[][] goalBoard, int sizeOfBoard,
                                         int indexOfWhiteSpace) {
        int whiteSpaceIndexCounter = 0;
        int naturalNumbers = 1;
        for (int i = 0; i < sizeOfBoard; i++) {
            for (int j = 0; j < sizeOfBoard; j++) {
                initialBoard[i][j] = scanner.nextInt();

                whiteSpaceIndexCounter++;
                if (whiteSpaceIndexCounter == indexOfWhiteSpace) {
                    goalBoard[i][j] = 0;
                    continue;
                }
                goalBoard[i][j] = naturalNumbers++;
            }
        }
    }

    private static void printMatrix(int[][] initialBoard, int sizeOfBoard) {
        for (int i = 0; i < sizeOfBoard; i++) {
            for (int j = 0; j < sizeOfBoard; j++) {
                System.out.print(initialBoard[i][j] + " ");
            }
            System.out.println();
        }
    }
}
