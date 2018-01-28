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
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;

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
	
	Encoder encRight;
	Encoder encLeft;
	
	double axisX;
	double axisY;
	double axisZ;
	
	double distanceRight;
	double distanceLeft;
	double distance;
	
	int testInt = 0;
	
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

		encRight = new Encoder(0,1,false,Encoder.EncodingType.k4X); //0 and 1 are the digital port numbers, false tells the encoder not to invert the direction, and k4X im not so sure about but like just use it and if doesn't work change it to 1X or 2X...something about counting edges... 
		encRight.setMinRate(5);
		encRight.setReverseDirection(true); //might have to change
		encRight.setSamplesToAverage(7);
		encRight.setDistancePerPulse(18.849556/4096);
		encRight.reset();
		
		
		encLeft = new Encoder(2,3,true, Encoder.EncodingType.k4X); //you might have to change around the true and false because of how the encoders will be mounted
		encLeft.setMinRate(5); //rpm 
		encLeft.setReverseDirection(false); //might have to change
		encLeft.setSamplesToAverage(7);
		encLeft.setDistancePerPulse(18.849556/4096); //circumference of a 6" diameter wheel divided by a 200p/m encoder which has a 4x multiplier
		encLeft.reset();	

	}

	
	
	@Override
	public void autonomousPeriodic() {
		drivebase.arcadeDrive(0.3, 0);
		
		distanceLeft = encLeft.getDistance(); //get encoder distance
		distanceRight = encRight.getDistance();
		distance = (distanceLeft + distanceRight)/2; //average of what the two encoders have detected
		System.out.println(distance); //this will spam your dashboard, but itll allow you to see whether your encoders are working fine
			if(distance > 36) { //drive till encoders have been going for three feet
				drivebase.arcadeDrive(0, 0); //stop motors
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
		
		System.out.println("This is a test");
		
	}
			

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		
	}
}
