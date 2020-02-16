package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name="Red Loading Zone", group ="Nex+Gen Griffins"  )
@Disabled
public class SkyAutoRedLoad extends GriffinLinearRobot {

    private static final double power           = 0.75;
    private static final double foundation_bump = 0.35;
    private static final double foundation_turn_power = 0.50;
    private static final long   sleep_delay     = 750;

    private static final boolean use_arm     = true;
    private static final int     lift_target = -300;
    private static final double  lift_power  = 0.75;

    private static final double robot_width = 16;			// robot long axis (port to stbd)
    private static final double robot_depth = 14;			// robot short axis (front to back)

    // see https://www.firstinspires.org/sites/default/files/uploads/resource_library/ftc/game-manual-part-2.pdf, pg. 36
    // and https://www.firstinspires.org/sites/default/files/uploads/resource_library/ftc/field-setup-guide.pdf, pg. 11
    private static final double foundation_width    = 34.5;     // distance from foundation port to starboard sides
    private static final double foundation_depth    = 18.5;     // distance from foundation front to back
    private static final double foundation_height   = 2.25;	    // distance from floor to top of foundation
    private static final double foundation_space    = 4;        // distance from foundation to building zone wall
    private static final double foundation_alliance = 47.25;    // distance from foundation to alliance wall

    private double distance = 0;
    private double bearing  = 0;


    @Override
    public void runOpMode()
    {
        telemetry.addData("runOpMode", "start initialization"); telemetry.update();
        initialize_robot(false);
        telemetry.addData("runOpMode", "initialization complete"); telemetry.update();

        waitForStart();

        // make sure claw and hook are open, but don't wait for them...
        // there's plenty of time for them to open while we're in transit
        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode", "set claw and hook"); telemetry.update();
        arms.grab(false);
        arms.hook(SkyStoneArms.Hook.open);

        distance = 48 - robot_depth + 2;
        bearing  =  0;
        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode ", "move to blocks d=%g, b=%g", distance, bearing); telemetry.update();
        motors.move_to(bearing, power, distance);

        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode ", "close claw"); telemetry.update();
        arms.grab(true);                // close the claw to grab the block
        sleep(sleep_delay);                    // give the claw time to react

        distance = 14;
        bearing  = 180;
        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode ", "back from blocks d=%g, b=%g", distance, bearing); telemetry.update();
        motors.move_to(bearing, power, distance);

        distance = 42;
        bearing  = 90;
        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode ", "move to bridge d=%g, b=%g", distance, bearing); telemetry.update();
        motors.move_to(bearing, power, distance);

        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode", "drop stone"); telemetry.update();
        arms.grab(false);

        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode", "reorient robot"); telemetry.update();
        motors.turn_to(-5, foundation_turn_power);

        distance = 36 + robot_width*3/4;
        bearing  = 80;
        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode ", "move to foundation d=%g, b=%g", distance, bearing); telemetry.update();
        motors.move_to(bearing, power, distance);

        distance = 9;
        bearing  = 0;
        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode ", "move forward d=%g, b=%g", distance, bearing); telemetry.update();
        motors.move_to(bearing, foundation_bump, distance);

        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode", "close hook"); telemetry.update();
        arms.hook(SkyStoneArms.Hook.hooked);
        sleep(sleep_delay);                     // give the hook time to react

        distance = 12;
        bearing  = 180;
        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode ", "backup d=%g, b=%g", distance, bearing); telemetry.update();
        motors.move_to(bearing, power, distance);

        bearing  = -225;
        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode", "turn foundation"); telemetry.update();
        motors.turn_to(bearing, foundation_turn_power);

        distance = 36;
        bearing  = 0;
        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode ", "move forward d=%g, b=%g", distance, bearing); telemetry.update();
        motors.move_to(bearing, power, distance);

        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode", "open hook"); telemetry.update();
        arms.hook(SkyStoneArms.Hook.open);
        sleep(sleep_delay);                     // give the claw time to react

        distance = 16;
        bearing  = 180;
        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode ", "backup d=%g, b=%g", distance, bearing); telemetry.update();
        motors.move_to(bearing, power, distance);

        distance = 54;
        bearing  = 90;
        if (! opModeIsActive()) { shutdown(); return; }
        telemetry.addData("runOpMode ", "move to bridge d=%g, b=%g", distance, bearing); telemetry.update();
        motors.move_to(bearing, power, distance);

        telemetry.addData("runOpMode", "shut down"); telemetry.update();
        shutdown();
    }
}