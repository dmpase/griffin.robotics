package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class SkyStoneHolonomic extends SkyStoneMotors {
    private final double CLICKS_PER_INCH;       // conversion from inches to encoder clicks
    private final double INCH_THRESHHOLD;       // max distance tolerance when moving to location
    private final double CLICKS_PER_DEGREE;     // conversion from degrees turn to encoder clicks
    private final double DEGREE_THRESHHOLD;     // max angle tolerance when moving to a bearing
    private final double DEGREES_DRIFT;         // deviation in degrees bearing from true course

    // class constructor for the motors, must include:
    // (a) op:                "this" pointer to give motors access to FTC software
    // (b) clicks_per_inch:   encoder clicks per inch for this platform
    // (c) clicks_per_degree: encoder clicks per degree for this platform
    // (d) degrees_drift:     relative drift (in degrees) to starboard for this platform
    public SkyStoneHolonomic(OpMode op, double clicks_per_inch, double clicks_per_degree, double degrees_drift) {
        super(op);

        CLICKS_PER_INCH   = clicks_per_inch;
        INCH_THRESHHOLD   = 1.5 * CLICKS_PER_INCH;
        CLICKS_PER_DEGREE = clicks_per_degree;
        DEGREE_THRESHHOLD = 10 * CLICKS_PER_DEGREE;
        DEGREES_DRIFT     = degrees_drift;
    }

    // these are the motors we will use in our kaboom drive
    private DcMotor front_left  = null;
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;

    // these are the names of our motors as found in the robot configuration file
    private static final String FRONT_LEFT_MOTOR  = "front left";
    private static final String FRONT_RIGHT_MOTOR = "front right";
    private static final String BACK_LEFT_MOTOR   = "back left";
    private static final String BACK_RIGHT_MOTOR  = "back right";

    // initialize the drives:
    // (a) look them up in the robot configuration file
    // (b) set the direction to FORWARD
    // (c) reset the encoders
    // (d) set the mode to continuous running
    @Override
    public void init() {
        // init the front port_left motor
        front_left = op_mode.hardwareMap.get(DcMotor.class, FRONT_LEFT_MOTOR);
        front_left.setDirection(DcMotor.Direction.FORWARD);
        front_left.setPower(0);
        front_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_left.setTargetPosition(0);
        front_left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        op_mode.telemetry.addData("motors", "front port_left init ");
        op_mode.telemetry.update();


        // init the front stbd_left motor
        front_right = op_mode.hardwareMap.get(DcMotor.class, FRONT_RIGHT_MOTOR);
        front_right.setDirection(DcMotor.Direction.FORWARD);
        front_right.setPower(0);
        front_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_right.setTargetPosition(0);
        front_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        op_mode.telemetry.addData("motors", "front stbd_left init ");
        op_mode.telemetry.update();

        // init the port_right port_left motor
        back_left = op_mode.hardwareMap.get(DcMotor.class, BACK_LEFT_MOTOR);
        back_left.setDirection(DcMotor.Direction.FORWARD);
        back_left.setPower(0);
        back_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_left.setTargetPosition(0);
        back_left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        op_mode.telemetry.addData("motors", "port_right port_left init ");
        op_mode.telemetry.update();

        // init the port_right stbd_left motor
        back_right = op_mode.hardwareMap.get(DcMotor.class, BACK_RIGHT_MOTOR);
        back_right.setDirection(DcMotor.Direction.FORWARD);
        back_right.setPower(0);
        back_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_right.setTargetPosition(0);
        back_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        op_mode.telemetry.addData("motors", "port_right stbd_left init ");
        op_mode.telemetry.update();
    }

    public static final double max_power = 1.0;


    // get the robot moving at a given direction, power and turn rate
    @Override
    public void move(double bearing, double speed, double turn) {
        // convert bearing in degrees to an angle in radians (for the math functions)
        double radians = (bearing + 45 + DEGREES_DRIFT)*Math.PI/180;

        // compute the power for each wheel
        double fl = -Math.sin(radians)*speed + turn;
        double fr =  Math.cos(radians)*speed + turn;
        double br =  Math.sin(radians)*speed + turn;
        double bl = -Math.cos(radians)*speed + turn;

        // make sure that  -max power < power < max power
        fl = Math.max(-max_power, Math.min(max_power, fl));
        fr = Math.max(-max_power, Math.min(max_power, fr));
        bl = Math.max(-max_power, Math.min(max_power, bl));
        br = Math.max(-max_power, Math.min(max_power, br));

        // tell the driver what's going on
        op_mode.telemetry.addData("move", "br:%7.2f rd:%7.2f", bearing, radians);
        op_mode.telemetry.addData("move", "sp:%7.2f tn:%7.2f", speed, turn);
        op_mode.telemetry.addData("move", "fl:%7.2f fr:%7.2f", fl, fr);
        op_mode.telemetry.addData("move", "bl:%7.2f br:%7.2f", bl, br);

        // set the mode for continuous running...
        // the time required to set the mode has changed between RoverRuckus and SkyStone,
        // causing SkyStone to delay longer when this routine is called.
        // the delay causes the robot to lurch about unevenly, and move like a drunken sailor.
        // getMode() is still fast, though, so it's used to create a guard around the setMode call.
        // if the mode is already set correctly, we don't take the time to reset the mode.
        if (front_left.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            front_left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if (front_right.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            front_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if (back_left.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            back_left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if (back_right.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
            back_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        // set the power and GO!
        front_left.setPower(fl);
        front_right.setPower(fr);
        back_left.setPower(bl);
        back_right.setPower(br);
    }

    // kill the power to all motors
    @Override
    public void stop() {
        front_left.setPower(0);
        front_right.setPower(0);
        back_left.setPower(0);
        back_right.setPower(0);
    }


    // move to a given distance in a given direction
    @Override
    public void move_to(double bearing, double power, double inches) {
        // convert our target distance from inches to clicks
        int target = (int) (inches * CLICKS_PER_INCH);

        // convert bearing in degrees to an angle in radians (for the math functions)
        double radians = (bearing + 45 + DEGREES_DRIFT)*Math.PI/180;

        // compute the encoder targets for each motor
        double front_left_target  = -Math.sin(radians)*target;
        double front_right_target =  Math.cos(radians)*target;
        double back_right_target  =  Math.sin(radians)*target;
        double back_left_target   = -Math.cos(radians)*target;

        // find the max of all four encoder targets
        double m = Math.abs(front_left_target);
        m = Math.max(m, Math.abs(front_right_target));
        m = Math.max(m, Math.abs(back_left_target));
        m = Math.max(m, Math.abs(back_right_target));

        // compute the power to each motor
        // max power goes to the most distant target,
        // others are scaled to maintain desired track
        double front_left_power  = power*(front_left_target)/m;
        double front_right_power = power*(front_right_target)/m;
        double back_right_power  = power*(back_right_target)/m;
        double back_left_power   = power*(back_left_target)/m;

        // set the encoder targets and power
        op_mode.telemetry.addData("motors", "before set target power");
        op_mode.telemetry.update();

        // used to work for RoverRuckus but doesn't work well for SkyStone...
        // makes the robot move like a drunken sailor
        // problem is LIKELY that timings have changed on setting motor modes, so
        // interleaved solution (implemented below) is needed
//        set_target_power(front_left,  (int) front_left_target,  front_left_power);
//        set_target_power(front_right, (int) front_right_target, front_right_power);
//        set_target_power(back_left,   (int) back_left_target,   back_left_power);
//        set_target_power(back_right,  (int) back_right_target,  back_right_power);

        // kill power to all motors while the modes are reset
        op_mode.telemetry.addData("move_to", "set power to zero");
        op_mode.telemetry.update();
        front_left.setPower(0);
        front_right.setPower(0);
        back_left.setPower(0);
        back_right.setPower(0);

        // reset all encoder values to zero
        op_mode.telemetry.addData("move_to", "set mode to stop_and_reset_encoder");
        op_mode.telemetry.update();
        front_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // set the new encoder target values
        op_mode.telemetry.addData("move_to", "target: %4d %4d %4d %4d", (int)front_left_target, (int)front_right_target, (int)back_right_target, (int)back_left_target);
        op_mode.telemetry.update();
        front_left.setTargetPosition((int)front_left_target);
        front_right.setTargetPosition((int)front_right_target);
        back_left.setTargetPosition((int)back_left_target);
        back_right.setTargetPosition((int)back_right_target);

        // set the motor mode to run to the new encoder target value
        op_mode.telemetry.addData("move_to", "set mode to run_to_position");
        op_mode.telemetry.update();
        front_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        front_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        back_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        back_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // set the poer for each motor
        op_mode.telemetry.addData("move_to", "power: %5.2f %5.2f %5.2f %5.2f", front_left_power, front_right_power, back_right_power, back_left_power);
        op_mode.telemetry.update();
        front_left.setPower(front_left_power);
        front_right.setPower(front_right_power);
        back_left.setPower(back_left_power);
        back_right.setPower(back_right_power);

        op_mode.telemetry.addData("move_to", "after set target power");
        op_mode.telemetry.update();

        // wait until the motors are within an acceptable distance of the target location
        // or the driver hits the "stop" button
        double distance = 0;
        int i=0;
        do {
            double flcp = front_left .getCurrentPosition();
            double frcp = front_right.getCurrentPosition();
            double blcp = back_left  .getCurrentPosition();
            double brcp = back_right .getCurrentPosition();

            double fld = front_left_target  - flcp;
            double frd = front_right_target - frcp;
            double bld = back_left_target   - blcp;
            double brd = back_right_target  - brcp;

            // how far is the robot from its target?
            distance = Math.sqrt(fld * fld + frd * frd + bld * bld + brd * brd);

            // tell the driver what's going on
            op_mode.telemetry.addData("move_to", "FL: t=%7.0f c=%7.0f d=%7.0f", front_left_target,  flcp, fld);
            op_mode.telemetry.addData("move_to", "FR: t=%7.0f c=%7.0f d=%7.0f", front_right_target, frcp, frd);
            op_mode.telemetry.addData("move_to", "BR: t=%7.0f c=%7.0f d=%7.0f", back_right_target,  brcp, brd);
            op_mode.telemetry.addData("move_to", "BL: t=%7.0f c=%7.0f d=%7.0f", back_left_target,   blcp, bld);
            op_mode.telemetry.addData("move_to", "i=%03d distance=%7.0f", i++, distance);
            op_mode.telemetry.update();

        } while (INCH_THRESHHOLD < distance && ((LinearOpMode) op_mode).opModeIsActive());

        op_mode.telemetry.addData("", "");
        op_mode.telemetry.update();

        // we're there, so kill power to the motors
        front_left.setPower(0);
        front_right.setPower(0);
        back_left.setPower(0);
        back_right.setPower(0);
    }


    // turn to a given relative bearing
    @Override
    public void turn_to(double angle, double power) {
        // convert the target bearing in degrees to an angle in radians (for the math functions)
        int target = (int) (-angle * CLICKS_PER_DEGREE);

        // set the encoder targets and power
//        set_target_power(front_left,  target, power);
//        set_target_power(front_right, target, power);
//        set_target_power(back_left,   target, power);
//        set_target_power(back_right,  target, power);

        op_mode.telemetry.addData("turn_to", "set power to zero");
        op_mode.telemetry.update();
        front_left.setPower(0);
        front_right.setPower(0);
        back_left.setPower(0);
        back_right.setPower(0);

        op_mode.telemetry.addData("turn_to", "set encoder value to zero");
        op_mode.telemetry.update();
        front_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        op_mode.telemetry.addData("turn_to", "set target to %d", target);
        op_mode.telemetry.update();
        front_left.setTargetPosition(target);
        front_right.setTargetPosition(target);
        back_left.setTargetPosition(target);
        back_right.setTargetPosition(target);

        op_mode.telemetry.addData("turn_to", "set mode to run_to_position");
        op_mode.telemetry.update();
        front_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        front_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        back_left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        back_right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        op_mode.telemetry.addData("turn_to", "set power to %5.2f", power);
        op_mode.telemetry.update();
        front_left.setPower(power);
        front_right.setPower(power);
        back_left.setPower(power);
        back_right.setPower(power);

        // tell the driver what's happening
        op_mode.telemetry.addData("turn_to", "an:%7.2f tg:%7.2f", (double) angle, (double) target);
        op_mode.telemetry.addData("turn_to", "fl:%7.2f fr:%7.2f", (double) target, (double) target);
        op_mode.telemetry.addData("turn_to", "bl:%7.2f br:%7.2f", (double) target, (double) target);
        op_mode.telemetry.update();

        // wait until we reach our target bearing
        double distance = 0;
        int i=0;
        do {
            double fld = front_left.getTargetPosition()  - front_left.getCurrentPosition();
            double frd = front_right.getTargetPosition() - front_right.getCurrentPosition();
            double bld = back_left.getTargetPosition()   - back_left.getCurrentPosition();
            double brd = back_right.getTargetPosition()  - back_right.getCurrentPosition();

            distance = Math.sqrt(fld * fld + frd * frd + bld * bld + brd * brd);

            op_mode.telemetry.addData("turn_to", "fl:%7.2f fr:%7.2f", (double) fld, (double) frd);
            op_mode.telemetry.addData("turn_to", "bl:%7.2f br:%7.2f", (double) brd, (double) brd);
            op_mode.telemetry.addData("turn_to", "i=%03d distance=%7.0f", i++, distance);
            op_mode.telemetry.update();

        } while (DEGREE_THRESHHOLD < distance && ((LinearOpMode) op_mode).opModeIsActive());

        // we're there, so kill all power to the motors
        front_left.setPower(0);
        front_right.setPower(0);
        back_left.setPower(0);
        back_right.setPower(0);
    }

    // reset the power, encoder value and mode

    // set the power, encoder target and mode
    private void set_target_power(DcMotor motor, int target, double power) {
        motor.setPower(0);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(target);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(power);
    }
}
