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
    public static final int GROUND = 0; //VALUE TBD

    public static final int BACKBOARD = 1000; //value TBD
    public static final int ARM_LIMIT = 1000; //VALUE TBD
    public static final int SERVO_GROUND = 1; //VALUE TBD
    public static final double ENCODER_TO_SERVO = 0; //VALUE TBD
    private static final int DROP_HEIGHT = 100;

    public enum Direction {
        UP,
        DOWN,
    }
    public enum ArmState {
        ARM_RAISING,
        ARM_LOWERING,
        ARM_LOCKED,
        ARM_DRIVING_UP,
        ARM_DRIVING_DOWN
    }
    private DcMotor armLeft;
    public DcMotor armRight;
    public Servo leftServo;
    public Servo rightServo;
    public IntakeSystem intake;

    public int currentPos;
    private int mTargetPosition;

    public ArmSystem(DcMotor motor1, DcMotor motor2, Servo servo1, Servo servo2, Servo intake1, Servo intake2){
        this.armLeft = motor1;
        this.armRight = motor2;
        this.leftServo = servo1;
        this.rightServo = servo2;
        leftServo.setPosition(0);
        rightServo.setPosition(1);
        armLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        armRight.setDirection(DcMotorSimple.Direction.FORWARD);
        initMotors();
        intake = new IntakeSystem(intake1, intake2);
        mTargetPosition = 0;
    }
    public void initMotors() {
        armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armLeft.setPower(0);
        armLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRight.setPower(0);
        armRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public boolean driveToLevel(int targetPosition, double power){
        armLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        armRight.setDirection(DcMotorSimple.Direction.FORWARD);

        if(mTargetPosition != targetPosition){
            mTargetPosition = targetPosition;
            armLeft.setTargetPosition(targetPosition);
            armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armLeft.setPower(power);
            armRight.setTargetPosition(targetPosition);
            armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armRight.setPower(power);
        }

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
        if(driveToLevel(GROUND, 0.2)) {
            armRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            armLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            armRight.setPower(0);
            armLeft.setPower(0);
            return true;
        }
        leftServo.setPosition(SERVO_GROUND);
        rightServo.setPosition(SERVO_GROUND);
        return false;
    }
    public boolean armToBackboard(){
        if(driveToLevel(BACKBOARD, 0.2)) {
            armRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            armLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            armRight.setPower(0);
            armLeft.setPower(0);
            return true;
        }
        leftServo.setPosition(SERVO_GROUND);
        rightServo.setPosition(SERVO_GROUND);
        return false;
    }
    public void lockArm(boolean firstCall)
    {
        if(firstCall) {
            currentPos = (armLeft.getCurrentPosition() + armRight.getCurrentPosition()) / 2;
        }
        driveToLevel(currentPos, 0.5);
    }
    public double getMotorPower()
    {
        return armLeft.getPower();
    }
    public void driveArm(Direction direction, double pow){
        /*
        if(direction == Direction.UP && (armLeft.getCurrentPosition() >= ARM_LIMIT || armRight.getCurrentPosition() >= ARM_LIMIT))
        {
            armLeft.setPower(0);
            armRight.setPower(0);
        }
        if(direction == Direction.DOWN && (armLeft.getCurrentPosition() <= 0 || armRight.getCurrentPosition() <= 0))
        {
            armLeft.setPower(0);
            armRight.setPower(0);
        }

         */
        if(direction == direction.UP)
        {
            armLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            armRight.setDirection(DcMotorSimple.Direction.FORWARD);
        }
        if(direction == direction.DOWN)
        {
            armLeft.setDirection(DcMotorSimple.Direction.FORWARD);
            armRight.setDirection(DcMotorSimple.Direction.REVERSE);
        }

        armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armLeft.setPower(pow);
        armRight.setPower(pow);

        Log.d("leftPosition", "" + armLeft.getCurrentPosition());

        leftServo.setPosition(((armLeft.getCurrentPosition() + armRight.getCurrentPosition())/2) * ENCODER_TO_SERVO);
        rightServo.setPosition(((armLeft.getCurrentPosition() + armRight.getCurrentPosition())/2) * ENCODER_TO_SERVO);
        armLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void killMotors()
    {
        armRight.setPower(0);
        armLeft.setPower(0);
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
        private final int INTAKE_POS = 0; //VALUE TBD
        private final int OUTTAKE_POS = 1; //VALUE TBD

        public IntakeSystem(Servo intake1, Servo intake2){
            this.intakeLeft = intake1;
            this.intakeRight = intake2;

        }
        public void intakeLeft(){
            intakeLeft.setPosition(INTAKE_POS);
        }
        public void intakeRight(){
            intakeRight.setPosition(INTAKE_POS);
        }

        public void outtakeLeft(){
            intakeLeft.setPosition(OUTTAKE_POS);
        }
        public void outtakeRight(){
            intakeRight.setPosition(OUTTAKE_POS);
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
        while (!armToGround()) {

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

    private boolean armToDropHeight () {
        if (driveToLevel(DROP_HEIGHT, 0.2)) {
            armRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            armRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            armRight.setPower(0.0);
            armLeft.setPower(0.0);
            return true;
        }
        return false;
    }


}