package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    public static final double CLOSE_POSITION = 0;
    public static final double OPEN_POSITION = 1;

    Servo clawServo;

    public Claw(HardwareMap hardwareMap) {
        clawServo = hardwareMap.get(Servo.class, "clawServo");
    }

    public void goTo(double pos) {
        clawServo.setPosition(pos);
    }

    public void update() {

    }
}
