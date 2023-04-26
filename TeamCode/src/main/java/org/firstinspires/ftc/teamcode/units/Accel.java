package org.firstinspires.ftc.teamcode.units;

import static org.firstinspires.ftc.teamcode.units.ConvFactors.*;

public class Accel {
    private final double val; // in millimeters per second squared

    public static final Accel ZERO = new Accel(0);

    private Accel(double val) {
        this.val = val;
    }



    public static Accel inTilesPerSecSq(double val) {
        return Accel.inInchesPerSecSq(val * IN_PER_TILE);
    }

    public static Accel inFeetPerSecSq(double val) {
        return Accel.inMMPerSecSq(val * MM_PER_FOOT);
    }

    public static Accel inInchesPerSecSq(double val) {
        return Accel.inMMPerSecSq(val * MM_PER_IN);
    }

    public static Accel inMMPerSecSq(double val) {
        return new Accel(val);
    }



    public double valInTilesPerSecSq() {
        return this.valInInchesPerSecSq() / IN_PER_TILE;
    }

    public double valInFeetPerSecSq() {
        return this.valInMMPerSecSq() / MM_PER_FOOT;
    }

    public double valInInchesPerSecSq() {
        return this.valInMMPerSecSq() / MM_PER_IN;
    }

    public double valInMMPerSecSq() {
        return this.val;
    }



    public String toString() {
        return this.valInFeetPerSecSq() + "ft/s^2";
    }

    public boolean isZero() {
        return this.val == 0;
    }

    public Accel add(Accel rhs) {
        return new Accel(this.val + rhs.val);
    }

    public Accel sub(Accel rhs) {
        return new Accel(this.val - rhs.val);
    }

    public Accel mul(double rhs) {
        return new Accel(this.val * rhs);
    }

    public Speed mul(Time rhs) {
        return Speed.inMMPerSec(this.valInMMPerSecSq() * rhs.valInSeconds());
    }

    public Accel div(double rhs) {
        return new Accel(this.val / rhs);
    }

    public double div(Accel rhs) {
        return this.val / rhs.val;
    }

    public Accel neg() {
        return new Accel(-this.val);
    }
}