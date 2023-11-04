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

@Autonomous(name = "Red Team Autonomous Close", group = "Autonomous")
public class RedTeamStartClose extends LinearOpMode {

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
        telemetry.addLine("test 1");
        initialize();
        telemetry.addLine("test 2");
        findModel(); // This will sometimes, but not always wait for start
        telemetry.update();
        while (!isStarted()) {
            sleep(10);
        }

        drive.followTrajectorySequence(trajectory);
    }

    private void findModel() {
        telemetry.addLine("test 3");
        while (!isStarted()) {
            detector.updateRecognitions();
            detector.updateTelemetry(true, true, true, true, true);
            telemetry.addLine("Current Threshold: " + detector.getConfidenceThreshold());
            telemetry.update();
            sleep(250);
            if (detector.getNumRecognitions() != 0) {
                if (detector.getHighestConfidenceRecognition().getConfidence() > 0.95) {
                    break;
                }
            }

        }// Keep searching for the model until the opMode is started. If the model is found with
        // high confidence, stop searching lest the model breaks

        if (detector.getNumRecognitions() == 0) {
            telemetry.addData("Object Detected - ", "No object was detected with a confidence above %f", detector.getConfidenceThreshold());
            telemetry.addData("Path Chosen - ", "Estimated angle = NULL deg, ready to follow 'r' path");
            telemetry.update();

            path = 'r';
        } else if (detector.getHighestConfidenceRecognition().estimateAngleToObject(AngleUnit.DEGREES) < DEG_THRESHOLD) {
            telemetry.addData("Object Detected - ", "A(n) %s was found with %f confidence", detector.getHighestConfidenceRecognition().getLabel(), detector.getHighestConfidenceRecognition().getConfidence());
            path = 'l';
            telemetry.addData("Path Chosen - ", "Estimated angle = %f deg, ready to follow %c path", detector.getHighestConfidenceRecognition().estimateAngleToObject(AngleUnit.DEGREES), path);
            telemetry.update();

        } else {
            path = 'c';
            telemetry.addData("Path Chosen - ", "Estimated angle = %f deg, ready to follow %c path", detector.getHighestConfidenceRecognition().estimateAngleToObject(AngleUnit.DEGREES), path);
            telemetry.update();
        }

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
        drive.setPoseEstimate(RED_START_POS_1);

        detector = new TensorFlowDetector("2023_Red_Team_Object_7330.tflite", new String[]{"Red_Owl"}, telemetry, hardwareMap);
        detector.initModel();


    }


    //**********************************************************************************************
    //**************************************** PATHS ***********************************************
    //**********************************************************************************************

    private void buildLeftPath(SampleMecanumDrive drive) {
        trajectory = drive.trajectorySequenceBuilder(RED_START_POS_1)
                .turn(Math.toRadians(-60))
                .splineTo(RED_OBJECT_POS_3.vec(), RED_OBJECT_POS_3.getHeading())
                .addTemporalMarker(() -> dropPurplePixel()) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1)
                .lineToLinearHeading(RED_BACKDROP_RIGHT)
                .addTemporalMarker(() -> placeYellowPixel())
                .build();
    }

    private void buildCenterPath(SampleMecanumDrive drive) {
        trajectory = drive.trajectorySequenceBuilder(RED_START_POS_1)
                .turn(Math.toRadians(-60))
                .splineTo(RED_OBJECT_POS_2_1.vec(), RED_OBJECT_POS_2_1.getHeading())
                .splineTo(RED_OBJECT_POS_2_2.vec(), RED_OBJECT_POS_2_2.getHeading())
                .addTemporalMarker(() -> dropPurplePixel()) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1)
                .lineToSplineHeading(RED_BACKDROP_CENTER)
                .addTemporalMarker(() -> placeYellowPixel())
                .build();

    }

    private void buildRightPath(SampleMecanumDrive drive) {
        trajectory = drive.trajectorySequenceBuilder(RED_START_POS_1)
                .turn(Math.toRadians(-60))
                .splineToSplineHeading(RED_OBJECT_POS_1, Math.toRadians(90))
                .addTemporalMarker(() -> dropPurplePixel()) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1)
                .lineToSplineHeading(RED_BACKDROP_LEFT)
                .addTemporalMarker(() -> placeYellowPixel())
                .build();
    }

    private void placeYellowPixel() {
        //@TODO Implement placeYellowPixel()
    }

    private void dropPurplePixel() {
        //@TODO Implement dropPurplePixel()
    }

}