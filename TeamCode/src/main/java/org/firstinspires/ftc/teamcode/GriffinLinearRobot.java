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
            motors = new SkyStoneHolonomic(this, 120, 6000.0/360.0, 5);
        } else if (exists("UwU")) {
            motors = new SkyStoneHolonomic(this, 120, 6000.0/360.0, 5);
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