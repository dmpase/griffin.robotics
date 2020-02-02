package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class KaboomArms {
    public OpMode op_mode = null;

    protected KaboomArms(OpMode op) {
        op_mode = op;
    }

    // initialize the motors
    public abstract void init();

    // continuous motion (driver op mode)
    public abstract void hook(boolean extend, boolean mode);

    public abstract void expand_auto();

    public abstract void expand_driver();

    public abstract void retract();

    enum Swing {INIT, DELIVER, READY, PICKUP}

    public abstract void swing(double power);

    public abstract void swing_to(Swing position);

    // function to cease motion
    public abstract void stop();
}
