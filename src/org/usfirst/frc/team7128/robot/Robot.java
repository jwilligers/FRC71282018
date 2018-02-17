/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team7128.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private DifferentialDrive m_robotDrive
			= new DifferentialDrive(new Spark(0), new Spark(1));
	private Joystick m_stick = new Joystick(0);
	private Timer m_timer = new Timer();
	DigitalInput limitSwitchA; 
	DigitalInput limitSwitchB; 

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		limitSwitchA = new DigitalInput(0);
		limitSwitchB = new DigitalInput(1);
	}
	
	Spark leftSpark; //Left Drive Motor
	Spark rightSpark; //Right Drive Motor
	
	Jaguar leftWheelMotor = new Jaguar(3);
	Jaguar rightWheelMotor = new Jaguar(3);
	TalonSRX intakeMotorR = new TalonSRX(5);
	TalonSRX intakeMotorL = new TalonSRX(6);
	boolean robotIsOn = true;
	

	/**
	 * This function is run once each time the robot enters autonomous mode.
	 */
	@Override
	public void autonomousInit() {
		m_timer.reset();
		m_timer.start();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		// Drive for 2 seconds
		if (m_timer.get() < 2.0) {
			m_robotDrive.arcadeDrive(0.5, 0.0); // drive forwards half speed
		} else {
			m_robotDrive.stopMotor(); // stop robot
		}
	}

	/**
	 * This function is called once each time the robot enters teleoperated mode.
	 */
	@Override
	public void teleopInit() {
	}
	/**
	 * This function is called periodically during teleoperated mode.
	 */
	@Override
	public void teleopPeriodic() {
		
		m_robotDrive.arcadeDrive(m_stick.getY(), m_stick.getX());
		
		if(m_stick .getRawButton(5) && !limitSwitchA.get()) { // move carriage up
			rightWheelMotor.set(0.5);
			leftWheelMotor.set(0.5);
		}
		else if (m_stick .getRawButton(3) /*&& !limitSwitchB.get()*/) {  // move carriage down
			rightWheelMotor.set(-0.5);
			leftWheelMotor.set(-0.5);
		} else {
			rightWheelMotor.set(0);
			leftWheelMotor.set(0);
		}
		System.out.println(limitSwitchA.get());
		
	     
		if(m_stick.getRawButton(6)) {
			intakeMotorR.set(ControlMode.PercentOutput, 0.5);
			intakeMotorL.set(ControlMode.PercentOutput, -0.5);
		}
		else if(m_stick.getRawButton(7)) {
			intakeMotorR.set(ControlMode.PercentOutput, -0.5);
			intakeMotorL.set(ControlMode.PercentOutput, 0.5);
		}
		else {
			intakeMotorR.set(ControlMode.PercentOutput, 0);
			intakeMotorL.set(ControlMode.PercentOutput, 0);
		}
	
	}
	
	

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {

	}
}
