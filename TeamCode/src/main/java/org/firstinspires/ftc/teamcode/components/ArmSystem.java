package org.firstinspires.ftc.teamcode.components;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.util.Log;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.params.DriveParams;

public class ArmSystem {
    public static final int GROUND = 0;
    public static final int BACKBOARD_HIGH = 1759;
    public static final int BACKBOARD_LOW = 2082;
    private static final int BACKBOARD_AUTON = 2224;
    public static final double SERVO_GROUND = 0.13; //VALUE TBD
    public static final double SERVO_BACKBOARD_LOW = 0.6; //value tbd
    public static final double SERVO_BACKBOARD_HIGH = 0.7; //value tbd
    public static final double SERVO_BACKBOARD_AUTON =0.75; //value tbd
    public static final int DROP_HEIGHT = 200;


    public enum Direction {
        UP,
        DOWN,
    }
    private boolean drivingToPos;
    private DcMotor armLeft;
    public DcMotor armRight;
    public Servo leftServo;
    public Servo rightServo;
    public IntakeSystem intake;
    private int mTargetPosition;
    private int mTargetPosition2;

    public ArmSystem(DcMotor motor1, DcMotor motor2, Servo servo1, Servo servo2, Servo intake1, Servo intake2){
        this.armLeft = motor1;
        this.armRight = motor2;
        this.leftServo = servo1;
        this.rightServo = servo2;
        leftServo.setPosition(SERVO_GROUND);
        rightServo.setPosition(SERVO_GROUND);
        initMotors();
        intake = new IntakeSystem(intake1, intake2);
        mTargetPosition = 0;
        mTargetPosition2 = 0;
    }
    public void initMotors() {
        armLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        armRight.setDirection(DcMotorSimple.Direction.FORWARD);
        armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armLeft.setPower(0);
        armLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRight.setPower(0);
        armRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
    public boolean isDrivingtoPosition()
    {
        return drivingToPos;
    }
    public void runManually()
    {
        drivingToPos = false;
    }
    public void setTargetPosition(int position)
    {
        mTargetPosition = position;
        drivingToPos = true;
    }
    public void reset()
    {
        killMotors();
        armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void armSystemUpdate(){
        if(drivingToPos)
        {
            driveToLevel(mTargetPosition, 0.2);
        }
    }
    public void setArmServos(double pos)
    {
        leftServo.setPosition(pos);
        rightServo.setPosition(pos);
    }
    public boolean driveToLevel(int targetPosition, double power){

            armLeft.setTargetPosition(targetPosition);
            armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armLeft.setPower(power);
            armRight.setTargetPosition(targetPosition);
            armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armRight.setPower(power);

        int offsetLeft = Math.abs(armLeft.getCurrentPosition() - targetPosition);
        int offsetRight = Math.abs(armRight.getCurrentPosition() - targetPosition);
        if(targetPosition != 0 && offsetLeft < 20 && offsetRight < 20 ){
            return true;
        }
        else if (targetPosition > 0 && offsetLeft < 15 && offsetRight < 20){
            return true;
        }
        return false;
    }

    public boolean armToGround(){
        setArmServos(SERVO_GROUND);
        if(driveToLevel(GROUND, 0.2)) {
            armRight.setPower(0);
            armLeft.setPower(0);
            return true;
        }
        leftServo.setPosition(SERVO_GROUND);
        rightServo.setPosition(SERVO_GROUND);
        return false;
    }
    public boolean armToBackboard(){
        if(driveToLevel(BACKBOARD_AUTON, 0.2)) {
            armRight.setPower(0);
            armLeft.setPower(0);
            return true;
        }
        leftServo.setPosition(SERVO_BACKBOARD_AUTON);
        rightServo.setPosition(SERVO_BACKBOARD_AUTON);
        return false;
    }


    public void driveArm(Direction direction, double pow){
        armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if(direction == direction.UP)
        {
            armLeft.setPower(pow);
            armRight.setPower(pow);
        }
        if(direction == direction.DOWN)
        {
            armLeft.setPower(-pow);
            armRight.setPower(-pow);
        }
        armLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void killMotors()
    {
        armRight.setPower(0);
        armLeft.setPower(0);
    }
    public int returnPos()
    {
        return (armLeft.getCurrentPosition() + armRight.getCurrentPosition())/2;
    }
    public void intakeLeft(){
        intake.intakeLeft();
    }
    public void intakeRight(){
        intake.intakeRight();
    }

    public void outtakeLeft(){
        intake.outtakeLeft();
    }
    public void outtakeRight(){
        intake.outtakeRight();
    }

    public static class IntakeSystem {
        private final Servo intakeLeft;
        private final Servo intakeRight;
        private final double INTAKE_POS_LEFT = 0; //VALUE TBD
        private final double OUTTAKE_POS_LEFT = 1; //VALUE TBD

        private final double INTAKE_POS_RIGHT = 0;
        private final double OUTTAKE_POS_RIGHT = 1;


        public IntakeSystem(Servo intake1, Servo intake2){
            this.intakeLeft = intake1;
            this.intakeRight = intake2;
            intakeLeft();
            intakeRight();
        }
        public void intakeLeft(){
            intakeLeft.setPosition(INTAKE_POS_LEFT);
        }
        public void intakeRight(){
            intakeRight.setPosition(INTAKE_POS_RIGHT);
        }

        public void outtakeLeft(){
            intakeLeft.setPosition(OUTTAKE_POS_LEFT);
        }
        public void outtakeRight(){
            intakeRight.setPosition(OUTTAKE_POS_RIGHT);
        }
    }

    public void placeYellowPixel(char armSide) {
        while (!armToBackboard()) {

        }
        if (armSide == 'l') {
            outtakeLeft();
        } else if (armSide == 'r') {
            outtakeRight();
        } else {
            throw new IllegalArgumentException("armSide should be 'l' or 'r'");
        }

        while (!armToDropHeight()) {

        }


    }

    public void dropPurplePixel(char armSide) {
        /*
        setArmServos(SERVO_GROUND);
        while (!armToGround()) {

        }
        */
        if (armSide == 'l') {
            outtakeLeft();
        } else if (armSide == 'r') {
            outtakeRight();
        } else {
            throw new IllegalArgumentException("armSide should be 'l' or 'r'");
        }

        while (!armToDropHeight()) {

        }

    }

    private boolean armToDropHeight () {
        if (driveToLevel(DROP_HEIGHT, 0.2)) {
            armRight.setPower(0.0);
            armLeft.setPower(0.0);
            return true;
        }
        return false;
    }


}