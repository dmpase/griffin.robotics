package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class KaboomUltraRev extends KaboomSensors {

    ColorSensor    color;
    DistanceSensor left;
    DistanceSensor center;
    DistanceSensor right;

    protected KaboomUltraRev(OpMode op) {
        super(op);
    }

    // initialize the motors
    public void init()
    {

        color  = op_mode.hardwareMap.get(ColorSensor.class, "color");
        //left   = op_mode.hardwareMap.get(DistanceSensor.class, "left_ultra");
        center = op_mode.hardwareMap.get(DistanceSensor.class, "color");
        //right  = op_mode.hardwareMap.get(DistanceSensor.class, "right_ultra");

    }

    private static long last_sensor_check = -1000000000L;
    private static Sensors last_sensor_value = new Sensors();

    @Override
    public Sensors check() {
        long current_time = System.currentTimeMillis();

        if (last_sensor_check+850 < current_time) {
            Sensors values = new Sensors();

            /*values.left = left.getDistance(DistanceUnit.INCH);
            values.right = right.getDistance(DistanceUnit.INCH);*/
            values.center = center.getDistance(DistanceUnit.INCH);
            values.alpha = color.alpha();
            values.red = color.red();
            values.green = color.green();
            values.blue = color.blue();

            /*values.left = (values.left < 0) ? 0 : (256 < values.left) ? 256 : values.left;
            values.right = (values.right < 0) ? 0 : (256 < values.right) ? 256 : values.right;*/
            values.center = (values.center < 0) ? 0 : (256 < values.center) ? 256 : values.center;
            values.alpha = (values.alpha < 0) ? 0 : (256 < values.alpha) ? 256 : values.alpha;
            values.red = (values.red < 0) ? 0 : (256 < values.red) ? 256 : values.red;
            values.green = (values.green < 0) ? 0 : (256 < values.green) ? 256 : values.green;
            values.blue = (values.blue < 0) ? 0 : (256 < values.blue) ? 256 : values.blue;

            if (values.center < 9 && values.red / values.blue > 1.6) {
                values.mineral = Mineral.GOLD;
            } else if (values.center < 9) {
                values.mineral = Mineral.SILVER;
            } else {
                values.mineral = Mineral.NONE;
            }

            last_sensor_check = current_time;
            last_sensor_value = values;
        }

        return last_sensor_value;

    }

    // function to cease motion
    public void stop()
    {
    }
}
