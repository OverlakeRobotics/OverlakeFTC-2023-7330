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
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(BLUE_START_POS_1)
                                .splineToSplineHeading(BLUE_OBJECT_POS_1, Math.toRadians (-75))
                                .addTemporalMarker(() -> dropPurplePixel()) // This action should take X seconds or less, where X is the .waitSeconds below
                                .waitSeconds(1)
                                .lineToSplineHeading(BLUE_BACKDROP_LEFT)
                                .addTemporalMarker(() -> placeYellowPixel())
                                .build()
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