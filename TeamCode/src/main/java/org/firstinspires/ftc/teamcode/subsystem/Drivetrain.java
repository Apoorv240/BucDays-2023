package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Drivetrain {
    // TODO: make sure this is actually right (i am writing this without access to the robot)

    public final DcMotor fl;
    public final DcMotor fr;
    public final DcMotor bl;
    public final DcMotor br;

    public Drivetrain(HardwareMap hardwareMap) {
        this.fl = hardwareMap.get(DcMotor.class, "fl");
        this.fr = hardwareMap.get(DcMotor.class, "fr");
        this.bl = hardwareMap.get(DcMotor.class, "bl");
        this.br = hardwareMap.get(DcMotor.class, "br");

        this.fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.fl.setDirection(DcMotorSimple.Direction.REVERSE);
        this.fr.setDirection(DcMotorSimple.Direction.FORWARD);
        this.bl.setDirection(DcMotorSimple.Direction.REVERSE);
        this.br.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void move(double powerX, double powerY, double turn) {
        this.fl.setPower(powerY + powerX + turn);
        this.fr.setPower(powerY - powerX - turn);
        this.bl.setPower(powerY - powerX + turn);
        this.br.setPower(powerY + powerX - turn);
    }
}
