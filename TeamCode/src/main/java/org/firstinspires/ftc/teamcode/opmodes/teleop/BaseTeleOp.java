package org.firstinspires.ftc.teamcode.opmodes.teleop;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.Launcher;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "Base TeleOp", group="TeleOp")
public class BaseTeleOp extends BaseOpMode {
    boolean armRaising = false;
    boolean armLocked = false;
    boolean driving = false;
    boolean armLowering = false;
    boolean firstCall;
    ArmSystem.ArmState armState;
    public void loop() {
        float rx = (float) Math.pow(gamepad1.right_stick_x, 3);
        float lx = (float) Math.pow(gamepad1.left_stick_x, 3);
        float ly = (float) Math.pow(gamepad1.left_stick_y, 3);

      //  driveSystem.drive(rx, lx, ly);


        // dpad up joystick arm up
        if (gamepad1.dpad_up) {
            armState = ArmSystem.ArmState.ARM_DRIVING_UP;
            armSystem.driveArm(ArmSystem.Direction.UP, 0.5);
        }
        if(armState == ArmSystem.ArmState.ARM_DRIVING_UP && !gamepad1.dpad_up)
        {
            armState = ArmSystem.ArmState.ARM_LOCKED;
        }

        //dpad down joystick arm down
        if (gamepad1.dpad_down) {
            armState = ArmSystem.ArmState.ARM_DRIVING_DOWN;
            armSystem.driveArm(ArmSystem.Direction.DOWN, 0.5);
        }
        if(armState == ArmSystem.ArmState.ARM_DRIVING_DOWN && !gamepad1.dpad_down)
        {
            armState = ArmSystem.ArmState.ARM_LOCKED;
        }

        // y button airplane launch
        if (gamepad1.y) {
          //  launcher.launch();
        }

        if (gamepad1.x) {
            armState = ArmSystem.ArmState.ARM_RAISING;
        }
        if(armState == ArmSystem.ArmState.ARM_RAISING && armSystem.armToBackboard())
        {
            armState = ArmSystem.ArmState.ARM_LOCKED;
        }

        if (gamepad1.a) {
            armState = ArmSystem.ArmState.ARM_LOWERING;
        }
        if(armState == ArmSystem.ArmState.ARM_LOWERING && armSystem.armToGround())
        {
            armState = ArmSystem.ArmState.ARM_LOCKED;
        }

        if(armState == ArmSystem.ArmState.ARM_LOCKED)
        {
            //make lock system so that it locks and unlocks
            if(firstCall)
            {
                Log.d("mystery", "true");
                armSystem.killMotors();
            }
            armSystem.lockArm(firstCall);
            firstCall = false;

        }
        else
        {
            firstCall = true;
        }
        Log.d("mystery", "" + armState);

        // left trigger left intake expansion
        if (gamepad1.left_trigger > 0) {
            armSystem.intakeLeft();
        }
        if(gamepad1.right_trigger > 0) {
            armSystem.intakeRight();
        }
        // left bumper left intake closing
        if (gamepad1.left_bumper){
            armSystem.outtakeLeft();
        }
        if (gamepad1.right_bumper){
            armSystem.outtakeRight();
        }
        Log.d("mystery", "" + armSystem.getMotorPower());
    }
}
//make each servo one button
//make make lock system on arm
//check to make sure encoder values are recieved