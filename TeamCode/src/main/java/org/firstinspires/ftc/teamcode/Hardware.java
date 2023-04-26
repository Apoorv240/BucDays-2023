package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystem.Claw;
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystem.Imu;
import org.firstinspires.ftc.teamcode.subsystem.Intake;
import org.firstinspires.ftc.teamcode.subsystem.Outtake;

public class Hardware {
    public final Drivetrain dt;
    public final Claw claw;
    public final Intake intake;
    public final Outtake outtake;
    public final Imu imu;

    public final OpMode opMode;
    public final DashboardTelemetryWrapper dashboardTelemetry;

    public Hardware(OpMode opMode) {
        this.opMode = opMode;
        this.dashboardTelemetry = new DashboardTelemetryWrapper(FtcDashboard.getInstance());
        opMode.telemetry = new MultipleTelemetry(opMode.telemetry, this.dashboardTelemetry);

        this.dt = new Drivetrain(opMode.hardwareMap);
        this.claw = new Claw(opMode.hardwareMap);
        this.intake = new Intake(opMode.hardwareMap);
        this.outtake = new Outtake(opMode.hardwareMap);
        this.imu = new Imu(opMode.hardwareMap);
    }
}
