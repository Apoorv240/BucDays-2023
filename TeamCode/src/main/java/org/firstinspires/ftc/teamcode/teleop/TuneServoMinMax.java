package org.firstinspires.ftc.teamcode.teleop;

import com.google.gson.JsonObject;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ReadWriteFile;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.units.Time;

import java.io.File;

@TeleOp(group = "Tune", name = "Tune Servo Min/Max")
public class TuneServoMinMax extends LinearOpMode {
    private double tuneOneServo(Servo servo, String instr) {
        boolean prevA = true;
        double val = 0.5;
        Time lastUpdateTime = Time.now();

        servo.scaleRange(0, 1);
        servo.setDirection(Servo.Direction.FORWARD);

        while (true) {
            Time now = Time.now();
            Time dt = now.sub(lastUpdateTime);
            lastUpdateTime = now;

            val += -gamepad1.left_stick_y * dt.div(Time.inSeconds(3));
            val = Range.clip(val, 0, 1);

            servo.setPosition(val);
            telemetry.addLine("Instructions: Use the left stick on controller 1 to " + instr + ", then press A.");
            telemetry.addData("Servo position", val);
            telemetry.update();

            boolean a = gamepad1.a;
            if (a && !prevA) {
                break;
            }
            prevA = a;
        }

        ((PwmControl)servo).setPwmDisable();

        return val;
    }

    @Override
    public void runOpMode() {
        Hardware hardware = new Hardware(this);

        waitForStart();

        double intakeClawOpenPos = this.tuneOneServo(hardware.intake.claw, "open the claw");
        double intakeClawClosePos = this.tuneOneServo(hardware.intake.claw, "close the claw");

        double intakeElbow1UpPos = this.tuneOneServo(hardware.intake.elbow1, "lower the intake arm");
        double intakeElbow1DownPos = this.tuneOneServo(hardware.intake.elbow1, "raise the intake arm");

        double intakeElbow2UpPos = this.tuneOneServo(hardware.intake.elbow2, "lower the intake arm");
        double intakeElbow2DownPos = this.tuneOneServo(hardware.intake.elbow2, "raise the intake arm");

        double outtakeScorerHoldingPos = this.tuneOneServo(hardware.outtake.scoringServo, "move the outtake cone scorer to the \"holding a cone\" position");
        double outtakeScorerDroppedPos = this.tuneOneServo(hardware.outtake.scoringServo, "move the outtake cone scorer to the \"just dropped a cone\" position");

        double outtakeJunctionGuideUpPos = this.tuneOneServo(hardware.outtake.junctionGuide, "raise the junction guide");
        double outtakeJunctionGuideDownPos = this.tuneOneServo(hardware.outtake.junctionGuide, "lower the junction guide");

        telemetry.addLine("Instructions: Wait while the configuration data is written to a file...");
        telemetry.update();

        String filename = "RobotServoMinMaxCalibration.json";
        File file = AppUtil.getInstance().getSettingsFile(filename);
        JsonObject obj = new JsonObject();
        obj.addProperty("intakeClawOpenPos", intakeClawOpenPos);
        obj.addProperty("intakeClawClosePos", intakeClawClosePos);
        obj.addProperty("intakeElbow1UpPos", intakeElbow1UpPos);
        obj.addProperty("intakeElbow2UpPos", intakeElbow2UpPos);
        obj.addProperty("intakeElbow1DownPos", intakeElbow1DownPos);
        obj.addProperty("intakeElbow2DownPos", intakeElbow2DownPos);
        obj.addProperty("outtakeScorerHoldingPos", outtakeScorerHoldingPos);
        obj.addProperty("outtakeScorerDroppedPos", outtakeScorerDroppedPos);
        obj.addProperty("outtakeJunctionGuideUpPos", outtakeJunctionGuideUpPos);
        obj.addProperty("outtakeJunctionGuideDownPos", outtakeJunctionGuideDownPos);
        ReadWriteFile.writeFile(file, obj.toString());

        telemetry.addLine("Instructions: Calibration data written to \"" + file.getAbsolutePath() + "\", you can stop the opmode now.");
        telemetry.update();
        while (!this.isStopRequested());
    }
}
