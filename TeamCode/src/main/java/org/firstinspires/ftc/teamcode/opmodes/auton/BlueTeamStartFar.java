package org.firstinspires.ftc.teamcode.opmodes.auton;

import static org.firstinspires.ftc.teamcode.components.GamePositions.*;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.profile.VelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.TensorFlowDetector;
import org.firstinspires.ftc.teamcode.drive.DriveConstants;
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
        telemetry.addLine("test 3");
        while (!isStarted()) {
            detector.updateRecognitions();
            detector.updateTelemetry(true, true, true, true, true);
            telemetry.addLine("Current Threshold: " + detector.getConfidenceThreshold());
            telemetry.update();
            sleep(100);
//            if (detector.getNumRecognitions() != 0) {
//                if (detector.getHighestConfidenceRecognition().getConfidence() > 0.95) {
//                    break;
//                }
//            }
 
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
                hardwareMap.get(Servo.class, "left_servo"),
                hardwareMap.get(Servo.class, "right_servo"),
                hardwareMap.get(Servo.class, "intake_left"),
                hardwareMap.get(Servo.class, "intake_right")
        );
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(BLUE_START_POS_2);

        detector = new TensorFlowDetector("2023_Blue_Team_Object_3770.tflite", new String[]{"Blue_Owl"}, telemetry, hardwareMap);
        detector.initModel();
        detector.setConfidenceThreshold(0.88f);

        trajL = drive.trajectorySequenceBuilder(BLUE_START_POS_2)
                .splineToLinearHeading(BLUE_OBJECT_POS_4, Math.toRadians(-30))
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.intakeRight())
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.dropPurplePixel('l'))
                .waitSeconds(1.0)
                .setReversed(true)
                .splineToSplineHeading(BLUE_WAYPOINT_1, Math.toRadians(0))
                .splineToSplineHeading(BLUE_WAYPOINT_2, Math.toRadians(0))
                .setVelConstraint(new MecanumVelocityConstraint(30, DriveConstants.TRACK_WIDTH))
                .splineToLinearHeading(BLUE_BACKDROP_LEFT_F, Math.toRadians(90))
                .waitSeconds(0.5)
                .addTemporalMarker(() -> armSystem.placeYellowPixel('r'))
                .waitSeconds(1.0)
                .setReversed(false)
                .strafeLeft(24)
                .build();

        trajC = drive.trajectorySequenceBuilder(BLUE_START_POS_2)
                .splineToLinearHeading(BLUE_OBJECT_POS_5, Math.toRadians(-60))
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.intakeRight())
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.dropPurplePixel('l'))
                .waitSeconds(1.0)
                .setReversed(true)
//                .splineToSplineHeading(BLUE_WAYPOINT_1, Math.toRadians(0))
                .splineToSplineHeading(BLUE_WAYPOINT_C, 0)
                .splineToSplineHeading(BLUE_WAYPOINT_2, Math.toRadians(0))
                .setVelConstraint(new MecanumVelocityConstraint(30, DriveConstants.TRACK_WIDTH))
                .splineToLinearHeading(BLUE_BACKDROP_CENTER_F, Math.toRadians(90))
                .waitSeconds(0.5)
                .addTemporalMarker(() -> armSystem.placeYellowPixel('r'))
                .waitSeconds(1.0)
                .setReversed(false)
                .strafeLeft(20)
                .build();

        trajR = drive.trajectorySequenceBuilder(BLUE_START_POS_2)
                .splineToLinearHeading(BLUE_OBJECT_POS_6, Math.toRadians(-90))
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.intakeRight())
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.dropPurplePixel('l'))
                .waitSeconds(1.0)
                .setReversed(true)
                .splineToSplineHeading(BLUE_WAYPOINT_1, Math.toRadians(0))
                .splineToSplineHeading(BLUE_WAYPOINT_2, Math.toRadians(0))
                .setVelConstraint(new MecanumVelocityConstraint(30, DriveConstants.TRACK_WIDTH))
                .splineToLinearHeading(BLUE_BACKDROP_RIGHT_F, Math.toRadians(90))
                .waitSeconds(0.5)
                .addTemporalMarker(() -> armSystem.placeYellowPixel('r'))
                .waitSeconds(1.0)
                .setReversed(false)
                .strafeLeft(13)
                .build();



    }

    public void setArmSystem (ArmSystem armSystem) {
        this.armSystem = armSystem;
    }


    //**********************************************************************************************
    //**************************************** PATHS ***********************************************
    //**********************************************************************************************

    public TrajectorySequence buildLeftPath(SampleMecanumDrive drive) {

        // UPDATED



        trajectory = trajL;


        return trajectory;

        // Ready to Test
    }

    public TrajectorySequence buildCenterPath(SampleMecanumDrive drive) {

        // UPDATED

        trajectory = trajC;

        return trajectory;

    }

    public TrajectorySequence buildRightPath(SampleMecanumDrive drive) {

        // UPDATED

        trajectory = trajR;

        return trajectory;

    }

    public TrajectorySequence buildLeftPathSimple(SampleMecanumDrive drive) {
        trajectory = drive.trajectorySequenceBuilder(BLUE_START_POS_2)
                .turn(Math.toRadians(-30))
                .forward(22)
                .turn(Math.toRadians(90))
                .forward (4)
                .addTemporalMarker(() -> armSystem.intakeLeft())
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.dropPurplePixel('r')) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1.1)
                .back(4)
                .strafeLeft(22)
                .forward(82)
                .turn(Math.toRadians(180))
                .strafeLeft(13)
                .addTemporalMarker(() -> armSystem.placeYellowPixel('l')) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1.1)
                .strafeLeft(34)
                .build();
        return trajectory;
    }

    public TrajectorySequence buildCenterPathSimple(SampleMecanumDrive drive) {
        trajectory = drive.trajectorySequenceBuilder(BLUE_START_POS_2)
                .turn(Math.toRadians(-30))
                .forward(22)
                .forward (6)
                .addTemporalMarker(() -> armSystem.intakeLeft())
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.dropPurplePixel('r')) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1.1)
                .back(27.5)
                .turn(Math.toRadians(-90))
                .back(80)
                .strafeLeft(24)
                .addTemporalMarker(() -> armSystem.placeYellowPixel('l')) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1.1)
                .strafeLeft(23)
                .build();

        return trajectory;
    }

    public TrajectorySequence buildRightPathSimple(SampleMecanumDrive drive) {
        trajectory = drive.trajectorySequenceBuilder(BLUE_START_POS_2)
                .turn(Math.toRadians(-30))
                .forward(28)
                .turn(Math.toRadians(-90))
                .forward (0.1)
                .addTemporalMarker(() -> armSystem.intakeLeft())
                .waitSeconds(0.1)
                .addTemporalMarker(() -> armSystem.dropPurplePixel('r')) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1.1)
                .back (3)
                .strafeRight(14)
                .forward(3)
                .strafeRight(13)
                .turn(Math.toRadians(0))
                .back(81)
                .strafeLeft(27.5)
                .addTemporalMarker(() -> armSystem.placeYellowPixel('l')) // This action should take X seconds or less, where X is the .waitSeconds below
                .waitSeconds(1.1)
                .strafeLeft(16.5)
                .build();

        return trajectory;
    }
}