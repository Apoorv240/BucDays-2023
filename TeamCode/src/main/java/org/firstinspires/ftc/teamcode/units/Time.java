package org.firstinspires.ftc.teamcode.units;

import static org.firstinspires.ftc.teamcode.units.ConvFactors.*;

public class Time {
    private final double val; // in nanoseconds

    public static final Time ZERO = new Time(0);

    private Time(double val) {
        this.val = val;
    }

    public static Time now() {
        return Time.inNanos(System.nanoTime());
    }


    public static Time inSeconds(double val) {
        return Time.inNanos(val * NANOS_PER_SEC);
    }

    public static Time inMillis(double val) {
        return Time.inSeconds(val / MILLIS_PER_SEC);
    }

    public static Time inNanos(double val) {
        return new Time(val);
    }



    public double valInSeconds() {
        return this.valInNanos() / NANOS_PER_SEC;
    }

    public double valInMillis() {
        return this.valInSeconds() * MILLIS_PER_SEC;
    }

    public double valInNanos() {
        return this.val;
    }



    public String toString() {
        return this.valInSeconds() + "s";
    }

    public boolean isZero() {
        return this.val == 0;
    }

    public Time add(Time rhs) {
        return new Time(this.val + rhs.val);
    }

    public Time sub(Time rhs) {
        return new Time(this.val - rhs.val);
    }

    public Time mul(double rhs) {
        return new Time(this.val * rhs);
    }

    public Distance mul(Speed rhs) {
        return rhs.mul(this);
    }

    public Time div(double rhs) {
        return new Time(this.val / rhs);
    }

    public double div(Time rhs) {
        return this.valInNanos() / rhs.valInNanos();
    }

    public Time neg() {
        return new Time(-this.val);
    }
}