package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Claw {
    // TODO: make sure this is actually right (i am writing this without access to the robot)
    private static final double CLOSE_POSITION = 0;
    private static final double OPEN_POSITION = 1;

    Servo clawServo;

    public Claw(HardwareMap hardwareMap) {
        this.clawServo = hardwareMap.get(Servo.class, "clawServo");
    }

    public void goTo(double pos) {
        this.clawServo.setPosition(Range.scale(pos, 0, 1, CLOSE_POSITION, OPEN_POSITION));
    }

    public void open() {
        this.goTo(1);
    }

    public void close() {
        this.goTo(0);
    }
}
