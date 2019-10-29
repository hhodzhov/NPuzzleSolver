package com.company;

import org.apache.commons.math3.util.Pair;

public class ManhattanDistance implements IHeuristicFunction {
    @Override
    public int getHeuristicValue(int[][] currentState, int[][] goalState, int sizeOfBoard) {

        int heuristicValue = 0;
        for (int i = 0; i < sizeOfBoard; i++) {
            for (int j = 0; j < sizeOfBoard; j++) {
                int goalElement = goalState[i][j];
                if (goalElement == 0) {
                    continue;
                }

                Pair<Integer, Integer> positionOfGoalElementInCurrentState = getPosition(goalElement, currentState, sizeOfBoard);

                int distanceFromGoalState = Math.abs(i - positionOfGoalElementInCurrentState.getFirst()) +
                                            Math.abs(j - positionOfGoalElementInCurrentState.getSecond());
                heuristicValue += distanceFromGoalState;
            }
        }
        return heuristicValue;
    }

    private Pair<Integer, Integer> getPosition(int goalElement, int[][] currentState, int sizeOfBoard) {
        for (int i = 0; i < sizeOfBoard; i++) {
            for (int j = 0; j < sizeOfBoard; j++) {
                if (currentState[i][j] == goalElement) {
                    return new Pair<>(i, j);
                }
            }
        }
        return new Pair<>(1, 1);
    }
}
