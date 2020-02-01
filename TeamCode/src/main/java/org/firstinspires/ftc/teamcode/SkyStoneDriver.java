/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="SkyStone Driver", group="Nex+Gen Griffins")
@Disabled
public abstract class SkyStoneDriver extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    SkyStoneMotors motors = null;
    SkyStoneArms arms = null;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Starting SkyStoneDriver Initialization.");
        telemetry.update();

        motors.init();
        arms.init(false);

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "SkyStoneDriver Initialization Complete.");
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
    public void loop() {
        // get the throttle command from the driver by reading the gamepad
        double fast_x =   gamepad1.left_stick_x;
        double fast_y = - gamepad1.left_stick_y;

        double slow_x = 0.35 * gamepad1.right_stick_x;
        double slow_y = 0.35 * -gamepad1.right_stick_y;

        double x = fast_x;
        double y = fast_y;
        if (slow_x != 0 || slow_y != 0) {
            x = slow_x;
            y = slow_y;
        } else if (gamepad1.dpad_left) {
            x = -0.20;
            y = 0;
        } else if (gamepad1.dpad_right) {
            x = 0.20;
            y = 0;
        } else if (gamepad1.dpad_up) {
            x = 0;
            y = 0.20;
        } else if (gamepad1.dpad_down) {
            x = 0;
            y = -0.20;
        }

        // convert the command to a bearing relative to the robot
        double alpha   = Math.atan2(y,x);
        double bearing = 90 - alpha * 180 / Math.PI;

        // figure out the driver's desired speed (make sure it is less than 1.0)
        double length = Math.sqrt(x*x + y*y);
        double speed  = Math.min(length, 1.0);

        // tell the driver what's happening
        telemetry.addData("loop: ", "br:%7.2f ln:%7.2f x:%7.2f y:%7.2f", bearing, length, x, y);

        // get the turn rate from the triggers or bumpers
        double turn   = 0;
        boolean left  = (0 < gamepad1.left_trigger) || gamepad1.left_bumper;
        boolean right = (0 < gamepad1.right_trigger) || gamepad1.right_bumper;

        if ((left && right) || (!left && !right)) {
            ;           // do nothing
        } else if (left) {
            if (gamepad1.left_bumper) {
                turn = 0.25;
            } else {
                turn = 0.75 * gamepad1.left_trigger;
            }
        } else if (right) {
            if (gamepad1.right_bumper) {
                turn = - 0.25;
            } else {
                turn = - 0.75 * gamepad1.right_trigger;
            }
        }

        // send the driver commands to the motors
        motors.move(bearing, speed, turn);


        if (gamepad2.a && gamepad2.b) {
            ; //nothing
        } else if (gamepad2.a){
            arms.grab(false); //open
        } else if (gamepad2.b) {
            arms.grab(true); //close
        } else {
            ; //nothing
        }

        double lift_high_power_limit = 0.30;    //test value
        double lift_low_power_limit  = 0.075;   //test value
        if (gamepad2.right_trigger > 0 && gamepad2.left_trigger > 0) {
            arms.lift(0);
        } else if (gamepad2.right_trigger > 0) {
            arms.lift(-gamepad2.right_trigger * lift_high_power_limit);
        } else if (gamepad2.left_trigger > 0) {
            arms.lift(gamepad2.left_trigger * lift_high_power_limit);
        } else if (gamepad2.right_bumper && gamepad2.left_bumper) {
            arms.lift(0);
        } else if (gamepad2.right_bumper) {
            arms.lift(-lift_low_power_limit);
        } else if (gamepad2.left_bumper) {
            arms.lift(lift_low_power_limit);
        } else {
            arms.lift(0);
        }

        /*
        int lift_factor = -10;
        if (gamepad1.a) {
            arms.lift_to(lift_factor * 0);
            telemetry.addData("arm", ""+(lift_factor*0));
        } else if (gamepad1.x) {
            arms.lift_to(lift_factor * 1);
            telemetry.addData("arm", ""+(lift_factor*1));
        } else if (gamepad1.y) {
            arms.lift_to(lift_factor * 2);
            telemetry.addData("arm", ""+(lift_factor*2));
        } else if (gamepad1.b) {
            arms.lift_to(lift_factor * 3);
            telemetry.addData("arm", ""+(lift_factor*3));
        }
        */


        if (gamepad2.x && gamepad2.y) {
            arms.hook(SkyStoneArms.Hook.open);
        } else if (gamepad2.y) {
            arms.hook(SkyStoneArms.Hook.open);
        } else if (gamepad2.x) {
            arms.hook(SkyStoneArms.Hook.hooked);
        } else {
            arms.hook(SkyStoneArms.Hook.open);
        }

    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        // stop the motors
        motors.stop();
        arms.stop();
    }
}