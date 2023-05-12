package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.subsystems.*;
import org.firstinspires.ftc.teamcode.utils.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.utils.SleeveDetector;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;


@Autonomous(name = "CarMAN's Auto"/*, preselectTeleop = "Zesty Teleop" */)
@Config
public class CarmansAuto extends LinearOpMode {
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS, WE MAY NEED TO ALTER TAG SIZE
    double tagsize = 0.166;

    // Tag ID 1,2,3 from the 36h11 family
    final int LEFT = 1;
    final int MIDDLE = 2;
    final int RIGHT = 3;

    AprilTagDetection tagOfInterest = null;

    private Extendo intake;
    private Lift lift;
    private Turret turret;
    private Drive drive;
    private Arm arm;
    private Flipper flipper;
    //private SleeveDetector sleeveDetector;

    public static double t1 = 1.8;
    public static double t2 = 0.5;

    public static int ticks1 = 2850;

    @Override
    public void runOpMode() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        drive = new Drive(hardwareMap, telemetry);
        intake = new Extendo(hardwareMap, telemetry);
        arm = new Arm(hardwareMap, telemetry);
        turret = new Turret(hardwareMap, telemetry);
        turret.setAngle(-45);
        lift = new Lift(hardwareMap, telemetry);
        flipper = new Flipper(hardwareMap, telemetry);
        flipper.unflip();

       //sleeveDetector = new SleeveDetector(hardwareMap, telemetry);

        final var armPoses = new double[]{0.15, 0.14, 0.13, 0.1, 0.07};

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(800, 448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
        FtcDashboard.getInstance().startCameraStream(camera, 30.0);

        telemetry.setMsTransmissionInterval(50);

        while (opModeInInit()) {
            drive.setVels(0, 0, 0);
            drive.update();
            intake.update();
            arm.update();
            turret.update();
            lift.update();
            flipper.update();

            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if (currentDetections.size() != 0) {
                boolean tagFound = false;

                for (AprilTagDetection tag : currentDetections) {
                    if (tag.id == LEFT || tag.id == MIDDLE || tag.id == RIGHT) {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if (tagFound) {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                } else {
                    telemetry.addLine("Don't see tag of interest :(");

                    if (tagOfInterest == null) {
                        telemetry.addLine("(The tag has never been seen)");
                    } else {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            } else {
                telemetry.addLine("Don't see tag of interest :(");

                if (tagOfInterest == null) {
                    telemetry.addLine("(The tag has never been seen)");
                } else {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
        }

        waitForStart();

        //final var verdict = sleeveDetector.getVerdict();

        int stage = -1;
        int conesLeft = 5;
        ElapsedTime timer = new ElapsedTime();

        while (opModeIsActive()) {
            switch (stage) {
                case -1 : { // go to cycle spot
                    turret.setAngle(-23);
                    //drive.setPos(-ticks1, ticks1, ticks1, -ticks1);
                    //drive.RTP();
                    drive.setVels(-0.9, 0, 0.0);
                    if (timer.time() > t1) {
                        stage++;
                        drive.setVels(0, 0, 0);
                        //drive.resetEncoders();
                    }
                    break;
                } case 0 : {
                    if (turret.getCurrentAngle() < -15) {
                        stage++;
                    }
                    break;
                }
                case 1 : { // lift to safe pole
                    lift.setPosition(Lift.UP - 200);
                    flipper.brace();

                    if (conesLeft > 0) {
                        arm.setPosition(armPoses[5 - conesLeft]);
                        arm.open();
                        intake.setPosition(conesLeft == 1 ? 1270 : 1230);
                    }
                    stage++;
                    break;
                }
                case 2 : { // deposit cone
                    if (lift.getCurrentPosition() > Lift.UP - 250) {
                        flipper.flip();
                        stage++;
                        timer.reset();
                    }
                    break;
                }
                case 3 : { // stow flipper and brace
                    if (timer.seconds() > 0.5) {
                        flipper.unflip();
                        flipper.unbrace();
                        stage++;
                        timer.reset();
                    }
                    break;
                }
                case 4 : { // stow lift, grab cone
                    if (timer.seconds() > 0.2) {
                        lift.down();
                        turret.setAngle(0);
                        if (conesLeft == 0) stage = 10;

                        arm.close();
                        stage++;
                        timer.reset();
                    }
                    break;
                }
                case 5 : { // lift cone
                    if (arm.isClawFinished() && timer.seconds() > 0.6) {
                        arm.up();
                        stage++;
                        timer.reset();
                    }
                    break;
                }
                case 6 : { // stow intake
                    if (timer.seconds() > 0.5) {
                        intake.retract();
                        stage++;
                    }
                    break;
                }
                case 7 : { // transfer
                    if (intake.getCurrentPosition() < 10) {
                        arm.open();
                        turret.setAngle(-20);
                        stage++;
                        timer.reset();
                    }
                    break;
                }
                case 8 : { // lower arm
                    if (timer.seconds() > 0.4) {
                        if (conesLeft > 1) arm.down();
                        else arm.mid();
                        stage++;
                    }
                    break;
                }
                case 9 : { // wait for transfer, go back for another cycle
                    if (turret.getCurrentAngle() < -18) {
                        conesLeft--;
                        stage = 1;
                    }
                    break;
                }
                case 10 : { // park
                    if (lift.isDown()) {
                        switch (tagOfInterest.id) {
                            case LEFT : drive.setVels(0, 0.9, 0); break;
                            case RIGHT : drive.setVels(0, -0.9, 0); break;
                            default : drive.setVels(0, 0, 0); break;
                        }
                        stage++;
                        timer.reset();
                    }
                    break;
                }
                case 11 : { // in zone
                    if (timer.seconds() > t2) {
                        drive.setVels(0, 0, 0);
                        stage++;
                    }
                    break;
                }
                default : {
                    break;
                }
            }

            drive.update();
            intake.update();
            arm.update();
            turret.update();
            lift.update();
            flipper.update();
            telemetry.addData("stage", stage);
            telemetry.update();
        }
    }

    void tagToTelemetry(AprilTagDetection detection) {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x * FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y * FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z * FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }
}
