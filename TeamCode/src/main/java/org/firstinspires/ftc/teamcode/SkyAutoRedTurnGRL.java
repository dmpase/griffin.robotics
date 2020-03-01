package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name="Red Building Zone Turn GRL", group ="Griffin Robot Language"  )
// @Disabled
public class SkyAutoRedTurnGRL extends GriffinLinearRobot {

    public static final double power = 0.33;
    private static final double owo_turn_power  = 0.40;
    private static final double uwu_turn_power  = 0.30;
    private static final long   sleep_delay     = 750;

    private static final String audio_path = "/storage/emulated/legacy/Music/audio";

    // instructions for the OwO robot
    Object[][] owo_instructions = {
            {MOVE,    90, power, 12, "lining up with the foundation"},
            {AUDIO, audio_path, "rust.mp3", "Do you want me to sit in a corner and rust?"},
            {MOVE,     0,    0.35,           36, "move to the foundation"},
            {HOOK,  true,                        "close hook"},
            {SLEEP, sleep_delay,                 "sleep for hook"},
            {MOVE,   180,    power,          12, "pull foundation back"},
            {TURN,  -200,    owo_turn_power,     "turn the foundation"},
            {HOOK, false,                        "open hook"},
            {MOVE,     0,    power,          30, "push the foundation"},
            {MOVE,   180,    power,           16, "back away from foundation"},
            {TURN,    10,    power,              "correct retreat angle"},
            {MOVE,    90,    power,          48, "move to bridge"},
            {AUDIO,      audio_path, "pain.mp3", "I have this terrible pain in all the diodes down my left side!"}
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