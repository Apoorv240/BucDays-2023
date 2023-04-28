package org.firstinspires.ftc.teamcode.teleop;

import com.google.gson.JsonObject;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Hardware;

import java.io.File;

@TeleOp(group = "Tune", name = "Tune Motor Encoder Positions")
public class TuneMotorEncoderPositions extends LinearOpMode {
    @Override
    public void runOpMode() {
        Hardware hardware = new Hardware(this);
        boolean prevA;

        hardware.intake.slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        hardware.outtake.slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        hardware.outtake.turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        waitForStart();

        do {
            prevA = gamepad1.a;
            telemetry.addLine("Instructions: Move the intake slide to the default, retracted position, then press A on controller 1.");
            telemetry.update();
        } while (!(gamepad1.a && !prevA));
        hardware.intake.slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.intake.slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int intakeSlideFullyExtendedPos;
        do {
            prevA = gamepad1.a;
            intakeSlideFullyExtendedPos = hardware.intake.getSlideEncoder();
            telemetry.addLine("Instructions: Move the intake slide to the fully extended position, then press A on controller 1.");
            telemetry.addData("Encoder value", intakeSlideFullyExtendedPos);
            telemetry.update();
        } while (!(gamepad1.a && !prevA));

        do {
            prevA = gamepad1.a;
            telemetry.addLine("Instructions: Move the outtake slide to the default, retracted position, then press A on controller 1.");
            telemetry.update();
        } while (!(gamepad1.a && !prevA));
        hardware.outtake.slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.outtake.slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int outtakeSlideHighJunctionPos;
        do {
            prevA = gamepad1.a;
            outtakeSlideHighJunctionPos = hardware.outtake.getSlideEncoder();
            telemetry.addLine("Instructions: Move the outtake slide to the height of a high junction, then press A on controller 1.");
            telemetry.addData("Encoder value", outtakeSlideHighJunctionPos);
            telemetry.update();
        } while (!(gamepad1.a && !prevA));

        do {
            prevA = gamepad1.a;
            telemetry.addLine("Instructions: Rotate the turret to the default, initial position, then press A on controller 1.");
            telemetry.update();
        } while (!(gamepad1.a && !prevA));
        hardware.outtake.turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.outtake.turret.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int outtakeTurretZeroPos;
        do {
            prevA = gamepad1.a;
            outtakeTurretZeroPos = hardware.outtake.getTurretEncoder();
            telemetry.addLine("Instructions: Rotate the turret to face backward, then press A on controller 1.");
            telemetry.addData("Encoder value", outtakeTurretZeroPos);
            telemetry.update();
        } while (!(gamepad1.a && !prevA));

        telemetry.addLine("Instructions: Wait while the configuration data is written to a file...");
        telemetry.update();

        String filename = "RobotMotorEncoderCalibration.json";
        File file = AppUtil.getInstance().getSettingsFile(filename);
        JsonObject obj = new JsonObject();
        obj.addProperty("intakeSlideFullyExtendedPos", intakeSlideFullyExtendedPos);
        obj.addProperty("outtakeSlideHighJunctionPos", outtakeSlideHighJunctionPos);
        obj.addProperty("outtakeTurretZeroPos", outtakeTurretZeroPos);
        ReadWriteFile.writeFile(file, obj.toString());

        telemetry.addLine("Instructions: Calibration data written to \"" + file.getAbsolutePath() + "\", you can stop the opmode now.");
        telemetry.update();
        while (!this.isStopRequested());
    }
}
