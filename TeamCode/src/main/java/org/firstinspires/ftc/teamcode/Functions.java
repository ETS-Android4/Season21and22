package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Functions {
    private int zero = 0;
    public float speedMod = 1f;
    public float frontright, frontleft, rearright, rearleft = 0f;
    private DcMotorEx FrontLeft = null;
    private DcMotorEx FrontRight  = null;
    private DcMotorEx RearLeft  = null;
    private DcMotorEx RearRight  = null;
    private DcMotorEx Dump = null;
    private DcMotorEx Spin = null;
    private DcMotorEx Collect = null;
    //private DcMotorEx Rubber = null;
    private BNO055IMU imu = null;

    public Functions (DcMotorEx fl, DcMotorEx fr, DcMotorEx rl, DcMotorEx rr, DcMotorEx d, DcMotorEx s, DcMotorEx c, BNO055IMU i) {
        FrontLeft = fl;
        FrontRight = fr;
        RearLeft = rl;
        RearRight = rr;
        Dump = d;
        Spin = s;
        Collect = c;
        imu = i;
    }

    public void setMode(DcMotor.RunMode mode){
        FrontRight.setMode(mode);
        FrontLeft.setMode(mode);
        RearRight.setMode(mode);
        RearLeft.setMode(mode);
    }

    public void changeZero(){
        if(zero % 2 == 0) {
            FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
        else {
            FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        zero++;
    }

    public void spinPos(int position) {
        Spin.setTargetPosition(position);

        Spin.setPower(1);
        while (Spin.isBusy()){}
        Spin.setPower(0);
    }

    public void dumpPos(int position) {
        Dump.setTargetPosition(position);

        Dump.setPower(1);
        while (Dump.isBusy()){}
        Dump.setPower(0);
    }

    public void runToPosition(DcMotorEx motor){
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void setVelocityExp(){
        FrontRight.setVelocity(speedMod*((frontright*frontright*Math.signum(frontright))/2700));
        FrontLeft.setVelocity(speedMod*((frontleft*frontleft*Math.signum(frontleft))/2700));
        RearRight.setVelocity(speedMod*((rearright*rearright*Math.signum(rearright))/2700));
        RearLeft.setVelocity(speedMod*((rearleft*rearleft*Math.signum(rearleft))/2700));
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
