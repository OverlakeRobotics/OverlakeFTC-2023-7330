package org.firstinspires.ftc.teamcode.opmodes;

import static org.firstinspires.ftc.teamcode.components.GamePositions.*;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.TensorFlowDetector;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;



@TeleOp(name = "Roadrunner Test", group = "Concept")
@Config
public class RoadRunnerTest extends LinearOpMode {

    private ArmSystem armSystem;
    private SampleMecanumDrive drive;

    private Queue<TrajectorySequence> trajectory;

    public static double FORWARD_DISTANCE = 100;
    public static double BACKWARD_DISTANCE = 100;
    public static double LEFT_DISTANCE = 100;
    public static double RIGHT_DISTANCE = 100;

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        while (!isStarted()) {
            sleep(10);
        }

        trajectory.add (drive.trajectorySequenceBuilder(new Pose2d(-50, 0, Math.toRadians(0)))
                .forward(FORWARD_DISTANCE)
                .waitSeconds(1)
                .back (BACKWARD_DISTANCE)
                .strafeRight(RIGHT_DISTANCE)
                .strafeLeft(LEFT_DISTANCE)
                .waitSeconds(1)
//                .back(24)
//                .strafeLeft(24)
//                .strafeRight(24)
                .build());



        while (trajectory.size() != 0) {
            drive.followTrajectorySequence(trajectory.remove());
        }
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
        drive.setPoseEstimate (new Pose2d (-50, 0, Math.toRadians(0)));

        trajectory = new LinkedList<>();
    }
}