package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;

public class Intake {
    // 5202 Yellow Jacket 13.7:1
    public final DcMotor slide;

    public final Servo claw;
    public final Servo elbow1;
    public final Servo elbow2;

    public static final int SLIDE_FULLY_EXTENDED_POS = 3300;
    public static final Distance SLIDE_EXTENSION_PER_ENCODER_TICK = Distance.inMM(5 * 48).div(384.5);

    public static final Distance ARM_LENGTH = Distance.inInches(10);

    public Intake(HardwareMap hardwareMap) {
        this.slide = hardwareMap.get(DcMotor.class, "extendo");
        this.claw = hardwareMap.get(Servo.class, "intake");
        this.elbow1 = hardwareMap.get(Servo.class, "extendoOne");
        this.elbow2 = hardwareMap.get(Servo.class, "extendoTwo");

        this.slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.slide.setDirection(DcMotorSimple.Direction.REVERSE);

        this.slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.claw.scaleRange(0.3378042483850344, 0.6345603949755267);
        this.claw.setDirection(Servo.Direction.REVERSE);
        this.elbow1.scaleRange(0.06125983894394051, 0.6013382544473806);
        this.elbow2.scaleRange(0.33803935587562695, 0.8883754557250095);
        this.elbow2.setDirection(Servo.Direction.REVERSE);
    }

    public void setSlidePower(double power) {
        this.slide.setPower(power);
    }

    public int getSlideEncoder() {
        return this.slide.getCurrentPosition();
    }

    public void setClawPosition(double pos) {
        this.claw.setPosition(pos);
    }

    public void openClaw() {
        this.setClawPosition(1);
    }

    public void closeClaw() {
        this.setClawPosition(0);
    }

    public void setElbowPosition(double pos) {
        this.elbow1.setPosition(pos);
        this.elbow2.setPosition(pos);
    }

    public void raiseArm() {
        this.setElbowPosition(0);
    }

    public void lowerArm() {
        this.setElbowPosition(1);
    }
}
