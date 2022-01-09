package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Functionsa {
    private int zero = 0;
    private float targetHeading = 0;
    private float currentHeading = 0;
    private int countsDistance = 0;
    private static final float COUNTS_PER_REV = 537.7f;
    private static final float GEAR_REDUCTION = 1.0f;
    private static final float WHEEL_DIAMETER = 3.75f;
    private static final double COUNTS_PER_INCH = (COUNTS_PER_REV * GEAR_REDUCTION) / (WHEEL_DIAMETER * 3.1415926535);
    public float speedMod = 1f;
    public float frontright, frontleft, rearright, rearleft = 0f;
    public DcMotorEx FrontLeft = null;
    public DcMotorEx FrontRight  = null;
    public DcMotorEx RearLeft  = null;
    public DcMotorEx RearRight  = null;
    public DcMotorEx Dump = null;
    public DcMotorEx LinearSlide = null;
    public DcMotorEx FrontCollector = null;
    public DcMotorEx MidCollector = null;
    public CRServo duck = null;
    public DistanceSensor distRight = null;
    public BNO055IMU imu = null;

    HardwareMap hwMap = null;
    Orientation angles;

    public Functionsa (){}

    public void init (HardwareMap ahwMap){
        hwMap = ahwMap;

        FrontLeft = (DcMotorEx) hwMap.dcMotor.get("frontLeft");
        FrontRight = (DcMotorEx) hwMap.dcMotor.get("frontRight");
        RearLeft = (DcMotorEx) hwMap.dcMotor.get("rearLeft");
        RearRight = (DcMotorEx) hwMap.dcMotor.get("rearRight");
        Dump = (DcMotorEx) hwMap.dcMotor.get("dump");
        LinearSlide = (DcMotorEx)hwMap.dcMotor.get("linearslide");
        FrontCollector = (DcMotorEx)hwMap.dcMotor.get("frontcollector");
        MidCollector = (DcMotorEx)hwMap.dcMotor.get("midcollector");
        duck = hwMap.crservo.get("duck");
        distRight = hwMap.get(DistanceSensor.class, "distright");

        imu = hwMap.get(BNO055IMU.class, "imu");
        runToPosition(LinearSlide);
        changeZero();
        LinearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Dump.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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

    public void linearSlidePos(int position) {
        LinearSlide.setTargetPosition(position);

        LinearSlide.setPower(1);
        while (LinearSlide.isBusy()){}
        LinearSlide.setPower(0);
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

    private void DriveStraight(double power) {
        FrontRight.setPower(power);
        FrontLeft.setPower(power);
        RearRight.setPower(power);
        RearLeft.setPower(power);
    }

    private void StopDriving() {
        DriveStraight(0);
    }

    public void DriveStraightDistance(double distance, double power) {
        countsDistance = (int)(COUNTS_PER_INCH*distance);
        FrontRight.setTargetPosition(FrontRight.getCurrentPosition() - countsDistance);
        FrontLeft.setTargetPosition(FrontLeft.getCurrentPosition() + countsDistance);
        RearRight.setTargetPosition(RearRight.getCurrentPosition() - countsDistance);
        RearLeft.setTargetPosition(RearLeft.getCurrentPosition() + countsDistance);

        DriveStraight(power);
        while ((FrontRight.isBusy() && RearLeft.isBusy() && RearRight.isBusy() && FrontLeft.isBusy())) {
            checkOrientation();
            if(distance == Math.abs(distance)) {
                if (currentHeading > targetHeading + 1) {
                    FrontRight.setPower(power * 0.9);
                    FrontLeft.setPower(power * 1.1);
                    RearRight.setPower(power * 0.9);
                    RearLeft.setPower(power * 1.1);
                } else if (currentHeading < targetHeading - 1) {
                    FrontRight.setPower(power * 1.1);
                    FrontLeft.setPower(power * 0.9);
                    RearRight.setPower(power * 1.1);
                    RearLeft.setPower(power * 0.9);
                } else {
                    FrontRight.setPower(power);
                    FrontLeft.setPower(power);
                    RearRight.setPower(power);
                    RearLeft.setPower(power);
                }
            }
            else{
                if (currentHeading > targetHeading + 1) {
                    FrontRight.setPower(power * 1.1);
                    FrontLeft.setPower(power * 0.9);
                    RearRight.setPower(power * 1.1);
                    RearLeft.setPower(power * 0.9);
                } else if (currentHeading < targetHeading - 1) {
                    FrontRight.setPower(power * 0.9);
                    FrontLeft.setPower(power * 1.1);
                    RearRight.setPower(power * 0.9);
                    RearLeft.setPower(power * 1.1);
                } else {
                    FrontRight.setPower(power);
                    FrontLeft.setPower(power);
                    RearRight.setPower(power);
                    RearLeft.setPower(power);
                }
            }
        }

        StopDriving();
    }

    public int Scan() {
        int distance = 0;
        for(int i = 0; i < 50; i++) {
            if (distRight.getDistance(DistanceUnit.INCH) < 20) {
                distance += 1;
            } else if (distRight.getDistance(DistanceUnit.INCH) > 30) {
                distance += 3;
            } else {
                distance += 2;
            }
        }
        return distance/100;
    }

    public void Strafe(double distance, double power) {
        countsDistance = (int)(COUNTS_PER_INCH*distance);
        FrontRight.setTargetPosition(FrontRight.getCurrentPosition() - countsDistance);
        FrontLeft.setTargetPosition(FrontLeft.getCurrentPosition() - countsDistance);
        RearRight.setTargetPosition(RearRight.getCurrentPosition() + countsDistance);
        RearLeft.setTargetPosition(RearLeft.getCurrentPosition() + countsDistance);

        DriveStraight(power);
        while ((FrontRight.isBusy() && RearLeft.isBusy() && RearRight.isBusy() && FrontLeft.isBusy())) {
            checkOrientation();
            if(distance == Math.abs(distance)) {
                if (currentHeading > targetHeading + 1) {
                    FrontRight.setPower(power * 0.9);
                    FrontLeft.setPower(power * 0.9);
                    RearRight.setPower(power * 1.1);
                    RearLeft.setPower(power * 1.1);
                } else if (currentHeading < targetHeading - 1) {
                    FrontRight.setPower(power * 1.1);
                    FrontLeft.setPower(power * 1.1);
                    RearRight.setPower(power * 0.9);
                    RearLeft.setPower(power * 0.9);
                } else {
                    FrontRight.setPower(power);
                    FrontLeft.setPower(power);
                    RearRight.setPower(power);
                    RearLeft.setPower(power);
                }
            }
            else{
                if (currentHeading > targetHeading + 1) {
                    FrontRight.setPower(power * 1.1);
                    FrontLeft.setPower(power * 1.1);
                    RearRight.setPower(power * 0.9);
                    RearLeft.setPower(power * 0.9);
                } else if (currentHeading < targetHeading - 1) {
                    FrontRight.setPower(power * 0.9);
                    FrontLeft.setPower(power * 0.9);
                    RearRight.setPower(power * 1.1);
                    RearLeft.setPower(power * 1.1);
                } else {
                    FrontRight.setPower(power);
                    FrontLeft.setPower(power);
                    RearRight.setPower(power);
                    RearLeft.setPower(power);
                }
            }
        }

        StopDriving();
    }

    public void Turn(int distance, double power, float target) {
        targetHeading = target;

        FrontRight.setTargetPosition(FrontRight.getCurrentPosition() + distance);
        FrontLeft.setTargetPosition(FrontLeft.getCurrentPosition() + distance);
        RearRight.setTargetPosition(RearRight.getCurrentPosition() + distance);
        RearLeft.setTargetPosition(RearLeft.getCurrentPosition() + distance);

        DriveStraight(power);
        while ((FrontRight.isBusy() && RearLeft.isBusy() && RearRight.isBusy() && FrontLeft.isBusy())) {
            checkOrientation();
            if(Math.abs(currentHeading)-Math.abs(targetHeading) > 45){
                FrontRight.setPower(power*1.1);
                FrontLeft.setPower(power*1.1);
                RearRight.setPower(power*1.1);
                RearLeft.setPower(power*1.1);
            }
            else {
                FrontRight.setPower(power*0.9);
                FrontLeft.setPower(power*0.9);
                RearRight.setPower(power*0.9);
                RearLeft.setPower(power*0.9);
            }
        }

        StopDriving();
    }

    public void checkOrientation() {
        // read the orientation of the robot
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        imu.getPosition();
        // and save the heading
        currentHeading = angles.firstAngle;
    }
}
