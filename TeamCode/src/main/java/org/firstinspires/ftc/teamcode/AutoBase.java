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

@Autonomous(name = "AutoBase", group = "Final")
public class AutoBase extends LinearOpMode {
    HWMap robot = new HWMap();
    Orientation angles;
    private float targetHeading = 0;
    private float currentHeading = 0;
    private double turn_error = 0;
    private static final float COUNTS_PER_REV = 537.7f;
    private static final float GEAR_REDUCTION = 1.0f;
    private static final float WHEEL_DIAMETER = 3.75f;
    private static final double COUNTS_PER_INCH = (COUNTS_PER_REV * GEAR_REDUCTION) / (WHEEL_DIAMETER * 3.1415926535);

    @Override
    public void runOpMode() throws InterruptedException {
        /*BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);*/

        robot.init(hardwareMap);
        //robot.imu.initialize(parameters);
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
        robot.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        /*robot.imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }
        });*/

        DriveStraightDistance(-500, 0.5);
        sleep(1000);
        duck(10);
        DriveStraightDistance(50000, 1);
        //DriveStraightDistance((int)(537.7/(3.1415926535*3.75)), 0.5);

        //DISTANCES
        //38" from edge to shipping hub
        //17" robot length
        //48" to duck
        /*DriveStraightDistance(21, 1);
        int i = Scan();
        switch (i) {
            case 1:
                linearSlidePos(0);
                break;
            case 2:
                linearSlidePos(-500);
                break;
            case 3:
                linearSlidePos(-1000);
                break;
        }
        dumpPos(-300);
        sleep(500);
        dumpPos(0);
        linearSlidePos(0);
        DriveStraightDistance(-22, 1);
        DriveStraightDistance(2, 0.6);
        Strafe(-48, 0.8);
        duck(3);
        Strafe(2, 0.8);
        Turn(90, 0.6);
        Strafe(3, 1);
        DriveStraightDistance(144, 1);*/
    }

    private void DriveStraight(double power) {
        robot.FrontRight.setPower(power);
        robot.FrontLeft.setPower(power);
        robot.RearRight.setPower(power);
        robot.RearLeft.setPower(power);
    }

    private void StopDriving() {
        DriveStraight(0);
    }

    public void DriveStraightDistance(int distance, double power) {
        robot.FrontRight.setTargetPosition(robot.FrontRight.getCurrentPosition() - distance);
        robot.FrontLeft.setTargetPosition(robot.FrontLeft.getCurrentPosition() + distance);
        robot.RearRight.setTargetPosition(robot.RearRight.getCurrentPosition() - distance);
        robot.RearLeft.setTargetPosition(robot.RearLeft.getCurrentPosition() + distance);

        DriveStraight(power);
        while ((robot.FrontRight.isBusy() && robot.RearLeft.isBusy() && robot.RearRight.isBusy() && robot.FrontLeft.isBusy()) && opModeIsActive()) {
            idle();
        }

        StopDriving();
    }

    private void Strafe(int distance, double power) {
        telemetry.update();

        robot.FrontRight.setTargetPosition(robot.FrontRight.getCurrentPosition() - distance);
        robot.FrontLeft.setTargetPosition(robot.FrontLeft.getCurrentPosition() - distance);
        robot.RearRight.setTargetPosition(robot.RearRight.getCurrentPosition() + distance);
        robot.RearLeft.setTargetPosition(robot.RearLeft.getCurrentPosition() + distance);

        DriveStraight(power);
        while ((robot.FrontRight.isBusy() && robot.RearLeft.isBusy() && robot.RearRight.isBusy() && robot.FrontLeft.isBusy()) && opModeIsActive()) {
            idle();
        }

        StopDriving();
    }

    private void Turn(int distance, double power) {
        robot.FrontRight.setTargetPosition(robot.FrontRight.getCurrentPosition() + distance);
        robot.FrontLeft.setTargetPosition(robot.FrontLeft.getCurrentPosition() + distance);
        robot.RearRight.setTargetPosition(robot.RearRight.getCurrentPosition() + distance);
        robot.RearLeft.setTargetPosition(robot.RearLeft.getCurrentPosition() + distance);

        DriveStraight(power);
        while ((robot.FrontRight.isBusy() && robot.RearLeft.isBusy() && robot.RearRight.isBusy() && robot.FrontLeft.isBusy()) && opModeIsActive()) {
            idle();
        }
        /*if(half) {
            checkOrientation();
            offset -= currentHeading;
            targetHeading = 0;
        }*/
        StopDriving();
        sleep(10);
    }

    /*public void checkOrientation() {
        // read the orientation of the robot
        angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        robot.imu.getPosition();
        // and save the heading
        currentHeading = angles.firstAngle;
    }*/

    public int Scan() {
        if(robot.distRight.getDistance(DistanceUnit.INCH) < 20) {
            return 1;
        }
        else if(robot.distRight.getDistance(DistanceUnit.INCH) > 30) {
            return 3;
        }
        else {
            return 2;
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

    public void duck (int seconds) {
        robot.duck.setPower(1);
        sleep(seconds*1000);
        robot.duck.setPower(0);
    }
}
