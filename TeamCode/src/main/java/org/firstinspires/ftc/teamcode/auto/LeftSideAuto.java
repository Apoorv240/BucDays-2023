package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.poser.Pose;
import org.firstinspires.ftc.teamcode.poser.Poser;
import org.firstinspires.ftc.teamcode.subsystem.Intake;
import org.firstinspires.ftc.teamcode.subsystem.IntakeSlideController;
import org.firstinspires.ftc.teamcode.subsystem.Outtake;
import org.firstinspires.ftc.teamcode.subsystem.OuttakeSlideController;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.units.Distance2;
import org.firstinspires.ftc.teamcode.units.Time;

@Autonomous(name = "Left Side Auto")
public class LeftSideAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
        Hardware hardware = new Hardware(this);
        Poser poser = new Poser(hardware, Pose.INITIAL_POSE_A2_AUTO, 0.7);
        IntakeSlideController intakeCtrl = new IntakeSlideController(hardware.intake);
        OuttakeSlideController outtakeCtrl = new OuttakeSlideController(hardware.outtake);
        intakeCtrl.setTargetSlidePosition(Distance.ZERO);
        outtakeCtrl.setTargetSlidePosition(Distance.ZERO);
        outtakeCtrl.setTargetTurretAngle(Angle.inDegrees(45));
        hardware.intake.openClaw();
        hardware.intake.raiseArm();
        hardware.outtake.lowerJunctionGuide();
        hardware.outtake.unDropCone();

        while (!isStarted()) {
            intakeCtrl.update();
            outtakeCtrl.update();
        }

        int stackConesLeft = 5;

        // move to cycle pos
        poser.setTargetPose(new Pose(
                Distance2.inTiles(-1.5, -1.5),
                Angle.inDegrees(-90)
        ));
        outtakeCtrl.setTargetTurretAngle(Angle.inDegrees(20));
        while (!(poser.update() && outtakeCtrl.update()) && !isStopRequested());

        // this loops once for each cone
        while (!isStopRequested()) {
            // we need this boolean to implement the fact that while the intake extends 5 times, the outtake extends 6 times
            boolean lastIteration = stackConesLeft == 0;

            int intakeState;
            Time intakeWaitUntil = Time.ZERO;

            if (stackConesLeft > 0) {
                intakeCtrl.setTargetSlidePosition(Distance.inTiles(1.5).sub(Intake.ARM_LENGTH));
                hardware.intake.setElbowPosition(1 - 0.04 * stackConesLeft);
                intakeState = 0;
            } else {
                intakeState = 5; // dont bother doing anything with the intake as there are no cones on the stack
            }

            hardware.outtake.raiseJunctionGuide();
            outtakeCtrl.setTargetSlidePosition(Outtake.SLIDE_FULLY_EXTENDED_POS);
            int outtakeState = 0;
            Time outtakeWaitUntil = Time.ZERO;

            while (!isStopRequested()) {
                if (stackConesLeft > 0) {
                    switch (intakeState) {
                        case 0: // extending the intake
                            // must wait until the outtake drops the cone before we can move on
                            if (intakeCtrl.update() && outtakeState >= 2) {
                                hardware.intake.closeClaw();
                                intakeWaitUntil = Time.now().add(Time.inMillis(400));
                                intakeState = 1;
                            }
                            break;
                        case 1: // waiting for the claw to grab the cone
                            if (Time.now().valInNanos() > intakeWaitUntil.valInNanos()) {
                                hardware.intake.setElbowPosition(0.25);
                                intakeWaitUntil = Time.now().add(Time.inMillis(400));
                                intakeState = 2;
                            }
                            break;
                        case 2: // waiting for the elbow to lift a little so we can start retracting the slide
                            if (Time.now().valInNanos() > intakeWaitUntil.valInNanos()) {
                                stackConesLeft--;
                                intakeCtrl.setTargetSlidePosition(Distance.ZERO);
                                intakeState = 3;
                            }
                            break;
                        case 3: // waiting for the slide to retract
                            // must wait for the outtake to return before we can give the cone to it
                            if (intakeCtrl.update() && outtakeState >= 3) {
                                hardware.intake.raiseArm();
                                intakeWaitUntil = Time.now().add(Time.inMillis(200));
                                intakeState = 4;
                            }
                        case 4: // moving the elbow to pos 0
                            if (Time.now().valInNanos() > intakeWaitUntil.valInNanos()) {
                                hardware.intake.openClaw();
                                // we dont bother setting a timer on this one
                                intakeState = 5;
                            }
                        case 5: // opening the claw to transfer the cone to the intake (done)
                            break;
                    }
                }

                switch (outtakeState) {
                    case 0: // extending the outtake
                        if (outtakeCtrl.update()) {
                            hardware.outtake.dropCone();
                            outtakeWaitUntil = Time.now().add(Time.inMillis(400));
                            outtakeState = 1;
                        }
                        break;
                    case 1: // waiting to make sure the cone is dropped (done need to update controller)
                        if (Time.now().valInNanos() > outtakeWaitUntil.valInNanos()) {
                            hardware.outtake.unDropCone();
                            hardware.outtake.lowerJunctionGuide();
                            outtakeCtrl.setTargetSlidePosition(Distance.ZERO);
                            outtakeState = 2;
                        }
                        break;
                    case 2: // moving back
                        if (outtakeCtrl.update()) {
                            outtakeState = 3;
                        }
                    case 3: // done
                        break;
                }

                if (intakeState >= 5 && outtakeState >= 3) {
                    break;
                }
            }

            if (lastIteration) {
                break;
            }
        }

        // TODO: parking time!
    }
}
