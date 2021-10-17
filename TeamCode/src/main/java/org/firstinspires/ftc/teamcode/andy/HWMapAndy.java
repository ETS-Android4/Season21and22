package org.firstinspires.ftc.teamcode.andy;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

//This is NOT an OpMode

public class HWMapAndy {
    /* Public OpMode members. */
    public DcMotorEx FrontLeft = null;
    public DcMotorEx FrontRight  = null;
    public DcMotorEx RearLeft  = null;
    public DcMotorEx RearRight  = null;
    public DcMotorEx Scoop  = null;

    public BNO055IMU imu = null;
    /* local OpMode members. */
    HardwareMap hwMap           =  null;

    public HWMapAndy(){}

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        FrontLeft   = (DcMotorEx) hwMap.dcMotor.get("frontLeft");
        FrontRight  = (DcMotorEx) hwMap.dcMotor.get("frontRight");
        RearLeft = (DcMotorEx) hwMap.dcMotor.get("rearLeft");
        RearRight = (DcMotorEx) hwMap.dcMotor.get("rearRight");
        Scoop = (DcMotorEx) hwMap.dcMotor.get("scoop");

        imu = hwMap.get(BNO055IMU.class, "imu");
    }
}