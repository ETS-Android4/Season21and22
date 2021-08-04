package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

//This is NOT an OpMode

public class HWMap {
    /* Public OpMode members. */
    public DcMotorEx FrontLeft = null;
    public DcMotorEx FrontRight  = null;
    public DcMotorEx RearLeft  = null;
    public DcMotorEx RearRight  = null;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;

    public HWMap(){}

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        FrontLeft   = (DcMotorEx) hwMap.dcMotor.get("frontLeft");
        FrontRight  = (DcMotorEx) hwMap.dcMotor.get("frontRight");
        RearLeft = (DcMotorEx) hwMap.dcMotor.get("rearLeft");
        RearRight = (DcMotorEx) hwMap.dcMotor.get("rearRight");
    }
}