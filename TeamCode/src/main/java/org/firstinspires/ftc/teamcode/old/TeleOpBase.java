package org.firstinspires.ftc.teamcode.old;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

// import org.firstinspires.ftc.teamcode.robotctions;
import org.firstinspires.ftc.teamcode.HWMap;

import java.lang.*;

@TeleOp(name = "TeleOpBase", group = "Test")
public class TeleOpBase extends OpMode {
    HWMap robot = new HWMap();
    //robotctions robot = new robotctions();
    boolean startCheck = true;
    boolean speedModulo = true;
    int start = 0;
    float speedMod = 1f;
    float frontright = 0;
    float frontleft = 0;
    float rearright = 0;
    float rearleft = 0;

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.LinearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.Dump.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        robot.FrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.FrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.RearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.RearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.Dump.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.LinearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        frontright = 2700 * (-gamepad1.right_stick_y - gamepad1.right_stick_x);
        frontleft = 2700 * (gamepad1.left_stick_y - gamepad1.left_stick_x);
        rearright = 2700 * (-gamepad1.right_stick_y + gamepad1.right_stick_x);
        rearleft = 2700 * (gamepad1.left_stick_y + gamepad1.left_stick_x);

        robot.FrontRight.setVelocity(speedMod*(frontright*frontright*Math.signum(frontright))/2700);
        robot.FrontLeft.setVelocity(speedMod*(frontleft*frontleft*Math.signum(frontleft))/2700);
        robot.RearRight.setVelocity(speedMod*(rearright*rearright*Math.signum(rearright))/2700);
        robot.RearLeft.setVelocity(speedMod*(rearleft*rearleft*Math.signum(rearleft))/2700);

        if(gamepad2.x){
            robot.LinearSlide.setPower(0.5);
        }
        else if(gamepad2.b) {
            robot.LinearSlide.setPower(-0.5);
        }
        else {
            robot.LinearSlide.setPower(0);
        }

        if(gamepad1.a && speedModulo) {
            if(speedMod == 1f) {
                speedMod = 0.5f;
            }
            else {
                speedMod = 1f;
            }
            speedModulo = false;
        }
        else if(!gamepad1.a) {
            speedModulo = true;
        }

        if(gamepad2.dpad_left){
            robot.Dump.setPower(-0.5);
        }
        else if(gamepad2.dpad_right) {
            robot.Dump.setPower(0.5);
        }
        else {
            robot.Dump.setPower(0);
        }

        if(gamepad1.right_trigger > 0 || gamepad1.right_bumper){
            robot.duck.setPower(1);
        }
        else if(gamepad1.left_trigger > 0 || gamepad1.left_bumper){
            robot.duck.setPower(-1);
        }
        else {
            robot.duck.setPower(0);
        }

        if(gamepad2.right_trigger > 0) {
            robot.FrontCollector.setPower(gamepad2.right_trigger);
        }
        else {
            robot.FrontCollector.setPower(-gamepad2.left_trigger);
        }

        if(gamepad2.right_bumper){
            robot.MidCollector.setPower(-1);
        }
        else if(gamepad2.left_bumper){
            robot.MidCollector.setPower(1);
        }
        else {
            robot.MidCollector.setPower(0);
        }

        if (gamepad1.start && startCheck) {
            robot.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            startCheck = false;
        }
        else if (!gamepad1.start) {
            startCheck = true;
        }
    }

    public void linearSlidePos(int position) {
        robot.LinearSlide.setTargetPosition(position);

        robot.LinearSlide.setPower(1);
        while (robot.LinearSlide.isBusy()){}
        robot.LinearSlide.setPower(0);
    }

    public void dumpPos(int position) {
        robot.Dump.setTargetPosition(position);

        robot.Dump.setPower(1);
        while (robot.Dump.isBusy()){}
        robot.Dump.setPower(0);
    }
}