package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class SkyStoneServos {
    public OpMode op_mode = null;

    protected SkyStoneServos(OpMode op) {
        op_mode = op;
    }

    // initialize the motors
    public abstract void init();

    public abstract void rotat(boolean direction);

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
