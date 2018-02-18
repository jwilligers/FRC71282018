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
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

//import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
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
	
	Jaguar liftMotor1 = new Jaguar(2);
	Jaguar liftMotor2 = new Jaguar(3);
	
	
	Victor intakeMotorR = new Victor(5);
	Victor intakeMotorL = new Victor(4);
	DigitalInput limitSwitchA; 
	DigitalInput limitSwitchB; 
	DigitalInput limitSwitchC;
	
	
	boolean robotIsOn1 = true;
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
		
		/*autoChooser = new SendableChooser();
		autoChooser.addDefault("Left", AutoModes.LEFT);+
		autoChooser.addObject("Right", AutoModes.RIGHT);
		autoChooser.addObject("Middle", AutoModes.MIDDLE);
		*/
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		leftSpark = new Spark(0);
		rightSpark = new Spark(1);
		drivebase = new DifferentialDrive(leftSpark, rightSpark);
		
		gyro = new ADXRS450_Gyro();
		gyro.calibrate();
		gyro.reset();
		
		pdp = new PowerDistributionPanel();
		targetVoltage = 5.5;
		
		limitSwitchA = new DigitalInput(2);
		limitSwitchB = new DigitalInput(1);
		limitSwitchC = new DigitalInput(0);

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
		System.out.println(gyroAngle);
		currentVoltage = pdp.getVoltage();
		
		switch(autonomous) {
			case LEFT:
				if(gameData.length() > 0) {
					if(gameData.charAt(0) == 'L') {
						if (seconds < 1) {
							drivebase.arcadeDrive(10/currentVoltage, 0.08*(gyroAngle - 0)); //drive forward
						}
						else if(seconds < 1.5) {
							drivebase.arcadeDrive(-currentVoltage/currentVoltage, 0.08*(gyroAngle - 0));//drive backwards
						}
						else if(seconds < 3) {
							drivebase.arcadeDrive(6/currentVoltage, 0.08*(gyroAngle - 0)); //pick up cube
							intakeMotorL.set(.5);
							intakeMotorR.set(-.5);
						}
						else if(seconds < 9) {
							liftMotor.set(1); //turn on lift
						}
						if (seconds < 8) {
							drivebase.arcadeDrive(0, 0.08*(gyroAngle - 90));
							liftMotor.set(.5);
						}
						if (seconds < 8.5) {
							drivebase.arcadeDrive(5/currentVoltage, 0.08*(gyroAngle - 90));
							intakeMotorL.set(-.5);
							intakeMotorR.set(.5);
						}
						if (seconds > 9.5) {
							drivebase.arcadeDrive(0, 0);
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
							drivebase.arcadeDrive(10/currentVoltage, 0.08*(gyroAngle - 0)); //drive forward
						}
						else if(seconds < 1.5) {
							drivebase.arcadeDrive(-currentVoltage/currentVoltage, 0.08*(gyroAngle - 0));//drive backwards
						}
						else if(seconds < 3) {
							drivebase.arcadeDrive(6/currentVoltage, 0.08*(gyroAngle - 0)); //pick up cube
							intakeMotorL.set(.5);
							intakeMotorR.set(-.5);
						}
						else if(seconds < 4) {
							drivebase.arcadeDrive(5/currentVoltage, 0.08*(gyroAngle - 270));//turn 90 degrees
						}
						else if(seconds < 6) {
							liftMotor.set(.5);//drive forward and turn on lift
							drivebase.arcadeDrive(9.5/currentVoltage, 0.08*(gyroAngle - 270));
						}
						else if(seconds < 7.5) {
							drivebase.arcadeDrive(0, 0.8*(gyroAngle - 0)); //straighten up with switch
						}
						else if(seconds < 8) {
							drivebase.arcadeDrive(8/currentVoltage, 0.08*(gyroAngle - 0)); //drive into the switch
						}
						else if(seconds < 9) {
							liftMotor.set(1);//turn on lift - might move this with the 'slowly drive forward part
						}
						else if(seconds < 10) {
							intakeMotorL.set(-.5);//dispense cube(but not a cube because FIRST doesn't know how to define a cube)
							intakeMotorR.set(.5);
						}
					}
				}
				
				else if(gameData.length() > 0) {
					if(gameData.charAt(0) == 'R') {
						if (seconds < 1) {
							drivebase.arcadeDrive(10/currentVoltage, 0.08*(gyroAngle - 0)); //drive forward
						}
						else if(seconds < 1.5) {
							drivebase.arcadeDrive(-currentVoltage/currentVoltage, 0.08*(gyroAngle - 0));//drive backwards
						}
						else if(seconds < 3) {
							drivebase.arcadeDrive(6/currentVoltage, 0.08*(gyroAngle - 0)); //pick up cube
							intakeMotorL.set(.5);
							intakeMotorR.set(-.5);
						}
						else if(seconds < 4) {
							drivebase.arcadeDrive(5/currentVoltage, 0.08*(gyroAngle - 90));//turn 90 degrees
						}
						else if(seconds < 6) {
							liftMotor.set(.5);//drive forward and turn on lift
							drivebase.arcadeDrive(9.5/currentVoltage, 0.08*(gyroAngle - 90));
						}
						else if(seconds < 7.5) {
							drivebase.arcadeDrive(0, 0.8*(gyroAngle - 0)); //straighten up with switch
						}
						else if(seconds < 8) {
							drivebase.arcadeDrive(8/currentVoltage, 0.08*(gyroAngle - 90)); //drive into the switch
						}
						else if(seconds < 9) {
							liftMotor.set(1);//turn on lift
						}
						else if(seconds < 10) {
							intakeMotorL.set(-5);//dispense cube
							intakeMotorR.set(.5);
						}
					}
					}
				
					
			break;
			case RIGHT:
				if(gameData.length() > 0) {
					if(gameData.charAt(0) == 'R') {
						if (seconds < 1) {
							drivebase.arcadeDrive(10/currentVoltage, 0.08*(gyroAngle - 0)); //drive forward
						}
						else if(seconds < 6) {
							drivebase.arcadeDrive(0, 0.8*(gyroAngle - 270)); //turn 90 degrees from 5-6 secs
						}
						else if(seconds < 3) {
							drivebase.arcadeDrive(6/currentVoltage, 0.08*(gyroAngle - 0)); //pick up cube
							intakeMotorL.set(.5);
							intakeMotorR.set(-.5);
						}
						else if(seconds < 9) {
							liftMotor.set(1); //turn on lift
						}
						if (seconds < 8) {
							drivebase.arcadeDrive(0, 0.08*(gyroAngle - 270));
							liftMotor.set(.5);
						}
						if (seconds < 8.5) {
							drivebase.arcadeDrive(5/currentVoltage, 0.08*(gyroAngle - 0));
							intakeMotorL.set(-.5);
							intakeMotorR.set(.5);
						}
						if (seconds > 9.5) {
							drivebase.arcadeDrive(0, 0);
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
		
		    
		drivebase.arcadeDrive(joystick.getY(), joystick.getX());
		
		if(joystick.getRawButton(5)) { // move carriage up
			liftMotor1.set(1);
			liftMotor2.set(1);
		} else if(joystick.getRawButton(3) && !limitSwitchB.get() ) {  // move carriage down
			liftMotor1.set(-.3);
			liftMotor2.set(-.3);
		} else {
			liftMotor1.set(0);
			liftMotor2.set(0);
		}
	    
		
		if(joystick.getRawButton(1 )&& !limitSwitchC.get() ) {  // may not be intake but may be the out take, therefore you may need to switch the two around.
			
			intakeMotorR.set(intakeSpeed); // intake
			intakeMotorL.set(intakeSpeed *-1);
		
		} else if(joystick.getRawButton(2)) { // outake
			
			intakeMotorR.set(intakeSpeed*-1);
			intakeMotorL.set(intakeSpeed);
		
		} else {
			
			intakeMotorR.set(0);
			intakeMotorL.set(0);
		
		}
	if ( !limitSwitchB.get() && joystick.getY () > 0 ) {
		drivebase.arcadeDrive(joystick.getY()*0.6, joystick.getX());
	}
	else {
		drivebase.arcadeDrive(joystick.getY(), joystick.getX());
	
	}
	/*if ( joystick.getRawButton(11)) {
		leftSpark.setDisabled();
	}	else if (joystick.getRawButton(12)) {
		rightSpark.setDisabled();;
	}
	*/

	
	}

		}
	
		

