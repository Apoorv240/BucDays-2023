package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.subsystem.Intake;
import org.firstinspires.ftc.teamcode.subsystem.IntakeSlideController;
import org.firstinspires.ftc.teamcode.subsystem.Outtake;
import org.firstinspires.ftc.teamcode.subsystem.OuttakeSlideController;
import org.firstinspires.ftc.teamcode.units.Time;

@TeleOp(name = "CCCCCCCCC")
public class MainTeleop extends OpMode {
    Hardware hardware;

    Time lastUpdate;
    Gamepad prevGamepad1;
    Gamepad prevGamepad2;

    double dtSpeed;

    IntakeSlideController intakeSlideController;
    boolean isIntakeSlideControllerRunning;
    boolean isClawClosed;
    double elbowPos;

    OuttakeSlideController outtakeSlideController;
    boolean isOuttakeSlideControllerRunning;
    boolean isConeDropped;
    boolean isJunctionGuideUp;

    @Override
    public void init() {
        this.hardware = new Hardware(this);

        this.lastUpdate = Time.now();
        this.prevGamepad1 = new Gamepad();
        this.prevGamepad2 = new Gamepad();

        this.dtSpeed = 0.7;

        this.intakeSlideController = new IntakeSlideController(this.hardware.intake);
        this.isIntakeSlideControllerRunning = false;
        this.isClawClosed = false;
        this.hardware.intake.openClaw();
        this.elbowPos = 1;
        this.hardware.intake.setElbowPosition(this.elbowPos);

        this.outtakeSlideController = new OuttakeSlideController(this.hardware.outtake);
        this.isOuttakeSlideControllerRunning = false;
        this.isConeDropped = false;
        this.hardware.outtake.unDropCone();
        this.isJunctionGuideUp = false;
        this.hardware.outtake.lowerJunctionGuide();
    }

    @Override
    public void loop() {
        Time now = Time.now();
        Time dt = now.sub(this.lastUpdate);
        this.lastUpdate = now;

        ////////
        // dt //
        ////////

        // speed control
        if (gamepad1.dpad_up && !prevGamepad1.dpad_up) {
            this.dtSpeed += 0.15;
        }
        if (gamepad1.dpad_down && !prevGamepad1.dpad_down) {
            this.dtSpeed -= 0.15;
        }
        this.dtSpeed = Range.clip(this.dtSpeed, 0.1, 1);
        telemetry.addData("Drivetrain speed", this.dtSpeed);

        // movement
        this.hardware.dt.move(
                gamepad1.left_stick_x * this.dtSpeed,
                -gamepad1.left_stick_y * this.dtSpeed,
                gamepad1.right_stick_x * this.dtSpeed
        );

        ////////////
        // intake //
        ////////////

        // claw
        if (gamepad1.a && !prevGamepad1.a) {
            this.isClawClosed = !this.isClawClosed;
            if (this.isClawClosed) {
                this.hardware.intake.closeClaw();
            } else {
                this.hardware.intake.openClaw();
            }
        }
        telemetry.addData("Claw", this.isClawClosed ? "closed" : "open");

        // elbow
        if (gamepad1.left_bumper) {
            this.elbowPos = 0;
        }
        if (gamepad1.right_bumper) {
            this.elbowPos += dt.div(Time.inSeconds(2.5));
        }
        this.elbowPos = Range.clip(this.elbowPos, 0, 1);
        this.hardware.intake.setElbowPosition(this.elbowPos);
        telemetry.addData("Elbow pos", this.elbowPos);

        // slide
        double intakeSlidePower = gamepad1.left_trigger - gamepad1.right_trigger;

        // slide presets
        if (intakeSlidePower != 0) {
            this.isIntakeSlideControllerRunning = false;
        } else if (gamepad1.x) {
            this.intakeSlideController.setTargetSlidePosition(0);
            this.isIntakeSlideControllerRunning = true;
        } else if (gamepad1.y) {
            this.intakeSlideController.setTargetSlidePosition(Intake.SLIDE_FULLY_EXTENDED_POS);
            this.isIntakeSlideControllerRunning = true;
        }

        // slide control
        if (this.isIntakeSlideControllerRunning) {
            this.intakeSlideController.update();
        } else {
            int slideEncoder = this.hardware.intake.getSlideEncoder();
            if (slideEncoder < -50) {
                intakeSlidePower = Math.max(intakeSlidePower, 0.2);
            } else if (slideEncoder < 0) {
                intakeSlidePower = Math.max(intakeSlidePower, 0);
            } else if (slideEncoder > Intake.SLIDE_FULLY_EXTENDED_POS + 50) {
                intakeSlidePower = Math.min(intakeSlidePower, -0.2);
            } else if (slideEncoder > Intake.SLIDE_FULLY_EXTENDED_POS) {
                intakeSlidePower = Math.min(intakeSlidePower, 0);
            }
            this.hardware.intake.setSlidePower(intakeSlidePower);
        }

        /////////////
        // outtake //
        /////////////

        // cone scorer
        if (gamepad2.a && !prevGamepad2.a) {
            this.isConeDropped = !this.isConeDropped;
            if (this.isConeDropped) {
                this.hardware.outtake.dropCone();
            } else {
                this.hardware.outtake.unDropCone();
            }
        }
        telemetry.addData("Cone scorer", this.isConeDropped ? "dropping cone" : "holding cone");

        // junction guide
        if (gamepad2.b && !prevGamepad2.b) {
            this.isJunctionGuideUp = !this.isJunctionGuideUp;
            if (this.isJunctionGuideUp) {
                this.hardware.outtake.raiseJunctionGuide();
            } else {
                this.hardware.outtake.lowerJunctionGuide();
            }
        }
        telemetry.addData("Junction guide", this.isJunctionGuideUp ? "up" : "down");

        // turret
        this.hardware.outtake.setTurretPower(gamepad2.left_stick_x);

        // slide
        double outtakeSlidePower = -gamepad2.right_stick_y;

        // slide presets
        if (outtakeSlidePower != 0) {
            this.isOuttakeSlideControllerRunning = false;
        } else if (gamepad2.x) {
            this.outtakeSlideController.setTargetSlidePosition(0);
            this.isOuttakeSlideControllerRunning = true;
        } else if (gamepad2.y) {
            this.outtakeSlideController.setTargetSlidePosition(Outtake.SLIDE_FULLY_EXTENDED_POS);
            this.isOuttakeSlideControllerRunning = true;
        }

        // slide control
        if (this.isOuttakeSlideControllerRunning) {
            outtakeSlideController.update();
        } else {
            int slideEncoder = this.hardware.outtake.getSlideEncoder();
            if (slideEncoder < -50) {
                outtakeSlidePower = Math.max(outtakeSlidePower, 0.2);
            } else if (slideEncoder < 0) {
                outtakeSlidePower = Math.max(outtakeSlidePower, 0);
            } else if (slideEncoder > Outtake.SLIDE_FULLY_EXTENDED_POS + 50) {
                outtakeSlidePower = Math.min(outtakeSlidePower, -0.2);
            } else if (slideEncoder > Outtake.SLIDE_FULLY_EXTENDED_POS) {
                outtakeSlidePower = Math.min(outtakeSlidePower, 0);
            }
            this.hardware.outtake.setSlidePower(outtakeSlidePower);
        }

        prevGamepad1.copy(gamepad1);
        prevGamepad2.copy(gamepad2);
    }
}
