package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="SkyStone Driver REV", group="Nex+Gen Griffins")
// @Disabled
public class SkyStoneDriverREV extends SkyStoneDriver
{
    private ElapsedTime runtime = new ElapsedTime();

     // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        telemetry.addData("Status", "Starting Initialization.");

        if (DeviceFinder.exists(this, "OwO")) {
            lift_high_power_limit = 0.30;
            motors = new SkyStoneHolonomic(this, 100.84, 4975.0/360.0, 1);
        } else if (DeviceFinder.exists(this, "UwU")) {
            lift_high_power_limit = 0.20;
            motors = new SkyStoneHolonomic(this, 131.3, 6200.0/360.0, 0);
        } else {
            lift_high_power_limit = 0.15;
            motors = new SkyStoneHolonomic(this, 120, 6000.0 / 360.0, 5);
        }
        motors.init();

        arms = new SkyStoneArmREV(this);
        arms.init(false);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialization Complete.");
    }
}
