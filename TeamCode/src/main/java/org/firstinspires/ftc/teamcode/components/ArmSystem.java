package org.firstinspires.ftc.teamcode.components;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.params.DriveParams;

public class ArmSystem {
    public static final int GROUND = 0; //VALUE TBD
    public static final int ARM_LIMIT = 100; //VALUE TBD
    public static final int SERVO_GROUND = 1; //VALUE TBD;

    public enum Direction {
        UP,
        DOWN,
    }
    private DcMotor armLeft;
    public DcMotor armRight;
    public Servo leftServo;
    public Servo rightServo;
    public IntakeSystem intake;
    private int mTargetPosition;



    public ArmSystem(DcMotor motor1, DcMotor motor2, Servo servo1, Servo servo2, Servo intake1, Servo intake2){
        this.armLeft = motor1;
        this.armRight = motor2;
        this.leftServo = servo1;
        this.rightServo = servo2;
        armLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        armRight.setDirection(DcMotorSimple.Direction.REVERSE);
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
        armLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        armRight.setDirection(DcMotorSimple.Direction.REVERSE);
        if(mTargetPosition == 0){
            armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
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
        Log.d("what is happening", offsetLeft + " " + offsetRight + " " + armLeft.getCurrentPosition() + " " + armRight.getCurrentPosition() + " power " + armLeft.getPower() + " " + armRight.getPower());
        if(targetPosition != 0 && offsetLeft < 20 && offsetRight < 20 ){
            Log.d("reached", armLeft.getCurrentPosition() + " " + armRight.getCurrentPosition() + " power " + armLeft.getPower() + " " + armRight.getPower() );
            return true;
        }
        else if (targetPosition > 0 && offsetLeft < 15 && offsetRight < 20){
            return true;
        }
        return false;
    }

    public void armToGround(){
        if(driveToLevel(GROUND, 0.1)) {
            armRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            armLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            armRight.setPower(0);
            armLeft.setPower(0);
        }
        leftServo.setPosition(SERVO_GROUND);
        rightServo.setPosition(SERVO_GROUND);
    }
    public void driveArm(Direction direction, double pow){
        if(direction == Direction.UP && (armLeft.getCurrentPosition() >= ARM_LIMIT || armRight.getCurrentPosition() >= ARM_LIMIT))
        {
            killMotors();
        }
        if(direction == Direction.DOWN && (armLeft.getCurrentPosition() <= 5 || armRight.getCurrentPosition() <= 5))
        {
            killMotors();
        }
        if(direction == direction.UP)
        {
            armLeft.setDirection(DcMotorSimple.Direction.FORWARD);
            armRight.setDirection(DcMotorSimple.Direction.REVERSE);
        }
        else
        {
            armLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            armRight.setDirection(DcMotorSimple.Direction.FORWARD);
        }

        armLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armLeft.setPower(pow);//change to velocity
        armRight.setPower(pow); 

        leftServo.setPosition(armLeft.getCurrentPosition());//scale to range of inputs for servo
        rightServo.setPosition(armLeft.getCurrentPosition());//scale to range of inputs for servo
    }
    public void killMotors(){
        armLeft.setPower(0);
        armRight.setPower(0);
    }
    public void intake(){
        intake.intake();
    }

    public void outtake(){
        intake.outtake();
    }

    public static class IntakeSystem {
        private final Servo intakeLeft;
        private final Servo intakeRight;
        private final int INTAKE_POS = 1; //VALUE TBD
        private final int OUTTAKE_POS = 1; //VALUE TBD

        public IntakeSystem(Servo intake1, Servo intake2){
            this.intakeLeft = intake1;
            this.intakeRight = intake2;
            intake();
        }
        public void intake(){
            intakeLeft.setPosition(INTAKE_POS);
            intakeRight.setPosition(INTAKE_POS);
        }

        public void outtake(){
            intakeLeft.setPosition(OUTTAKE_POS);
            intakeRight.setPosition(OUTTAKE_POS);
        }
    }
}