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
                        drive.trajectorySequenceBuilder(RED_START_POS_2)
                                .splineToSplineHeading(RED_OBJECT_POS_5_1, Math.toRadians(100))
                                .splineToSplineHeading(RED_OBJECT_POS_5_2, Math.toRadians(90))
                                .waitSeconds(1)
                                .lineToConstantHeading(RED_OBJECT_POS_5_4.vec())
                                .turn(Math.toRadians(60))
                                .lineToConstantHeading(RED_OBJECT_POS_5_3.vec())
                                .splineToSplineHeading(RED_WAYPOINT_1, Math.toRadians(0))
                                .splineToSplineHeading(RED_WAYPOINT_1_5, Math.toRadians(0))
                                .splineToSplineHeading(RED_WAYPOINT_2, Math.toRadians(-45))
                                .splineToSplineHeading(RED_BACKDROP_RIGHT, Math.toRadians(0))
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