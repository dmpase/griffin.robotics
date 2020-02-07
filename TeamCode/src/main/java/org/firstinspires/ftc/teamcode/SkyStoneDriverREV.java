package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="SkyStone Driver REV", group="Nex+Gen Griffins")
// @Disabled
public class SkyStoneDriverREV extends SkyStoneDriver
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Starting Initialization.");

        if (DeviceFinder.exists(this, "OwO")) {
            motors = new SkyStoneHolonomic(this, 120, 6000.0 / 360.0, 5);
        } else if (DeviceFinder.exists(this, "UwU")) {
            motors = new SkyStoneHolonomic(this, 120, 6000.0 / 360.0, 5);
        } else {
            motors = new SkyStoneHolonomic(this, 120, 6000.0 / 360.0, 5);
        }
        motors.init();

        arms = new SkyStoneArmREV(this);
        arms.init(false);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialization Complete.");
    }
}
