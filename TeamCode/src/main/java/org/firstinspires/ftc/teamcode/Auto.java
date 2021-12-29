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
        fun.changeZero();

        waitForStart();
        fun.imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }
        });

        fun.DriveStraightDistance(538, 0.5);
        sleep(5000);
        fun.DriveStraightDistance((int)(537.7/(3.1415926535*3.75)), 0.5);

        //DISTANCES
        //38" from edge to shipping hub
        //17" robot length
        //48" to duck
        /*fun.DriveStraightDistance(21, 1);
        int i = fun.scan();
        switch (i) {
            case 1:
                fun.linearSlidePos(0);
                break;
            case 2:
                fun.linearSlidePos(-500);
                break;
            case 3:
                fun.linearSlidePos(-1000);
                break;
        }
        fun.dumpPos(-300);
        sleep(500);
        fun.dumpPos(0);
        fun.linearSlidePos(0);
        fun.DriveStraightDistance(-22, 1);
        fun.DriveStraightDistance(2, 0.6);
        fun.Strafe(-48, 0.8);
        fun.duck(3);
        fun.Strafe(2, 0.8);
        fun.Turn(90, 0.6);
        fun.Strafe(3, 1);
        fun.DriveStraightDistance(144, 1);*/
    }
}
