package org.firstinspires.ftc.teamcode.opmodes.auton;

import static org.firstinspires.ftc.teamcode.components.GamePositions.*;

import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.TensorFlowDetector;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Red Team Autonomous Far", group = "Autonomous")
public class RedTeamStartFar extends LinearOpMode {

    // Constants
    private static final double DEG_THRESHOLD = 0.0;

    // Components
    private ArmSystem armSystem;
    private SampleMecanumDrive drive;
    private TensorFlowDetector detector;

    // Fields
    private char path;
    private TrajectorySequence trajectory;

    private TrajectorySequence trajL;
    private TrajectorySequence trajC;
    private TrajectorySequence trajR;

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
            detector.updateTelemetry(true, true, true, true, true);
            telemetry.addLine("Current Threshold: " + detector.getConfidenceThreshold());
            telemetry.update();
            sleep (100);
//            if (detector.getHighestConfidenceRecognition().getConfidence() > 0.95) {
//                break;
//            }
        } // Keep searching for the model until the opMode is started. If the model is found with
        // high confidence, stop searching lest the model breaks

        if (detector.getNumRecognitions() == 0) {
            telemetry.addData("Object Detected - ", "No object was detected with a confidence above %f", detector.getConfidenceThreshold());
            telemetry.addData("Path Chosen - ", "Estimated angle = NULL deg, ready to follow 'l' path");
            path = 'l';
        } else if (detector.getHighestConfidenceRecognition().estimateAngleToObject(AngleUnit.DEGREES) < DEG_THRESHOLD) {
            telemetry.addData("Object Detected - ", "A(n) %s was found with %f confidence", detector.getHighestConfidenceRecognition().getLabel(), detector.getHighestConfidenceRecognition().getConfidence());
            path = 'c';
            telemetry.addData("Path Chosen - ", "Estimated angle = %f deg, ready to follow %c path", detector.getHighestConfidenceRecognition().estimateAngleToObject(AngleUnit.DEGREES), path);
        } else {
            path = 'r';
            telemetry.addData("Path Chosen - ", "Estimated angle = %f deg, ready to follow %c path", detector.getHighestConfidenceRecognition().estimateAngleToObject(AngleUnit.DEGREES), path);
        } // Set the path to the appropriate path ('l'eft, 'r'ight, 'c'enter), and update the telemetry to let us know whats going on

        telemetry.update();
        if (path == 'l') {
            buildLeftPathSimple(drive);
        } else if (path == 'c') {
            buildCenterPathSimple(drive);
        } else if (path == 'r') {
            buildRightPathSimple(drive);
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
        drive.setPoseEstimate (RED_START_POS_2);

        detector = new TensorFlowDetector("2023_Red_Team_Object_7330.tflite", new String[]{"Red_Owl"}, telemetry, hardwareMap, "Webcam 2");
        detector.initModel();
        detector.setConfidenceThreshold(0.88f);


        trajL = drive.trajectorySequenceBuilder(RED_START_POS_2)
                        .splineToLinearHeading(RED_OBJECT_POS_6, Math.toRadians(90))
                        .waitSeconds(0.1)
                        .addTemporalMarker(() -> armSystem.intakeLeft())
                        .waitSeconds(0.1)
                        .addTemporalMarker(() -> armSystem.dropPurplePixel('r'))
                        .waitSeconds(1.0)
                        .setReversed(true)
                        .splineToSplineHeading(RED_WAYPOINT_1, Math.toRadians(0))
                        .splineToSplineHeading(RED_WAYPOINT_2, Math.toRadians(0))
                        //.setVelConstraint(new MecanumVelocityConstraint(30, DriveConstants.TRACK_WIDTH))
                        .splineToLinearHeading(RED_BACKDROP_LEFT_F, Math.toRadians(-90))
                        .waitSeconds(0.5)
                        .addTemporalMarker(() -> armSystem.placeYellowPixel('l'))
                        .waitSeconds(1.0)
                        .setReversed(false)
                        .strafeRight(5)
                        .build();

        trajC = drive.trajectorySequenceBuilder(RED_START_POS_2)
                .splineToLinearHeading(RED_OBJECT_POS_5, Math.toRadians(60))
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.intakeLeft())
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.dropPurplePixel('r'))
                .waitSeconds(1.0)
                .setReversed(true)
//                .splineToSplineHeading(RED_WAYPOINT_1, Math.toRadians(0))
                .splineToSplineHeading(RED_WAYPOINT_C, 0)
                .splineToSplineHeading(RED_WAYPOINT_2, Math.toRadians(0))
                .setVelConstraint(new MecanumVelocityConstraint(30, DriveConstants.TRACK_WIDTH))
                .splineToLinearHeading(RED_BACKDROP_CENTER_F, Math.toRadians(-90))
                .waitSeconds(0.5)
                .addTemporalMarker(() -> armSystem.placeYellowPixel('l'))
                .waitSeconds(1.0)
                .setReversed(false)
                .strafeRight(18)
                .build();

        trajR = drive.trajectorySequenceBuilder(RED_START_POS_2)
                .splineToLinearHeading(RED_OBJECT_POS_4, Math.toRadians(30))
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.intakeLeft())
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.dropPurplePixel('r'))
                .waitSeconds(1.0)
                .setReversed(true)
                .splineToSplineHeading(RED_WAYPOINT_1, Math.toRadians(0))
                .splineToSplineHeading(RED_WAYPOINT_2, Math.toRadians(0))
                .setVelConstraint(new MecanumVelocityConstraint(30, DriveConstants.TRACK_WIDTH))
                .splineToLinearHeading(RED_BACKDROP_RIGHT_F, Math.toRadians(-90))
                .waitSeconds(0.5)
                .addTemporalMarker(() -> armSystem.placeYellowPixel('l'))
                .waitSeconds(1.0)
                .setReversed(false)
                .strafeRight(12)
                .build();
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

    }

    public TrajectorySequence buildCenterPath(SampleMecanumDrive drive) {
        trajectory = trajC;

        return trajectory;

    }

    public TrajectorySequence buildRightPath(SampleMecanumDrive drive) {
        trajectory = trajR;
        return trajectory;

    }


    public TrajectorySequence buildLeftPathSimple(SampleMecanumDrive drive) {
        trajectory = drive.trajectorySequenceBuilder(RED_START_POS_2)
                .turn(Math.toRadians(30))
                .forward(28)
                .turn(Math.toRadians(90))
                .waitSeconds(0.1)
                .forward (3)
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.intakeRight())
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.dropPurplePixel('l')) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1.1)
                .back (5)
                .strafeLeft(10)
                .forward(2)
                .strafeLeft(17)
                .turn(Math.toRadians(0))
                .back(83)
                .strafeRight(28)
                .addTemporalMarker(() -> armSystem.placeYellowPixel('r')) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1.1)
                .strafeRight(19)
                .build();

        return trajectory;
    }

    public TrajectorySequence buildCenterPathSimple(SampleMecanumDrive drive) {
        trajectory = drive.trajectorySequenceBuilder(RED_START_POS_2)
                .turn(Math.toRadians(30))
                .forward(22)
                .forward (5)
                .addTemporalMarker(() -> armSystem.intakeRight())
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.dropPurplePixel('l')) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1.1)
                .back (5)
                .back(21.5)
                .turn(Math.toRadians(90))
                .back(80)
                .strafeRight(20)
                .addTemporalMarker(() -> armSystem.placeYellowPixel('r')) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1.1)
                .strafeRight(27)
                .build();

        return trajectory;
    }

    public TrajectorySequence buildRightPathSimple(SampleMecanumDrive drive) {
        trajectory = drive.trajectorySequenceBuilder(RED_START_POS_2)
                .turn(Math.toRadians(30))
                .forward(24)
                .turn(Math.toRadians(-90))
                .forward (4)
                .addTemporalMarker(() -> armSystem.intakeRight())
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.dropPurplePixel('l')) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1.1)
                .back (4)
                .strafeRight(23.5)
                .turn(Math.toRadians(180))
                .back(80)
                .strafeRight(11)
                .addTemporalMarker(() -> armSystem.placeYellowPixel('r')) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1.1)
                .strafeRight(37)
                .build();

        return trajectory;
    }

}