package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class SkyStoneSensors {
    protected OpMode op_mode = null;

    protected SkyStoneSensors(OpMode op) {
        op_mode = op;
    }

    public static class Sensors {
        public double  front      = 0;
        public boolean front_valid = false;

        public double port_left   = 0;          // left sensor distance to wall (inches)
        public double port_right  = 0;          // right sensor distance to wall (inches)
        public double port_min    = 0;          // minimum distance to wall in inches
        public double port_angle  = 0;          // angle to wall in degrees
        public boolean port_valid = false;

        public double stbd_left   = 0;          // left sensor distance to wall (inches)
        public double stbd_right  = 0;          // right sensor distance to wall (inches)
        public double stbd_min    = 0;          // minimum distance to wall in inches
        public double stbd_angle  = 0;          // angle to wall in degrees
        public boolean stbd_valid = false;

        public double back_left   = 0;          // left sensor distance to wall (inches)
        public double back_right  = 0;          // right sensor distance to wall (inches)
        public double back_min    = 0;          // minimum distance to wall in inches
        public double back_angle  = 0;          // angle to wall in degrees
        public boolean back_valid = false;

        public double red         = 0;
        public double green       = 0;
        public double blue        = 0;
        public boolean color_valid = false;

        public boolean red() {
            return 0.5 <= red;
        }

        public boolean green() {
            return 0.5 <= green;
        }

        public boolean blue() {
            return 0.5 <= blue;
        }
    }

    // functions needed for all subclasses

    // initialize the sensors
    public abstract void init();

    // check sensors
    public abstract Sensors check(boolean frnt, boolean port, boolean stbd, boolean back, boolean down);

    // stop checking sensors
    public abstract void stop();

    public static void sleep (long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            ;
        }
    }
}
