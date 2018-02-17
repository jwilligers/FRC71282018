/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team7128.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;

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
	
	Jaguar leftWheelMotor = new Jaguar(3);
	Jaguar rightWheelMotor = new Jaguar(4);
	
	Jaguar intakeLeft = new Jaguar(5);
	Jaguar intakeRight = new Jaguar(6);
	
	boolean robotIsOn = true;
	
	double axisX;
	double axisY;
	double axisZ;
	double axisW;
	
	double targetVoltage;
	double currentVoltage;
	double speedVoltage;
	double oldPower=0;
	
	ADXRS450_Gyro gyro;
	PowerDistributionPanel pdp;
	
	AutoModes autonomous;
	
	Timer time;
	SendableChooser autoChooser;
	String gameData;
	
	double distanceRight;
	double distanceLeft;
	double distance;
	
	double desiredAngle;
	double power;
	
	DifferentialDrive drivebase;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public enum AutoModes {
		LEFT,MIDDLE,RIGHT
	}
	public AutoModes AutoMode(AutoModes autonomous) {
		this.autonomous = autonomous;
		return autonomous;
	}
		
	
	@Override
	public void robotInit() {
		
		autoChooser = new SendableChooser();
		autoChooser.addDefault("Left", AutoModes.LEFT);
		autoChooser.addObject("Right", AutoModes.RIGHT);
		autoChooser.addObject("Middle", AutoModes.MIDDLE);
		
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		leftSpark = new Spark(0);
		rightSpark = new Spark(1);
		drivebase = new DifferentialDrive(leftSpark, rightSpark);
		
		gyro = new ADXRS450_Gyro();
		gyro.calibrate();
		gyro.reset();
		
		pdp = new PowerDistributionPanel();
		targetVoltage = 5.5;
		
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
		
		time = new Timer();
		time.reset();
		gyro.reset();
		time.start();
	}

	
	
	@Override
	public void autonomousPeriodic() {
		double seconds = time.get();
		double gyroAngle = gyro.getAngle();
		//System.out.println(gyroAngle);
		currentVoltage = pdp.getVoltage();
		
		switch(autonomous) {
			case LEFT:
				if(gameData.length() > 0) {
					if(gameData.charAt(0) == 'L') {
						if (seconds < 5) {
							drivebase.arcadeDrive(10/currentVoltage, 0.8*(gyroAngle-0)); //change so it goes next to the switch (120")
						}
						else if(seconds < 5.5) {
							drivebase.arcadeDrive(0, 0.8*(gyroAngle - 90)); //turn 90 degrees
						}
						else if(seconds < 8) {
							drivebase.arcadeDrive(5/currentVoltage, 0.8*(gyroAngle-90)); //move towards the switch
						}
						else if(seconds < 9) {
							leftWheelMotor.set(1); //turn on lift
							leftWheelMotor.set(1);
						}
						else if(seconds < 10) { //dispense cube
							intakeLeft.set(-.5);
							intakeRight.set(.5);
						}
					}
					
					else {
						if (seconds < 5) {
							drivebase.arcadeDrive(10/currentVoltage, 0.8*(gyroAngle-0)); //change so it goes next to the switch (120")
						}
					}
				}
			break;
			case MIDDLE:
				if(gameData.length() > 0) {
					if(gameData.charAt(0) == 'L') {
						if (seconds < 1) {
							drivebase.arcadeDrive(0.0, 0.08*(gyroAngle - 305.5)); //turn to the left
						}
						else if(seconds < 5) {
							drivebase.arcadeDrive(10/currentVoltage, 0.08*(gyroAngle - 305.5));//drive forward 60"
						}
						else if(seconds < 6) {
							drivebase.arcadeDrive(0, 0.08*(gyroAngle - 0)); //straighten up with switch
						}
						else if(seconds < 8) {
							drivebase.arcadeDrive(5/currentVoltage, 0.08*(gyroAngle - 0));//slowly drive forward 49"
						}
						else if(seconds < 9) {
							rightWheelMotor.set(1);//turn on lift - might move this with the 'slowly drive forward part
							leftWheelMotor.set(1);
						}
						else if(seconds < 10) {
							intakeLeft.set(-5);//dispense cube(but not a cube because FIRST doesn't know how to define a cube)
							intakeRight.set(.5);
						}
							}
				}
				
				else if(gameData.length() > 0) {
					if(gameData.charAt(0) == 'R') {
						if (seconds < 1) {
							drivebase.arcadeDrive(0.0, 0.08*(gyroAngle - 54.46)); //turn to the right
						}
						else if(seconds < 5) {
							drivebase.arcadeDrive(10/currentVoltage, 0.08*(gyroAngle - 54.46));//drive forward
						}
						else if(seconds < 6) {
							drivebase.arcadeDrive(0, 0.08*(gyroAngle - 0));//straighten up
						}
						else if(seconds < 8) {
							drivebase.arcadeDrive(5/currentVoltage, 0.08*(gyroAngle - 0));//slowly drive foward
						}
						else if(seconds < 9) {
							rightWheelMotor.set(1);//turn on lift
							leftWheelMotor.set(1);
						}
						else if(seconds < 10) {
							intakeLeft.set(-5);//dispense cube
							intakeRight.set(.5);
						}
					}
				}
					
			break;
			case RIGHT:
				if(gameData.length() > 0) {
					if(gameData.charAt(0) == 'R') {
						if (seconds < 5) {
							drivebase.arcadeDrive(10/currentVoltage, 0.8*(gyroAngle-0)); //change so it goes next to the switch (120") from 0-5 secs
						}
						else if(seconds < 6) {
							drivebase.arcadeDrive(0, 0.8*(gyroAngle - 270)); //turn 90 degrees from 5-6 secs
						}
						else if(seconds < 8) {
							drivebase.arcadeDrive(5/currentVoltage, 0.8*(gyroAngle-90)); //move towards the switch from 6-8 secs
						}
						else if(seconds < 9) {
							leftWheelMotor.set(1); //turn on lift
							leftWheelMotor.set(1);
						}
						else if(seconds < 10) { //dispense cube from 8-9 secs
							intakeLeft.set(-.5);
							intakeRight.set(.5);
						}
					}
					else {
						if (seconds < 5) {
							drivebase.arcadeDrive(10/currentVoltage, 0.8*(gyroAngle-0)); //change so it goes next to the switch (120") from 0-5 secs
						}
					}
				}
			break;
		}
	}
	
	@Override
	public void teleopInit() {
		gyro.reset();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {

		axisX = joystick.getRawAxis(0);
		axisY = joystick.getRawAxis(1);
		axisZ = joystick.getRawAxis(2);
		axisW = joystick.getThrottle();
		
		/*double gyroAngle = gyro.getAngle();
		//double throttle = (-axisW+1)/2;
		if(Math.abs(axisX) >= JOYSTICK_DEAD_ZONE || Math.abs(axisY) >= JOYSTICK_DEAD_ZONE) {
			desiredAngle = Math.toDegrees(Math.abs(Math.atan(axisX/axisY)));
			power = axisY * Math.sqrt(Math.pow(axisX, 2) + Math.pow(axisY, 2))/Math.sqrt(2);
		} else {
			power = 0;
        	desiredAngle = 0;
		}

		if(axisX < -JOYSTICK_DEAD_ZONE && axisY > JOYSTICK_DEAD_ZONE) { //check if joystick is in quad 2
			desiredAngle += 270;
		}
		else if(axisX <= -JOYSTICK_DEAD_ZONE && axisY <= -JOYSTICK_DEAD_ZONE) { //check if joystick is in quad 3
			desiredAngle += 180;
        }
		else if(axisX > JOYSTICK_DEAD_ZONE && axisY < -JOYSTICK_DEAD_ZONE) { //check if joystick is in quad 4
			desiredAngle += 90;
        }

		//double currentPower = (power-oldPower)*0.2 + oldPower;
		//oldPower = currentPower;
		//double currentPower = power;
		*/
		drivebase.arcadeDrive(axisY, axisZ);
		
		
	
		if(joystick.getRawButton(11)) {
			leftWheelMotor.set(axisW);
			rightWheelMotor.set(-axisW);	
		} else if (joystick.getRawButton(12)) {
			leftWheelMotor.set(-axisW);
			rightWheelMotor.set(axisW);
		} else {
			leftWheelMotor.set(0);
			rightWheelMotor.set(0);      
		}
	}
}
