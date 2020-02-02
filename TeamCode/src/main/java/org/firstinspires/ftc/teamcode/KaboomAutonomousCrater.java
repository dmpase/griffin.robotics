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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

// superclass that describes what the robot does in autonomous op mode
// @Autonomous(name="Kaboom Auto", group="Linear Opmode")
// @Disabled
public abstract class KaboomAutonomousCrater extends LinearOpMode {

    public abstract void kaboom_init();

    private ElapsedTime runtime = new ElapsedTime();

    KaboomMotors motors = null;
    KaboomSensors sensors = null;
    KaboomArms arms = null;


    private enum Position {LEFT, CENTER, RIGHT}

    @Override
    public void runOpMode() {
        // initialize the robot
        telemetry.addData("Status", "Starting Initialization");
        kaboom_init();
        telemetry.addData("Status", "Initialization Complete");
        telemetry.update();

        // wait for autonomous op mode to start
        waitForStart();
        runtime.reset();

        telemetry.addData("Status", "Raise Hook");
        telemetry.update();

        // lower robot goes here
        arms.hook(true, true);

        telemetry.addData("Status", "Turn, Lower Hook, Turn");
        telemetry.update();

        motors.turn_to(-15, 0.1);
        arms.hook(false, true);
        motors.turn_to(15, 0.1);

        motors.move_to(-15,0.3,19);
        motors.move_to(180,0.3,8);
        motors.move_to(270,0.3,35);
        motors.turn_to(135,0.2);
        motors.move_to(90,0.3,30);
        arms.expand_auto();
        motors.turn_to(45,0.2);
        motors.turn_to(-40,0.2);
        motors.move_to(270,0.3,50);

        try {
            sleep(500);
        } catch (Exception e) {
            ;
        }

        motors.move_to(270,0.05,1);
        //motors.move_to(270,0.2,24);


        while (true && runtime.seconds() < 30) {
            telemetry.addData("Status", "Waiting "+runtime.seconds());
            telemetry.update();
        }

        // op mode is done, inform the driver
        telemetry.addData("Status", "Autonomous Op Mode Complete");
        telemetry.update();

    }
}
