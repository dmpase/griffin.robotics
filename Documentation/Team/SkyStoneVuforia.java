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


@TeleOp(name="SkyStoneVuforia", group ="Concept")
//@Disabled
public class SkyStoneVuforia extends LinearOpMode {

    public static final String VUFORIA_LICENSE_KEY = 
	"AWVXYZn/////AAAAGcG6g8XSSUMJsDaizcApOtsaA0fWzUQwImrdEn1MqH4JNqCzUwlyvEX0YALy7XyU" + 
	"eSpiANJkBg9kplUtcniUZKw8bF0dSpEfXZKXxn1yhbIohmpVmIK+Ngv1imYrkY6ePmvTfO2IpyQi5yO5" + 
	"ZmfSC8OzlH+XEMD0vRIXHMhxFpin7vTIHaoz8MEifSjRTznh1ZUSRnJfQ01KvMHEefES0kwhehlEKoqg" + 
	"pNMOYg0B5pV0bDDi9/Qh4eMR7sEk1GSx3QPxl/lYuZVcWSh8DutXv8oo9LhnbAaHTecCAR6gnNODow0W" + 
	"UAH2N9vxdLOjk2UfWVEJgqmHembIDHRzJN4fjcOECTFfLHIVmZ66GwgjPWxV";



    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private static final boolean PHONE_IS_PORTRAIT = false  ;

    // Since ImageTarget trackables use mm to specifiy their dimensions, we must use mm for all the physical dimension.
    // We will define some constants and conversions here
    private static final float mmPerInch      = 25.4f;
    private static final float mmTargetHeight = (6) * mmPerInch; // height of the center of the target image above the floor

    // Constant for Stone Target
    private static final float stoneZ = 2.00f * mmPerInch;

    // Constants for the center support targets
    private static final float bridgeZ = 6.42f * mmPerInch;
    private static final float bridgeY = 23 * mmPerInch;
    private static final float bridgeX = 5.18f * mmPerInch;
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

    private VuforiaTrackable stoneTarget     = null;
    private VuforiaTrackable blueRearBridge  = null;
    private VuforiaTrackable redRearBridge   = null;
    private VuforiaTrackable redFrontBridge  = null;
    private VuforiaTrackable blueFrontBridge = null;
    private VuforiaTrackable red1            = null;
    private VuforiaTrackable red2            = null;
    private VuforiaTrackable front1          = null;
    private VuforiaTrackable front2          = null;
    private VuforiaTrackable blue1           = null;
    private VuforiaTrackable blue2           = null;
    private VuforiaTrackable rear1           = null;
    private VuforiaTrackable rear2           = null;

    private List<VuforiaTrackable> allTrackables = null;

    final float CAMERA_FORWARD_DISPLACEMENT  = 4.0f * mmPerInch;   // eg: Camera is 4 Inches in front of robot center
    final float CAMERA_VERTICAL_DISPLACEMENT = 8.0f * mmPerInch;   // eg: Camera is 8 Inches above ground
    final float CAMERA_LEFT_DISPLACEMENT     = 0;                  // eg: Camera is ON the robot's center line

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
	// start by initializing space for tracking targets
	all_targets = new Target[TARGET_COUNT];
	for (int i=0; i < all_targets.length; i++) {
	    all_targets[i] = new Target(i);
	}

        // Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
        // We can pass Vuforia the handle to a camera preview resource (on the RC phone);
        // If no camera monitor is desired, use the parameter-less constructor instead (commented out below).
	if (CAMERA_MONITOR) {
	    cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
		"cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
	    parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
	} else {
	    cameraMonitorViewId = null;
	    parameters = new VuforiaLocalizer.Parameters();
	}

        parameters.vuforiaLicenseKey = VUFORIA_LICENSE_KEY;
        parameters.cameraDirection   = CAMERA_CHOICE;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the data sets for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");

        all_targets[STONE_TARGET].trackable = stoneTarget = targetsSkyStone.get(STONE_TARGET);
        stoneTarget.setName( all_targets[STONE_TARGET].name = "Stone Target" );

        all_targets[BLUE_REAR_BRIDGE].trackable = blueRearBridge = targetsSkyStone.get(BLUE_REAR_BRIDGE);
        blueRearBridge.setName( all_targets[BLUE_REAR_BRIDGE].name = "Blue Rear Bridge" );

        all_targets[RED_REAR_BRIDGE].trackable = redRearBridge = targetsSkyStone.get(RED_REAR_BRIDGE);
        redRearBridge.setName( all_targets[RED_REAR_BRIDGE].name = "Red Rear Bridge" );

        all_targets[RED_FRONT_BRIDGE].trackable = redFrontBridge = targetsSkyStone.get(RED_FRONT_BRIDGE);
        redFrontBridge.setName( all_targets[RED_FRONT_BRIDGE].name = "Red Front Bridge" );

        all_targets[BLUE_FRONT_BRIDGE].trackable = blueFrontBridge = targetsSkyStone.get(BLUE_FRONT_BRIDGE);
        blueFrontBridge.setName( all_targets[BLUE_FRONT_BRIDGE].name = "Blue Front Bridge" );

        all_targets[RED_PERIMETER_ONE].trackable = red1 = targetsSkyStone.get(RED_PERIMETER_ONE);
        red1.setName( all_targets[RED_PERIMETER_ONE].name = "Red Perimeter 1" );

        all_targets[RED_PERIMETER_TWO].trackable = red2 = targetsSkyStone.get(RED_PERIMETER_TWO);
        red2.setName( all_targets[RED_PERIMETER_TWO].name = "Red Perimeter 2" );

        all_targets[FRONT_PERIMETER_ONE].trackable = front1 = targetsSkyStone.get(FRONT_PERIMETER_ONE);
        front1.setName( all_targets[FRONT_PERIMETER_ONE].name = "Front Perimeter 1" );

        all_targets[FRONT_PERIMETER_TWO].trackable = front2 = targetsSkyStone.get(FRONT_PERIMETER_TWO);
        front2.setName( all_targets[FRONT_PERIMETER_TWO].name = "Front Perimeter 2" );

        all_targets[BLUE_PERIMETER_ONE].trackable = blue1 = targetsSkyStone.get(BLUE_PERIMETER_ONE);
        blue1.setName( all_targets[BLUE_PERIMETER_ONE].name = "Blue Perimeter 1" );

        all_targets[BLUE_PERIMETER_TWO].trackable = blue2 = targetsSkyStone.get(BLUE_PERIMETER_TWO);
        blue2.setName( all_targets[BLUE_PERIMETER_TWO].name = "Blue Perimeter 2" );

        all_targets[REAR_PERIMETER_ONE].trackable = rear1 = targetsSkyStone.get(REAR_PERIMETER_ONE);
        rear1.setName( all_targets[REAR_PERIMETER_ONE].name = "Rear Perimeter 1" );

        all_targets[REAR_PERIMETER_TWO].trackable = rear2 = targetsSkyStone.get(REAR_PERIMETER_TWO);
        rear2.setName( all_targets[REAR_PERIMETER_TWO].name = "Rear Perimeter 2" );


        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsSkyStone);

        /**
         * In order for localization to work, we need to tell the system where each target is on the field, and
         * where the phone resides on the robot.  These specifications are in the form of <em>transformation matrices.</em>
         * Transformation matrices are a central, important concept in the math here involved in localization.
         * See <a href="https://en.wikipedia.org/wiki/Transformation_matrix">Transformation Matrix</a>
         * for detailed information. Commonly, you'll encounter transformation matrices as instances
         * of the {@link OpenGLMatrix} class.
         *
         * If you are standing in the Red Alliance Station looking towards the center of the field,
         *     - The X axis runs from your left to the right. (positive from the center to the right)
         *     - The Y axis runs from the Red Alliance Station towards the other side of the field
         *       where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
         *     - The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
         *
         * Before being transformed, each target image is conceptually located at the origin of the field's
         *  coordinate system (the center of the field), facing up.
         */

        // Set the position of the Stone Target.  Since it's not fixed in position, assume it's at the field origin.
        // Rotated it to to face forward, and raised it to sit on the ground correctly.
        // This can be used for generic target-centric approach algorithms
        stoneTarget.setLocation(OpenGLMatrix
                .translation(0, 0, stoneZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        //Set the position of the bridge support targets with relation to origin (center of field)
        blueFrontBridge.setLocation(OpenGLMatrix
                .translation(-bridgeX, bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, bridgeRotZ)));

        blueRearBridge.setLocation(OpenGLMatrix
                .translation(-bridgeX, bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, bridgeRotZ)));

        redFrontBridge.setLocation(OpenGLMatrix
                .translation(-bridgeX, -bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, 0)));

        redRearBridge.setLocation(OpenGLMatrix
                .translation(bridgeX, -bridgeY, bridgeZ)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, 0)));

        //Set the position of the perimeter targets with relation to origin (center of field)
        red1.setLocation(OpenGLMatrix
                .translation(quadField, -halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

        red2.setLocation(OpenGLMatrix
                .translation(-quadField, -halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

        front1.setLocation(OpenGLMatrix
                .translation(-halfField, -quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90)));

        front2.setLocation(OpenGLMatrix
                .translation(-halfField, quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));

        blue1.setLocation(OpenGLMatrix
                .translation(-quadField, halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        blue2.setLocation(OpenGLMatrix
                .translation(quadField, halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        rear1.setLocation(OpenGLMatrix
                .translation(halfField, quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , -90)));

        rear2.setLocation(OpenGLMatrix
                .translation(halfField, -quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

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

        // We need to rotate the camera around it's long axis to bring the correct camera forward.
        if (CAMERA_CHOICE == BACK) {
            phoneYRotate = -90;
        } else {
            phoneYRotate = 90;
        }

        // Rotate the phone vertical about the X axis if it's in portrait mode
        if (PHONE_IS_PORTRAIT) {
            phoneXRotate = 90 ;
        }

        // Next, translate the camera lens to where it is on the robot.
        // In this example, it is centered (left to right), but forward of the middle of the robot, and above ground level.
        OpenGLMatrix robotFromCamera = OpenGLMatrix
	    .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
	    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));

        // Let all the trackable listeners know where the phone is.
        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener())
		.setPhoneInformation(robotFromCamera, parameters.cameraDirection);
        }
    }

    public void activate()
    {
        targetsSkyStone.activate();
    }

    public void deactivate()
    {
        targetsSkyStone.deactivate();
    }


    public static class Target {
        VuforiaTrackable trackable  = null;
	boolean          is_visible = false;
	String           name       = null;
	double           id         = 0;
	double           x          = 0;
	double           y          = 0;
	double           z          = 0;
	double           roll       = 0;
	double           pitch      = 0;
	double           yaw        = 0;

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
    }

    public static Target[] all_targets = null;

    public Target[] check()
    {
	for (Target target : all_targets) {
	    VuforiaTrackable trackable = target.trackable;
	    if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
		target.is_visible = true;

		// getUpdatedRobotLocation() will return null if no new information is available since
		// the last time that call was made, or if the trackable is not currently visible.
		OpenGLMatrix robotLocationTransform = 
		    ((VuforiaTrackableDefaultListener)trackable.getListener()).getUpdatedRobotLocation();

		if (robotLocationTransform != null) {
		    VectorF translation = robotLocationTransform.getTranslation();
		    target.x = translation.get(0) / mmPerInch;
		    target.y = translation.get(1) / mmPerInch;
		    target.z = translation.get(2) / mmPerInch;

		    // express the rotation of the robot in degrees.
		    Orientation rotation = Orientation.getOrientation(robotLocationTransform, EXTRINSIC, XYZ, DEGREES);
		    target.roll  = rotation.firstAngle;
		    target.pitch = rotation.secondAngle;
		    target.yaw   = rotation.thirdAngle;
		}
	    } else {
		target.is_visible = false;
		target.x          = 0;
		target.y          = 0;
		target.z          = 0;
		target.roll       = 0;
		target.pitch      = 0;
		target.yaw        = 0;
	    }
	}

	return all_targets;
    }

    @Override public void runOpMode() 
    {
	SkyStoneVuforia vuforia = new SkyStoneVuforia();

	vuforia.init();

        waitForStart();

	vuforia.activate();
        while (!isStopRequested()) {

            // check all the trackable targets to see which one (if any) is visible.
            Target[] all_targets = check();

	    for (Target target: all_targets) {
		if (target.is_visible) {
		    telemetry.addData(target.name, "{X, Y, Z} = %.1f, %.1f, %.1f", target.x, target.y, target.z);
		}
	    }
            telemetry.update();
        }

        // Disable Tracking when we are done;
	vuforia.deactivate();
    }
}
