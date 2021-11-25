package org.firstinspires.ftc.teamcode.old;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Functions;
import org.firstinspires.ftc.teamcode.HWMap;

import java.lang.*;

public class TeleOpBase extends OpMode {

    HWMap robot = new HWMap();
    Functions fun = new Functions();
    boolean startCheck = true;
    int start = 0;
    float frontright = 0;
    float frontleft = 0;
    float rearright = 0;
    float rearleft = 0;

    @Override
    public void init() {
        robot.init(hardwareMap);
        fun.changeZero();
        robot.Spin.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.Dump.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        fun.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.Spin.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.Dump.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.Spin.setTargetPosition(0);
        robot.Dump.setTargetPosition(0);
        robot.Spin.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.Dump.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    @Override
    public void loop() {
        frontright = 2700 * (-gamepad1.right_stick_y - gamepad1.right_stick_x);
        frontleft = 2700 * (gamepad1.left_stick_y - gamepad1.left_stick_x);
        rearright = 2700 * (-gamepad1.right_stick_y + gamepad1.right_stick_x);
        rearleft = 2700 * (gamepad1.left_stick_y + gamepad1.left_stick_x);

        robot.FrontRight.setVelocity((frontright*frontright*Math.signum(frontright))/2700);
        robot.FrontLeft.setVelocity((frontleft*frontleft*Math.signum(frontleft))/2700);
        robot.RearRight.setVelocity((rearright*rearright*Math.signum(rearright))/2700);
        robot.RearLeft.setVelocity((rearleft*rearleft*Math.signum(rearleft))/2700);

        if(gamepad1.a){
            fun.spinPos(0);
        }
        else if(gamepad1.b){
            fun.spinPos(-500);
        }
        else if(gamepad1.x) {
            fun.spinPos(-1000);
        }

        if(gamepad1.dpad_left){
            fun.dumpPos(0);
        }
        else if(gamepad1.dpad_up){
            fun.dumpPos(-300);
        }
        else if(gamepad1.dpad_right) {
            fun.dumpPos(500);
        }

        if(gamepad1.right_trigger > 0) {
            robot.Collect.setPower(gamepad1.right_trigger);
        }
        else {
            robot.Collect.setPower(-gamepad1.left_trigger);
        }

        if(gamepad1.right_bumper){
            robot.Rubber.setPower(1);
        }
        else if(gamepad1.left_bumper){
            robot.Rubber.setPower(-1);
        }
        else {
            robot.Rubber.setPower(0);
        }

        if (gamepad1.start && startCheck) {
            fun.changeZero();
            startCheck = false;
        }
        else if (!gamepad1.start) {
            startCheck = true;
        }
    }
}