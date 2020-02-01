package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class RobotIdentifier {

    public static boolean get_robot(OpMode op, String name) {
        boolean result = false;

        if (op != null && name != null) {
            try {
                op.hardwareMap.get(name);
                result = true;
            } catch (Exception e) {
                result = false;
            }
        }

        return result;
    }
}
