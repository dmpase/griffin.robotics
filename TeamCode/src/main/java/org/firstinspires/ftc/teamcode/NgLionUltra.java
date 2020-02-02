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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * This file contains ...
 */

public class NgLionUltra extends GriffinUltra {
    /**************************************************************************
     *                           robot configuration                          *
     **************************************************************************/


    // Expansion Hub Portal 1
    //     Expansion Hub 2
    //         I2C Bus 1
    public static final String STBD_BOW_MR_RANGE_NAME = "stbd bow mr range";     // Hub 2.I2C Bus 1[0].MR Range Sensor
    public static final String STBD_AFT_MR_RANGE_NAME = "stbd aft mr range";     // Hub 2.I2C Bus 1[0].MR Range Sensor

    //         I2C Bus 2
    public static final String PORT_BOW_MR_RANGE_NAME = "port bow mr range";     // Hub 2.I2C Bus 2[0].MR Range Sensor
    public static final String PORT_AFT_MR_RANGE_NAME = "port aft mr range";     // Hub 2.I2C Bus 2[0].MR Range Sensor



    /**************************************************************************
     *                            OpMode functions                            *
     **************************************************************************/


    public NgLionUltra(OpMode op_mode)
    {
        super(op_mode);
    }



    /**************************************************************************
     *                            sensor functions                            *
     **************************************************************************/


    // Modern Robotics ultrasonic range sensors
    private DistanceSensor port_bow_mr_range = null;
    private DistanceSensor stbd_bow_mr_range = null;
    private DistanceSensor stbd_aft_mr_range = null;
    private DistanceSensor port_aft_mr_range = null;

    @Override
    public void init()
    {
        if (port_bow_mr_range == null) {
            try {
                port_bow_mr_range = hardwareMap.get(DistanceSensor.class, PORT_BOW_MR_RANGE_NAME);
            } catch (Exception e) {
                port_bow_mr_range = null;
            }
        }

        if (stbd_bow_mr_range == null) {
            try {
                stbd_bow_mr_range = hardwareMap.get(DistanceSensor.class, STBD_BOW_MR_RANGE_NAME);
            } catch (Exception e) {
                stbd_bow_mr_range = null;
            }
        }

        if (stbd_aft_mr_range == null) {
            try {
                stbd_aft_mr_range = hardwareMap.get(DistanceSensor.class, STBD_AFT_MR_RANGE_NAME);
            } catch (Exception e) {
                stbd_aft_mr_range = null;
            }
        }

        if (port_aft_mr_range == null) {
            try {
                port_aft_mr_range = hardwareMap.get(DistanceSensor.class, PORT_AFT_MR_RANGE_NAME);
            } catch (Exception e) {
                port_aft_mr_range = null;
            }
        }
    }


    public static final double DEFAULT_RANGE = 0;

    @Override
    public double[] read()
    {
        double[] rv = new double[4];

        if (port_bow_mr_range != null) {
            rv[PORT_BOW] = port_bow_mr_range.getDistance(DistanceUnit.INCH);
        } else {
            rv[PORT_BOW] = DEFAULT_RANGE;
        }

        if (port_bow_mr_range != null) {
            rv[STBD_BOW] = stbd_bow_mr_range.getDistance(DistanceUnit.INCH);
        } else {
            rv[STBD_BOW] = DEFAULT_RANGE;
        }

        if (port_bow_mr_range != null) {
            rv[STBD_AFT] = stbd_aft_mr_range.getDistance(DistanceUnit.INCH);
        } else {
            rv[STBD_AFT] = DEFAULT_RANGE;
        }

        if (port_bow_mr_range != null) {
            rv[PORT_AFT] = port_aft_mr_range.getDistance(DistanceUnit.INCH);
        } else {
            rv[PORT_AFT] = DEFAULT_RANGE;
        }

        return rv;
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop()
    {
    }
}