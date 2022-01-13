package org.aussie.anarchy.util;

import java.util.Arrays;

class Average {
    private final double[] values;
    private double average;
    private boolean dirty;

    public Average(int size) {
        this.values = new double[size];
        DoubleArrayUtils.fill(this.values, 0.0D);
        this.average = 0.0D;
        this.dirty = false;
    }

    public void put(double i) {
        DoubleArrayUtils.shiftRight(this.values, i);
        this.dirty = true;
    }

    public double getAverage() {
        if (this.dirty) {
            this.calculateAverage();
            return this.getAverage();
        } else {
            return this.average;
        }
    }

    public int size() {
        return this.values.length;
    }

    private void calculateAverage() {
        double d = 0.0D;
        double[] var3 = this.values;
        int var4 = var3.length;

        for (double i : var3) {
            d += i;
        }

        this.average = d / (double)this.values.length;
        this.dirty = false;
    }
}


class DoubleArrayUtils {
    public DoubleArrayUtils() {
    }

    public static void shiftRight(double[] values, double push) {
        if (values.length - 2 + 1 >= 0) {
            System.arraycopy(values, 0, values, 1, values.length - 2 + 1);
        }

        values[0] = push;
    }

    public static void wrapRight(double[] values) {
        double last = values[values.length - 1];
        shiftRight(values, last);
    }

    public static void fill(double[] values, double value) {
        Arrays.fill(values, value);
    }
}