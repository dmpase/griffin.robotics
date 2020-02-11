package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name="Blue Building Zone GRL", group ="Griffin Robot Language"  )
// @Disabled
public class SkyAutoBlueBuildGRL extends GriffinLinearRobot {

    public static final double power = 0.33;

    Object[][] owo_instructions = {
            {SLEEP, 20000,            "sleeping 20 seconds"},
            {MOVE,     90, power, 12, "moving 12\" to starboard"},
    };

    Object[][] uwu_instructions = {
            {SLEEP, 20000,            "sleeping 20 seconds"},
            {MOVE,     90, power, 12, "moving 12\" to starboard"},
    };

    @Override
    public void runOpMode()
    {
        telemetry.addData("runOpMode", "starting initialization"); telemetry.update();
        initialize_robot(false);
        telemetry.addData("runOpMode", "initialization complete"); telemetry.update();

        Object[][] instructions = null;

        if (exists("OwO")) {
            instructions = owo_instructions;
        } else if (exists("UwU")) {
            instructions = uwu_instructions;
        } else {
            instructions = owo_instructions;
        }

        waitForStart();

        if (instructions != null) {
            execute_loop(instructions);
        }

        shutdown();
    }
}