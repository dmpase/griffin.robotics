package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name="Red Building Zone GRL", group ="Griffin Robot Language"  )
// @Disabled
public class SkyAutoRedBuildGRL extends GriffinLinearRobot {

    public static final double power = 0.33;

    private static final String audio_path = "/storage/emulated/legacy/Music/audio";

    // instructions for the OwO robot
    Object[][] owo_instructions = {
            {SLEEP, 5000,            "sleeping 5 seconds"},
            {MOVE,    -90, power, 15, "moving 32\" to port"},
            {AUDIO, audio_path, "rust.mp3", "Do you want me to sit in a corner and rust?"},
    };

    // instructions for the UwU robot
    Object[][] uwu_instructions = {
            {SLEEP, 5000,            "sleeping 5 seconds"},
            {MOVE,    -90, power, 15, "moving 32\" to port"},
            {AUDIO, audio_path, "rust.mp3", "Do you want me to sit in a corner and rust?"},
    };

    // this is the main Red Build Zone autonomous op mode
    // it replaces SkyAutoRedBuild
    @Override
    public void runOpMode()
    {
        telemetry.addData("runOpMode", "starting initialization"); telemetry.update();
        initialize_robot(false, false);
        {
            AudioFile af = new AudioFile(audio_path, "do.mp3");
            af.async_play();
        }
        telemetry.addData("runOpMode", "initialization complete"); telemetry.update();

        Object[][] instructions = null;

        // figure which robot we're running and choose the instructions accordingly
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

        telemetry.addData("runOpMode: ", "shutting down"); telemetry.update();

        shutdown();
    }
}