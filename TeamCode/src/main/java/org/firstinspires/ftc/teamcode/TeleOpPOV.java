package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@TeleOp(name = "TeleOpPOV", group = "Test")
public class TeleOpPOV extends OpMode {

    HWMap robot = new HWMap();
    boolean startCheck = true;
    int start = 0;
    float currentHeading = 0;

    Orientation angles;
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

    @Override
    public void init() {
        robot.init(hardwareMap);
        robot.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        robot.FrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.FrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.RearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.RearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        robot.imu.initialize(parameters);
        robot.imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    }

    @Override
    public void loop() {
        checkOrientation();
        telemetry.addLine("currentHeading: "+currentHeading);

        robot.FrontRight.setVelocity(2700 * ((-gamepad1.right_stick_y) * convert(currentHeading, 1)));
        robot.FrontLeft.setVelocity(2700 * ((gamepad1.right_stick_y) * convert(currentHeading, 2)));
        robot.RearRight.setVelocity(2700 * ((-gamepad1.right_stick_y) * convert(currentHeading, 3)));
        robot.RearLeft.setVelocity(2700 * ((gamepad1.right_stick_y) * convert(currentHeading, 4)));

        telemetry.update();

        /*if(start == 0) {
            robot.FrontRight.setVelocity(2700 * ((-gamepad1.right_stick_y - gamepad1.right_stick_x) * convert(currentHeading, 1)));
            robot.FrontLeft.setVelocity(2700 * ((gamepad1.right_stick_y + gamepad1.right_stick_x) * convert(currentHeading, 2)));
            robot.RearRight.setVelocity(2700 * ((-gamepad1.right_stick_y + gamepad1.right_stick_x) * convert(currentHeading, 3)));
            robot.RearLeft.setVelocity(2700 * ((gamepad1.right_stick_y - gamepad1.right_stick_x) * convert(currentHeading, 4)));
        }
        else {
            float frontright = 2700 * (-gamepad1.right_stick_y - gamepad1.right_stick_x);
            frontright = (frontright*frontright*Math.signum(frontright)/2700) * convert(currentHeading, 1);
            float frontleft = 2700 * (gamepad1.left_stick_y - gamepad1.left_stick_x);
            frontleft = (frontleft*frontleft*Math.signum(frontleft)/2700) * convert(currentHeading, 2);
            float rearright = 2700 * (-gamepad1.right_stick_y + gamepad1.right_stick_x);
            rearright = (rearright*rearright*Math.signum(rearright)/2700) * convert(currentHeading, 3);
            float rearleft = 2700 * (gamepad1.left_stick_y + gamepad1.left_stick_x);
            rearleft = (rearleft*rearleft*Math.signum(rearleft)/2700) * convert(currentHeading, 4);

            robot.FrontRight.setVelocity(frontright);
            robot.FrontLeft.setVelocity(frontleft);
            robot.RearRight.setVelocity(rearright);
            robot.RearLeft.setVelocity(rearleft);
        }

        if (gamepad1.start && startCheck && start == 0) {
            robot.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            start++;
            startCheck = false;
        } else if (gamepad1.start && startCheck && start == 1) {
            robot.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            start--;
            startCheck = false;
        } else if (!gamepad1.start) {
            startCheck = true;
        }*/
    }

    private float convert (float deg, int wheel) {
        //convert to correct power for wheel
        float powera = power(deg, wheel);
        float powerb = 0;
        if(deg == Math.abs(deg)) {
            powerb = power(deg + 45, wheel);
        }
        else {
            powerb = power(deg - 45, wheel);
        }
        telemetry.addLine("Wheel "+wheel+" powera: "+powera);
        telemetry.addLine("Wheel "+wheel+" powerb: "+powerb);
        telemetry.addLine("Wheel "+wheel+" Final power: "+(((1 - ((deg/90)%1))*powera) + (((deg/90)%1)*powerb)));
        //float degrees3 = (((1 - ((deg/45)%1))*powera) + (((deg/45)%1)*powerb))/2;
        return (((1 - ((deg/90)%1))*powera) + (((deg/90)%1)*powerb)); //turns power of top and bottom into the in between
    }

    private int power (float deg, int wheel){
        int degree = (int)(deg/90);
        if (wheel == 1 || wheel == 3) {   //frontRight and rearLeft
            if(degree <= -1 || degree == 2) {
                return -1;
            }
            else if (deg == Math.abs(deg) && (degree == 0 || deg == 1)){
                return 1;
            }
        }
        else if (wheel == 2 || wheel == 4) {   //frontLeft and rearRight
            if(degree >= 1 || degree == -2) {
                return -1;
            }
            else if ((deg != Math.abs(deg) || deg==0) && (degree == 0 || deg == -1)){
                return 1;
            }
        }
        //degrees/90 = 0 | -90 : 90
        //degrees/90 = 1 | 90 : 180
        //degrees/90 = -1 | -90 : -180

        // degrees/45 = 0 | -45 : 45
        // degrees/45 = 1 | 45 : 90
        // degrees/45 = 2 | 90 : 135
        // degrees/45 = 3 | 135 : 180
        // degrees/45 = -1 | -45 : -90
        // degrees/45 = -2 | -90 : -135
        // degrees/45 = -3 | -135 : -180
        return 0;
    }

    private void checkOrientation() {
        // read the orientation of the robot
        angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        robot.imu.getPosition();
        // and save the heading
        currentHeading = angles.firstAngle;
    }
}