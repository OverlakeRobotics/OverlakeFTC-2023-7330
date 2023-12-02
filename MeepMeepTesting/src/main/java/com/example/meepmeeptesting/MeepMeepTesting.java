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
                .setConstraints(30, 30, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(BLUE_START_POS_2)
                                .turn(Math.toRadians(-30))
                                .forward(30)
                                .turn(Math.toRadians(-90))
                                .forward (4)
                                .waitSeconds(1.1)
                                .back (4)
                                .strafeRight(26)
                                .turn(Math.toRadians(0))
                                .back(80)
                                .strafeLeft(24)
                                .waitSeconds(1.1)
                                .strafeLeft(23)
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