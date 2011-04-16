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
public class NoRepeatRandom2D {

    private ArrayList<Integer2D> numbers; // Integer array for holding repeat checking data
    private Random gen1;
    private Random gen2;
    private Integer2D max;

    public NoRepeatRandom2D(int maxX, int maxY) {
        numbers = new ArrayList<Integer2D>();
        max = new Integer2D();
        max.x = maxX;
        max.y = maxY;
        gen1 = new Random();
        gen2 = new Random();
    }

    public Integer2D nextInt2D() throws AllValuesUsedException { // get a random integer between 0 and 'max', not including max
        Integer2D temp = new Integer2D();
        temp.x = gen1.nextInt(max.x);
        temp.y = gen2.nextInt(max.y);

        if (isDone()) {
            throw new AllValuesUsedException();
        }

        while (repeated(temp)) {
            temp.x = gen1.nextInt(max.x);
            temp.y = gen2.nextInt(max.y);
        }

        numbers.add(temp);
        return temp;
    }

    public boolean isDone() {
        return (numbers.size() == (max.x * max.y));
    }

    public void restart() { // clear the repeat list
        numbers.clear();
    }

    private boolean repeated(Integer2D temp) {
        for (Integer2D num : numbers) {
            if (num.x == temp.x && num.y == temp.y) {
                return true;
            }
        }
        return false;
    }
}
