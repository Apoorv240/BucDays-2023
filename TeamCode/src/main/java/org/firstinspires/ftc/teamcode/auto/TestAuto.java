package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.poser.Pose;
import org.firstinspires.ftc.teamcode.poser.Poser;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance2;

@Autonomous(name = "Test Auto")
public class TestAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
        Pose a = new Pose(Distance2.ZERO, Angle.ZERO);
        Pose b = new Pose(Distance2.inTiles(2, 0), Angle.ZERO);

        Hardware hardware = new Hardware(this);
        Poser poser = new Poser(hardware, a, 0.7);
        poser.setTargetPose(b);

        int state = 0;
        while (!this.isStopRequested()) {
            switch (state) {
                case 0:
                    if (poser.update()) {
                        poser.setTargetPose(a);
                        state = 1;
                    }
                    break;
                case 1:
                    if (poser.update()) {
                        poser.setTargetPose(b);
                        state = 0;
                    }
                    break;
                default:
                    throw new RuntimeException("unexpected state: found " + state);
            }
        }
    }
}
