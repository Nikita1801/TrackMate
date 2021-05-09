package com.nikita.game.peakdetector;

import java.util.List;

public class CFG {

    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }

    // Find the peak element in the array, it will return the index of element that is peak
    // if returns  0  peak isn't exist
    public static int findPeak(int arr[]) {

        int n = arr.length;

        // First or last element is peak element
        if (n == 1)
            return arr[0];
        if (arr[0] >= arr[1])
            return 0;
        if (arr[n - 1] >= arr[n - 2])
            return n - 1;

        // Check for every other element
        for (int i = 1; i < n - 1; i++) {

            // Check if the neighbors are smaller
            if (arr[i] >= arr[i - 1] &&
                    arr[i] >= arr[i + 1])
                return i;
        }
        return 0;
    }
}