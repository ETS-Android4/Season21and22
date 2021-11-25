package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Functions {
    HWMap robot = new HWMap();
    private int zero = 0;
    public float speedMod = 1f;
    public float frontright, frontleft, rearright, rearleft = 0f;

    public void setMode(DcMotor.RunMode mode){
        robot.FrontRight.setMode(mode);
        robot.FrontLeft.setMode(mode);
        robot.RearRight.setMode(mode);
        robot.RearLeft.setMode(mode);
    }

    public void changeZero(){
        if(zero % 2 == 0) {
            robot.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
        else {
            robot.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        zero++;
    }

    public void spinPos(int position) {
        robot.Spin.setTargetPosition(position);

        robot.Spin.setPower(1);
        while (robot.Spin.isBusy()){}
        robot.Spin.setPower(0);
    }

    public void dumpPos(int position) {
        robot.Dump.setTargetPosition(position);

        robot.Dump.setPower(1);
        while (robot.Dump.isBusy()){}
        robot.Dump.setPower(0);
    }

    public void runToPosition(DcMotorEx motor){
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void setVelocityExp(){
        robot.FrontRight.setVelocity(speedMod*((frontright*frontright*Math.signum(frontright))/2700));
        robot.FrontLeft.setVelocity(speedMod*((frontleft*frontleft*Math.signum(frontleft))/2700));
        robot.RearRight.setVelocity(speedMod*((rearright*rearright*Math.signum(rearright))/2700));
        robot.RearLeft.setVelocity(speedMod*((rearleft*rearleft*Math.signum(rearleft))/2700));
    }

    public void altSpeed(){
        if(speedMod == 1f) {
            speedMod = 0.5f;
        }
        else {
            speedMod = 1f;
        }
    }
}
