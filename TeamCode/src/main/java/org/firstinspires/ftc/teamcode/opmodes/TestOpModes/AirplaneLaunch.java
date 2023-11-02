package org.firstinspires.ftc.teamcode.opmodes.TestOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

//not sure if this should be a TeleOp
//feel free to change if necessary - Ved

@TeleOp(name = "AirplaneLaunch", group = "Concept")
public class AirplaneLaunch extends LinearOpMode {
    Servo airplaneServo;

    @Override
    public void runOpMode() throws InterruptedException {
        airplaneServo = hardwareMap.get(Servo.class, "left_hand");
        airplaneServo.setPosition(0);
        while (true) {
            if (gamepad1.y) {
                airplaneServo.setPosition(1);
            }
        }
    }
}