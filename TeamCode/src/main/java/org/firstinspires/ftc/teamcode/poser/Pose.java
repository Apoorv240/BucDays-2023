package org.firstinspires.ftc.teamcode.poser;

import org.firstinspires.ftc.teamcode.units.Angle;
import org.firstinspires.ftc.teamcode.units.Distance2;

public class Pose {
    public final Distance2 pos;
    public final Angle heading;

    public Pose(Distance2 pos, Angle heading) {
        this.pos = pos;
        this.heading = heading;
    }
}
