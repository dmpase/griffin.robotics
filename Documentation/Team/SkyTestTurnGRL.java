package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name="Test Turn GRL", group ="Griffin Robot Tests"  )
// @Disabled
public class SkyTestTurnGRL extends GriffinLinearRobot {

    public static final double power = 0.33;

    Object[][] owo_instructions = {
            {TURN,    360, power, "turning 360 degrees"},
    };

    Object[][] uwu_instructions = {
            {TURN,    360, power, "turning 360 degrees"},
    };

    // this test spins the robot 360 degrees
    // it is used for calibrating the wheel encoder count for turns in the SkyStoneHolonomic constructor
    @Override
    public void runOpMode()
    {
        telemetry.addData("runOpMode", "starting initialization"); telemetry.update();
        initialize_robot(false, false);
        telemetry.addData("runOpMode", "initialization complete " + robot); telemetry.update();

        Object[][] instructions = null;

        // figure out which robot we're using and choose the correct set of instructions
        if (exists("OwO")) {
            instructions = owo_instructions;
        } else if (exists("UwU")) {
            instructions = uwu_instructions;
        } else {
            instructions = owo_instructions;
        }

        waitForStart();

        // execute the program
        if (instructions != null) {
            execute_loop(instructions);
        }

        telemetry.addData("runOpMode: ", "shutting down"); telemetry.update();

        shutdown();
    }
}