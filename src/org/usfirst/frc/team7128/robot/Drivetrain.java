package org.usfirst.frc.team7128.robot;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

/**
 * Created by Joshua on 16/04/2018.
 */
public class Drivetrain implements Subsystem{
    Spark leftSpark; //Left Drive Motor
    Spark rightSpark; //Right Drive Motor

    ADXRS450_Gyro gyro;

    DifferentialDrive drivebase;


    public void Drivetrain()
    {
        leftSpark = new Spark(0);
        rightSpark = new Spark(1);
        drivebase = new DifferentialDrive(leftSpark, rightSpark);

        gyro = new ADXRS450_Gyro();
        gyro.calibrate();
        resetGyro();
    }

    public void resetGyro()
    {
        gyro.reset();
    }
    public double getGyroAngle()
    {
        return gyro.getAngle();
    }
    public void arcadeDrive(double y, double rotation)
    {
        drivebase.arcadeDrive(y, rotation);
    }

    public void updateSmartDashboard()
    {
        SmartDashboard.putNumber("Left Motor Output", leftSpark.get());
        SmartDashboard.putNumber("Right Motor Output", rightSpark.get());
        SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
    }
}
