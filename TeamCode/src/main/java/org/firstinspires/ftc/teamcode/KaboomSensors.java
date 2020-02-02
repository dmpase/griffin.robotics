package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class KaboomSensors {
    protected OpMode op_mode = null;

    protected KaboomSensors(OpMode op) {
        op_mode = op;
    }

    public enum Mineral {GOLD, SILVER, NONE}
    public enum Position {LEFT, CENTER, RIGHT, NONE}

    // functions needed for all subclasses
    public static class Sensors {
        double left;
        double center;
        double right;
        double alpha;
        double red;
        double green;
        double blue;
        Mineral mineral;
    }

    // initialize the motors
    public abstract void init();

    // continuous motion (driver op mode)
    public abstract Sensors check();

    // function to cease motion
    public abstract void stop();
}
