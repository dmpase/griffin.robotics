package org.firstinspires.ftc.teamcode;

/**
 * Created by Doug Pase on 2/1/2020.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DeviceFinder {

    // this routine checks for the existence of a named device. 
    // this may be useful in several ways, namely, identifying
    // whether a device exists before using it, or by having 
    // a specific device in a configuration file that identifies
    // the particular robot in use. For example:

    // 1) Robot "OwO" contains a device named "OwO" in its configuration file
    // 2) The "OwO" device is registered as an Analog Device in the config file
    // 3) Registering "OwO" as an Analog Device means the robot will not fail 
    //    if no device is actually installed at that port
    // 4) A different configuration file contains a device named "UwU"
    // 5) The robot checks for the existence of both "OwO" and "UwU"
    // 6) Whichever exists, that is the robot being used
    // 7) Software then customizes its parameters with whatever details are
    //    appropriate to that specific robot
    public static boolean exists(OpMode op, String name) {
        boolean result = false;

        if (op != null && name != null) {
            try {
                HardwareDevice device = op.hardwareMap.get(name);
                result = device != null;
            } catch (Exception e) {
                result = false;
            }
        }

        return result;
    }
}
