package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.components.GamePositions.*;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.ArrayList;
import java.util.List;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;



@TeleOp(name = "Roadrunner Test", group = "Concept")
public class RoadRunnerTest extends LinearOpMode {

    public static final Pose2d START_POSE = new Pose2d(8, 4, Math.toRadians(-60));

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(BLUE_START_POS_1);

//        drive.setPoseEstimate(new Pose2d (0,0,0));
//
//        testPlusSign();
//
//        sleep (2000);
//
//        testSplineConstantHeading();
//
//        sleep (2000);
//
//        testSplineChangingHeading();
//
//        sleep (2000);

        Trajectory testTrajectory = drive.trajectoryBuilder(new Pose2d(0, 0))
                .splineTo(new Vector2d(12, 12), 0)
                .build();


        TrajectorySequence myTrajectory = drive.trajectorySequenceBuilder(BLUE_START_POS_1)
                .splineToLinearHeading(BLUE_OBJECT_POS_1, Math.toRadians (0))

                .waitSeconds(1)
                .splineToSplineHeading(BLUE_BACKDROP_LEFT, Math.toRadians(180))
                .build();


        waitForStart();
        drive.followTrajectorySequence(myTrajectory);
        //Trajectory myTrajectory = buildTrajectoryFromPoses(BLUE_START_POS_1, poses, drive);

        // DELTA I CHANGED .SPLINETO TO .SPLINETOCONSTANTHEADING BC I THINK ITS BETTER?? IDK FEEL FREE TO CHANGE BACK - VED
        // DOUBLE CHECKED THE DOCS, .SPINETOSPLINEHEADING actually uses Pose2ds, not Vectors, so im
        // actually going to try that, but we'll try yto see which one works best - DELTA

        //drive.followTrajectory(myTrajectory); //we live we love we lie
    }


    private void testPlusSign() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(new Pose2d (0,0,0));

        List<Pose2d> path1 = new ArrayList<Pose2d>();
        path1.add(new Pose2d (12, 0, 0));

        List<Pose2d> path2 = new ArrayList<Pose2d>();
        path1.add(new Pose2d (-12, 0, 0));

        List<Pose2d> path3 = new ArrayList<Pose2d>();
        path1.add(new Pose2d (0, 0, 0));

        List<Pose2d> path4 = new ArrayList<Pose2d>();
        path1.add(new Pose2d (0, 12, 0));

        List<Pose2d> path5 = new ArrayList<Pose2d>();
        path1.add(new Pose2d (0, -12, 0));

        List<Pose2d> path6 = new ArrayList<Pose2d>();
        path1.add(new Pose2d (0, 0, 0));



        Trajectory test1 = buildTrajectoryFromPoses(new Pose2d (0,0,0), path1, drive);
        Trajectory test2 = buildTrajectoryFromPoses(test1.end(), path2, drive);
        Trajectory test3 = buildTrajectoryFromPoses(test2.end(), path3, drive);
        Trajectory test4 = buildTrajectoryFromPoses(test3.end(), path4, drive);
        Trajectory test5 = buildTrajectoryFromPoses(test4.end(), path5, drive);
        Trajectory test6 = buildTrajectoryFromPoses(test5.end(), path6, drive);



        drive.followTrajectory(test1);
        drive.followTrajectory(test2);
        drive.followTrajectory(test3);
        drive.followTrajectory(test4);
        drive.followTrajectory(test5);
        drive.followTrajectory(test6);

    }

    private void testSplineConstantHeading() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(new Pose2d (0,0,0));

        List<Pose2d> poses = new ArrayList<Pose2d>();
        poses.add(new Pose2d (12, 0, 0));
        poses.add(new Pose2d (-12, 0, 0));
        poses.add(new Pose2d (0, 0, 0));
        poses.add(new Pose2d (0, 12, 0));
        poses.add(new Pose2d (0, -12, 0));
        poses.add(new Pose2d (0, 0, 0));

        Trajectory test = buildTrajectoryFromPoses(new Pose2d (0,0,0), poses, drive);

        drive.followTrajectory(test);

    }

    private void testSplineChangingHeading() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(new Pose2d (0,0,0));

        Trajectory test = drive.trajectoryBuilder(new Pose2d ())
                .splineToSplineHeading(new Pose2d(6,12, Math.toRadians(90)), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(12,0, Math.toRadians(180)), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(6,-12, Math.toRadians(270)), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(0,0, Math.toRadians(360)), Math.toRadians(0))
                .build();

        drive.followTrajectory(test);
    }

    private void testSplineChangingHeadingWithTangents() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(new Pose2d (0,0,0));

        Trajectory test = drive.trajectoryBuilder(new Pose2d ())
                .splineToSplineHeading(new Pose2d(6,12, Math.toRadians(90)), Math.toRadians(-45))
                .splineToSplineHeading(new Pose2d(12,0, Math.toRadians(180)), Math.toRadians(-135))
                .splineToSplineHeading(new Pose2d(6,-12, Math.toRadians(270)), Math.toRadians(135))
                .splineToSplineHeading(new Pose2d(0,0, Math.toRadians(360)), Math.toRadians(0))
                .build();

        drive.followTrajectory(test);
    }

    private void trajectorySequenceBuilderTest() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setPoseEstimate(new Pose2d (0,0,0));

        TrajectorySequence test = drive.trajectorySequenceBuilder(new Pose2d ())
                .splineToSplineHeading(new Pose2d(6,12, Math.toRadians(90)), Math.toRadians(-45))
                .splineToSplineHeading(new Pose2d(12,0, Math.toRadians(180)), Math.toRadians(-135))
                .splineToSplineHeading(new Pose2d(6,-12, Math.toRadians(270)), Math.toRadians(135))
                .splineToSplineHeading(new Pose2d(0,0, Math.toRadians(360)), Math.toRadians(0))
                .build();

        drive.followTrajectorySequence(test);
    }

    private Trajectory buildTrajectoryFromPoses (Pose2d startPose, List<Pose2d> poses, SampleMecanumDrive drive) {

        TrajectoryBuilder test = drive.trajectoryBuilder(startPose); //instantiate trajectory builder
        for (int i = 0; i < poses.size(); i++) {
            test.splineToConstantHeading(poses.get(i).vec(), poses.get(i).getHeading()); //spline to each elem in list poses
        }

        return test.build(); //build and return
    }
}