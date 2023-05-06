package org.firstinspires.ftc.teamcode.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;

@Config
public class OuttakeSlideController {
    private final Outtake outtake;
    private double targetSlidePosition;
    private Angle targetTurretAngle;

    private static final double TURRET_ENCODER_TICKS_PER_REV = 537.7;
    private static final double TURRET_ENCODER_TICKS_PER_RADIAN = TURRET_ENCODER_TICKS_PER_REV / Angle.FULL_REV.valInRadians();

    private static double MAX_SLIDE_POWER = 0.5;
    private static double SLIDE_SLOWDOWN_THRESHOLD = 50;
    private static double SLIDE_STOP_THRESHOLD = 5;
    private static double SLIDE_D = 1.8;
    private static double MAX_TURRET_POWER = 1;
    private static double TURRET_SLOWDOWN_THRESHOLD = Angle.inDegrees(10).valInRadians() * TURRET_ENCODER_TICKS_PER_RADIAN;
    private static double TURRET_STOP_THRESHOLD = Angle.inDegrees(1).valInRadians() * TURRET_ENCODER_TICKS_PER_RADIAN;
    private static double TURRET_D = 1.8;

    public OuttakeSlideController(Outtake outtake) {
        this.outtake = outtake;
        this.targetSlidePosition = 0;
        this.targetTurretAngle = Angle.ZERO;
    }

    public void setTargetSlidePosition(double targetSlidePosition) {
        this.targetSlidePosition = targetSlidePosition;
    }

    public void setTargetSlidePosition(Distance distance) {
        this.setTargetSlidePosition(distance.div(Intake.SLIDE_EXTENSION_PER_ENCODER_TICK));
    }

    public void setTargetTurretAngle(Angle targetTurretAngle) {
        this.targetTurretAngle = Angle.inRadians(Range.clip(
                targetTurretAngle.valInRadians(),
                Outtake.TURRET_MIN_ANGLE.valInRadians(),
                Outtake.TURRET_MAX_ANGLE.valInRadians()
        ));
    }

    public boolean update() {
        int currentSlidePosition = this.outtake.getSlideEncoder();
        int currentTurretPosition = this.outtake.getTurretEncoder();

        double targetTurretPosition = this.targetTurretAngle.valInRadians() * TURRET_ENCODER_TICKS_PER_RADIAN;

        double slideError = this.targetSlidePosition - currentSlidePosition;
        double turretError = targetTurretPosition - currentTurretPosition;

        double outputSlidePower = MAX_SLIDE_POWER * Math.signum(slideError);
        if (Math.abs(slideError) < SLIDE_SLOWDOWN_THRESHOLD) {
            outputSlidePower *= Math.pow(
                    1 - Math.pow(
                            1 - (Math.abs(slideError) / SLIDE_SLOWDOWN_THRESHOLD),
                            SLIDE_D
                    ),
                    1/SLIDE_D
            );
        }
        boolean isSlideDone = Math.abs(slideError) < SLIDE_STOP_THRESHOLD;

        double outputTurretPower = MAX_TURRET_POWER * Math.signum(turretError);
        if (Math.abs(turretError) < TURRET_SLOWDOWN_THRESHOLD) {
            outputTurretPower *= Math.pow(
                    1 - Math.pow(
                            1 - (Math.abs(turretError) / TURRET_SLOWDOWN_THRESHOLD),
                            TURRET_D
                    ),
                    1/TURRET_D
            );
        }
        boolean isTurretDone = Math.abs(turretError) < TURRET_STOP_THRESHOLD;

        this.outtake.setSlidePower(outputSlidePower);
        this.outtake.setTurretPower(outputTurretPower);

        return isSlideDone && isTurretDone;
    }
}
