package org.firstinspires.ftc.teamcode.opmodes.TestOpModes;

import static org.firstinspires.ftc.teamcode.components.GamePositions.*;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.ArmSystem;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.opmodes.auton.BlueTeamStartClose;
import org.firstinspires.ftc.teamcode.opmodes.auton.BlueTeamStartFar;
import org.firstinspires.ftc.teamcode.opmodes.auton.RedTeamStartClose;
import org.firstinspires.ftc.teamcode.opmodes.auton.RedTeamStartFar;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Auton Test", group="TeleOp")
@Disabled
public class AutonTest extends LinearOpMode {

    private char team = 'n';
    private char closeFar = 'n';

    private SampleMecanumDrive drive;
    private LinearOpMode opMode;
    private TrajectorySequence trajectory;
    private ArmSystem armSystem;

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        waitForStart();

        drive.followTrajectorySequence(trajectory);

    }

    private void initialize() {

        drive = new SampleMecanumDrive(hardwareMap);

        armSystem = new ArmSystem(
                hardwareMap.get(DcMotor.class, "arm_left"),
                hardwareMap.get(DcMotor.class, "arm_right"),
                hardwareMap.get(Servo.class, "left_servo"),
                hardwareMap.get(Servo.class, "right_servo"),
                hardwareMap.get(Servo.class, "intake_left"),
                hardwareMap.get(Servo.class, "intake_right")
        );

        while (true) {
            telemetry.addData("Current Team - ", "%s", team == 'n' ? "none" : (team == 'b' ? "blue" : "red"));
            telemetry.addData("Current Start Position - ", "%s", closeFar == 'n' ? "none" : (closeFar == 'c' ? "close" : "far"));
            if (team == 'n') {
                telemetry.addLine("Press d-pad left for blue team \nPress d-pad right for red team");
                if (gamepad1.dpad_left) {
                    team = 'b';
                } else if (gamepad1.dpad_right) {
                    team = 'r';
                }
            } else {
                telemetry.addLine("Press d-pad up for close start \nPress d-pad down for far start");
                if (gamepad1.dpad_up) {
                    closeFar = 'c';
                    break;
                } else if (gamepad1.dpad_down) {
                    closeFar = 'f';
                    break;
                }
            }
            telemetry.update();
        }

        if (team == 'b') {
            if (closeFar == 'c'){
                opMode = new BlueTeamStartClose();
                drive.setPoseEstimate(BLUE_START_POS_1);
            } else {
                opMode = new BlueTeamStartFar();
                drive.setPoseEstimate(BLUE_START_POS_2);
            }
        } else {
            if (closeFar == 'c') {
                opMode = new RedTeamStartClose();
                drive.setPoseEstimate(RED_START_POS_1);
            } else {
                opMode = new RedTeamStartFar();
                drive.setPoseEstimate(RED_START_POS_2);
            }
        }

        sleep(250);

        char path;
        while (true) {
            telemetry.addData("Current Team - ", "%s", team == 'n' ? "none" : (team == 'b' ? "blue" : "red"));
            telemetry.addData("Current Start Position - ", "%s", closeFar == 'n' ? "none" : (closeFar == 'c' ? "close" : "far"));

            telemetry.addLine ("press d-pad left for left path, \npress d-pad up for center path, \npress d-pad right for right path");

            if (gamepad1.dpad_left) {
                path = 'l';
                if (team == 'b') {
                    if (closeFar == 'c'){
                        trajectory = ((BlueTeamStartClose) opMode).buildLeftPath(drive);

                    } else {
                        trajectory = ((BlueTeamStartFar) opMode).buildLeftPath(drive);
                    }
                } else {
                    if (closeFar == 'c') {
                        trajectory = ((RedTeamStartClose) opMode).buildLeftPath(drive);
                    } else {
                        trajectory = ((RedTeamStartFar) opMode).buildLeftPath(drive);
                    }
                }
                break;
            } else if (gamepad1.dpad_up) {
                path = 'c';
                if (team == 'b') {
                    if (closeFar == 'c'){
                        trajectory = ((BlueTeamStartClose) opMode).buildCenterPath(drive);
                    } else {
                        trajectory = ((BlueTeamStartFar) opMode).buildCenterPath(drive);
                    }
                } else {
                    if (closeFar == 'c') {
                        trajectory = ((RedTeamStartClose) opMode).buildCenterPath(drive);
                    } else {
                        trajectory = ((RedTeamStartFar) opMode).buildCenterPath(drive);
                    }
                }
                break;
            } else if (gamepad1.dpad_right) {
                path = 'r';
                if (team == 'b') {
                    if (closeFar == 'c'){
                        trajectory = ((BlueTeamStartClose) opMode).buildRightPath(drive);
                    } else {
                        trajectory = ((BlueTeamStartFar) opMode).buildRightPath(drive);
                    }
                } else {
                    if (closeFar == 'c') {
                        trajectory = ((RedTeamStartClose) opMode).buildRightPath(drive);
                    } else {
                        trajectory = ((RedTeamStartFar) opMode).buildRightPath(drive);
                    }
                }
                break;
            }
            ((BlueTeamStartClose) opMode).setArmSystem(armSystem);
            telemetry.update();
        }

        telemetry.addData("", "Ready to follow %s team start %s, %s path.",
                team == 'b' ? "blue" : "red",
                closeFar == 'c' ? "close" : "far",
                path == 'l' ? "left" : (path == 'c' ? "center" : "right"));
        telemetry.update();
    }
}
