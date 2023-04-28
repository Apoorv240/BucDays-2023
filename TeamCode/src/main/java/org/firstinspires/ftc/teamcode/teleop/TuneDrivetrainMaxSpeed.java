package org.firstinspires.ftc.teamcode.teleop;

import com.google.gson.JsonObject;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.poser.EncoderIntegrator;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.units.Distance2;
import org.firstinspires.ftc.teamcode.units.Speed;
import org.firstinspires.ftc.teamcode.units.Time;

import java.io.File;

@TeleOp(group = "Tune", name = "Tune Drivetrain Max Speed")
public class TuneDrivetrainMaxSpeed extends LinearOpMode {
    @Override
    public void runOpMode() {
        Hardware hardware = new Hardware(this);
        boolean prevA;

        waitForStart();

        do {
            prevA = gamepad1.a;
            telemetry.addLine("Instructions: Put the robot in a flat area where it has space to move forward for about 3-4 seconds, then press A on controller 1. The robot will move on its own when you press A.");
            telemetry.update();
        } while (!(gamepad1.a && !prevA));

        telemetry.addLine("Instructions: Wait as the calibration runs...");
        telemetry.update();

        EncoderIntegrator localizer = new EncoderIntegrator(hardware, Distance2.ZERO, Angle.ZERO);

        // accel
        Time loopStart = Time.now();
        while (true) {
            localizer.update();
            Time timePassed = Time.now().sub(loopStart);
            hardware.dt.move(0, Range.clip(timePassed.valInSeconds(), 0, 1), 0);
            if (timePassed.valInSeconds() > 1.2) {
                break;
            }
        }

        // starting snapshot
        Time startTime = Time.now();
        localizer.update();
        Distance2 startPos = localizer.pos;

        // update localizer
        loopStart = Time.now();
        while (Time.now().sub(loopStart).valInSeconds() < 1) {
            localizer.update();
        }

        Time endTime = Time.now();
        localizer.update();
        Distance2 endPos = localizer.pos;

        loopStart = Time.now();
        while (true) {
            localizer.update();
            Time timePassed = Time.now().sub(loopStart);
            hardware.dt.move(0, Range.clip(1 - timePassed.valInSeconds(), 0, 1), 0);
            if (timePassed.valInSeconds() > 1.2) {
                break;
            }
        }

        Distance displacement = endPos.sub(startPos).magnitude();
        Time elapsedTime = endTime.sub(startTime);
        Speed speedEstimate = displacement.div(elapsedTime);

        telemetry.addLine("Instructions: Wait while the configuration data is written to a file...");
        telemetry.update();

        String filename = "RobotMaxSpeedCalibration.json";
        File file = AppUtil.getInstance().getSettingsFile(filename);
        JsonObject obj = new JsonObject();
        obj.addProperty("startTime", startTime.valInSeconds());
        obj.addProperty("startPosX", startPos.x.valInMM());
        obj.addProperty("startPosY", startPos.y.valInMM());
        obj.addProperty("endTime", endTime.valInSeconds());
        obj.addProperty("endPosX", endPos.x.valInMM());
        obj.addProperty("endPosY", endPos.y.valInMM());
        obj.addProperty("speedEstimate", speedEstimate.valInMMPerSec());
        ReadWriteFile.writeFile(file, obj.toString());

        telemetry.addLine("Instructions: Calibration data written to \"" + file.getAbsolutePath() + "\", you can stop the opmode now.");
        telemetry.update();
        while (!this.isStopRequested());
    }
}
