package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareDevice;


public abstract class GriffinLinearRobot extends LinearOpMode {

    protected SkyStoneVuforia camera  = null;
    protected SkyStoneArms    arms    = null;
    protected SkyStoneMotors  motors  = null;
    protected SkyStoneSensors sensors = null;


    public void initialize_robot(boolean use_camera)
    {
        telemetry.addData("initialize_robot", "starting initialization");
        telemetry.update();

        if (use_camera) {
            this.camera = new SkyStoneVuforia(this, SkyStoneVuforia.FRONT_CAMERA);
            telemetry.addData("initialize_robot", "front camera created");
            telemetry.update();

            this.camera.init();
            telemetry.addData("initialize_robot", "front camera initalized");
            telemetry.update();

            this.camera.activate();
            telemetry.addData("initialize_robot", "front camera activated");
            telemetry.update();
        }

        telemetry.addData("initialize_robot","creating motors");
        telemetry.update();
        if (exists("OwO")) {
            motors = new SkyStoneHolonomic(this, 100, 6000.0/360.0, 5);
        } else if (exists("UwU")) {
            motors = new SkyStoneHolonomic(this, 128, 6375.0/360.0, 5);
        } else {
            motors = new SkyStoneHolonomic(this, 120, 6000.0/360.0, 5);
        }
        motors.init();

        telemetry.addData("initialize_robot","creating arm");
        telemetry.update();
        arms = new SkyStoneArmREV(this);
        arms.init(true);
        arms.hook(SkyStoneArms.Hook.open);

        telemetry.addData("initialize_robot","creating sensors");
        telemetry.update();
        // boot up the sensors
        sensors = new SkyStoneUltraRev(this);
        sensors.init();

        telemetry.addData("initialize_robot", "done initializing");
        telemetry.update();
    }

    // instruction components
    public static final int OP_CODE    = 0;
    public static final int CLOSE      = 1;    // claw & hook, close or grab
    public static final int TIME       = 1;    // sleep time in ms
    public static final int BEARING    = 1;    // bearing in degrees
    public static final int POWER      = 2;    // power, -1 to +1
    public static final int RANGE      = 3;    // range in inches
    public static final int GRAB_MSG   = 2;    // message to player
    public static final int HOOK_MSG   = 2;    // message to player
    public static final int MOVE_MSG   = 4;    // message to player
    public static final int TURN_MSG   = 3;    // message to player
    public static final int SLEEP_MSG  = 2;    // message to player

    // op codes
    public static final int GRAB  = 1;
    public static final int HOOK  = 2;
    public static final int MOVE  = 3;
    public static final int TURN  = 4;
    public static final int SLEEP = 5;

    public void execute_loop(Object[][] instructions)
    {
        if (instructions == null || ! opModeIsActive()) {
            // something is coded incorrectly
            shutdown();
            return;
        }

        try {
            for (int i=0; i < instructions.length; i++) {
                Object[] instruction = instructions[i];

                if (instruction == null || ! opModeIsActive()) {
                    // player hit the stop button
                    shutdown();
                    return;
                }

                int op_code = (int) instruction[OP_CODE];

                if (op_code == GRAB) {
                    // execute a grab operation
                    boolean grab = (boolean) instruction[CLOSE];
                    String message = (String) instruction[GRAB_MSG];
                    telemetry.addData("execute loop", message);
                    telemetry.update();
                    arms.grab(grab);
                } else if (op_code == HOOK) {
                    // execute a hook operation
                    boolean close = (boolean) instruction[CLOSE];
                    String message = (String) instruction[HOOK_MSG];
                    telemetry.addData("execute loop", message);
                    telemetry.update();
                    if (close) {
                        arms.hook(SkyStoneArms.Hook.hooked);
                    } else {
                        arms.hook(SkyStoneArms.Hook.open);
                    }
                } else if (op_code == MOVE) {
                    // execute a move operation
                    double bearing = (double) instruction[BEARING];
                    double range   = (double) instruction[RANGE];
                    double power   = (double) instruction[POWER];
                    String message = (String) instruction[MOVE_MSG];
                    telemetry.addData("execute loop", message);
                    telemetry.update();
                    motors.move_to(bearing, range, power);
                } else if (op_code == TURN) {
                    // execute a turn operation
                    double bearing = (double) instruction[BEARING];
                    double power   = (double) instruction[POWER];
                    String message = (String) instruction[TURN_MSG];
                    telemetry.addData("execute loop", message);
                    telemetry.update();
                    motors.turn_to(bearing, power);
                } else if (op_code == SLEEP) {
                    // execute a sleep operation
                    long sleep_time = (long) instruction[TIME];
                    String message = (String) instruction[SLEEP_MSG];
                    telemetry.addData("execute loop", message);
                    telemetry.update();
                    sleep(sleep_time);
                }
            }
        } catch (Exception e) {
            // something is coded incorrectly
            shutdown();
            return;
        }
    }

    public void shutdown()
    {
        if (camera != null) {
            telemetry.addData("shutdown", "deactivate camera");
            telemetry.update();
            camera.deactivate();
            telemetry.addData("shutdown", "camera deactivated");
            telemetry.update();
        }

        if (motors != null) {
            telemetry.addData("shutdown", "stop motors");
            telemetry.update();
            motors.stop();
            telemetry.addData("shutdown", "motors stopped");
            telemetry.update();
        }

        if (arms != null) {
            telemetry.addData("shutdown", "stop arms");
            telemetry.update();
            arms.stop();
            telemetry.addData("shutdown", "arms stopped");
            telemetry.update();
        }

        if (sensors != null) {
            telemetry.addData("shutdown", "stop sensors");
            telemetry.update();
            sensors.stop();
            telemetry.addData("shutdown", "sensors stopped");
            telemetry.update();
        }

        telemetry.addData("shutdown", "robot is shutdown");
        telemetry.update();
    }

    // this routine checks for the existence of a named device.
    // this may be useful in several ways, namely, identifying
    // whether a device exists before using it, or by having
    // a specific device in a configuration file that identifies
    // the particular robot in use. For example:

    // 1) Robot "OwO" contains a device named "OwO" in its configuration file
    // 2) The "OwO" device is registered as an Analog Device in the config file
    // 3) Registering "OwO" as an Analog Device means the robot will not fail
    //    if no device is actually installed at that port
    // 4) A different configuration file contains a device named "UwU"
    // 5) The robot checks for the existence of both "OwO" and "UwU"
    // 6) Whichever exists, that is the robot being used
    // 7) Software then customizes its parameters with whatever details are
    //    appropriate to that specific robot
    public boolean exists(String name) {
        boolean result = false;

        if (name != null) {
            try {
                HardwareDevice device = hardwareMap.get(name);
                result = device != null;
            } catch (Exception e) {
                result = false;
            }
        }

        return result;
    }
}