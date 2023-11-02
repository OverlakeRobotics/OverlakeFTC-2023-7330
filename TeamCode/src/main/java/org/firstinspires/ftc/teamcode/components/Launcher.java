package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.Servo;

public class Launcher {
    Servo airplaneServo;
    boolean launched = false;

    public Launcher(Servo s) {
        airplaneServo = s;
        airplaneServo.setPosition(0);
    }

    public void launch() {
        launched = true;
        airplaneServo.setPosition(1); //launch
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            airplaneServo.setPosition(0); //if for whatev reason this does error then just like do it w/o a delay
            launched = false;
        }
        airplaneServo.setPosition(0);
        launched = false;

    }

    public boolean isLaunching() {
        return launched;
    }

}
