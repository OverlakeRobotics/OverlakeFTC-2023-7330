package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.Launcher;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "Base TeleOp", group="TeleOp")
public class BaseTeleOp extends BaseOpMode {
    boolean armRaising = false;

    boolean driving = false;
    boolean armLowering = false;
    public void loop() {
        float rx = (float) Math.pow(gamepad1.right_stick_x, 3);
        float lx = (float) Math.pow(gamepad1.left_stick_x, 3);
        float ly = (float) Math.pow(gamepad1.left_stick_y, 3);

//        driveSystem.drive(rx, lx, ly);


        // dpad up joystick arm up
        if (gamepad1.dpad_up) {
            armSystem.driveArm(ArmSystem.Direction.UP, 0.8);
            driving = true;
        }
        else
        {
            driving = false;
        }

        //dpad down joystick arm down
        if (gamepad1.dpad_down) {
            armSystem.driveArm(ArmSystem.Direction.DOWN, 0.8);
            driving = true;
        }
        else
        {
            driving = false;
        }

        // y button airplane launch
        if (gamepad1.y) {
//            launcher.launch();
        }

        if (gamepad1.x) {
            armRaising = true;
            armLowering = false;
        }
        if(armRaising && armSystem.armToBackboard())
        {
            armRaising = false;
        }

        if (gamepad1.a) {
            armLowering = true;
            armRaising = false;
        }
        if(armLowering && armSystem.armToGround())
        {
            armLowering = false;
        }

        if(!armRaising && !armLowering && !driving)
        {
            armSystem.lockArm();
        }

        // left trigger left intake expansion
        if (gamepad1.left_trigger > 0 || gamepad1.right_trigger > 0) {
            armSystem.intake();
        }
        // left bumper left intake closing
        if (gamepad1.left_bumper || gamepad1.right_bumper) {
            armSystem.outtake();
        }
    }
}
