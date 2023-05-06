package org.firstinspires.ftc.teamcode.poser;

import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance;
import org.firstinspires.ftc.teamcode.units.Distance2;

public class Pose {
    public final Distance2 pos;
    public final Angle heading;

    /**
     * In tile A2, up against the wall, with the right edge of the robot aligned with the
     * edge of the tile, and facing away from the alliance station.
     */
    public static final Pose INITIAL_POSE_A2_AUTO = new Pose(
            Distance2.inTiles(-1.5, -2.5).add(new Distance2(
                    Distance.ONE_TILE_WITHOUT_BORDER.sub(Distance.ROBOT_LENGTH).div(2).neg(),
                    Distance.ONE_TILE_WITHOUT_BORDER.sub(Distance.ROBOT_WIDTH).div(2)
            )),
            Angle.ZERO
    );

    public Pose(Distance2 pos, Angle heading) {
        this.pos = pos;
        this.heading = heading;
    }
}
