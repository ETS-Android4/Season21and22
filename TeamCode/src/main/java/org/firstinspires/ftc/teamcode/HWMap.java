package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

//This is NOT an OpMode

public class HWMap {
    /* Public OpMode members. */
    public DcMotorEx FrontLeft = null;
    public DcMotorEx FrontRight  = null;
    public DcMotorEx RearLeft  = null;
    public DcMotorEx RearRight  = null;
    public DcMotorEx Dump = null;
    public DcMotorEx Spin = null;
    public DcMotorEx Collect = null;
    //public DcMotorEx Rubber = null;

    public BNO055IMU imu = null;
    /* local OpMode members. */
    HardwareMap hwMap = null;

    public HWMap(){}

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        FrontLeft = (DcMotorEx) hwMap.dcMotor.get("frontLeft");
        FrontRight = (DcMotorEx) hwMap.dcMotor.get("frontRight");
        RearLeft = (DcMotorEx) hwMap.dcMotor.get("rearLeft");
        RearRight = (DcMotorEx) hwMap.dcMotor.get("rearRight");
        Dump = (DcMotorEx) hwMap.dcMotor.get("dump");
        Spin = (DcMotorEx)hwMap.dcMotor.get("spin");
        Collect = (DcMotorEx)hwMap.dcMotor.get("collect");
        //Rubber = (DcMotorEx)hwMap.dcMotor.get("rubber");

        imu = hwMap.get(BNO055IMU.class, "imu");
    }
}