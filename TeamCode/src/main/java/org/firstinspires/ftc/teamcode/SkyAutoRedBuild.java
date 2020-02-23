package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous(name="Red Building Zone", group ="Nex+Gen Griffins"  )
@Disabled
public class SkyAutoRedBuild extends GriffinLinearRobot {

    public static final double power = 0.33;

    // this is the main Red Building Zone autonomous op mode, now obsolete
    // it works by direct calls to the robot subsystems
    // it was replaced by SkyAutoRedBuildGRL
    @Override
    public void runOpMode()
    {
        telemetry.addData("runOpMode", "starting initialization"); telemetry.update();
        initialize_robot(false, false);
        telemetry.addData("runOpMode", "initialization complete"); telemetry.update();

        // wait for the player to hit play
        waitForStart();

        telemetry.addData("runOpMode: ", "sleeping 20 seconds"); telemetry.update();

        sleep(20000);

        telemetry.addData("runOpMode: ", "moving 12\" to port"); telemetry.update();

        motors.move_to(270, power, 12);

        telemetry.addData("runOpMode: ", "shutting down"); telemetry.update();

        shutdown();
    }
}