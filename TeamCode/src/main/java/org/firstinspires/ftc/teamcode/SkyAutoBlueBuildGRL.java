package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name="Blue Building Zone GRL", group ="Griffin Robot Language"  )
// @Disabled
public class SkyAutoBlueBuildGRL extends GriffinLinearRobot {

    public static final double power = 0.33;

    private static final String audio_path = "/storage/emulated/legacy/Music/audio";

    // instructions for the OwO robot
    Object[][] owo_instructions = {
            {SLEEP, 20000,            "sleeping 20 seconds"},
            {MOVE,     90, power, 15, "moving 15\" to starboard"},
            {AUDIO, audio_path, "planet.mp3", "Here I am, brain the size of a planet..."},
    };

    // instructions for the UwU robot
    Object[][] uwu_instructions = {
            {SLEEP, 20000,            "sleeping 20 seconds"},
            {MOVE,     90, power, 32, "moving 32\" to starboard"},
            {AUDIO, audio_path, "planet.mp3", "Here I am, brain the size of a planet..."},
    };

    // this is the main Blue Build Zone autonomous op mode
    // it replaces SkyAutoBlueBuild
    @Override
    public void runOpMode()
    {
        telemetry.addData("runOpMode", "starting initialization"); telemetry.update();
        initialize_robot(false, false);
        {
            AudioFile af = new AudioFile(audio_path, "life.mp3");
            af.async_play();
        }
        telemetry.addData("runOpMode", "initialization complete"); telemetry.update();

        Object[][] instructions = null;

        // figure which robot is running and choose the appropriate instructions
        if (exists("OwO")) {
            instructions = owo_instructions;
        } else if (exists("UwU")) {
            instructions = uwu_instructions;
        } else {
            instructions = owo_instructions;
        }

        // wait for the player to press play
        waitForStart();

        // run the op mode for the robot in use
        if (instructions != null) {
            execute_loop(instructions);
        }

        shutdown();
    }
}