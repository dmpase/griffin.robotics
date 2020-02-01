package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class SkyStoneUltraRev extends SkyStoneSensors {

    // sensor objects
    private DistanceSensor port_left  = null;
    private DistanceSensor port_right = null;
    private DistanceSensor stbd_left  = null;
    private DistanceSensor stbd_right = null;
    private DistanceSensor back_left  = null;
    private DistanceSensor back_right = null;


    // baseline distance separating left and right sensors
    private final double port_base;
    private final double stbd_base;
    private final double back_base;

    // sensor names in robot configuration file
    private static final String PORT_LEFT  = "port s left";
    private static final String PORT_RIGHT = "port s right";
    private static final String STBD_LEFT  = "stbd s left";
    private static final String STBD_RIGHT = "stbd s right";
    private static final String BACK_LEFT  = "back s left";
    private static final String BACK_RIGHT = "back s right";

    public SkyStoneUltraRev(OpMode op)
    {
        super(op);

        port_base =  8;   // port sensor separation baseline in inches
        stbd_base =  8;   // stbd sensor separation baseline in inches
        back_base = 12;   // back sensor separation baseline in inches
    }

    public SkyStoneUltraRev(OpMode op, double port, double stbd, double back)
    {
        super(op);

        port_base = port;   // port sensor separation baseline in inches
        stbd_base = stbd;   // stbd sensor separation baseline in inches
        back_base = back;   // back sensor separation baseline in inches
    }

    // initialize the sensors
    public void init()
    {
        op_mode.telemetry.addData("sensors.init", "initializing sensors");
        op_mode.telemetry.update();

        try {
            op_mode.telemetry.addData("sensors.init", "port sensors");
            op_mode.telemetry.update();
            port_left  = op_mode.hardwareMap.get(DistanceSensor.class, PORT_LEFT);
            port_right = op_mode.hardwareMap.get(DistanceSensor.class, PORT_RIGHT);
        } catch (Exception e) {
            port_left = port_right = null;
            op_mode.telemetry.addData("sensors.init", e.getMessage());
        }

        try {
            op_mode.telemetry.addData("sensors.init", "stbd sensors");
            op_mode.telemetry.update();
            stbd_left  = op_mode.hardwareMap.get(DistanceSensor.class, STBD_LEFT);
            stbd_right = op_mode.hardwareMap.get(DistanceSensor.class, STBD_RIGHT);
        } catch (Exception e) {
            stbd_left = stbd_right = null;
            op_mode.telemetry.addData("sensors.init", e.getMessage());
        }

        try {
            op_mode.telemetry.addData("sensors.init", "back sensors");
            op_mode.telemetry.update();
            back_left  = op_mode.hardwareMap.get(DistanceSensor.class, BACK_LEFT);
            back_right = op_mode.hardwareMap.get(DistanceSensor.class, BACK_RIGHT);
        } catch (Exception e) {
            back_left = back_right = null;
            op_mode.telemetry.addData("sensors.init", e.getMessage());
        }

        op_mode.telemetry.addData("sensors.init", "sensors done");
        op_mode.telemetry.update();
    }

    private static long last_sensor_check = -1000000000L;
    private static Sensors last_sensor_value = new Sensors();

    private static final int min_sensor_time =   0;         // minimum delay, in milliseconds
    private static final int max_sensor_dist = 256;         // maximum distance, in inches

    @Override
    public Sensors check(boolean frnt, boolean port, boolean stbd, boolean back, boolean down) {
        long current_time = System.currentTimeMillis();

        if (last_sensor_check+min_sensor_time < current_time) {
            Sensors values = new Sensors();

            if (port && port_left != null && port_right != null) {
                values.port_left  = port_left.getDistance(DistanceUnit.INCH);
                values.port_left  = (values.port_left < 0) ? 0 : (max_sensor_dist < values.port_left) ? max_sensor_dist : values.port_left;
                values.port_right = port_right.getDistance(DistanceUnit.INCH);
                values.port_right = (values.port_right < 0) ? 0 : (max_sensor_dist < values.port_right) ? max_sensor_dist : values.port_right;
                values.port_min   = Math.min(values.port_left, values.port_right);
                values.port_angle = 180*Math.atan2(values.port_left - values.port_right, port_base)/Math.PI;
                values.port_valid = true;
                op_mode.telemetry.addData("check", "port: %6.1f %6.1f %6.1f", values.port_left, values.port_right, values.port_angle);
            }

            if (stbd && stbd_left != null && stbd_right != null) {
                values.stbd_left  = stbd_left.getDistance(DistanceUnit.INCH);
                values.stbd_left  = (values.stbd_left < 0) ? 0 : (max_sensor_dist < values.stbd_left) ? max_sensor_dist : values.stbd_left;
                values.stbd_right = stbd_right.getDistance(DistanceUnit.INCH);
                values.stbd_right = (values.stbd_right < 0) ? 0 : (max_sensor_dist < values.stbd_right) ? max_sensor_dist : values.stbd_right;
                values.stbd_min   = Math.min(values.stbd_left, values.stbd_right);
                values.stbd_angle = 180*Math.atan2(values.stbd_left - values.stbd_right, stbd_base)/Math.PI;
                values.stbd_valid = true;
                op_mode.telemetry.addData("check", "stbd: %6.1f %6.1f %6.1f", values.stbd_left, values.stbd_right, values.stbd_angle);
            }

            if (back && back_left != null && back_right != null) {
                values.back_left  = back_left.getDistance(DistanceUnit.INCH);
                values.back_left  = (values.back_left < 0) ? 0 : (max_sensor_dist < values.back_left) ? max_sensor_dist : values.back_left;
                values.back_right = back_right.getDistance(DistanceUnit.INCH);
                values.back_right = (values.back_right < 0) ? 0 : (max_sensor_dist < values.back_right) ? max_sensor_dist : values.back_right;
                values.back_min   = Math.min(values.back_left, values.back_right);
                values.back_angle = 180*Math.atan2(values.back_left - values.back_right, back_base)/Math.PI;
                values.back_valid = true;
                op_mode.telemetry.addData("check", "back: %6.1f %6.1f %6.1f", values.back_left, values.back_right, values.back_angle);
            }

            op_mode.telemetry.update();

            last_sensor_check = current_time;
            last_sensor_value = values;
        }

        return last_sensor_value;
    }

    // function to terminate sensors
    public void stop()
    {
    }
}
