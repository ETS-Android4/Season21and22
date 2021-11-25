package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "TeleOpOmni", group = "Final")
public class TeleOpOmni extends OpMode {
    HWMap robot = new HWMap();
    Functions fun = new Functions();

    boolean startCheck = true;
    boolean xCheck = true;
    boolean speedModulo = true;
    int driveMode = 0;

    float r, l, r2, l2, right, left, speed, turn = 0f;
    public void init(){
        robot.init(hardwareMap);
        fun.changeZero();
        robot.Spin.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.Dump.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fun.runToPosition(robot.Spin);
        fun.runToPosition(robot.Dump);
        fun.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void loop(){
        if(gamepad1.x && xCheck){
            driveMode++;
            if(driveMode > 2){ driveMode = 0; }
            xCheck = false;
        }
        else if(!gamepad1.x){
            xCheck = true;
        }

        if(gamepad1.dpad_left && speedModulo) {
            fun.altSpeed();
            speedModulo = false;
        }
        else if(!gamepad1.dpad_left) {
            speedModulo = true;
        }

        if(gamepad1.a){
            fun.spinPos(0);
        }
        else if(gamepad1.b){
            fun.spinPos(-500);
        }
        else if(gamepad1.y) {
            fun.spinPos(-1000);
        }

        if(gamepad1.dpad_up){
            fun.dumpPos(0);
        }
        else if(gamepad1.dpad_left){
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

        switch (driveMode) {
            case 0:     // Tank Drive
                right = Math.abs(gamepad1.right_stick_y)+Math.abs(gamepad1.right_stick_x);
                left = Math.abs(gamepad1.left_stick_y)+Math.abs(gamepad1.left_stick_x);
                fun.frontright = 2700 * ((-gamepad1.right_stick_y - gamepad1.right_stick_x)/right);
                fun.frontleft = 2700 * ((gamepad1.left_stick_y - gamepad1.left_stick_x)/left);
                fun.rearright = 2700 * ((-gamepad1.right_stick_y + gamepad1.right_stick_x)/right);
                fun.rearleft = 2700 * ((gamepad1.left_stick_y + gamepad1.left_stick_x)/left);

                fun.setVelocityExp();
                break;

            case 1:     // Arcade Drive
                left = Math.abs(gamepad1.left_stick_y)+Math.abs(gamepad1.left_stick_x);
                r = ((gamepad1.left_stick_y - gamepad1.left_stick_x)/left);     //FrontRight and RearLeft
                l = ((gamepad1.left_stick_y + gamepad1.left_stick_x)/left);     //FrontLeft and RearRight
                r2 = Math.abs(r)+Math.abs(gamepad1.right_stick_x);
                l2 = Math.abs(l)+Math.abs(gamepad1.right_stick_x);
                fun.frontright = 2700 * ((r-gamepad1.right_stick_x)/r2);
                fun.frontleft = 2700 * ((l+gamepad1.right_stick_x)/l2);
                fun.rearright = 2700 * ((l-gamepad1.right_stick_x)/l2);
                fun.rearleft = 2700 * ((r+gamepad1.right_stick_x)/r2);

                fun.setVelocityExp();
                break;

            case 2:     // Car Drive
                speed = gamepad1.right_trigger - gamepad1.left_trigger;
                turn = gamepad1.left_stick_x;

                left = ((1 - Math.abs(turn)) * speed + (1 - Math.abs(speed)) * turn + turn + speed) / 2;
                right = ((1 - Math.abs(turn)) * speed - (1 - Math.abs(speed)) * turn - turn + speed) / 2;

                robot.FrontRight.setVelocity(fun.speedMod * 2700 * right);
                robot.FrontLeft.setVelocity(fun.speedMod * 2700 * left);
                robot.RearRight.setVelocity(fun.speedMod * 2700 * right);
                robot.RearLeft.setVelocity(fun.speedMod * 2700 * left);
                break;
        }
    }
}
