package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@TeleOp(name = "Summer", group = "Test")
public class SummerTeleOp extends OpMode {

    HWMap robot = new HWMap();
    boolean startCheck = true;
    int start = 0;

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        robot.FrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.FrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.RearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.RearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        robot.FrontLeft.setVelocity(2700 * (gamepad1.left_stick_y + gamepad1.left_stick_x));
        robot.RearLeft.setVelocity(2700 * (gamepad1.left_stick_y - gamepad1.left_stick_x));

        robot.FrontRight.setVelocity(2700 * (-gamepad1.right_stick_y - gamepad1.right_stick_x));
        robot.RearRight.setVelocity(2700 * (-gamepad1.right_stick_y + gamepad1.right_stick_x));
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