package com.example.meepmeeptesting;

import static com.example.meepmeeptesting.GamePositions.*;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

                RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(45, 45, Math.toRadians(220), Math.toRadians(220), 15.6)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(RED_START_POS_2)
                                .splineToLinearHeading(RED_OBJECT_POS_6, Math.toRadians(90))
                                .waitSeconds(0.1)
                                .addTemporalMarker(() -> intakeLeft())
                                .waitSeconds(0.1)
                                .addTemporalMarker(() -> dropPurplePixel('r'))
                                .waitSeconds(1.0)
                                .setReversed(true)
                                .splineToConstantHeading(RED_WAYPOINT_1.vec(), Math.toRadians(90))
                                .splineToSplineHeading(RED_WAYPOINT_2, Math.toRadians(0))
                                //.setVelConstraint(new MecanumVelocityConstraint(30, DriveConstants.TRACK_WIDTH))
                                .splineToLinearHeading(RED_BACKDROP_LEFT_F, Math.toRadians(-90))
                                .waitSeconds(0.5)
                                .addTemporalMarker(() -> placeYellowPixel('l'))
                                .waitSeconds(1.0)
                                .setReversed(false)
                                .strafeRight(5)
                                .build()
                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
    private static void dropPurplePixel(char c) {

    }

    private static void intakeLeft() {

    }
    private static void placeYellowPixel(char c) {

    }
}