package com.example.meepmeeptesting;


import com.acmerobotics.roadrunner.geometry.Pose2d;



public class GamePositions {

    //**********************************************************************************************
    //**************************************** BLUE TEAM *******************************************
    //**********************************************************************************************

    public static final Pose2d BLUE_START_POS_1 = new Pose2d(14.5, 61, Math.toRadians(-90));

    public static final Pose2d BLUE_OBJECT_POS_1 = new Pose2d(31, 35, Math.toRadians(-135));
    public static final Pose2d BLUE_OBJECT_POS_2 = new Pose2d(22, 24, Math.toRadians(-135));
    public static final Pose2d BLUE_OBJECT_POS_3 = new Pose2d(8, 33, Math.toRadians(-150));

    public static final Pose2d BLUE_BACKDROP_LEFT = new Pose2d(49, 39.5, Math.toRadians(-180));
    public static final Pose2d BLUE_BACKDROP_CENTER = new Pose2d(49, 35, Math.toRadians(-180));
    public static final Pose2d BLUE_BACKDROP_RIGHT = new Pose2d(49, 27.5, Math.toRadians(-180));

    //**********************************************************************************************

    public static final Pose2d BLUE_START_POS_2 = new Pose2d(-38, 61, Math.toRadians(-90));

    public static final Pose2d BLUE_OBJECT_POS_4 = new Pose2d(-30, 33, Math.toRadians(-45));
    public static final Pose2d BLUE_OBJECT_POS_5 = new Pose2d(-36, 11.5, Math.toRadians(65));
    public static final Pose2d BLUE_OBJECT_POS_6 = new Pose2d(-43, 20, Math.toRadians(90));

    public static final Pose2d BLUE_WAYPOINT_1 = new Pose2d(-35, 8, Math.toRadians(180));
    public static final Pose2d BLUE_WAYPOINT_2 = new Pose2d(37, 8, Math.toRadians(180));
    public static final Pose2d BLUE_WAYPOINT_C = new Pose2d (0, 8, Math.toRadians(180));

    public static final Pose2d BLUE_BACKDROP_LEFT_F = new Pose2d(49, 39.5, Math.toRadians(-180));
    public static final Pose2d BLUE_BACKDROP_CENTER_F = new Pose2d(49, 29, Math.toRadians(-180));
    public static final Pose2d BLUE_BACKDROP_RIGHT_F = new Pose2d(49, 22, Math.toRadians(-180));


    //**********************************************************************************************
    //***************************************** RED TEAM *******************************************
    //**********************************************************************************************

    public static final Pose2d RED_START_POS_1 = new Pose2d(14.5, -61, Math.toRadians(90));

    public static final Pose2d RED_OBJECT_POS_1 = new Pose2d(30.5, -33, Math.toRadians(150));
    public static final Pose2d RED_OBJECT_POS_2 = new Pose2d(20, -25, Math.toRadians(135));
    public static final Pose2d RED_OBJECT_POS_3 = new Pose2d(7.5, -34, Math.toRadians(150));

    public static final Pose2d RED_BACKDROP_RIGHT = new Pose2d(46, -28, Math.toRadians(180));
    public static final Pose2d RED_BACKDROP_CENTER = new Pose2d(46, -38.7, Math.toRadians(180));
    public static final Pose2d RED_BACKDROP_LEFT = new Pose2d(46, -44, Math.toRadians(180));

    //**********************************************************************************************

    public static final Pose2d RED_START_POS_2 = new Pose2d(-38, -61, Math.toRadians(90));

    public static final Pose2d RED_OBJECT_POS_4 = new Pose2d(-30, -33, Math.toRadians(45));
    public static final Pose2d RED_OBJECT_POS_5 = new Pose2d(-36, -11.5, Math.toRadians(-65));
    public static final Pose2d RED_OBJECT_POS_6 = new Pose2d(-43, -23, Math.toRadians(-90));

    public static final Pose2d RED_WAYPOINT_1 = new Pose2d(-42, -8, Math.toRadians(-180));
    public static final Pose2d RED_WAYPOINT_2 = new Pose2d(24, -8, Math.toRadians(180));
    public static final Pose2d RED_WAYPOINT_C = new Pose2d(0, -12, Math.toRadians(-180));


    public static final Pose2d RED_BACKDROP_RIGHT_F = new Pose2d(48, -27, Math.toRadians(180));
    public static final Pose2d RED_BACKDROP_CENTER_F = new Pose2d(48, -34, Math.toRadians(180));
    public static final Pose2d RED_BACKDROP_LEFT_F = new Pose2d(48, -27, Math.toRadians(180));
}