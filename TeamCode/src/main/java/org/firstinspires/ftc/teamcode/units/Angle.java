package org.firstinspires.ftc.teamcode.units;

public class Angle {
    private final double val; // in radians

    public static final Angle ZERO = new Angle(0);
    public static final Angle QUARTER_REV = Angle.inDegrees(90);
    public static final Angle HALF_REV = Angle.inDegrees(180);
    public static final Angle FULL_REV = Angle.inDegrees(360);

    private Angle(double val) {
        this.val = val;
    }



    public static Angle inDegrees(double val) {
        return Angle.inRadians(Math.toRadians(val));
    }

    public static Angle inRadians(double val) {
        return new Angle(val);
    }



    public double valInDegrees() {
        return Math.toDegrees(this.valInRadians());
    }

    public double valInRadians() {
        return this.val;
    }



    public String toString() {
        return this.valInDegrees() + " degrees";
    }

    public boolean isZero() {
        return this.val == 0;
    }

    public Angle add(Angle rhs) {
        return new Angle(this.val + rhs.val);
    }

    public Angle sub(Angle rhs) {
        return new Angle(this.val - rhs.val);
    }

    public Angle mul(double rhs) {
        return new Angle(this.val * rhs);
    }

    public Angle div(double rhs) {
        return new Angle(this.val / rhs);
    }

    public Angle neg() {
        return new Angle(-this.val);
    }

    public Angle normalize() {
        return Angle.inRadians(((this.valInRadians() + Math.PI) % (Math.PI * 2)) - Math.PI);
    }

    public double sin() {
        return Math.sin(this.val);
    }

    public double cos() {
        return Math.cos(this.val);
    }

    public double sinc() {
        if (this.val == 0) {
            return 1;
        } else {
            return this.sin() / this.val;
        }
    }

    public double cosc() {
        if (this.val == 0) {
            return 0;
        } else {
            return (1 - this.cos()) / this.val;
        }
    }
}
