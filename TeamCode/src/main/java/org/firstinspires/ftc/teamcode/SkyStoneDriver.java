package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="SkyStone Driver", group="Nex+Gen Griffins")
@Disabled
public abstract class SkyStoneDriver extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    SkyStoneMotors motors = null;
    SkyStoneArms   arms   = null;

    double lift_high_power_limit = 0.30;    // test value
    double lift_low_power_limit  = 0.075;   // test value

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Starting SkyStoneDriver Initialization.");
        telemetry.update();

        motors.init();
        arms.init(false);

        if (DeviceFinder.exists(this,"OwO")) {
            lift_high_power_limit = 0.30;
        } else if (DeviceFinder.exists(this,"UwU")) {
            lift_high_power_limit = 0.07;
        } else {
            lift_high_power_limit = 0.15;
        }

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "SkyStoneDriver Initialization Complete.");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // get the throttle command from the driver by reading the gamepad
        // right (slow) takes priority over dpad (precise) or left (fast),
        // dpad takes priority over left.
        double fast_x =   gamepad1.left_stick_x;
        double fast_y = - gamepad1.left_stick_y;

        double slow_x = 0.35 * gamepad1.right_stick_x;
        double slow_y = 0.35 * -gamepad1.right_stick_y;

        double x = fast_x;
        double y = fast_y;
        if (slow_x != 0 || slow_y != 0) {
            x = slow_x;
            y = slow_y;
        } else if (gamepad1.dpad_left) {
            x = -0.20;
            y = 0;
        } else if (gamepad1.dpad_right) {
            x = 0.20;
            y = 0;
        } else if (gamepad1.dpad_up) {
            x = 0;
            y = 0.20;
        } else if (gamepad1.dpad_down) {
            x = 0;
            y = -0.20;
        }

        // convert the command to a bearing relative to the robot
        double alpha   = Math.atan2(y,x);
        double bearing = 90 - alpha * 180 / Math.PI;

        // figure out the driver's desired speed (make sure it is less than 1.0)
        double length = Math.sqrt(x*x + y*y);
        double speed  = Math.min(length, 1.0);

        // tell the driver what's happening
        telemetry.addData("loop: ", "br:%7.2f ln:%7.2f x:%7.2f y:%7.2f", bearing, length, x, y);

        // get the turn rate from the triggers or bumpers
        double turn   = 0;
        boolean left  = (0 < gamepad1.left_trigger) || gamepad1.left_bumper;
        boolean right = (0 < gamepad1.right_trigger) || gamepad1.right_bumper;

        if ((left && right) || (!left && !right)) {
            ;           // do nothing
        } else if (left) {
            if (gamepad1.left_bumper) {
                turn = 0.25;
            } else {
                turn = 0.75 * gamepad1.left_trigger;
            }
        } else if (right) {
            if (gamepad1.right_bumper) {
                turn = - 0.25;
            } else {
                turn = - 0.75 * gamepad1.right_trigger;
            }
        }

        // send the driver commands to the motors
        motors.move(bearing, speed, turn);

        // open or close the claw
        if (gamepad2.a && gamepad2.b) {
            ;                          // do nothing
        } else if (gamepad2.a){
            arms.grab(false);   // open
        } else if (gamepad2.b) {
            arms.grab(true);    // close
        } else {
            ;                          // do nothing
        }

        // raise or lower the arm
        telemetry.addData("lift power", "= "+lift_high_power_limit);
        if (gamepad2.right_trigger > 0 && gamepad2.left_trigger > 0) {
            arms.lift(0);
        } else if (gamepad2.right_trigger > 0) {
            arms.lift(-gamepad2.right_trigger * lift_high_power_limit);
        } else if (gamepad2.left_trigger > 0) {
            arms.lift(gamepad2.left_trigger * lift_high_power_limit);
        } else if (gamepad2.right_bumper && gamepad2.left_bumper) {
            arms.lift(0);
        } else if (gamepad2.right_bumper) {
            arms.lift(-lift_low_power_limit);
        } else if (gamepad2.left_bumper) {
            arms.lift(lift_low_power_limit);
        } else {
            arms.lift(0);
        }

        // open or close the hook
        if (gamepad2.x && gamepad2.y) {
            arms.hook(SkyStoneArms.Hook.open);  // open
        } else if (gamepad2.y) {
            arms.hook(SkyStoneArms.Hook.open);  // open
        } else if (gamepad2.x) {
            arms.hook(SkyStoneArms.Hook.hooked);// close
        } else {
            arms.hook(SkyStoneArms.Hook.open);  // open
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        // stop the motors
        motors.stop();
        arms.stop();
    }
}
