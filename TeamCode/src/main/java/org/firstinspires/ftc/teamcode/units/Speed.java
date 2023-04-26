package org.firstinspires.ftc.teamcode.units;

import static org.firstinspires.ftc.teamcode.units.ConvFactors.*;

public class Speed {
    private final double val; // in millimeters per second

    public static final Speed ZERO = new Speed(0);

    private Speed(double val) {
        this.val = val;
    }



    public static Speed inTilesPerSec(double val) {
        return Speed.inInchesPerSec(val * IN_PER_TILE);
    }

    public static Speed inFeetPerSec(double val) {
        return Speed.inMMPerSec(val * MM_PER_FOOT);
    }

    public static Speed inInchesPerSec(double val) {
        return Speed.inMMPerSec(val * MM_PER_IN);
    }

    public static Speed inMMPerSec(double val) {
        return new Speed(val);
    }



    public double valInTilesPerSec() {
        return this.valInInchesPerSec() / IN_PER_TILE;
    }

    public double valInFeetPerSec() {
        return this.valInMMPerSec() / MM_PER_FOOT;
    }

    public double valInInchesPerSec() {
        return this.valInMMPerSec() / MM_PER_IN;
    }

    public double valInMMPerSec() {
        return this.val;
    }



    public String toString() {
        return this.valInFeetPerSec() + "ft/s";
    }

    public boolean isZero() {
        return this.val == 0;
    }

    public Speed add(Speed rhs) {
        return new Speed(this.val + rhs.val);
    }

    public Speed sub(Speed rhs) {
        return new Speed(this.val - rhs.val);
    }

    public Speed mul(double rhs) {
        return new Speed(this.val * rhs);
    }

    public Distance mul(Time rhs) {
        return Distance.inMM(this.valInMMPerSec() * rhs.valInSeconds());
    }

    public Speed div(double rhs) {
        return new Speed(this.val / rhs);
    }

    public double div(Speed rhs) {
        return this.val / rhs.val;
    }

    public Accel div(Time rhs) {
        return Accel.inMMPerSecSq(this.valInMMPerSec() / rhs.valInSeconds());
    }

    public Time div(Accel rhs) {
        return Time.inSeconds(this.valInMMPerSec() / rhs.valInMMPerSecSq());
    }

    public Speed neg() {
        return new Speed(-this.val);
    }
}