package org.firstinspires.ftc.teamcode.opmodes.auton;

import static org.firstinspires.ftc.teamcode.components.GamePositions.*;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.TensorFlowDetector;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Blue Team Autonomous Far", group = "Autonomous")
public class BlueTeamStartFar extends LinearOpMode {

    // Constants
    private static final double DEG_THRESHOLD = 0.0;

    // Components
    private ArmSystem armSystem;
    private SampleMecanumDrive drive;
    private TensorFlowDetector detector;

    // Fields
    private char path;
    private TrajectorySequence trajectory;

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        findModel(); // This will sometimes, but not always wait for start

        while (!isStarted()) {
            sleep(10);
        }

        drive.followTrajectorySequence(trajectory);
    }

    private void findModel() {

        while (!isStarted()) {
            detector.updateRecognitions();
            sleep (250);
            if (detector.getHighestConfidenceRecognition().getConfidence() > 0.95) {
                break;
            }
        } // Keep searching for the model until the opMode is started. If the model is found with
        // high confidence, stop searching lest the model breaks

        if (detector.getNumRecognitions() == 0) {
            telemetry.addData("Object Detected - ", "No object was detected with a confidence above %f", detector.getConfidenceThreshold());
            telemetry.addData("Path Chosen - ", "Estimated angle = NULL deg, ready to follow r path");
            path = 'r';
        } else if (detector.getHighestConfidenceRecognition().estimateAngleToObject(AngleUnit.DEGREES) < DEG_THRESHOLD) {
            telemetry.addData("Object Detected - ", "A(n) %s was found with %f confidence", detector.getHighestConfidenceRecognition().getLabel(), detector.getHighestConfidenceRecognition().getConfidence());
            path = 'l';
            telemetry.addData("Path Chosen - ", "Estimated angle = %f deg, ready to follow %c path", detector.getHighestConfidenceRecognition().estimateAngleToObject(AngleUnit.DEGREES), path);
        } else {
            path = 'c';
            telemetry.addData("Path Chosen - ", "Estimated angle = %f deg, ready to follow %c path", detector.getHighestConfidenceRecognition().estimateAngleToObject(AngleUnit.DEGREES), path);
        } // Set the path to the appropriate path ('l'eft, 'r'ight, 'c'enter), and update the telemetry to let us know whats going on

        if (path == 'l') {
            buildLeftPath(drive);
        } else if (path == 'c') {
            buildCenterPath(drive);
        } else if (path == 'r') {
            buildRightPath(drive);
        } else {
            throw new IllegalStateException("Path was not 'c', 'l', or 'r'");
        }// build the appropriate path

    }


    private void initialize() {

        armSystem = new ArmSystem(
                hardwareMap.get(DcMotor.class, "arm_left"),
                hardwareMap.get(DcMotor.class, "arm_right"),
                hardwareMap.get(Servo.class, "servo_left"),
                hardwareMap.get(Servo.class, "servo_right"),
                hardwareMap.get(Servo.class, "intake_left"),
                hardwareMap.get(Servo.class, "intake_right")
        );
        drive = new SampleMecanumDrive(hardwareMap);

        detector = new TensorFlowDetector("2023_Blue_Team_Object_3770.tflite", new String[]{"Blue_Owl"}, telemetry, hardwareMap);
        detector.initModel();


        { // Old Code
            //detector.updateRecognitions();
            //Recognition teamObject = detector.getHighestConfidenceRecognition();

//        if (teamObject == null) {
//            telemetry.addData("Object Detected - ", "No object was detected with a confidence above %f", detector.getConfidenceThreshold());
//            telemetry.addData("Path Chosen - ", "Estimated angle = NULL deg, ready to follow c path");
//            path = 'r';
//        } else {
//            telemetry.addData("Object Detected - ", "A(n) %s was found with %f confidence", teamObject.getLabel(), teamObject.getConfidence());
//            if (teamObject.estimateAngleToObject(AngleUnit.DEGREES) < DEG_THRESHOLD) {
//                path = 'l';
//            } else {
//                path = 'c';
//            }
//            telemetry.addData("Path Chosen - ", "Estimated angle = %f deg, ready to follow %c path", teamObject.estimateAngleToObject(AngleUnit.DEGREES), path);
//        }
        }

    }


    //**********************************************************************************************
    //**************************************** PATHS ***********************************************
    //**********************************************************************************************

    private void buildLeftPath(SampleMecanumDrive drive) {
        trajectory = drive.trajectorySequenceBuilder(BLUE_START_POS_2)
                .splineToSplineHeading(BLUE_OBJECT_POS_4_1, Math.toRadians(-100))
                .splineToSplineHeading(BLUE_OBJECT_POS_4_2, Math.toRadians(-30))
                .waitSeconds(1)
                .splineToSplineHeading(BLUE_OBJECT_POS_4_3, Math.toRadians(135))
                .splineToSplineHeading(BLUE_WAYPOINT_1, Math.toRadians(0))
                .splineToSplineHeading(BLUE_WAYPOINT_1_5, Math.toRadians(0))
                .splineToSplineHeading(BLUE_WAYPOINT_2, Math.toRadians(45))
                .splineToSplineHeading(BLUE_BACKDROP_LEFT, Math.toRadians(0))
                .build();

        // Ready to Test
    }

    private void buildCenterPath(SampleMecanumDrive drive) {
        trajectory = drive.trajectorySequenceBuilder(BLUE_START_POS_2)
                .splineToSplineHeading(BLUE_OBJECT_POS_6_1, Math.toRadians(-100))
                .splineToSplineHeading(BLUE_OBJECT_POS_6_2, Math.toRadians(-90))
                .waitSeconds(1)
                .splineToConstantHeading(BLUE_OBJECT_POS_6_3.vec(), BLUE_OBJECT_POS_6_3.getHeading())
                .splineToSplineHeading(BLUE_WAYPOINT_1, Math.toRadians(0))
                .splineToSplineHeading(BLUE_WAYPOINT_1_5, Math.toRadians(0))
                .splineToSplineHeading(BLUE_WAYPOINT_2, Math.toRadians(45))
                .splineToSplineHeading(BLUE_BACKDROP_CENTER, Math.toRadians(0))
                .build();

        // Ready to Test
    }

    private void buildRightPath(SampleMecanumDrive drive) {
        trajectory = drive.trajectorySequenceBuilder(BLUE_START_POS_2)
                .splineToSplineHeading(BLUE_OBJECT_POS_5_1, Math.toRadians(-100))
                .splineToSplineHeading(BLUE_OBJECT_POS_5_2, Math.toRadians(-90))
                .waitSeconds(1)
                .lineToConstantHeading(BLUE_OBJECT_POS_5_4.vec())
                .turn(Math.toRadians(-60))
                .lineToConstantHeading(BLUE_OBJECT_POS_5_3.vec())
                .splineToSplineHeading(BLUE_WAYPOINT_1, Math.toRadians(0))
                .splineToSplineHeading(BLUE_WAYPOINT_1_5, Math.toRadians(0))
                .splineToSplineHeading(BLUE_WAYPOINT_2, Math.toRadians(45))
                .splineToSplineHeading(BLUE_BACKDROP_RIGHT, Math.toRadians(0))
                .build();

        // Ready to Test
    }

    private void placeYellowPixel() {
        //@TODO Implement placeYellowPixel()
    }

    private void dropPurplePixel() {
        armSystem.driveToLevel(1, 2);
        //@TODO Implement dropPurplePixel()
    }

}