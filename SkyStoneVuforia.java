package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.*;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.*;


public class SkyStoneVuforia {

    public static final int FRONT_CAMERA = 0;
    public static final int BACK_CAMERA  = 1;

    private LinearOpMode op = null;
    private final VuforiaLocalizer.CameraDirection CAMERA_CHOICE;

    public SkyStoneVuforia(LinearOpMode me)
    {
        CAMERA_CHOICE = BACK;
        op = me;
    }

    public SkyStoneVuforia(LinearOpMode me, int camera)
    {
        if (camera == FRONT_CAMERA) {
            CAMERA_CHOICE = FRONT;
        } else if (camera == BACK_CAMERA) {
            CAMERA_CHOICE = BACK;
        } else {
            CAMERA_CHOICE = BACK;
        }
        op = me;
    }

    public static final String VUFORIA_LICENSE_KEY = 
	    "AWVXYZn/////AAAAGcG6g8XSSUMJsDaizcApOtsaA0fWzUQwImrdEn1MqH4JNqCzUwlyvEX0YALy7XyUeSpiANJkBg9kplUtcniUZKw8bF0dSpEfXZKXxn1yhbIohmpVmIK+Ngv1imYrkY6ePmvTfO2IpyQi5yO5ZmfSC8OzlH+XEMD0vRIXHMhxFpin7vTIHaoz8MEifSjRTznh1ZUSRnJfQ01KvMHEefES0kwhehlEKoqgpNMOYg0B5pV0bDDi9/Qh4eMR7sEk1GSx3QPxl/lYuZVcWSh8DutXv8oo9LhnbAaHTecCAR6gnNODow0WUAH2N9vxdLOjk2UfWVEJgqmHembIDHRzJN4fjcOECTFfLHIVmZ66GwgjPWxV";



    private static final boolean PHONE_IS_PORTRAIT = true;

    // Since ImageTarget trackables use mm to specifiy their dimensions, we must use mm for all the physical dimension.
    // We will define some constants and conversions here
    private static final float mmPerInch      = 25.4f;
    private static final float mmTargetHeight = (6) * mmPerInch; // height of the center of the target image above the floor

    // Constant for Stone Target
    private static final float stoneZ = 2.00f * mmPerInch;

    // Constants for the center support targets
    private static final float bridgeZ    = 6.42f * mmPerInch;
    private static final float bridgeY    = 23f   * mmPerInch;
    private static final float bridgeX    = 5.18f * mmPerInch;
    private static final float bridgeRotY = 59;                                 // Units are degrees
    private static final float bridgeRotZ = 180;

    // Constants for perimeter targets
    private static final float halfField = 72 * mmPerInch;
    private static final float quadField = 36 * mmPerInch;

    // Class Members
    private OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia  = null;
    private boolean targetVisible     = false;
    private float phoneXRotate        = 0;
    private float phoneYRotate        = 0;
    private float phoneZRotate        = 0;



    private int cameraMonitorViewId = -1;
    private VuforiaLocalizer.Parameters parameters = null;

    private static final boolean CAMERA_MONITOR = true;

    private VuforiaTrackables targetsSkyStone = null;

    private List<VuforiaTrackable> allTrackables = null;

    private static final float CAMERA_FORWARD_DISPLACEMENT  = 4.0f * mmPerInch;   // eg: Camera is 4 Inches in front of robot center
    private static final float CAMERA_VERTICAL_DISPLACEMENT = 8.0f * mmPerInch;   // eg: Camera is 8 Inches above ground
    private static final float CAMERA_LEFT_DISPLACEMENT     = 0;                  // eg: Camera is ON the robot's center line

    public static final int STONE_TARGET        =  0;
    public static final int BLUE_REAR_BRIDGE    =  1;
    public static final int RED_REAR_BRIDGE     =  2;
    public static final int RED_FRONT_BRIDGE    =  3;
    public static final int BLUE_FRONT_BRIDGE   =  4;
    public static final int RED_PERIMETER_ONE   =  5;
    public static final int RED_PERIMETER_TWO   =  6;
    public static final int FRONT_PERIMETER_ONE =  7;
    public static final int FRONT_PERIMETER_TWO =  8;
    public static final int BLUE_PERIMETER_ONE  =  9;
    public static final int BLUE_PERIMETER_TWO  = 10;
    public static final int REAR_PERIMETER_ONE  = 11;
    public static final int REAR_PERIMETER_TWO  = 12;
    public static final int TARGET_COUNT        = REAR_PERIMETER_TWO + 1;


    public void init()
    {
        long time = 10;
        String caption = "SkyStoneVuforia.init";

        op.telemetry.addData(caption, "Starting Vuforia init"); op.telemetry.update();

    	// start by initializing space for tracking targets
    	all_targets = new Target[TARGET_COUNT];
    	for (int i=0; i < all_targets.length; i++) {
    	    all_targets[i] = new Target(i);
    	}

        sleep(time); op.telemetry.addData(caption, "Camera orientation"); op.telemetry.update();

        // Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
        // We can pass Vuforia the handle to a camera preview resource (on the RC phone);
        // If no camera monitor is desired, use the parameter-less constructor instead (commented out below).
        if (CAMERA_MONITOR) {
            cameraMonitorViewId = op.hardwareMap.appContext.getResources().getIdentifier(
            "cameraMonitorViewId", "id", op.hardwareMap.appContext.getPackageName());
            parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        } else {
            cameraMonitorViewId = -1;
            parameters = new VuforiaLocalizer.Parameters();
        }

        sleep(time); op.telemetry.addData(caption, "Creating Vuforia instance"); op.telemetry.update();

        parameters.vuforiaLicenseKey = VUFORIA_LICENSE_KEY;
        parameters.cameraDirection   = CAMERA_CHOICE;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        sleep(time); op.telemetry.addData(caption, "Loading trackable list"); op.telemetry.update();

        if (true) {
            sleep(time); op.telemetry.addData(caption, "Loading trackable list"); op.telemetry.update();

            // Load the data sets for the trackable objects. These particular data
            // sets are stored in the 'assets' part of our application.
            targetsSkyStone = vuforia.loadTrackablesFromAsset("Skystone");

            sleep(time); op.telemetry.addData(caption, "Loading targets"); op.telemetry.update();

            all_targets[STONE_TARGET].trackable = targetsSkyStone.get(STONE_TARGET);
            all_targets[STONE_TARGET].trackable.setName( all_targets[STONE_TARGET].name = "Stone Target" );

            all_targets[BLUE_REAR_BRIDGE].trackable = targetsSkyStone.get(BLUE_REAR_BRIDGE);
            all_targets[BLUE_REAR_BRIDGE].trackable.setName( all_targets[BLUE_REAR_BRIDGE].name = "Blue Rear Bridge" );

            all_targets[RED_REAR_BRIDGE].trackable = targetsSkyStone.get(RED_REAR_BRIDGE);
            all_targets[BLUE_REAR_BRIDGE].trackable.setName( all_targets[RED_REAR_BRIDGE].name = "Red Rear Bridge" );

            all_targets[RED_FRONT_BRIDGE].trackable = targetsSkyStone.get(RED_FRONT_BRIDGE);
            all_targets[BLUE_REAR_BRIDGE].trackable.setName( all_targets[RED_FRONT_BRIDGE].name = "Red Front Bridge" );

            all_targets[BLUE_FRONT_BRIDGE].trackable = targetsSkyStone.get(BLUE_FRONT_BRIDGE);
            all_targets[BLUE_REAR_BRIDGE].trackable.setName( all_targets[BLUE_FRONT_BRIDGE].name = "Blue Front Bridge" );

            all_targets[RED_PERIMETER_ONE].trackable = targetsSkyStone.get(RED_PERIMETER_ONE);
            all_targets[RED_PERIMETER_ONE].trackable.setName( all_targets[RED_PERIMETER_ONE].name = "Red Perimeter 1" );

            all_targets[RED_PERIMETER_TWO].trackable = targetsSkyStone.get(RED_PERIMETER_TWO);
            all_targets[RED_PERIMETER_TWO].trackable.setName( all_targets[RED_PERIMETER_TWO].name = "Red Perimeter 2" );

            all_targets[FRONT_PERIMETER_ONE].trackable = targetsSkyStone.get(FRONT_PERIMETER_ONE);
            all_targets[FRONT_PERIMETER_ONE].trackable.setName( all_targets[FRONT_PERIMETER_ONE].name = "Front Perimeter 1" );

            all_targets[FRONT_PERIMETER_TWO].trackable = targetsSkyStone.get(FRONT_PERIMETER_TWO);
            all_targets[FRONT_PERIMETER_TWO].trackable.setName( all_targets[FRONT_PERIMETER_TWO].name = "Front Perimeter 2" );

            all_targets[BLUE_PERIMETER_ONE].trackable = targetsSkyStone.get(BLUE_PERIMETER_ONE);
            all_targets[BLUE_PERIMETER_ONE].trackable.setName( all_targets[BLUE_PERIMETER_ONE].name = "Blue Perimeter 1" );

            all_targets[BLUE_PERIMETER_TWO].trackable = targetsSkyStone.get(BLUE_PERIMETER_TWO);
            all_targets[BLUE_PERIMETER_TWO].trackable.setName( all_targets[BLUE_PERIMETER_TWO].name = "Blue Perimeter 2" );

            all_targets[REAR_PERIMETER_ONE].trackable = targetsSkyStone.get(REAR_PERIMETER_ONE);
            all_targets[REAR_PERIMETER_ONE].trackable.setName( all_targets[REAR_PERIMETER_ONE].name = "Rear Perimeter 1" );

            all_targets[REAR_PERIMETER_TWO].trackable = targetsSkyStone.get(REAR_PERIMETER_TWO);
            all_targets[REAR_PERIMETER_TWO].trackable.setName( all_targets[REAR_PERIMETER_TWO].name = "Rear Perimeter 2" );


            // op.telemetry.addData(caption, "Creating ArrayList of trackables"); op.telemetry.update(); sleep(time);

            // For convenience, gather together all the trackable objects in one easily-iterable collection
            // allTrackables = new ArrayList<VuforiaTrackable>();
            // allTrackables.addAll(targetsSkyStone);

            if (local) {
                sleep(time); op.telemetry.addData(caption, "Locating targets"); op.telemetry.update();

                // Set the position of the Stone Target.  Since it's not fixed in position, assume it's at the field origin.
                // Rotated it to to face forward, and raised it to sit on the ground correctly.
                // This can be used for generic target-centric approach algorithms
                all_targets[STONE_TARGET].trackable.setLocation(OpenGLMatrix
                        .translation(0, 0, stoneZ)
                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

                //Set the position of the bridge support targets with relation to origin (center of field)
                all_targets[BLUE_FRONT_BRIDGE].trackable.setLocation(OpenGLMatrix
                        .translation(-bridgeX, bridgeY, bridgeZ)
                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, bridgeRotZ)));

                all_targets[BLUE_REAR_BRIDGE].trackable.setLocation(OpenGLMatrix
                        .translation(-bridgeX, bridgeY, bridgeZ)
                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, bridgeRotZ)));

                all_targets[RED_FRONT_BRIDGE].trackable.setLocation(OpenGLMatrix
                        .translation(-bridgeX, -bridgeY, bridgeZ)
                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, 0)));

                all_targets[RED_REAR_BRIDGE].trackable.setLocation(OpenGLMatrix
                        .translation(bridgeX, -bridgeY, bridgeZ)
                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, 0)));

                //Set the position of the perimeter targets with relation to origin (center of field)
                all_targets[RED_PERIMETER_ONE].trackable.setLocation(OpenGLMatrix
                        .translation(quadField, -halfField, mmTargetHeight)
                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

                all_targets[RED_PERIMETER_TWO].trackable.setLocation(OpenGLMatrix
                        .translation(-quadField, -halfField, mmTargetHeight)
                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

                all_targets[FRONT_PERIMETER_ONE].trackable.setLocation(OpenGLMatrix
                        .translation(-halfField, -quadField, mmTargetHeight)
                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));

                all_targets[FRONT_PERIMETER_TWO].trackable.setLocation(OpenGLMatrix
                        .translation(-halfField, quadField, mmTargetHeight)
                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));

                all_targets[BLUE_PERIMETER_ONE].trackable.setLocation(OpenGLMatrix
                        .translation(-quadField, halfField, mmTargetHeight)
                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

                all_targets[BLUE_PERIMETER_TWO].trackable.setLocation(OpenGLMatrix
                        .translation(quadField, halfField, mmTargetHeight)
                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

                all_targets[REAR_PERIMETER_ONE].trackable.setLocation(OpenGLMatrix
                        .translation(halfField, quadField, mmTargetHeight)
                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

                all_targets[REAR_PERIMETER_TWO].trackable.setLocation(OpenGLMatrix
                        .translation(halfField, -quadField, mmTargetHeight)
                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));
            }

            //
            // Create a transformation matrix describing where the phone is on the robot.
            //
            // NOTE !!!!  It's very important that you turn OFF your phone's Auto-Screen-Rotation option.
            // Lock it into Portrait for these numbers to work.
            //
            // Info:  The coordinate frame for the robot looks the same as the field.
            // The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
            // Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
            //
            // The phone starts out lying flat, with the screen facing Up and with the physical top of the phone
            // pointing to the LEFT side of the Robot.
            // The two examples below assume that the camera is facing forward out the front of the robot.


            if (xlate) {
                sleep(time); op.telemetry.addData(caption, "Setting camera information"); op.telemetry.update();

                // We need to rotate the camera around it's long axis to bring the correct camera forward.
                if (CAMERA_CHOICE == BACK) {
                    phoneYRotate = -90;
                } else {
                    phoneYRotate = 90;
                }

                // Rotate the phone vertical about the X axis if it's in portrait mode
                if (PHONE_IS_PORTRAIT) {
                    phoneXRotate = 90;
                }

                // Next, translate the camera lens to where it is on the robot.
                // In this example, it is centered (port_left to stbd_left), but forward of the middle of the robot, and above ground level.
                OpenGLMatrix robotFromCamera = OpenGLMatrix
                        .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                        .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));

                // Let all the trackable listeners know where the phone is.
                for (Target target : all_targets) {
                    VuforiaTrackable trackable = target.trackable;

                    ((VuforiaTrackableDefaultListener) trackable.getListener())
                            .setPhoneInformation(robotFromCamera, parameters.cameraDirection);
                }
            }

        }

        sleep(time); op.telemetry.addData(caption, "Done with initialization"); op.telemetry.update();
    }

    private static final boolean xlate = true;
    private static final boolean local = true;

    public void activate()
    {
        targetsSkyStone.activate();
    }

    public void deactivate()
    {
        targetsSkyStone.deactivate();
    }


    public static class Target {
        public  boolean is_visible = false;
        public  String  name       = null;
        public  int     id         = 0;
        public  double  x          = 0;
        public  double  y          = 0;
        public  double  z          = 0;
        public  double  bearing    = 0;

        private VuforiaTrackable trackable  = null;
        private double           roll       = 0;
        private double           pitch      = 0;
        private double           yaw        = 0;

        public Target(int i)
        {
            trackable  = null;
            is_visible = false;
            name       = null;
            id         = i;
            x          = 0;
            y          = 0;
            z          = 0;
            roll       = 0;
            pitch      = 0;
            yaw        = 0;
        }

        public void xlate()
        {
            VuforiaTrackableDefaultListener listener = (VuforiaTrackableDefaultListener) trackable.getListener();

            if (listener.isVisible()) {
                is_visible = true;

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                OpenGLMatrix robotLocationTransform = listener.getUpdatedRobotLocation();

                if (robotLocationTransform != null) {
                    VectorF translation = robotLocationTransform.getTranslation();
                    x = translation.get(0) / mmPerInch;
                    y = translation.get(1) / mmPerInch;
                    z = translation.get(2) / mmPerInch;

                    // x is originally the inches the robot is to the stbd_left of the object
                    // this is corrected by changing the sign, so
                    // x is now the distance the object is to the stbd_left of the robot
                    x = -x;

                    // y is originally the inches the robot is above the object
                    // this is corrected by changing the sign, so
                    // y is now the distance the object is above the robot
                    y = -y;

                    // z is in inches from the robot in the forward (0 degree bearing) direction
                    // no correction is needed


                    // express the rotation of the robot in degrees.
                    Orientation rotation = Orientation.getOrientation(robotLocationTransform, EXTRINSIC, XYZ, DEGREES);
                    roll  = rotation.firstAngle;
                    pitch = rotation.secondAngle;
                    yaw   = rotation.thirdAngle;

                    // a roll of 30 degrees indicates the object is rolling 30 degrees to the port_left
                    // relative to a horizontal phone. this isn't what we want, but these numbers
                    // are just not reliable.
                    roll  = rotation.thirdAngle;
                    pitch = rotation.secondAngle;
                    yaw   = rotation.firstAngle;


                    // bearing is computed from x and z only
                    bearing = 90 - 180 * Math.atan2(z,x) / Math.PI;
                }
            } else {
                is_visible = false;
            }
        }
    }

    public Target[] all_targets = null;

    public Target[] check()
    {
        for (Target target : all_targets) {
            target.xlate();
        }

	    return all_targets;
    }

    public Target first_visible_target()
    {
        for (Target target : all_targets) {
            op.telemetry.addData("first_visible_target", "xlate "+target); op.telemetry.update(); sleep(10);
            target.xlate();
        }

        for (Target target : all_targets) {
            if (target.is_visible) return target;
        }

        return null;
    }

    public static final int UNKNOWN_ZONE       =  0;
    public static final int BLUE_LOADING_ZONE  =  1;
    public static final int BLUE_BUILDING_ZONE =  2;
    public static final int RED_LOADING_ZONE   =  3;
    public static final int RED_BUILDING_ZONE  =  4;

    public static final String [] locations = {"Unknown Zone", "Blue Loading Zone", "Blue Building Zone", "Red Loading Zone", "Red Building Zone"};

    public int identify_location()
    {
        int action = UNKNOWN_ZONE;

        op.telemetry.addData("identify_location", "reading camera");
        op.telemetry.update();

        SkyStoneVuforia.Target target = first_visible_target();

        op.telemetry.addData("identify_location", ((target == null)?"target=null":"target="+target.name));
        op.telemetry.update();

        if (target == null) {
            action = UNKNOWN_ZONE;
        } else if (target.id == SkyStoneVuforia.BLUE_PERIMETER_ONE) {
            action = BLUE_LOADING_ZONE;
        } else if (target.id == SkyStoneVuforia.BLUE_PERIMETER_TWO) {
            action = BLUE_BUILDING_ZONE;
        } else if (target.id == SkyStoneVuforia.RED_PERIMETER_ONE) {
            action = RED_BUILDING_ZONE;
        } else if (target.id == SkyStoneVuforia.RED_PERIMETER_TWO) {
            action = RED_LOADING_ZONE;
        } else if (target.id == SkyStoneVuforia.FRONT_PERIMETER_ONE) {
            action = RED_LOADING_ZONE;
        } else if (target.id == SkyStoneVuforia.FRONT_PERIMETER_TWO) {
            action = BLUE_LOADING_ZONE;
        } else if (target.id == SkyStoneVuforia.REAR_PERIMETER_ONE) {
            action = BLUE_BUILDING_ZONE;
        } else if (target.id == SkyStoneVuforia.REAR_PERIMETER_TWO) {
            action = RED_BUILDING_ZONE;
        }

        op.telemetry.addData("identify_location", ((target == null)?"target=null":"target="+target.name));
        op.telemetry.addData("identify_location", "action="+action);
        op.telemetry.update();

        return action;
    }

    public void sleep(long millis)
    {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            ;
        }
    }
}
