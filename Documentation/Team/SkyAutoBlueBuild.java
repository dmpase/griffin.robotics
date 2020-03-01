package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;


@Autonomous(name="Blue Building Zone", group ="Nex+Gen Griffins"  )
@Disabled
public class SkyAutoBlueBuild extends GriffinLinearRobot {

    public static final double power = 0.33;

    // this is the main Blue Building Zone autonomous op mode, now obsolete
    // it is replaced by SkyAutoBlueBuildGRL
    // it works by using direct calls to the robot subsystems
    @Override
    public void runOpMode()
    {
        telemetry.addData("runOpMode", "starting initialization"); telemetry.update();
        initialize_robot(false, false);
        telemetry.addData("runOpMode", "initialization complete"); telemetry.update();

        // wait for the player to press play
        waitForStart();

        telemetry.addData("runOpMode: ", "sleeping 20 seconds"); telemetry.update();

        sleep(20000);

        telemetry.addData("runOpMode: ", "moving 12\" to starboard"); telemetry.update();

        motors.move_to(90, 0.33, 12);

        telemetry.addData("runOpMode: ", "shutting down"); telemetry.update();

        shutdown();
    }
}