package org.firstinspires.ftc.teamcode.andy;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.andy.HWMapAndy;

import java.lang.*;

@TeleOp(name = "AndyBot", group = "Test")
public class AndyBot extends OpMode {

    HWMapAndy robot = new HWMapAndy();
    boolean startCheck = true;
    int start = 0;

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        /*
        robot.FrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.FrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.RearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.RearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);*/
    }

    @Override
    public void loop() {
        /*robot.FrontRight.setVelocity(-2700 * (-gamepad1.right_stick_y - gamepad1.right_stick_x));
        robot.FrontLeft.setVelocity(2700 * (gamepad1.left_stick_y - gamepad1.left_stick_x));
        robot.RearRight.setVelocity(-2700 * (-gamepad1.right_stick_y + gamepad1.right_stick_x));
        robot.RearLeft.setVelocity(2700 * (gamepad1.left_stick_y + gamepad1.left_stick_x));*/
        robot.FrontRight.setPower(-gamepad1.right_stick_y - gamepad1.right_stick_x);
        robot.FrontLeft.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x);
        robot.RearRight.setPower(-(-gamepad1.right_stick_y + gamepad1.right_stick_x));
        robot.RearLeft.setPower(gamepad1.left_stick_y + gamepad1.left_stick_x);

        if(gamepad1.right_trigger > 0) {
            robot.Rubber.setPower(gamepad1.right_trigger);
        }
        else {
            robot.Rubber.setPower(-gamepad1.left_trigger);
        }

        if (gamepad1.start && startCheck && start == 0) {
            robot.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            start++;
            startCheck = false;
        } else if (gamepad1.start && startCheck && start == 1) {
            robot.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            start--;
            startCheck = false;
        } else if (!gamepad1.start) {
            startCheck = true;
        }
    }
}