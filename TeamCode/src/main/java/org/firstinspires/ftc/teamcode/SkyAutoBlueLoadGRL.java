package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name="Blue Loading Zone GRL", group ="Griffin Robot Language"  )
// @Disabled
public class SkyAutoBlueLoadGRL extends GriffinLinearRobot {

    private static final double power           = 0.75;
    private static final double owo_turn_power  = 0.40;
    private static final double uwu_turn_power  = 0.30;
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


    // instructions for the OwO robot
    Object[][] owo_instructions = {
            {GRAB, false,                        "open claw"},
            {HOOK, false,                        "open hook"},
            {MOVE,     0,   power,           34, "move to stones"},
            {GRAB,  true,                        "grab a stone"},
            {SLEEP, sleep_delay,                 "sleep for claw"},
            {MOVE,   180,    power,          14, "back away from stones"},
            {MOVE,   -90,    power,          60, "move to bridge"},
            {MOVE,     0,    power,          10, "move forward"},
            {GRAB, false,                        "drop stone"},
            {MOVE,   -90,    power,          36, "move to foundation center"},
            {MOVE,   180,    power,          18, "move away from the foundation"},
            {MOVE,     0,    0.45,           36, "move to the foundation"},
            {HOOK,  true,                        "close hook"},
            {SLEEP, sleep_delay,                 "sleep for hook"},
            {MOVE,   180,    power,          12, "pull foundation back"},
            {TURN,   200,    owo_turn_power,     "turn the foundation"},
            {HOOK, false,                        "open hook"},
            {MOVE,     0,    power,          30, "push the foundation"},
            {MOVE,   180,    power,           6, "back away from foundation"},
            {TURN,   -10,    power,              "correct retreat angle"},
            {MOVE,   -90,    power,          50, "move to bridge"},
    };

    // instructions for the UwU robot
    Object[][] uwu_instructions = {
            {GRAB, false,                        "open claw"},
            {HOOK, false,                        "open hook"},
            {MOVE,     0,   power,           32, "move to stones"},
            {GRAB,  true,                        "grab a stone"},
            {SLEEP, sleep_delay,                 "sleep for claw"},
            {MOVE,   180,    power,          14, "back away from stones"},
            {TURN,     5,    power,              "fix the drift."},
            {MOVE,   -90,    power,          42, "move to bridge"},
            {GRAB, false,                        "drop stone"},
            {TURN,     5,    power,              "turn compensation"},
            {MOVE,   -85,    power,          54, "center on the foundation"},
            {MOVE,     0,    0.50,           18, "move to the foundation"},
            {HOOK,  true,                        "close hook"},
            {SLEEP, sleep_delay,                 "sleep for hook"},
            {MOVE,   180,    0.50,           12, "pull foundation back"},
            {TURN,   225, uwu_turn_power,        "turn the foundation"},
            {HOOK, false,                        "open hook"},
            {MOVE,     0,    power,          36, "push the foundation"},
            {MOVE,   180,    power,           8, "back away from foundation"},
            {TURN,     0,    power,              "correct retreat angle"},
            {MOVE,   -90,    power,          54, "move to bridge"},
    };

    // this is the main Blue Load Zone autonomous op mode
    // it replaces SkyAutoBlueLoad
    @Override
    public void runOpMode()
    {
        telemetry.addData("runOpMode", "start initialization"); telemetry.update();
        initialize_robot(false, false);
        telemetry.addData("runOpMode", "initialization complete"); telemetry.update();

        Object[][] instructions = null;

        // figure which robot we're using and choose the set of instructions
        if (exists("OwO")) {
            instructions = owo_instructions;
        } else if (exists("UwU")) {
            instructions = uwu_instructions;
        } else {
            instructions = owo_instructions;
        }

        // wait for the player to press play
        waitForStart();

        // execute the instructions
        if (instructions != null) {
            execute_loop(instructions);
        }

        telemetry.addData("runOpMode", "shut down"); telemetry.update();
        shutdown();
    }
}