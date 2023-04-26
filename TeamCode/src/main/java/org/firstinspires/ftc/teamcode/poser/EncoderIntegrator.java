package org.firstinspires.ftc.teamcode.poser;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.subsystem.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystem.Imu;
import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance2;

public class EncoderIntegrator {
    Drivetrain dt;
    Imu imu;

    // these 2 fields are the actual robot poser
    public Distance2 pos;
    public Angle yaw;

    // and these 5 are the last measured value from the relevant hardware devices
    int fl;
    int fr;
    int bl;
    int br;
    Angle imuYaw;

    // TODO: tune
    //       `MM_PER_ENCODER_TICK` should be correct, the other values are currently unused but lets tune them anyway
    public static final double MM_PER_ENCODER_TICK = (96 * Math.PI) / 537.7;
//    public static final double WHEELBASE_LENGTH = 337;
//    public static final double WHEELBASE_WIDTH = 355;
//    public static final double WHEELBASE_DIAGONAL = Math.hypot(WHEELBASE_LENGTH, WHEELBASE_WIDTH);
//    public static final double DEGREES_PER_MM = 360 / (WHEELBASE_DIAGONAL * Math.PI);

    public EncoderIntegrator(Hardware hardware, Distance2 initialPos, Angle initialYaw) {
        this.dt = hardware.dt;
        this.imu = hardware.imu;
        this.pos = initialPos;
        this.yaw = initialYaw;

        this.fl = this.dt.fl.getCurrentPosition();
        this.fr = this.dt.fr.getCurrentPosition();
        this.bl = this.dt.bl.getCurrentPosition();
        this.br = this.dt.br.getCurrentPosition();
        this.imuYaw = this.imu.getYaw();
    }

    public void update() {
        int newFl = this.dt.fl.getCurrentPosition();
        int newFr = this.dt.fr.getCurrentPosition();
        int newBl = this.dt.bl.getCurrentPosition();
        int newBr = this.dt.br.getCurrentPosition();
        Angle newImuYaw = this.imu.getYaw();

        double flDiff = newFl - this.fl;
        double frDiff = newFr - this.fr;
        double blDiff = newBl - this.bl;
        double brDiff = newBr - this.br;
        Angle imuYawDiff = newImuYaw.sub(this.imuYaw);

        // fl = powerY + powerX + turn + noop
        // fr = powerY - powerX - turn + noop
        // bl = powerY - powerX + turn - noop
        // br = powerY + powerX - turn - noop

        double relativeYDiff = ((flDiff + frDiff + blDiff + brDiff) / 4.) * MM_PER_ENCODER_TICK;
        double relativeXDiff = ((flDiff - frDiff - blDiff + brDiff) / 4.) * MM_PER_ENCODER_TICK * (52/59.);
//        double turnDiff = ((flDiff - frDiff + blDiff - brDiff) / 4.) * MM_PER_ENCODER_TICK * DEGREES_PER_MM;
//        turnDiff *= WHEELBASE_DIAGONAL / WHEELBASE_WIDTH;
//        double noopDiff = (flDiff + frDiff - blDiff - brDiff) / 4.;

        // aaaaand then we are just going to ignore `turnDiff` and `noopDiff`. there is nothing
        // meaningful we can do with these values to help correct the other values. the noop value
        // is ignored for obvious reasons. the turn value is ignored since we get the turn from the
        // imu and using encoders for turn has proven to work poorly.

        double poseExponentiationX = imuYawDiff.cosc();
        double poseExponentiationY = imuYawDiff.sinc();

        Distance2 relativePosDiff = Distance2.inMM(
                relativeYDiff * poseExponentiationX + relativeXDiff * poseExponentiationY,
                relativeYDiff * poseExponentiationY - relativeXDiff * poseExponentiationX
        );

        Distance2 posDiff = relativePosDiff.rot(this.imuYaw);
        Angle yawDiff = imuYawDiff;

        this.pos = this.pos.add(posDiff);
        this.yaw = this.yaw.add(yawDiff);

        this.fl = newFl;
        this.fr = newFr;
        this.bl = newBl;
        this.br = newBr;
        this.imuYaw = newImuYaw;
    }
}
