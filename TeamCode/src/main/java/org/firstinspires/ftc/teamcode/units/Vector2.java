package org.firstinspires.ftc.teamcode.units;

public class Vector2 {
    public final double x;
    public final double y;

    public static final Vector2 ZERO = new Vector2(0, 0);

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }



    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public boolean isZero() {
        return this.x == 0 && this.y == 0;
    }

    public Vector2 add(Vector2 rhs) {
        return new Vector2(this.x + rhs.x, this.y + rhs.y);
    }

    public Vector2 sub(Vector2 rhs) {
        return new Vector2(this.x - rhs.x, this.y - rhs.y);
    }

    public Vector2 mul(double rhs) {
        return new Vector2(this.x * rhs, this.y * rhs);
    }

    public Distance2 mul(Distance rhs) {
        return new Distance2(rhs.mul(this.x), rhs.mul(this.y));
    }

    public Speed2 mul(Speed rhs) {
        return new Speed2(rhs.mul(this.x), rhs.mul(this.y));
    }

    public Accel2 mul(Accel rhs) {
        return new Accel2(rhs.mul(this.x), rhs.mul(this.y));
    }

    public Vector2 div(double rhs) {
        return new Vector2(this.x / rhs, this.y / rhs);
    }

    public Vector2 neg() {
        return new Vector2(-this.x, -this.y);
    }

    public Vector2 rot(Angle rhs) {
        double sin = rhs.sin();
        double cos = rhs.cos();
        return new Vector2(this.y * sin + this.x * cos, this.y * cos - this.x * sin);
    }

    public double sqMagnitude() {
        return this.x * this.x + this.y * this.y;
    }

    public double magnitude() {
        return Math.hypot(this.x, this.y);
    }

    public Vector2 normalized() {
        if (this.magnitude() == 0) {
            return Vector2.ZERO;
        } else {
            return this.div(this.magnitude());
        }
    }

    public Vector2 clipMagnitude(double magnitudeLimit) {
        double magnitude = this.magnitude();
        if (magnitude > magnitudeLimit) {
            return this.div(magnitude).mul(magnitudeLimit);
        } else {
            return this;
        }
    }

    public Angle angle() {
        return Angle.inRadians(Math.atan2(this.x, this.y));
    }
}