package org.firstinspires.ftc.teamcode;

import java.io.File;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="AudioOpMode", group="Iterative Opmode")
@Disabled
public class AudioOpMode extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

//    private static final String audio_dir = "/storage/emulated/legacy/Download/audio";
    private static final String audio_dir = "/storage/emulated/legacy/Music/audio";

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Starting Initialization.");

        telemetry.addData("File", new File(audio_dir,"help.mp3").exists());
        AudioFile af = new AudioFile(audio_dir + "/" + "help.mp3");
        af.async_play();

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialization Complete.");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop()
    {
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        AudioFile af = new AudioFile(audio_dir + "/" + "rust.mp3");
        af.async_play();
    }

}
