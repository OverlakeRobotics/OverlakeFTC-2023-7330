package org.firstinspires.ftc.teamcode.opmodes.teleop;

import static android.os.SystemClock.sleep;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.Launcher;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "Base TeleOp", group="TeleOp")
public class BaseTeleOp extends BaseOpMode {
    private boolean disableSlowPlace = false;
    public void loop() {
        float rx = (float) Math.pow(gamepad1.right_stick_x, 3);
        float lx = (float) Math.pow(gamepad1.left_stick_x, 3);
        float ly = (float) Math.pow(gamepad1.left_stick_y, 3);

//        driveSystem.slowDrive(true);
        driveSystem.drive(rx, lx, ly);

        if(gamepad1.dpad_up){
            armSystem.driveArm(ArmSystem.Direction.UP, 0.5);
            armSystem.runManually();
            driveSystem.slowPlace = false;
        }
        else if (gamepad1.dpad_down){
            armSystem.driveArm(ArmSystem.Direction.DOWN, 0.5);
            armSystem.runManually();
            driveSystem.slowPlace = false;
        }
        else if (gamepad1.y){
            launcher.launch();
        }
        else if(gamepad1.x){
            armSystem.setTargetPosition(ArmSystem.DROP_HEIGHT);
            armSystem.setArmServos(ArmSystem.SERVO_GROUND);
            driveSystem.slowPlace = false;
            //armSystem.outtakeLeft();
            //armSystem.outtakeRight();
        }
        else if(gamepad1.b){
            armSystem.reset();
        }
        else if (gamepad1.dpad_left){
            armSystem.setTargetPosition(ArmSystem.BACKBOARD_LOW);
            armSystem.setArmServos(ArmSystem.SERVO_BACKBOARD_LOW);
            if(disableSlowPlace == false)
            {
                driveSystem.slowPlace = true;
            }
        }
        else if(gamepad1.dpad_right)
        {
            armSystem.setTargetPosition(ArmSystem.BACKBOARD_HIGH);
            armSystem.setArmServos(ArmSystem.SERVO_BACKBOARD_HIGH);
            if(disableSlowPlace == false)
            {
                driveSystem.slowPlace = true;
            }
        }
        else if (gamepad1.a){
            armSystem.outtakeLeft();
            armSystem.outtakeRight();
            armSystem.setTargetPosition(ArmSystem.GROUND);
            armSystem.setArmServos(ArmSystem.SERVO_GROUND);
            driveSystem.slowPlace = false;
        }
        else if (gamepad1.right_bumper){
            if(disableSlowPlace) {
                disableSlowPlace = false;
            }
            else {
                disableSlowPlace = true;
            }
        }

        else if(gamepad1.left_trigger > 0){
            armSystem.intakeRight();
            armSystem.intakeLeft();
        }
        else if(gamepad1.right_trigger > 0){
            armSystem.outtakeRight();
            armSystem.outtakeLeft();
        }
        else{
            if(!armSystem.isDrivingtoPosition())
            {
                armSystem.killMotors();
            }
        }
        armSystem.armSystemUpdate();
        Log.d("Position", "" + (armSystem.returnPos()));
        //Pos 1: 1759
        //Pos 2: 2082
        //Pos 3: 2224


    }
}
