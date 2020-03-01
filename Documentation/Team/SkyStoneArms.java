package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public abstract class SkyStoneArms {
    public OpMode op_mode = null;

    protected SkyStoneArms(OpMode op) {
        op_mode = op;
    }

    public DcMotor lift = null;

    // initialize the motors
    public abstract void init(boolean autonomous);

    // continuous motion (driver op mode)
    enum Hook {stored, hooked, open}

    public abstract void hook(Hook position);

    public abstract void lift(double power);

    public abstract void lift_auto(boolean drop);

    public abstract void lift_to(int encoder, double power);

    public abstract void grab(boolean clamp);

    public abstract void grab(double power);


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
