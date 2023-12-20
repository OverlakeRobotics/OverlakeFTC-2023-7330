package com.example.meepmeeptesting;

import static com.example.meepmeeptesting.GamePositions.*;


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
                                drive.trajectorySequenceBuilder(RED_START_POS_1)
                                        .splineToLinearHeading(RED_OBJECT_POS_1, Math.toRadians(90))
                                        .waitSeconds(0.05)
                                        .addTemporalMarker(() -> dropPurplePixel())
                                        .waitSeconds(0.1)
                                        .addTemporalMarker(() -> dropPurplePixel()) // This action should take X seconds or less, where X is the .waitSeconds below
                                        .waitSeconds(1)
                                        .lineToSplineHeading(RED_BACKDROP_LEFT)
                                        .waitSeconds(0.05)
                                        .addTemporalMarker(() -> dropPurplePixel())
                                        .waitSeconds(1.0)
                                        .strafeRight(38)
                                        .build()

//                        drive.trajectorySequenceBuilder(RED_START_POS_2)
//                                .turn(Math.toRadians(30))
//                                .forward(22)
//                                .turn(Math.toRadians(-90))
//                                .forward (4)
//                                .addTemporalMarker(() -> dropPurplePixel()) // This action should take X seconds or less, where X is the .waitSeconds below
//                                .waitSeconds(1.1)
//                                .back (4)
//                                .strafeRight(22)
//                                .turn(Math.toRadians(180))
//                                .back(80)
//                                .strafeRight(14)
//                                .addTemporalMarker(() -> placeYellowPixel()) // This action should take X seconds or less, where X is the .waitSeconds below
//                                .waitSeconds(1.1)
//                                .strafeRight(37)
//                                .build()
                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
    private static void dropPurplePixel() {

    }
    private static void placeYellowPixel() {

    }
}