package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.units.Angle;

public class Imu {
    // TODO: make sure this is actually right (i am writing this without access to the robot)

    private IMU imu;

    public Imu(HardwareMap hardwareMap) {
        this.imu = hardwareMap.get(IMU.class, "imu");
        this.imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.UP
        )));
        this.imu.resetYaw();
    }

    public Angle getYaw() {
        // `Angle` is stored in radians but `YawPitchRollAngles` is stored in degrees so we are doing a conversion either way
        // negative sign b/c we choose to make cw + and ccw -
        return Angle.inDegrees(-this.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
    }
}
