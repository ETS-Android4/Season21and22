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
    boolean brake = true;

    //boolean frontLCheck = true;
    //boolean frontCheck = true;
    //int start = 0;
    //int mode = 0;
    //int modeL = 0;
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
        robot.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        robot.FrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.FrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.RearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.RearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.Dump.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.LinearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        frontright = 3000 * (-gamepad1.right_stick_y - gamepad1.right_stick_x);
        frontleft = 3000 * (gamepad1.left_stick_y - gamepad1.left_stick_x);
        rearright = 3000 * (-gamepad1.right_stick_y + gamepad1.right_stick_x);
        rearleft = 3000 * (gamepad1.left_stick_y + gamepad1.left_stick_x);

        robot.FrontRight.setVelocity(speedMod*(frontright*frontright*Math.signum(frontright))/3000);
        robot.FrontLeft.setVelocity(speedMod*(frontleft*frontleft*Math.signum(frontleft))/3000);
        robot.RearRight.setVelocity(speedMod*(rearright*rearright*Math.signum(rearright))/3000);
        robot.RearLeft.setVelocity(speedMod*(rearleft*rearleft*Math.signum(rearleft))/3000);

        /*robot.FrontRight.setVelocity(speedMod*2600 * (-gamepad1.right_stick_y - gamepad1.right_stick_x));
        robot.FrontLeft.setVelocity(speedMod*2600 * (gamepad1.left_stick_y - gamepad1.left_stick_x));
        robot.RearRight.setVelocity(speedMod*2600 * (-gamepad1.right_stick_y + gamepad1.right_stick_x));
        robot.RearLeft.setVelocity(speedMod*2600 * (gamepad1.left_stick_y + gamepad1.left_stick_x));*/
        if(gamepad2.x){
            robot.LinearSlide.setPower(0.6);
        }
        else if(gamepad2.b) {
            robot.LinearSlide.setPower(-0.6);
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
            robot.Dump.setPower(-0.2);
        }
        else if(gamepad2.dpad_right) {
            robot.Dump.setPower(0.2);
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
            robot.MidCollector.setPower(-gamepad2.right_trigger);
        }
        else {
            robot.FrontCollector.setPower(-gamepad2.left_trigger);
            robot.MidCollector.setPower(gamepad2.left_trigger);
        }
        /*
        if(gamepad2.right_bumper){
            robot.MidCollector.setPower(-1);
        }
        else if(gamepad2.left_bumper){
            robot.MidCollector.setPower(1);
        }
        else {
            robot.MidCollector.setPower(0);
        }

        if(gamepad2.right_trigger > 0 && frontCheck){
            if(mode == 0) {
                robot.FrontCollector.setPower(1);
                robot.MidCollector.setPower(-1);
                mode = 1;
            }
            else if (mode == 1){
                robot.FrontCollector.setPower(0);
                robot.MidCollector.setPower(0);
                mode = 0;
            }
            frontCheck = false;
        }
        else if(gamepad2.right_trigger == 0) {
            frontCheck = true;
        }

        if(gamepad2.left_trigger > 0 && frontLCheck){
            if(modeL == 0) {
                robot.FrontCollector.setPower(-1);
                robot.MidCollector.setPower(1);
                modeL = 1;
            }
            else if (modeL == 1){
                robot.FrontCollector.setPower(0);
                robot.MidCollector.setPower(0);
                modeL = 0;
            }
            frontLCheck = false;
        }
        else if(gamepad2.left_trigger == 0) {
            frontLCheck = true;
        }*/

        if (gamepad1.start && startCheck) {
            if(!brake) {
                robot.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                brake = true;
            }
            else {
                robot.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                robot.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                robot.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                robot.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                brake = false;
            }
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