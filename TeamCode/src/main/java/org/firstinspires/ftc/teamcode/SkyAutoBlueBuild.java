package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name="Blue Building Zone", group ="Nex+Gen Griffins"  )
//@Disabled
public class SkyAutoBlueBuild extends GriffinLinearRobot {

    @Override
    public void runOpMode()
    {
        telemetry.addData("runOpMode", "starting initialization"); telemetry.update();
        initialize_robot(false);
        telemetry.addData("runOpMode", "initialization complete"); telemetry.update();

        waitForStart();

        sleep(20000);

        telemetry.addData("runOpMode: ", "moving to port"); telemetry.update();

        motors.move_to(90, 0.33, 12);

        telemetry.addData("runOpMode: ", "shutting down"); telemetry.update();

        shutdown();
    }
}