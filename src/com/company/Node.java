package com.company;

import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node {
    private List<Node> children;

    private int[][] board;

    private int hValue;

    private int gValue;

    private int fValue;

    private Node parent;

    private Direction direction;

    Pair<Integer, Integer> indexOfZero;

    //We set the index of zero (the empty cell) in order to decide in which position we can move later
    public void setIndexOfZero() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    indexOfZero = new Pair<>(i, j);
                }
            }
        }
    }

    public Pair<Integer, Integer> getIndexOfZero() {
        return indexOfZero;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int gethValue() {
        return hValue;
    }

    public void sethValue(int hValue) {
        this.hValue = hValue;
    }

    public int getgValue() {
        return gValue;
    }

    public void setgValue(int gValue) {
        this.gValue = gValue;
    }

    public int getfValue() {
        return fValue;
    }

    public void setfValue(int fValue) {
        this.fValue = fValue;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int getDistFromParent() {
        return 1;
    }

    public void addChild(Node node) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(node);
    }

    @Override
    public boolean equals(Object o) {
        Node node = (Node) o;
        int [][] otherBoard = node.getBoard();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != otherBoard[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.getBoard());
    }
}
