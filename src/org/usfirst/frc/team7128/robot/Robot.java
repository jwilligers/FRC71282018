/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team7128.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private Joystick joystick = new Joystick(0);
	
	Spark leftSpark; //Left Drive Motor
	Spark rightSpark; //Right Drive Motor
	
	boolean robotIsOn = true;
	
	AnalogGyro gyro;
	double gyroAngle;
	
	double axisX;
	double axisY;
	double axisZ;
	
	double distanceRight;
	double distanceLeft;
	double distance;
	
	DifferentialDrive drivebase;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		leftSpark = new Spark(0);
		rightSpark = new Spark(1);
		drivebase = new DifferentialDrive(leftSpark, rightSpark);
		
		gyro.initGyro();
		gyro.setSensitivity(80); //sensitivity is 80 degrees/sec - from spec sheet
		gyro.calibrate();
		gyro.reset();
		CameraServer.getInstance().startAutomaticCapture();
		
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		
	}

	@Override
	public void autonomousPeriodic() {// wait till robot has turned 90 degrees then stop
		gyroAngle = gyro.getAngle(); //get robot angle and set it as gyroAngle
		System.out.println(gyroAngle);
		System.out.println("hi");
		
		if(gyroAngle < 90) { //if robot angle is smaller than 90 turn at 80%
			drivebase.arcadeDrive(0, 0.8);
		}
			else { //when larger than 90 stop
				drivebase.stopMotor();
				if(gyroAngle > 90) {//if robot heading is greater than 90
					drivebase.arcadeDrive(0, -0.4);//turn at 40% speed
				}
					else {
						drivebase.stopMotor(); //stop motor when smaller or equal to 90
					}
			}
			
		}
	
	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		axisX = joystick.getRawAxis(0);
		axisY = joystick.getRawAxis(1);
		axisZ = joystick.getRawAxis(2);
		
		drivebase.arcadeDrive(axisY, axisZ);
		leftSpark.stopMotor();
		
	}
			

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		
	}
}