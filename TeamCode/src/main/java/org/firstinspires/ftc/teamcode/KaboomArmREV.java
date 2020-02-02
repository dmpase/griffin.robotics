package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class KaboomArmREV extends KaboomArms {

    DcMotor hook     =null;
    DcMotor expand   =null;
    DcMotor retract  =null;
    DcMotor swing    =null;

    protected KaboomArmREV(OpMode op) {
        super(op);
    }

    // initialize the motors
    public void init()
    {
        hook  = op_mode.hardwareMap.get(DcMotor.class, "hook");
        hook.setDirection(DcMotor.Direction.REVERSE);
        hook.setPower(0);
        hook.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hook.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        expand = op_mode.hardwareMap.get(DcMotor.class, "expand");
        expand.setDirection(DcMotor.Direction.FORWARD);
        expand.setPower(0);
        expand.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        expand.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        retract = op_mode.hardwareMap.get(DcMotor.class, "retract");
        retract.setDirection(DcMotor.Direction.FORWARD);
        retract.setPower(0);
        retract.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        retract.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        swing = op_mode.hardwareMap.get(DcMotor.class, "swing");
        swing.setDirection(DcMotor.Direction.FORWARD);
        swing.setPower(0);
        swing.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //swing.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        swing.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

       // flag  = op_mode.hardwareMap.get(Servo.class, "flag");
    }

    private final int HOOK_ENC_TOP = 23000;
    private final int HOOK_ENC_BOT = 0;
    @Override
    public void hook(boolean extend, boolean mode) {
        if (extend) {
            hook.setTargetPosition(HOOK_ENC_TOP);
            hook.setPower(1);
            while (hook.getCurrentPosition() < HOOK_ENC_TOP-500 && mode) {
                op_mode.telemetry.addData("hook:", op_mode.getRuntime());
                op_mode.telemetry.update();
            }
        } else {
            hook.setTargetPosition(HOOK_ENC_BOT);
            hook.setPower(1);
            while (hook.getCurrentPosition() > HOOK_ENC_TOP-5000 && mode) {
                op_mode.telemetry.addData("hook:", op_mode.getRuntime());
                op_mode.telemetry.update();
            }
        }
        op_mode.telemetry.addData("Hook", hook.getCurrentPosition());
    }

    private final int EXP_ENC_AUTO_TOP = 2500;
    private final int EXP_ENC_AUTO_BOT = 0;
    @Override
    public void expand_auto() {
        retract.setPower(0);
        expand.setTargetPosition(EXP_ENC_AUTO_TOP);
        expand.setPower(1);
    }

    private final int EXP_ENC_DRIVER_TOP = 4000;
    private final int EXP_ENC_DRIVER_BOT = 0;
    @Override
    public void expand_driver() {
        retract.setPower(0);
        expand.setTargetPosition(EXP_ENC_DRIVER_TOP);
        expand.setPower(1);
    }

    private final int RET_ENC_TOP =    0;
    private final int RET_ENC_BOT = -1500;
    @Override
    public void retract() {
        expand.setPower(0);
        retract.setTargetPosition(RET_ENC_BOT);
        retract.setPower(1);
    }

    private final int SWING_ENC_DELTA   = 50;
    private final int SWING_ENC_INITIAL = 0;
    private final int SWING_ENC_DELIVER = 200;
    private final int SWING_ENC_READY   = 400;
    private final int SWING_ENC_PICKUP  = 600;
    @Override
    public void swing_to(Swing position) {
        int encoder = swing.getCurrentPosition();
        op_mode.telemetry.addData("Swing", encoder+" "+position);

        double power = 0.15;

        switch (position) {
            case INIT:
                swing.setTargetPosition(SWING_ENC_INITIAL);
                break;
            case DELIVER:
                swing.setTargetPosition(SWING_ENC_DELIVER);
                break;
            case READY:
                swing.setTargetPosition(SWING_ENC_READY);
                break;
            case PICKUP:
                swing.setTargetPosition(SWING_ENC_PICKUP);
                break;
        }
        swing.setPower(power);
    }

    @Override
    public void swing(double power) {
        swing.setPower(power);
    }



    // function to cease motion
    public void stop() {

        expand.setPower(0);
        hook.setPower(0);
        swing.setPower(0);
    }
}
