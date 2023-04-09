package ru.common.utils;

import java.util.Vector;

public class MainUtils {

    public static Vector<String> generatePercentages(int min, int max, int step) {
        Vector<String> vector = new Vector<>();

        for (int i = min; i <= max; i += step) {
            vector.add(i + "%");
        }

        return vector;
    }
}