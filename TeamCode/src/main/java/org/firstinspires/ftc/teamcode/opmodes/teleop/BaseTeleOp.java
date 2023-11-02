package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.components.Launcher;
import org.firstinspires.ftc.teamcode.opmodes.base.BaseOpMode;

@TeleOp(name = "Base TeleOp", group="TeleOp")
public class BaseTeleOp extends BaseOpMode {
    public void loop() {
        float rx = (float) Math.pow(gamepad1.right_stick_x, 3);
        float lx = (float) Math.pow(gamepad1.left_stick_x, 3);
        float ly = (float) Math.pow(gamepad1.left_stick_y, 3);

//        driveSystem.drive(rx, lx, ly);

        ArmSystem.Direction direction = ArmSystem.Direction.UP;
        double power = 0;

        // dpad up joystick arm up
        if (gamepad1.dpad_up) {
            power = 0.1;
        }

        //dpad down joystick arm down
        if (gamepad1.dpad_down) {
            direction = ArmSystem.Direction.DOWN;
            power = 0.1;
        }
        armSystem.driveArm(direction, power);

        // y button airplane launch
        if (gamepad1.y) {
//            launcher.launch();
        }

        // a button intake preset level
        if (gamepad1.a) {
            armSystem.armToGround();
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
