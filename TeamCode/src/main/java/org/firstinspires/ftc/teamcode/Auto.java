package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@Autonomous(name = "Auto", group = "Final")
public class Auto extends LinearOpMode {
    Functionsa fun = new Functionsa();

    @Override
    public void runOpMode() throws InterruptedException {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        fun.init(hardwareMap);
        fun.imu.initialize(parameters);
        fun.runToPosition(fun.FrontRight);
        fun.runToPosition(fun.FrontLeft);
        fun.runToPosition(fun.RearRight);
        fun.runToPosition(fun.RearLeft);
        fun.runToPosition(fun.LinearSlide);
        fun.runToPosition(fun.Dump);
        fun.changeZero();

        waitForStart();
        fun.imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

        //DISTANCES
        //38" from edge to shipping hub
        //17" robot length
        //48" to duck
        fun.DriveStraightDistance(17, 0.8);
        sleep(100);
        int i = fun.Scan();
        switch (i) {
            case 1:
                fun.linearSlidePos(0);
                sleep(100);
                fun.DriveStraightDistance(-1, 0.8);
                break;
            case 2:
                fun.linearSlidePos(-500);
                break;
            case 3:
                fun.linearSlidePos(-1000);
                break;
        }
        sleep(500);
        fun.dumpPos(-400);
        sleep(1000);
        fun.dumpPos(0);
        fun.linearSlidePos(0);
        fun.DriveStraightDistance(-20, 1);
        sleep(500);
        fun.DriveStraightDistance(4, 0.6);
        sleep(500);
        fun.Strafe(48, 0.8);
        sleep(500);
        duck(5);
        fun.Strafe(-2, 0.8);
        sleep(500);
        fun.Turn(-1000, 0.6, -90);
        sleep(500);
        fun.Strafe(-6, 1);
        sleep(500);
        fun.DriveStraightDistance(80, 1);
        fun.Strafe(-6, 1);
        fun.DriveStraightDistance(40, 1);
    }

    public void duck (int seconds) {
        fun.duck.setPower(-1);
        sleep(seconds*1000);
        fun.duck.setPower(0);
    }
}
