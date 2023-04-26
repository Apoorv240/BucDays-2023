package org.firstinspires.ftc.teamcode.units;

public class Speed2 {
    public final Speed x;
    public final Speed y;

    public static final Speed2 ZERO = new Speed2(Speed.ZERO, Speed.ZERO);

    public Speed2(Speed x, Speed y) {
        this.x = x;
        this.y = y;
    }



    public static Speed2 inTilesPerSec(double x, double y) {
        return new Speed2(Speed.inTilesPerSec(x), Speed.inTilesPerSec(y));
    }

    public static Speed2 inFeetPerSec(double x, double y) {
        return new Speed2(Speed.inFeetPerSec(x), Speed.inFeetPerSec(y));
    }

    public static Speed2 inInchesPerSec(double x, double y) {
        return new Speed2(Speed.inInchesPerSec(x), Speed.inInchesPerSec(y));
    }

    public static Speed2 inMMPerSec(double x, double y) {
        return new Speed2(Speed.inMMPerSec(x), Speed.inMMPerSec(y));
    }



    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public boolean isZero() {
        return this.x.isZero() && this.y.isZero();
    }

    public Speed2 add(Speed2 rhs) {
        return new Speed2(this.x.add(rhs.x), this.y.add(rhs.y));
    }

    public Speed2 sub(Speed2 rhs) {
        return new Speed2(this.x.sub(rhs.x), this.y.sub(rhs.y));
    }

    public Speed2 mul(double rhs) {
        return new Speed2(this.x.mul(rhs), this.y.mul(rhs));
    }

    public Distance2 mul(Time rhs) {
        return new Distance2(this.x.mul(rhs), this.y.mul(rhs));
    }

    public Speed2 div(double rhs) {
        return new Speed2(this.x.div(rhs), this.y.div(rhs));
    }

    public Vector2 div(Speed rhs) {
        return new Vector2(this.x.div(rhs), this.y.div(rhs));
    }

    public Accel2 div(Time rhs) {
        return new Accel2(this.x.div(rhs), this.y.div(rhs));
    }

    public Speed2 neg() {
        return new Speed2(this.x.neg(), this.y.neg());
    }


    public Speed2 rot(Angle rhs) {
        double sin = rhs.sin();
        double cos = rhs.cos();
        return new Speed2(this.y.mul(sin).add(this.x.mul(cos)), this.y.mul(cos).sub(this.x.mul(sin)));
    }

    public Speed magnitude() {
        return Speed.inMMPerSec(Math.hypot(this.x.valInMMPerSec(), this.y.valInMMPerSec()));
    }

    public Vector2 normalized() {
        if (this.magnitude().isZero()) {
            return Vector2.ZERO;
        } else {
            return this.div(this.magnitude());
        }
    }

    public Angle angle() {
        return Angle.inRadians(Math.atan2(this.x.valInMMPerSec(), this.y.valInMMPerSec()));
    }
}