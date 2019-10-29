package com.company;

public interface IHeuristicFunction {
    int getHeuristicValue(int[][] currentState, int[][] goalState, int sizeOfBoard);
}
