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

import java.util.LinkedList;
import java.util.Queue;

@Autonomous(name = "Blue Team Autonomous Close", group = "Autonomous")
public class BlueTeamStartClose extends LinearOpMode {

    // Constants
    private static final double DEG_THRESHOLD = 0.0;

    // Components
    private ArmSystem armSystem;
    private SampleMecanumDrive drive;
    private TensorFlowDetector detector;

    // Fields
    private char path;
    private TrajectorySequence trajectory;

    private Queue<TrajectorySequence> trajectories = new LinkedList<>();

    private TrajectorySequence trajL;
    private TrajectorySequence trajC;
    private TrajectorySequence trajR;

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
        while (!isStarted()) {
            detector.updateRecognitions();
            detector.updateTelemetry(true, true, true, true, true);
            telemetry.addLine("Current Threshold: " + detector.getConfidenceThreshold());
            if (detector.getNumRecognitions() == 0) {
                path = 'l';
                telemetry.addData("Path - ", "Estimated angle = NULL deg, ready to follow 'l' path");

            } else if (detector.getHighestConfidenceRecognition().estimateAngleToObject(AngleUnit.DEGREES) < DEG_THRESHOLD) {
                path = 'c';
                telemetry.addData("Path Chosen - ", "Estimated angle = %f deg, ready to follow %c path", detector.getHighestConfidenceRecognition().estimateAngleToObject(AngleUnit.DEGREES), path);

            } else {
                path = 'r';
                telemetry.addData("Path Chosen - ", "Estimated angle = %f deg, ready to follow %c path", detector.getHighestConfidenceRecognition().estimateAngleToObject(AngleUnit.DEGREES), path);
            }

            telemetry.update();
            sleep(20);
        }

//        if (detector.getNumRecognitions() == 0) {
//            telemetry.addData("Object Detected - ", "No object was detected with a confidence above %f", detector.getConfidenceThreshold());
//            telemetry.addData("Path Chosen - ", "Estimated angle = NULL deg, ready to follow 'l' path");
//            telemetry.update();
//
//            path = 'l';
//        } else if (detector.getHighestConfidenceRecognition().estimateAngleToObject(AngleUnit.DEGREES) < DEG_THRESHOLD) {
//            telemetry.addData("Object Detected - ", "A(n) %s was found with %f confidence", detector.getHighestConfidenceRecognition().getLabel(), detector.getHighestConfidenceRecognition().getConfidence());
//            path = 'l';
//            telemetry.addData("Path Chosen - ", "Estimated angle = %f deg, ready to follow %c path", detector.getHighestConfidenceRecognition().estimateAngleToObject(AngleUnit.DEGREES), path);
//            telemetry.update();
//
//        } else {
//            path = 'c';
//            telemetry.addData("Path Chosen - ", "Estimated angle = %f deg, ready to follow %c path", detector.getHighestConfidenceRecognition().estimateAngleToObject(AngleUnit.DEGREES), path);
//            telemetry.update();
//        } // Set the path to the appropriate path ('l'eft, 'r'ight, 'c'enter), and update the telemetry to let us know whats going on


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
                hardwareMap.get(Servo.class, "left_servo"),
                hardwareMap.get(Servo.class, "right_servo"),
                hardwareMap.get(Servo.class, "intake_left"),
                hardwareMap.get(Servo.class, "intake_right")
        );
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate (BLUE_START_POS_1);

        detector = new TensorFlowDetector("2023_Blue_Team_Object_3770.tflite", new String[]{"Blue_Owl"}, telemetry, hardwareMap, "Webcam 1");
        detector.initModel();
        detector.setConfidenceThreshold(0.88f);

        telemetry.addLine("Detector Initialized, generating trajectories...");
        telemetry.update();

        trajL = drive.trajectorySequenceBuilder(BLUE_START_POS_1)
                .splineToLinearHeading(BLUE_OBJECT_POS_1, Math.toRadians(-135))
                .waitSeconds(0.05)
                .addTemporalMarker(() -> armSystem.intakeLeft())
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.dropPurplePixel('r')) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1)
                .lineToSplineHeading(BLUE_BACKDROP_LEFT)
                .waitSeconds(0.05)
                .addTemporalMarker(() -> armSystem.placeYellowPixel('l'))
                .waitSeconds(1.0)
                .strafeLeft(35)
                .build();

        trajC = drive.trajectorySequenceBuilder(BLUE_START_POS_1)
                //.splineTo(BLUE_OBJECT_POS_2_1.vec(), BLUE_OBJECT_POS_2_1.getHeading())
                .splineToSplineHeading(BLUE_OBJECT_POS_2, Math.toRadians(-90))
                .waitSeconds(0.05)
                .addTemporalMarker(() -> armSystem.intakeLeft())
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.dropPurplePixel('r')) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1)
                .lineToSplineHeading(BLUE_BACKDROP_CENTER)
                .waitSeconds(0.05)
                .addTemporalMarker(() -> armSystem.placeYellowPixel('l'))
                .waitSeconds(1.0)
                .strafeLeft(28)
                .build();

        trajR = drive.trajectorySequenceBuilder(BLUE_START_POS_1)
                .splineTo(BLUE_OBJECT_POS_3.vec(), BLUE_OBJECT_POS_3.getHeading())
                .waitSeconds(0.05)
                .addTemporalMarker(() -> armSystem.intakeLeft())
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.dropPurplePixel('r')) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1)
                .lineToLinearHeading(BLUE_BACKDROP_RIGHT)
                .waitSeconds(0.05)
                .addTemporalMarker(() -> armSystem.placeYellowPixel('l'))
                .waitSeconds(1.0)
                .strafeLeft(20)
                .build();

        telemetry.addLine("Trajectories generated. Searching for objects...");
        telemetry.update();

    }

    public void setArmSystem (ArmSystem armSystem) {
        this.armSystem = armSystem;
    }


    //**********************************************************************************************
    //**************************************** PATHS ***********************************************
    //**********************************************************************************************

    public TrajectorySequence buildLeftPath(SampleMecanumDrive drive) {
        trajectory = trajL;

        return trajectory;
        // Ready to test
    }

    public TrajectorySequence buildCenterPath(SampleMecanumDrive drive) {
        trajectory = trajC;

        return trajectory;

        // Ready to test
    }

    public TrajectorySequence buildRightPath(SampleMecanumDrive drive) {

        trajectory = trajR;

        return trajectory;
        // Ready to test
    }


}