/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timetable.utilz;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class NoRepeatRandom {

    private ArrayList<Integer> numbers; // Integer array for holding repeat checking data
    private Random gen1;
    private int max;

    public NoRepeatRandom(int max) {
        numbers = new ArrayList<Integer>();
        gen1 = new Random();
        this.max = max;
    }

    public int nextInt() { // get a random integer between 0 and 'max', not including max
        int temp = gen1.nextInt(max);

        if (isDone()) {
            throw new AllValuesUsedException();
        }

        while (repeated(temp)) {
            temp = gen1.nextInt(max);
        }

        numbers.add(temp);
        return temp;
    }

    public boolean isDone() {
        return (numbers.size() == max);
    }

    public void restart() { // clear the repeat list
        numbers.clear();
    }

    private boolean repeated(int temp) {
        for (int num : numbers) {
            if (num == temp) {
                return true;
            }
        }
        return false;
    }
}
