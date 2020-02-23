package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name="Test Move GRL", group ="Griffin Robot Tests"  )
// @Disabled
public class SkyTestMoveGRL extends GriffinLinearRobot {

    public static final double power = 0.33;

    Object[][] owo_instructions = {
            {MOVE,    0, power, 120, "moving 120\" forward"},
    };

    Object[][] uwu_instructions = {
            {MOVE,    0, power, 120, "moving 120\" forward"},
    };

    // this test moves the robot forward 10 feet and stops
    // it is used to calibrate the number of encoder turns per inch in the SkyStoneHolonomic constructor
    @Override
    public void runOpMode()
    {
        telemetry.addData("runOpMode", "starting initialization"); telemetry.update();
        initialize_robot(false, false);
        telemetry.addData("runOpMode", "initialization complete " + robot); telemetry.update();

        Object[][] instructions = null;

        // figure out which robot we're using
        if (exists("OwO")) {
            instructions = owo_instructions;
        } else if (exists("UwU")) {
            instructions = uwu_instructions;
        } else {
            instructions = owo_instructions;
        }

        waitForStart();

        // run the instructions
        if (instructions != null) {
            execute_loop(instructions);
        }

        telemetry.addData("runOpMode: ", "shutting down"); telemetry.update();

        shutdown();
    }
}