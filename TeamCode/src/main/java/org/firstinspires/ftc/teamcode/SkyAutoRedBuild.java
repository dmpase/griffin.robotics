package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name="Red Building Zone", group ="Nex+Gen Griffins"  )
//@Disabled
public class SkyAutoRedBuild extends LinearOpMode {

    SkyStoneVuforia front_camera = null;
    SkyStoneArms    arms         = null;
    SkyStoneMotors  motors       = null;
    SkyStoneSensors sensors      = null;

    long sleep_time = 10;

    @Override public void runOpMode() 
    {
        telemetry.addData("runOpMode", "starting initialization"); telemetry.update();
        initialize_robot(false);
        telemetry.addData("runOpMode", "initialization complete"); telemetry.update();

        waitForStart();

        sleep(20000);

        telemetry.addData("runOpMode: ", "moving to port"); telemetry.update();

        motors.move_to(270, 0.33, 12);

        telemetry.addData("runOpMode: ", "shutting down"); telemetry.update();

        shutdown();
    }

    public void initialize_robot(boolean camera)
    {
        telemetry.addData("initialize_robot", "starting initialization");
        telemetry.update();

        if (camera) {
            front_camera = new SkyStoneVuforia(this, SkyStoneVuforia.FRONT_CAMERA);
            telemetry.addData("initialize_robot", "front camera created");
            telemetry.update();

            front_camera.init();
            telemetry.addData("initialize_robot", "front camera initalized");
            telemetry.update();

            front_camera.activate();
            telemetry.addData("initialize_robot", "front camera activated");
            telemetry.update();
        }

        telemetry.addData("initialize_robot","creating motors");
        telemetry.update();
        motors = new SkyStoneHolonomic(this, 120, 6000.0/360.0, 5);
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

    public int identify_location()
    {
        int action = UNKNOWN_ZONE;

        telemetry.addData("identify_location", "reading camera");
        telemetry.update();

        SkyStoneVuforia.Target target = front_camera.first_visible_target();

        telemetry.addData("identify_location", ((target == null)?"target=null":"target="+target.name));
        telemetry.update();

        if (target == null) {
            action = UNKNOWN_ZONE;
        } else if (target.id == SkyStoneVuforia.BLUE_PERIMETER_ONE) {
            action = BLUE_LOADING_ZONE;
        } else if (target.id == SkyStoneVuforia.BLUE_PERIMETER_TWO) {
            action = BLUE_BUILDING_ZONE;
        } else if (target.id == SkyStoneVuforia.RED_PERIMETER_ONE) {
            action = RED_BUILDING_ZONE;
        } else if (target.id == SkyStoneVuforia.RED_PERIMETER_TWO) {
            action = RED_LOADING_ZONE;
        } else if (target.id == SkyStoneVuforia.FRONT_PERIMETER_ONE) {
            action = RED_LOADING_ZONE;
        } else if (target.id == SkyStoneVuforia.FRONT_PERIMETER_TWO) {
            action = BLUE_LOADING_ZONE;
        } else if (target.id == SkyStoneVuforia.REAR_PERIMETER_ONE) {
            action = BLUE_BUILDING_ZONE;
        } else if (target.id == SkyStoneVuforia.REAR_PERIMETER_TWO) {
            action = RED_BUILDING_ZONE;
        }

        telemetry.addData("identify_location", ((target == null)?"target=null":"target="+target.name));
        telemetry.addData("identify_location", "action="+action);
        telemetry.update();

        return action;
    }

    public void blue_front()
    {
       /* back_camera.activate();
        while (!isStopRequested()) {

            // check all the trackable targets to see which one (if any) is visible.
            SkyStoneVuforia.Target target = back_camera.first_visible_target();

            if (target != null) {
                telemetry.addData(target.name, "{X,Y,Z}: %6.1f,  %6.1f,  %6.1f",
                        target.x, target.y, target.z);
                telemetry.addData(target.name, "bearing: %6.1f", target.bearing);
            } else {
                telemetry.addData("Target:", "No target is visible");
            }
            telemetry.update();
        }

        // Disable Tracking when we are done;
        back_camera.deactivate();

            //move to build zone, drag build zone, move to skystones, scan for pattern, pick up pattern block, move to to build zone
            //deliver skystone, park on tape
            */


    }

    public void blue_rear()
    {
        motors.move_to(90, 0.25, 6);
        //Look for the Skystone
        SkyStoneVuforia.Target target = front_camera.first_visible_target();
        if (target.is_visible && target.id == SkyStoneVuforia.STONE_TARGET){
            while (target.is_visible && target.z <= 15)  {
                motors.move_to(target.bearing, 0.1, target.z/2);
                target = front_camera.first_visible_target();
            }
        }
        //Pick up Skystone
        motors.move_to(180,0.25,4);
    }

    public void red_front()
    {
    }

    public void red_rear()
    {
    }

    public void shutdown()
    {
        if (front_camera != null) {
            front_camera.deactivate();
        }
        telemetry.addData("Front Camera", "front camera deactivated");
        telemetry.update();
    }

    public static final int UNKNOWN_ZONE       =  0;
    public static final int BLUE_LOADING_ZONE  =  1;
    public static final int BLUE_BUILDING_ZONE =  2;
    public static final int RED_LOADING_ZONE   =  3;
    public static final int RED_BUILDING_ZONE  =  4;

    public static final String [] locations = {"Unknown Zone", "Blue Loading Zone", "Blue Building Zone", "Red Loading Zone", "Red Building Zone"};
}