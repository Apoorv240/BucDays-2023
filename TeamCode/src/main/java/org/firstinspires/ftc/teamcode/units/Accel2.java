package org.firstinspires.ftc.teamcode.units;

public class Accel2 {
    public final Accel x;
    public final Accel y;

    public static final Accel2 ZERO = new Accel2(Accel.ZERO, Accel.ZERO);

    public Accel2(Accel x, Accel y) {
        this.x = x;
        this.y = y;
    }



    public static Accel2 inTilesPerSecSq(double x, double y) {
        return new Accel2(Accel.inTilesPerSecSq(x), Accel.inTilesPerSecSq(y));
    }

    public static Accel2 inFeetPerSecSq(double x, double y) {
        return new Accel2(Accel.inFeetPerSecSq(x), Accel.inFeetPerSecSq(y));
    }

    public static Accel2 inInchesPerSecSq(double x, double y) {
        return new Accel2(Accel.inInchesPerSecSq(x), Accel.inInchesPerSecSq(y));
    }

    public static Accel2 inMMPerSecSq(double x, double y) {
        return new Accel2(Accel.inMMPerSecSq(x), Accel.inMMPerSecSq(y));
    }



    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public boolean isZero() {
        return this.x.isZero() && this.y.isZero();
    }

    public Accel2 add(Accel2 rhs) {
        return new Accel2(this.x.add(rhs.x), this.y.add(rhs.y));
    }

    public Accel2 sub(Accel2 rhs) {
        return new Accel2(this.x.sub(rhs.x), this.y.sub(rhs.y));
    }

    public Accel2 mul(double rhs) {
        return new Accel2(this.x.mul(rhs), this.y.mul(rhs));
    }

    public Speed2 mul(Time rhs) {
        return new Speed2(this.x.mul(rhs), this.y.mul(rhs));
    }

    public Accel2 div(double rhs) {
        return new Accel2(this.x.div(rhs), this.y.div(rhs));
    }

    public Vector2 div(Accel rhs) {
        return new Vector2(this.x.div(rhs), this.y.div(rhs));
    }

    public Accel2 neg() {
        return new Accel2(this.x.neg(), this.y.neg());
    }


    public Accel2 rot(Angle rhs) {
        double sin = rhs.sin();
        double cos = rhs.cos();
        return new Accel2(this.y.mul(sin).add(this.x.mul(cos)), this.y.mul(cos).sub(this.x.mul(sin)));
    }

    public Accel magnitude() {
        return Accel.inMMPerSecSq(Math.hypot(this.x.valInMMPerSecSq(), this.y.valInMMPerSecSq()));
    }

    public Vector2 normalized() {
        if (this.magnitude().isZero()) {
            return Vector2.ZERO;
        } else {
            return this.div(this.magnitude());
        }
    }

    public Angle angle() {
        return Angle.inRadians(Math.atan2(this.x.valInMMPerSecSq(), this.y.valInMMPerSecSq()));
    }
}