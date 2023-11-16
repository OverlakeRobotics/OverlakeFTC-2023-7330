package org.firstinspires.ftc.teamcode.opmodes.TestOpModes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.opmodes.auton.BlueTeamStartClose;
import org.firstinspires.ftc.teamcode.opmodes.auton.BlueTeamStartFar;
import org.firstinspires.ftc.teamcode.opmodes.auton.RedTeamStartClose;
import org.firstinspires.ftc.teamcode.opmodes.auton.RedTeamStartFar;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Auton Test", group="TeleOp")
public class AutonTest extends LinearOpMode {



    private Pose2d startPosition;


    private char team = 'n';
    private char closeFar = 'n';

    private Pose2d startPos;

    private LinearOpMode opMode;

    private Telemetry telemetry;
    private final SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

    private TrajectorySequence trajectory;

    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        waitForStart();

        drive.followTrajectorySequence(trajectory);

    }

    private void initialize() {

        while (true) {
            telemetry.addData("Current Team - ", "%c", team == 'n' ? "none" : (team == 'b' ? "blue" : "red"));
            telemetry.addData("Current Start Position - ", "%s", closeFar == 'n' ? "none" : (closeFar == 'c' ? "close" : "far"));
            if (team == 'n') {
                telemetry.addLine("Press d-pad left for blue team \n Press d-pad right for red team");
                if (gamepad1.dpad_left) {
                    team = 'b';
                } else if (gamepad1.dpad_right) {
                    team = 'r';
                }
            } else {
                telemetry.addLine("Press d-pad up for close start \n Press d-pad down for far start");
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
            } else {
                opMode = new BlueTeamStartFar();
            }
        } else {
            if (closeFar == 'c') {
                opMode = new RedTeamStartClose();
            } else {
                opMode = new RedTeamStartFar();
            }
        }

        char path;
        while (true) {
            telemetry.addData("Current Team - ", "%c", team == 'n' ? "none" : (team == 'b' ? "blue" : "red"));
            telemetry.addData("Current Start Position - ", "%s", closeFar == 'n' ? "none" : (closeFar == 'c' ? "close" : "far"));

            telemetry.addLine ("press d-pad left for left path, press d-pad up for center path, press d-pad right for right path");

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

            telemetry.update();
        }

        telemetry.addData("", "Ready to follow %s team start %s, %s path.",
                team == 'b' ? "blue" : "red",
                closeFar == 'c' ? "close" : "far",
                path == 'l' ? "left" : (path == 'c' ? "center" : "right"));
        telemetry.update();
    }
}
