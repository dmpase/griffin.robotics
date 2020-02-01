package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class SkyStoneMotors {
    protected OpMode op_mode = null;

    protected SkyStoneMotors(OpMode op) {
        op_mode = op;
    }

    // functions needed for all subclasses

    // initialize the motors
    public abstract void init();

    // continuous motion (driver op mode)
    public abstract void move(double bearing, double power, double turn);

    // targeted motion (autonomous op mode)
    public abstract void move_to(double bearing, double power, double inches);
    public abstract void turn_to(double bearing, double power);

    // function to cease motion
    public abstract void stop();


    private static void sleep (long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            ;
        }
    }
}
