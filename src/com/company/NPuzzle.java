package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NPuzzle {

    private Node currentState;

    private int sizeOfBoard;

    private int[][] goalState;

    private IHeuristicFunction heuristicFunction;

    public NPuzzle(Node initialState, int[][] goalState, int sizeOfBoard, IHeuristicFunction heuristicFunction) {
        this.currentState = initialState;
        this.goalState = goalState;
        this.sizeOfBoard = sizeOfBoard;
        this.heuristicFunction = heuristicFunction;
    }

    public Node IDAStarSearch() {
        int currentFBound = heuristicFunction.getHeuristicValue(currentState.getBoard(), goalState, sizeOfBoard);

        ArrayList<Node> path = new ArrayList<>();
        path.add(0, currentState);

        int nextSmallestBound;

        do {
            currentState.setChildren(new ArrayList<>());
            nextSmallestBound = getSmallestBound(path, 0, currentFBound);

            if (nextSmallestBound == 0) {
                return path.get(path.size() - 1);
            }

            currentFBound = nextSmallestBound;

        } while (currentFBound != Integer.MAX_VALUE);

        return null;
    }

    private int getSmallestBound(ArrayList<Node> path, int pathCost, int currentFBound) {
        Node currentNode = path.get(path.size() - 1);
        currentNode.sethValue(heuristicFunction.getHeuristicValue(currentNode.getBoard(), goalState, sizeOfBoard));
        currentNode.setgValue(pathCost);
        currentNode.setfValue(pathCost + currentNode.gethValue());

        if (currentNode.getfValue() > currentFBound) {
            return currentNode.getfValue();
        }

        if (Arrays.deepEquals(currentNode.getBoard(), goalState)) {
            return 0;
        }

        int smallestFBound = Integer.MAX_VALUE;

        List<Node> children = generateChildren(currentNode);

        for (Node child : children) {
            if (!path.contains(child)) {
                path.add(child);
                int nextSmallestFBound =
                        getSmallestBound(path, currentNode.getgValue() + child.getDistFromParent(), currentFBound);

                if (nextSmallestFBound == 0) {
                    return 0;
                }

                if (nextSmallestFBound < smallestFBound) {
                    smallestFBound = nextSmallestFBound;
                }
                //Remove Child From Search Path Before Exploring Next Child
                path.remove(path.size() - 1);
            }
        }

        return smallestFBound;
    }

    public List<Node> getFinalPath(Node node) {
        ArrayList<Node> path = new ArrayList<>();
        path.add(node);

        while (node.getParent() != null) {
            path.add(0, node.getParent());
            node = node.getParent();
        }

        return path;
    }

    private List<Node> generateChildren(Node currentState) {
        List<Node> children = new ArrayList<>();

        int row = currentState.getIndexOfZero().getFirst();
        int col = currentState.getIndexOfZero().getSecond();

        if (canMoveLeft(col) == 1) {
            children.add(generateChild(currentState, Direction.LEFT));
        }
        if (canMoveRight(col, sizeOfBoard) == 1) {
            children.add(generateChild(currentState, Direction.RIGHT));
        }
        if (canMoveUp(row) == 1) {
            children.add(generateChild(currentState, Direction.UP));
        }
        if (canMoveDown(row, sizeOfBoard) == 1) {
            children.add(generateChild(currentState, Direction.DOWN));
        }

        return children;
    }

    private Node generateChild(Node currentState, Direction direction) {
        Node node = new Node();

        int[][] currentBoard = new int[sizeOfBoard][sizeOfBoard];
        for (int i = 0; i < sizeOfBoard; i++) {
            System.arraycopy(currentState.getBoard()[i], 0, currentBoard[i], 0, sizeOfBoard);
        }

        int rowIndexOfZero = currentState.getIndexOfZero().getFirst();
        int colIndexOfZero = currentState.getIndexOfZero().getSecond();

        switch (direction) {
            case LEFT:
                moveTile(currentBoard, rowIndexOfZero, colIndexOfZero, colIndexOfZero - 1, currentBoard[rowIndexOfZero]);
                node.setDirection(Direction.RIGHT);
                break;
            case UP:
                moveTile(currentBoard, rowIndexOfZero - 1, colIndexOfZero, colIndexOfZero, currentBoard[rowIndexOfZero]);
                node.setDirection(Direction.DOWN);
                break;
            case DOWN:
                moveTile(currentBoard, rowIndexOfZero + 1, colIndexOfZero, colIndexOfZero, currentBoard[rowIndexOfZero]);
                node.setDirection(Direction.UP);
                break;
            case RIGHT:
                moveTile(currentBoard, rowIndexOfZero, colIndexOfZero, colIndexOfZero + 1, currentBoard[rowIndexOfZero]);
                node.setDirection(Direction.LEFT);
                break;
        }

        node.setBoard(currentBoard);
        node.setParent(currentState);
        node.setIndexOfZero();
        currentState.addChild(node);

        return node;
    }

    private void moveTile(int[][] currentBoard, int rowIndex, int currentColIndex, int newColumnIndex,
                          int[] currentRow) {
        int temp;
        temp = currentBoard[rowIndex][newColumnIndex];
        currentBoard[rowIndex][newColumnIndex] = currentRow[currentColIndex];
        currentRow[currentColIndex] = temp;
    }

    private int canMoveDown(int i, int sizeOfBoard) {
        return (i + 1 < sizeOfBoard) ? 1 : 0;
    }

    private int canMoveUp(int i) {
        return (i - 1 >= 0) ? 1 : 0;
    }

    private int canMoveRight(int j, int sizeOfBoard) {
        return (j + 1 < sizeOfBoard) ? 1 : 0;
    }

    private int canMoveLeft(int j) {
        return (j - 1 >= 0) ? 1 : 0;
    }
}
