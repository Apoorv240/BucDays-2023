package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;

public class Outtake {
    // 5202 Yellow Jacket 13.7:1
    public final DcMotor slide;
    // 5203 Yellow Jacket 19.2:1
    public final DcMotor turret;

    public final Servo scoringServo;
    public final Servo junctionGuide;

    public static final int SLIDE_FULLY_EXTENDED_POS = 3161;
    public static final Distance SLIDE_EXTENSION_PER_ENCODER_TICK = Distance.inMM(5 * 48).div(384.5);

    public static final Angle TURRET_MIN_ANGLE = Angle.inDegrees(-45);
    public static final Angle TURRET_MAX_ANGLE = Angle.inDegrees(45);

    public Outtake(HardwareMap hardwareMap) {
        this.slide = hardwareMap.get(DcMotor.class, "lift");
        this.turret = hardwareMap.get(DcMotor.class, "turret");
        this.scoringServo = hardwareMap.get(Servo.class, "scoreCone");
        this.junctionGuide = hardwareMap.get(Servo.class, "poleGuide");

        this.slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.slide.setDirection(DcMotorSimple.Direction.REVERSE);
        this.turret.setDirection(DcMotorSimple.Direction.FORWARD);

        this.slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.scoringServo.scaleRange(0.08140933689214516, 0.7415691429917137);
        this.junctionGuide.scaleRange(0.007459274427002828, 0.42271928412087934);
        this.junctionGuide.setDirection(Servo.Direction.REVERSE);
    }

    public void setSlidePower(double power) {
        this.slide.setPower(power);
    }

    public void setTurretPower(double power) {
        this.turret.setPower(power);
    }

    public int getSlideEncoder() {
        return this.slide.getCurrentPosition();
    }

    public int getTurretEncoder() {
        return this.turret.getCurrentPosition();
    }

    public void dropCone() {
        this.scoringServo.setPosition(1);
    }

    public void unDropCone() {
        this.scoringServo.setPosition(0);
    }

    public void raiseJunctionGuide() {
        this.junctionGuide.setPosition(1);
    }

    public void lowerJunctionGuide() {
        this.junctionGuide.setPosition(0);
    }
}
