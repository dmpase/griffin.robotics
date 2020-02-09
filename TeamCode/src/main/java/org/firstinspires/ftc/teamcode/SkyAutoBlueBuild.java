package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name="Blue Building Zone", group ="Nex+Gen Griffins"  )
//@Disabled
public class SkyAutoBlueBuild extends GriffinLinearRobot {

    public static final double power = 0.33;

    Object[][] owo_instructions = {
            {SLEEP, 20000,            "wait patiently"},
            {MOVE,     90, power, 12, "move to port"},
    };

    Object[][] uwu_instructions = {
            {SLEEP, 20000,            "wait patiently"},
            {MOVE,     90, power, 12, "move to port"},
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
            shutdown();
            return;
        }

        sleep(20000);

        telemetry.addData("runOpMode: ", "moving to port"); telemetry.update();

        motors.move_to(90, 0.33, 12);

        telemetry.addData("runOpMode: ", "shutting down"); telemetry.update();

        shutdown();
    }
}