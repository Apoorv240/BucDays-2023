package org.firstinspires.ftc.teamcode.poser;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.units.Accel;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance2;
import org.firstinspires.ftc.teamcode.units.Speed2;
import org.firstinspires.ftc.teamcode.units.Time;
import org.firstinspires.ftc.teamcode.units.Speed;
import org.firstinspires.ftc.teamcode.units.Vector2;

public class Poser {
    private Pose targetPose;
    private EncoderIntegrator localizer;
    private Drivetrain dt;

    private Time lastUpdate;
    private Time s;
    private Speed2 velocity;

    // TODO: tune
    //       most of these values are taken from the shepton robot, which has a similar wheelbase
    // pos-related
    private static final Speed ROBOT_SPEED_AT_MAX_POWER = Speed.inMMPerSec(1319.9);
    private static final Accel ACCEL_LIMIT = Accel.inMMPerSecSq(1319.9);
    private static final Time TIME_OFFSET = Time.inSeconds(0.1);
    // heading-related
    // `D` is the exponent for the ellipse. high values of `D` lead to a sudden dropoff in power just above 0. low
    // values of `D` lead to a sudden dropoff in power just under `SLOWDOWN_THRESHOLD`. `D` should never go below `1`.
    private static final double D = 1.8;
    private static final Angle SLOWDOWN_THRESHOLD = Angle.inDegrees(45);
    private static final Angle STOP_THRESHOLD = Angle.inDegrees(1.5);
    private static final double MAX_ANGLE_POWER = 0.3;

    public Poser(Hardware hardware, Pose initialPose) {
        this.targetPose = initialPose;
        this.localizer = new EncoderIntegrator(hardware, initialPose.pos, initialPose.heading);
        this.dt = hardware.dt;

        this.lastUpdate = Time.now();
        this.s = Time.ZERO;
        // if this assumption doesnt hold then the robot will attempt to violently decelerate
        // on the first call to `update`, but at least there will be no dead reckoning errors.
        this.velocity = Speed2.ZERO;
    }

    public void setTargetPose(Pose newTarget) {
        this.targetPose = newTarget;
    }

    public Pose getTargetPose() {
        return this.targetPose;
    }

    public Pose getPoseEstimate() {
        return new Pose(this.localizer.pos, this.localizer.yaw);
    }

    public boolean update() {
        Time now = Time.now();
        Time dt = now.sub(this.lastUpdate);
        this.lastUpdate = now;

        this.localizer.update();
        Pose currentPose = this.getPoseEstimate();

        // region: pos
        Distance2 posError = this.targetPose.pos.sub(currentPose.pos);
        Speed targetSpeed = Speed.inMMPerSec(
                Math.sqrt(
                        2
                        * ACCEL_LIMIT.valInMMPerSecSq()
                        * posError.magnitude().valInMM()
                )
        ).sub(ACCEL_LIMIT.mul(this.s));

        if (targetSpeed.valInMMPerSec() < 0) {
            Time ds = targetSpeed.neg().div(ACCEL_LIMIT);
            if (ds.valInNanos() < dt.valInNanos()) {
                ds = dt;
            }
            if (ds.valInNanos() > s.valInNanos()) {
                ds = s;
            }

            this.s = this.s.sub(ds);

            targetSpeed = targetSpeed.add(ACCEL_LIMIT.mul(ds));
        }

        if (targetSpeed.valInMMPerSec() > 50) {
            this.s = this.s.add(dt);

            if (this.s.valInNanos() > TIME_OFFSET.valInNanos()) {
                this.s = TIME_OFFSET;
            }
        }

        Speed2 targetVelocity = posError.normalized().mul(targetSpeed);
        Speed2 targetAccel = targetVelocity.sub(this.velocity);
        Speed accelLimit = ACCEL_LIMIT.mul(dt);
        if (targetAccel.magnitude().valInMMPerSec() > accelLimit.valInMMPerSec()) {
            targetAccel = targetAccel.normalized().mul(accelLimit);
        }
        this.velocity = this.velocity.add(targetAccel);
        Vector2 outputPosPower = this.velocity.div(ROBOT_SPEED_AT_MAX_POWER);
        boolean isPosDone = s.isZero();
        // endregion: pos

        // region: heading
        Angle headingError = this.targetPose.heading.sub(currentPose.heading);

        double outputTurnPower = MAX_ANGLE_POWER * Math.signum(headingError.valInRadians());
        if (Math.abs(headingError.valInRadians()) < SLOWDOWN_THRESHOLD.valInRadians()) {
            outputTurnPower *= Math.pow(
                    1 - Math.pow(
                            1 - (Math.abs(headingError.valInRadians()) / SLOWDOWN_THRESHOLD.valInRadians()),
                            D
                    ),
                    1/D
            );
        }
        boolean isHeadingDone = headingError.valInRadians() < STOP_THRESHOLD.valInRadians();
        // endregion: heading

        this.dt.move(outputPosPower.x, outputPosPower.y, outputTurnPower);

        return isPosDone && isHeadingDone;
    }
}
