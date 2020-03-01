package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class SkyStoneArmREV extends SkyStoneArms {

    private Servo hook       =null;
    private Servo grab       =null;

//    boolean drivemode = true; // true means autonomous

    protected SkyStoneArmREV(OpMode op) {
        super(op);
    }

    private static final String HOOK = "hook";
    private static final String GRAB = "grab";
    private static final String LIFT = "lift";

    // initialize the motors
    public void init(boolean autonomous)
    {
//        drivemode = autonomous;
        hook  = op_mode.hardwareMap.get(Servo.class, HOOK);
        if (hook != null) {
            hook.setDirection(Servo.Direction.FORWARD);
        }

        grab  = op_mode.hardwareMap.get(Servo.class, GRAB);
        if (grab != null) {
            grab.setDirection(Servo.Direction.FORWARD);
        }

        lift = op_mode.hardwareMap.get(DcMotor.class, LIFT);
        if (lift != null) {
            lift.setDirection(DcMotor.Direction.FORWARD);
            lift.setPower(0);
            lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }


    // open, store, or close the hook
    private static double HOOK_OPEN   = 0.0;
    private static double HOOK_CLOSED = 0.65;
    private static double HOOK_STORED = HOOK_OPEN;
    @Override
    public void hook(Hook position) {
        if (hook != null) {
            if (position == Hook.open) {
                hook.setPosition(HOOK_OPEN);
            } else if (position == Hook.hooked) {
                hook.setPosition(HOOK_CLOSED);
            } else { //(position == Hook.stored)
                hook.setPosition(HOOK_STORED);
            }
        }
    }

    // open or close the claw
    @Override
    public void grab(double power) {
        if (grab != null) {
            grab.setPosition(power);
        }
    }


    private static final double GRAB_OPEN   = 0.0;
    private static final double GRAB_CLOSED = 1.0;
    @Override
    public void grab(boolean clamp) {
        if (grab != null) {
            if (clamp) {    // grabbing (closed)
                grab.setPosition(GRAB_CLOSED);
            } else {        // not grabbing (open)
                grab.setPosition(GRAB_OPEN);
            }
        }
    }

    @Override
    public void lift(double power)
    {
        if (lift != null) {
            if (lift.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
                lift.setPower(0);
                lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            lift.setPower(power);
            op_mode.telemetry.addData("lift", "enc=%5d",lift.getCurrentPosition()); op_mode.telemetry.update();
        }
    }

    @Override
    public void lift_auto(boolean drop) {
    }

    @Override
    public void lift_to(int encoder, double power) {
        if (lift != null) {
            if (lift.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
                lift.setPower(0);
                lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            lift.setTargetPosition(encoder);
            lift.setPower(power);
        }
    }

    // function to cease motion
    @Override
    public void stop() {
        if (lift != null) {
            if (lift.getMode() != DcMotor.RunMode.RUN_TO_POSITION) {
                lift.setTargetPosition(0);
            }
            lift.setPower(0);
        }

        if (hook != null) {
            hook(Hook.stored); //set to stored
        }

        if (grab != null) {
            grab(true); //set to closed
        }
    }

    private static void sleep (long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            ;
        }
    }
}
