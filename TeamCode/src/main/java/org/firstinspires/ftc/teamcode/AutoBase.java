package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@Autonomous(name = "AutoBase", group = "Final", preselectTeleOp = "TeleOpBase")
public class AutoBase extends LinearOpMode {
    HWMap robot = new HWMap();
    Orientation angles;
    private double targetHeading = 0;
    private double currentHeading = 0;
    private double turn_error = 0;
    int distance = -24;
    double offset = 0;
    double p = 0;
    double k_p = 0.002;
    double turn_k_p = 0.08;
    /*double k_p = 0.0028;
    double turn_k_p = 0.02;*/
    double steer = 0;
    double output = 0;
    double drive_speed = 0.9;
    double turn_speed = 0.7;
    double drive_speed3 = 1;
    double turn_speed3 = 0.8;
    private static final float COUNTS_PER_REV = 537.7f;
    private static final float GEAR_REDUCTION = 1.0f;
    private static final float WHEEL_DIAMETER = 3.75f;
    private static final double COUNTS_PER_INCH = (COUNTS_PER_REV * GEAR_REDUCTION) / (WHEEL_DIAMETER * 3.1415926535);

    @Override
    public void runOpMode() throws InterruptedException {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        robot.init(hardwareMap);
        robot.imu.initialize(parameters);
        robot.FrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.FrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.RearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.RearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.Dump.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.LinearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.FrontRight.setTargetPosition(0);
        robot.FrontLeft.setTargetPosition(0);
        robot.RearRight.setTargetPosition(0);
        robot.RearLeft.setTargetPosition(0);
        robot.Dump.setTargetPosition(0);
        robot.LinearSlide.setTargetPosition(0);

        robot.FrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.FrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.RearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.RearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.Dump.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.LinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.Dump.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.LinearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        while (!isStopRequested() && !robot.imu.isGyroCalibrated()) {
            sleep(50);
            idle();
        }
        telemetry.addData("IMU calibration status", robot.imu.getCalibrationStatus().toString());
        telemetry.update();
        waitForStart();
        robot.imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

        checkOrientation();
        offset = currentHeading;

        DriveStraightDistance(17, false);
        sleep(100);
        orient(0.5);
        int i = Scan();
        switch (i) {
            case 1:
                linearSlidePos(0);
                sleep(100);
                DriveStraightDistance(-2, false);
                distance += 2;
                break;
            case 2:
                linearSlidePos(-500);
                break;
            case 3:
                linearSlidePos(-1000);
                sleep(100);
                DriveStraightDistance(2, false);
                distance -= 2;
                break;
        }
        sleep(100);
        dumpPos(-400);
        sleep(1000);
        dumpPos(0);
        linearSlidePos(0);
        DriveStraightDistance(distance, false);
        sleep(100);
        DriveStraightDistance(8, false);
        orient(0.5);
        Strafe(50);
        StrafeSlow(3);
        sleep(100);
        duck(5);
        Strafe(-2);
        sleep(100);
        targetHeading = -90;
        Turn(-1000);
        sleep(100);
        Strafe(-6);
        sleep(100);
        DriveStraightDistance(60, false);
        sleep(100);
        Strafe(-20);
        DriveStraightDistance(40, false);
        dumpPos(200);
        robot.FrontCollector.setPower(1);
        robot.MidCollector.setPower(1);
        targetHeading = 90;
        Turn(-2000);
        DriveStraightDistance(-2, false);
        robot.FrontCollector.setPower(0);
        robot.MidCollector.setPower(0);
    }
    private void DriveStraight(double rightPower, double leftPower) {
        robot.FrontRight.setPower(rightPower);
        robot.FrontLeft.setPower(leftPower);
        robot.RearRight.setPower(rightPower);
        robot.RearLeft.setPower(leftPower);
    }

    private void StopDriving() {
        DriveStraight(0, 0);
    }

    private void DriveStraightDistance(int distance1, boolean fast) {
        int distance = (int)(distance1*COUNTS_PER_INCH);
        telemetry.update();

        robot.FrontRight.setTargetPosition(robot.FrontRight.getCurrentPosition() - distance);
        robot.FrontLeft.setTargetPosition(robot.FrontLeft.getCurrentPosition() + distance);
        robot.RearRight.setTargetPosition(robot.RearRight.getCurrentPosition() - distance);
        robot.RearLeft.setTargetPosition(robot.RearLeft.getCurrentPosition() + distance);

        DriveStraight(drive_speed, drive_speed);
        while ((robot.FrontRight.isBusy() && robot.RearLeft.isBusy() && robot.RearRight.isBusy() && robot.FrontLeft.isBusy()) && opModeIsActive()) {
            checkOrientation();
            telemetry.addData("IMU: ", currentHeading);
            telemetry.update();
            p = Math.abs(k_p * (robot.FrontRight.getTargetPosition() - robot.FrontRight.getCurrentPosition()));
            turn_error = turn_k_p*(targetHeading - currentHeading);
            while(turn_error > 180){
                turn_error -= 360;
            }
            while(turn_error < -180){
                turn_error += 360;
            }
            output = Range.clip(p - turn_error, -drive_speed, drive_speed);
            steer = Range.clip(p + turn_error, -drive_speed, drive_speed);
            DriveStraight(output, steer);
            /*if (distance == Math.abs(distance)) {
                if (currentHeading > targetHeading + 1) {
                    robot.FrontRight.setPower(power * 0.9);
                    robot.FrontLeft.setPower(power * 1.1);
                    robot.RearRight.setPower(power * 0.9);
                    robot.RearLeft.setPower(power * 1.1);
                } else if (currentHeading < targetHeading - 1) {
                    robot.FrontRight.setPower(power * 1.1);
                    robot.FrontLeft.setPower(power * 0.9);
                    robot.RearRight.setPower(power * 1.1);
                    robot.RearLeft.setPower(power * 0.9);
                } else {
                    robot.FrontRight.setPower(power);
                    robot.FrontLeft.setPower(power);
                    robot.RearRight.setPower(power);
                    robot.RearLeft.setPower(power);
                }
            } else {
                if (currentHeading > targetHeading + 1) {
                    robot.FrontRight.setPower(power * 1.1);
                    robot.FrontLeft.setPower(power * 0.9);
                    robot.RearRight.setPower(power * 1.1);
                    robot.RearLeft.setPower(power * 0.9);
                } else if (currentHeading < targetHeading - 1) {
                    robot.FrontRight.setPower(power * 0.9);
                    robot.FrontLeft.setPower(power * 1.1);
                    robot.RearRight.setPower(power * 0.9);
                    robot.RearLeft.setPower(power * 1.1);
                } else {
                    robot.FrontRight.setPower(power);
                    robot.FrontLeft.setPower(power);
                    robot.RearRight.setPower(power);
                    robot.RearLeft.setPower(power);
                }
            }*/
        }
        StopDriving();
        if(!fast) {
            sleep(10);
        }
    }

    private void Strafe(int distance1) {
        int distance = (int)(distance1*COUNTS_PER_INCH);
        telemetry.update();

        robot.FrontRight.setTargetPosition(robot.FrontRight.getCurrentPosition() - distance);
        robot.FrontLeft.setTargetPosition(robot.FrontLeft.getCurrentPosition() - distance);
        robot.RearRight.setTargetPosition(robot.RearRight.getCurrentPosition() + distance);
        robot.RearLeft.setTargetPosition(robot.RearLeft.getCurrentPosition() + distance);

        DriveStraight(turn_speed, turn_speed);
        while ((robot.FrontRight.isBusy() && robot.RearLeft.isBusy() && robot.RearRight.isBusy() && robot.FrontLeft.isBusy()) && opModeIsActive()) {
            idle();

            checkOrientation();
            p = Math.abs(k_p * (robot.FrontRight.getTargetPosition() - robot.FrontRight.getCurrentPosition()));
            turn_error = turn_k_p*(targetHeading - currentHeading);
            while(turn_error > 180){
                turn_error -= 360;
            }
            while(turn_error < -180){
                turn_error += 360;
            }
            output = Range.clip(p - turn_error, -turn_speed, turn_speed);
            steer = Range.clip(p + turn_error, -turn_speed, turn_speed);
            DriveStraight(output, steer);
            /*checkOrientation();
            if (distance == Math.abs(distance)) {
                if (currentHeading > targetHeading + 1) {
                    robot.FrontRight.setPower(power * 0.9);
                    robot.FrontLeft.setPower(power * 0.9);
                    robot.RearRight.setPower(power * 1.1);
                    robot.RearLeft.setPower(power * 1.1);
                } else if (currentHeading < targetHeading - 1) {
                    robot.FrontRight.setPower(power * 1.1);
                    robot.FrontLeft.setPower(power * 1.1);
                    robot.RearRight.setPower(power * 0.9);
                    robot.RearLeft.setPower(power * 0.9);
                } else {
                    robot.FrontRight.setPower(power);
                    robot.FrontLeft.setPower(power);
                    robot.RearRight.setPower(power);
                    robot.RearLeft.setPower(power);
                }
            } else {
                if (currentHeading > targetHeading + 1) {
                    robot.FrontRight.setPower(power * 1.1);
                    robot.FrontLeft.setPower(power * 1.1);
                    robot.RearRight.setPower(power * 0.9);
                    robot.RearLeft.setPower(power * 0.9);
                } else if (currentHeading < targetHeading - 1) {
                    robot.FrontRight.setPower(power * 0.9);
                    robot.FrontLeft.setPower(power * 0.9);
                    robot.RearRight.setPower(power * 1.1);
                    robot.RearLeft.setPower(power * 1.1);
                } else {
                    robot.FrontRight.setPower(power);
                    robot.FrontLeft.setPower(power);
                    robot.RearRight.setPower(power);
                    robot.RearLeft.setPower(power);
                }
            }*/
        }
        StopDriving();
        sleep(10);
    }

    private void StrafeSlow(int distance1) {
        int distance = (int)(distance1*COUNTS_PER_INCH);
        telemetry.update();

        robot.FrontRight.setTargetPosition(robot.FrontRight.getCurrentPosition() - distance);
        robot.FrontLeft.setTargetPosition(robot.FrontLeft.getCurrentPosition() - distance);
        robot.RearRight.setTargetPosition(robot.RearRight.getCurrentPosition() + distance);
        robot.RearLeft.setTargetPosition(robot.RearLeft.getCurrentPosition() + distance);

        DriveStraight(turn_speed/2, turn_speed/2);
        while ((robot.FrontRight.isBusy() && robot.RearLeft.isBusy() && robot.RearRight.isBusy() && robot.FrontLeft.isBusy()) && opModeIsActive()) {
            idle();

            checkOrientation();
            p = Math.abs(k_p * (robot.FrontRight.getTargetPosition() - robot.FrontRight.getCurrentPosition()));
            turn_error = turn_k_p*(targetHeading - currentHeading);
            while(turn_error > 180){
                turn_error -= 360;
            }
            while(turn_error < -180){
                turn_error += 360;
            }
            output = Range.clip(p - turn_error, -turn_speed, turn_speed);
            steer = Range.clip(p + turn_error, -turn_speed, turn_speed);
            DriveStraight(output, steer);
            /*checkOrientation();
            if (distance == Math.abs(distance)) {
                if (currentHeading > targetHeading + 1) {
                    robot.FrontRight.setPower(power * 0.9);
                    robot.FrontLeft.setPower(power * 0.9);
                    robot.RearRight.setPower(power * 1.1);
                    robot.RearLeft.setPower(power * 1.1);
                } else if (currentHeading < targetHeading - 1) {
                    robot.FrontRight.setPower(power * 1.1);
                    robot.FrontLeft.setPower(power * 1.1);
                    robot.RearRight.setPower(power * 0.9);
                    robot.RearLeft.setPower(power * 0.9);
                } else {
                    robot.FrontRight.setPower(power);
                    robot.FrontLeft.setPower(power);
                    robot.RearRight.setPower(power);
                    robot.RearLeft.setPower(power);
                }
            } else {
                if (currentHeading > targetHeading + 1) {
                    robot.FrontRight.setPower(power * 1.1);
                    robot.FrontLeft.setPower(power * 1.1);
                    robot.RearRight.setPower(power * 0.9);
                    robot.RearLeft.setPower(power * 0.9);
                } else if (currentHeading < targetHeading - 1) {
                    robot.FrontRight.setPower(power * 0.9);
                    robot.FrontLeft.setPower(power * 0.9);
                    robot.RearRight.setPower(power * 1.1);
                    robot.RearLeft.setPower(power * 1.1);
                } else {
                    robot.FrontRight.setPower(power);
                    robot.FrontLeft.setPower(power);
                    robot.RearRight.setPower(power);
                    robot.RearLeft.setPower(power);
                }
            }*/
        }
        StopDriving();
        sleep(10);
    }

    private void Turn(int distance) {
        robot.FrontRight.setTargetPosition(robot.FrontRight.getCurrentPosition() + distance);
        robot.FrontLeft.setTargetPosition(robot.FrontLeft.getCurrentPosition() + distance);
        robot.RearRight.setTargetPosition(robot.RearRight.getCurrentPosition() + distance);
        robot.RearLeft.setTargetPosition(robot.RearLeft.getCurrentPosition() + distance);

        DriveStraight(turn_speed, turn_speed);
        while ((robot.FrontRight.isBusy() && robot.RearLeft.isBusy() && robot.RearRight.isBusy() && robot.FrontLeft.isBusy()) && opModeIsActive()) {
            idle();
            //p = robot.FrontRight.getTargetPosition() - robot.FrontRight.getCurrentPosition();
            //output = Range.clip(k_p*p, -1, 1);
            checkOrientation();
            telemetry.addData("IMU: ", currentHeading);
            telemetry.update();
            turn_error = targetHeading - currentHeading;
            while(turn_error > 180){
                turn_error -= 360;
            }
            while(turn_error < -180){
                turn_error += 360;
            }
            steer = Range.clip(turn_k_p*turn_error, -turn_speed, turn_speed);
            DriveStraight(-steer, steer);
            /*checkOrientation();
            if (distance == Math.abs(distance)) {
                if (currentHeading > targetHeading + 1) {
                    robot.FrontRight.setPower(power * 0.9);
                    robot.FrontLeft.setPower(power * 1.1);
                    robot.RearRight.setPower(power * 0.9);
                    robot.RearLeft.setPower(power * 1.1);
                } else if (currentHeading < targetHeading - 1) {
                    robot.FrontRight.setPower(power * 1.1);
                    robot.FrontLeft.setPower(power * 0.9);
                    robot.RearRight.setPower(power * 1.1);
                    robot.RearLeft.setPower(power * 0.9);
                } else {
                    robot.FrontRight.setPower(power);
                    robot.FrontLeft.setPower(power);
                    robot.RearRight.setPower(power);
                    robot.RearLeft.setPower(power);
                }
            } else {
                if (currentHeading > targetHeading + 1) {
                    robot.FrontRight.setPower(power * 1.1);
                    robot.FrontLeft.setPower(power * 0.9);
                    robot.RearRight.setPower(power * 1.1);
                    robot.RearLeft.setPower(power * 0.9);
                } else if (currentHeading < targetHeading - 1) {
                    robot.FrontRight.setPower(power * 0.9);
                    robot.FrontLeft.setPower(power * 1.1);
                    robot.RearRight.setPower(power * 0.9);
                    robot.RearLeft.setPower(power * 1.1);
                } else {
                    robot.FrontRight.setPower(power);
                    robot.FrontLeft.setPower(power);
                    robot.RearRight.setPower(power);
                    robot.RearLeft.setPower(power);
                }
            }*/
        }
        /*if(half) {
            checkOrientation();
            offset -= currentHeading;
            targetHeading = 0;
        }*/
        StopDriving();
        sleep(10);
    }

    private void orient(double seconds){
        robot.FrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.FrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.RearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.RearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        for(int k = 0; k <= 100*seconds; k++){
            checkOrientation();
            turn_error = targetHeading - currentHeading;
            while(turn_error > 180){
                turn_error -= 360;
            }
            while(turn_error < -180){
                turn_error += 360;
            }
            steer = Range.clip(turn_k_p*turn_error, -1, 1);
            DriveStraight(steer, steer);
            sleep(10);
        }
        robot.FrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.FrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.RearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.RearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.FrontRight.setTargetPosition(0);
        robot.FrontLeft.setTargetPosition(0);
        robot.RearRight.setTargetPosition(0);
        robot.RearLeft.setTargetPosition(0);

        robot.FrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.FrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.RearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.RearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void checkOrientation() {
        // read the orientation of the robot
        angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        robot.imu.getPosition();
        // and save the heading
        currentHeading = angles.firstAngle - offset;
    }

    public int Scan() {
        int scanDistance = 0;
        double currentDistance = 0;
        for(int i = 0; i < 50; i++) {
            currentDistance = robot.distRight.getDistance(DistanceUnit.INCH);
            if (currentDistance < 11) {
                scanDistance += 1;
            } else if (currentDistance > 18) {
                scanDistance += 3;
            } else {
                scanDistance += 2;
            }
            telemetry.addData("scanDistance: ", scanDistance);
            telemetry.update();
        }
        return Math.round(scanDistance/50);
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

    public void duck (int seconds) {
        robot.duck.setPower(-1);
        sleep(seconds*1000);
        robot.duck.setPower(0);
    }
}
