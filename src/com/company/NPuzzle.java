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
        int threshold = heuristicFunction.getHeuristicValue(currentState.getBoard(), goalState, sizeOfBoard);

        ArrayList<Node> path = new ArrayList<>();
        path.add(0, currentState);

        int nextMinThreshold;

        do {
            //clean the children at the beginning of each iteration
            currentState.setChildren(new ArrayList<>());
            nextMinThreshold = getNextThreshold(path, 0, threshold);

            if (nextMinThreshold == 0) {
                return path.get(path.size() - 1);
            }

            threshold = nextMinThreshold;

        } while (threshold != Integer.MAX_VALUE);

        return null;
    }

    private int getNextThreshold(ArrayList<Node> path, int pathCost, int threshold) {
        Node currentNode = path.get(path.size() - 1);
        currentNode.sethValue(heuristicFunction.getHeuristicValue(currentNode.getBoard(), goalState, sizeOfBoard));
        currentNode.setgValue(pathCost);
        currentNode.setfValue(pathCost + currentNode.gethValue());

        //if the currentFValue is > threshold we return the currentFValue as the next min threshold
        if (currentNode.getfValue() > threshold) {
            return currentNode.getfValue();
        }

        //If the current board is equals to goal board -> we found the solution
        //else we continue with exploring the children of the current node
        if (Arrays.deepEquals(currentNode.getBoard(), goalState)) {
            return 0;
        }

        int minThreshold = Integer.MAX_VALUE;

        List<Node> children = generateChildren(currentNode);

        for (Node child : children) {
            //make a check in order to avoid already explored state
            if (!path.contains(child)) {
                path.add(child);
                int nextMinThreshold =
                        getNextThreshold(path, currentNode.getgValue() + child.getDistFromParent(), threshold);

                if (nextMinThreshold == 0) {
                    return 0;
                }

                //we always tend to take the min threshold for next iteration from all thresholds
                if (nextMinThreshold < minThreshold) {
                    minThreshold = nextMinThreshold;
                }
                //Remove child from search path before exploring next child
                path.remove(path.size() - 1);
            }
        }

        return minThreshold;
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
        Node child = new Node();

        //We should copy the current state's board, otherwise it will be changed in moveTile method.
        int[][] currentBoard = new int[sizeOfBoard][sizeOfBoard];
        for (int i = 0; i < sizeOfBoard; i++) {
            System.arraycopy(currentState.getBoard()[i], 0, currentBoard[i], 0, sizeOfBoard);
        }

        int rowIndexOfZero = currentState.getIndexOfZero().getFirst();
        int colIndexOfZero = currentState.getIndexOfZero().getSecond();

        //we move in reversed direction, because we represent 0 as a empty space
        //which means that for example if we can move 0 to left -> we can move some tile to the right.
        switch (direction) {
            case LEFT:
                moveTile(currentBoard, rowIndexOfZero, colIndexOfZero, colIndexOfZero - 1, currentBoard[rowIndexOfZero]);
                child.setDirection(Direction.RIGHT);
                break;
            case UP:
                moveTile(currentBoard, rowIndexOfZero - 1, colIndexOfZero, colIndexOfZero, currentBoard[rowIndexOfZero]);
                child.setDirection(Direction.DOWN);
                break;
            case DOWN:
                moveTile(currentBoard, rowIndexOfZero + 1, colIndexOfZero, colIndexOfZero, currentBoard[rowIndexOfZero]);
                child.setDirection(Direction.UP);
                break;
            case RIGHT:
                moveTile(currentBoard, rowIndexOfZero, colIndexOfZero, colIndexOfZero + 1, currentBoard[rowIndexOfZero]);
                child.setDirection(Direction.LEFT);
                break;
        }

        child.setBoard(currentBoard);
        child.setParent(currentState);
        child.setIndexOfZero();
        currentState.addChild(child);

        return child;
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
