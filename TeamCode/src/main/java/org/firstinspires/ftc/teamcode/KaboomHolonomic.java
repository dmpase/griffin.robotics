package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class KaboomHolonomic extends KaboomMotors {
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
    public KaboomHolonomic(OpMode op, double clicks_per_inch, double clicks_per_degree, double degrees_drift) {
        super(op);

        CLICKS_PER_INCH   = clicks_per_inch;
        INCH_THRESHHOLD   = 1.5 * CLICKS_PER_INCH;
        CLICKS_PER_DEGREE = clicks_per_degree;
        DEGREE_THRESHHOLD = 2.5 * CLICKS_PER_DEGREE;
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
        // init the front left motor
        front_left = op_mode.hardwareMap.get(DcMotor.class, FRONT_LEFT_MOTOR);
        front_left.setDirection(DcMotor.Direction.FORWARD);
        reset_mode(front_left, DcMotor.RunMode.RUN_USING_ENCODER);

        // init the front right motor
        front_right = op_mode.hardwareMap.get(DcMotor.class, FRONT_RIGHT_MOTOR);
        front_right.setDirection(DcMotor.Direction.FORWARD);
        reset_mode(front_right, DcMotor.RunMode.RUN_USING_ENCODER);

        // init the back left motor
        back_left = op_mode.hardwareMap.get(DcMotor.class, BACK_LEFT_MOTOR);
        back_left.setDirection(DcMotor.Direction.FORWARD);
        reset_mode(back_left, DcMotor.RunMode.RUN_USING_ENCODER);

        // init the back right motor
        back_right = op_mode.hardwareMap.get(DcMotor.class, BACK_RIGHT_MOTOR);
        back_right.setDirection(DcMotor.Direction.FORWARD);
        reset_mode(back_right, DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public static final double max_power = 0.5;


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

        // set the mode for continuous running
        front_left .setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        front_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back_left  .setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        back_right .setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
        double fl = -Math.sin(radians)*target;
        double fr =  Math.cos(radians)*target;
        double br =  Math.sin(radians)*target;
        double bl = -Math.cos(radians)*target;

        double m = Math.abs(fl);
        m = Math.max(m, Math.abs(fr));
        m = Math.max(m, Math.abs(bl));
        m = Math.max(m, Math.abs(br));

        double pfl = power*Math.abs(fl)/m;
        double pfr = power*Math.abs(fr)/m;
        double pbr = power*Math.abs(br)/m;
        double pbl = power*Math.abs(bl)/m;

        // set the encoder targets and power
        set_target_power(front_left,  (int) fl, pfl);
        set_target_power(front_right, (int) fr, pfr);
        set_target_power(back_left,   (int) bl, pbl);
        set_target_power(back_right,  (int) br, pbr);

        // wait until the motors are within an acceptable distance of the target location
        // or the driver hits the "stop" button
        double distance = 0;
        do {
            // how far is each motor from its target?
            double fld = front_left.getTargetPosition()  - front_left.getCurrentPosition();
            double frd = front_right.getTargetPosition() - front_right.getCurrentPosition();
            double bld = back_left.getTargetPosition()   - back_left.getCurrentPosition();
            double brd = back_right.getTargetPosition()  - back_right.getCurrentPosition();

            // how far is the robot from its target?
            distance = Math.sqrt(fld * fld + frd * frd + bld * bld + brd * brd);

            // tell the driver what's going on
            op_mode.telemetry.addData("move", "br:%7.2f rd:%7.2f di:%7.2f tg:%7.2f", 
		bearing, radians, (double) inches, (double) target);
            op_mode.telemetry.addData("move", "fl:%7.2f fr:%7.2f bl:%7.2f br:%7.2f", fl, fr, bl, br);
            op_mode.telemetry.addData("move", "fl:%7.2f fr:%7.2f bl:%7.2f br:%7.2f", fld, frd, bld, brd);
            op_mode.telemetry.addData("move", "en:%7.2f", distance);
            op_mode.telemetry.update();
        } while (INCH_THRESHHOLD < distance);

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
        set_target_power(front_left,  target, power);
        set_target_power(front_right, target, power);
        set_target_power(back_left,   target, power);
        set_target_power(back_right,  target, power);

        // tell the driver what's happening
        op_mode.telemetry.addData("move", "an:%7.2f tg:%7.2f", (double) angle, (double) target);
        op_mode.telemetry.addData("move", "fl:%7.2f fr:%7.2f", (double) target, (double) target);
        op_mode.telemetry.addData("move", "bl:%7.2f br:%7.2f", (double) target, (double) target);
        op_mode.telemetry.update();

        // wait until we reach our target bearing
        double distance = 0;
        do {
            double fld = front_left.getTargetPosition()  - front_left.getCurrentPosition();
            double frd = front_right.getTargetPosition() - front_right.getCurrentPosition();
            double bld = back_left.getTargetPosition()   - back_left.getCurrentPosition();
            double brd = back_right.getTargetPosition()  - back_right.getCurrentPosition();

            distance = Math.sqrt(fld * fld + frd * frd + bld * bld + brd * brd);
        } while (DEGREE_THRESHHOLD < distance && ((LinearOpMode) op_mode).opModeIsActive());

        // we're there, so kill all power to the motors
        front_left.setPower(0);
        front_right.setPower(0);
        back_left.setPower(0);
        back_right.setPower(0);
    }

    // reset the power, encoder value and mode
    private void reset_mode(DcMotor motor, DcMotor.RunMode mode) {
        motor.setPower(0);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(mode);
    }

    // set the power, encoder target and mode
    private void set_target_power(DcMotor motor, int target, double power) {
        reset_mode(motor, DcMotor.RunMode.RUN_TO_POSITION);
        motor.setTargetPosition(target);
        motor.setPower(power);
    }
}
