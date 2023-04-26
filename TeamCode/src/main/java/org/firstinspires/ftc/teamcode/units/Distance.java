package org.firstinspires.ftc.teamcode.units;

import static org.firstinspires.ftc.teamcode.units.ConvFactors.*;

public class Distance {
    private final double val; // in millimeters

    public static final Distance ZERO = new Distance(0);

    // TODO: tune
    public static final Distance ROBOT_LENGTH = Distance.inInches(18);
    public static final Distance ROBOT_WIDTH = Distance.inInches(18);

    public static final Distance TILE_BORDER = Distance.inInches(0.75);
    public static final Distance ONE_TILE_WITHOUT_BORDER = Distance.inTiles(1).sub(Distance.TILE_BORDER);
    public static final Distance ONE_TILE_WITH_BORDER = Distance.inTiles(1).add(Distance.TILE_BORDER);

    private Distance(double val) {
        this.val = val;
    }



    public static Distance inTiles(double val) {
        return Distance.inInches(val * IN_PER_TILE);
    }

    public static Distance inFeet(double val) {
        return Distance.inMM(val * MM_PER_FOOT);
    }

    public static Distance inInches(double val) {
        return Distance.inMM(val * MM_PER_IN);
    }

    public static Distance inMM(double val) {
        return new Distance(val);
    }



    public double valInTiles() {
        return this.valInInches() / IN_PER_TILE;
    }

    public double valInFeet() {
        return this.valInMM() / MM_PER_FOOT;
    }

    public double valInInches() {
        return this.valInMM() / MM_PER_IN;
    }

    public double valInMM() {
        return this.val;
    }



    public String toString() {
        return this.valInMM() + "mm";
    }

    public boolean isZero() {
        return this.val == 0;
    }

    public Distance add(Distance rhs) {
        return new Distance(this.val + rhs.val);
    }

    public Distance sub(Distance rhs) {
        return new Distance(this.val - rhs.val);
    }

    public Distance mul(double rhs) {
        return new Distance(this.val * rhs);
    }

    public Distance div(double rhs) {
        return new Distance(this.val / rhs);
    }

    public double div(Distance rhs) {
        return this.val / rhs.val;
    }

    public Speed div(Time rhs) {
        return Speed.inMMPerSec(this.valInMM() / rhs.valInSeconds());
    }

    public Distance neg() {
        return new Distance(-this.val);
    }
}