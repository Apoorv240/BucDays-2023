package org.firstinspires.ftc.teamcode.subsystem;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;

@Config
public class IntakeSlideController {
    private final Intake intake;
    private double targetSlidePosition;

    private static double MAX_SLIDE_POWER = 0.5;
    private static double SLOWDOWN_THRESHOLD = 50;
    private static double STOP_THRESHOLD = 5;
    private static double D = 1.8;

    public IntakeSlideController(Intake intake) {
        this.intake = intake;
        this.targetSlidePosition = 0;
    }

    public void setTargetSlidePosition(double targetSlidePosition) {
        this.targetSlidePosition = targetSlidePosition;
    }

    public void setTargetSlidePosition(Distance distance) {
        this.setTargetSlidePosition(distance.div(Intake.SLIDE_EXTENSION_PER_ENCODER_TICK));
    }

    public boolean update() {
        int currentSlidePosition = this.intake.getSlideEncoder();
        double error = this.targetSlidePosition - currentSlidePosition;

        double outputSlidePower = MAX_SLIDE_POWER * Math.signum(error);
        if (Math.abs(error) < SLOWDOWN_THRESHOLD) {
            outputSlidePower *= Math.pow(
                    1 - Math.pow(
                            1 - (Math.abs(error) / SLOWDOWN_THRESHOLD),
                            D
                    ),
                    1/D
            );
        }
        boolean isSlideDone = Math.abs(error) < STOP_THRESHOLD;

        this.intake.setSlidePower(outputSlidePower);

        return isSlideDone;
    }
}
