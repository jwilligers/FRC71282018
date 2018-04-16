/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team7128.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class Robot extends IterativeRobot {
	UsbCamera cam;
	private Joystick joystick = new Joystick(1);
	private XboxController Controller = new XboxController(2);

    // Subsystems
    Drivetrain drivetrain;
    Intake intake;
    Lift lift;

    // Sensors
	DigitalInput limitSwitchC;

	PowerDistributionPanel pdp;
	
	AutoModes autonomous;
	
	Timer timer;
	SendableChooser autoChooser;
	String gameData;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public enum AutoModes {
		LEFT,MIDDLE,RIGHT,SIMPLE
	}
	public AutoModes AutoMode(AutoModes autonomous) {
		this.autonomous = autonomous;
		return autonomous;
	}

	@Override
	public void robotInit() {
		
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		autoChooser = new SendableChooser();
        
        autoChooser.addDefault("Left", AutoModes.LEFT);
        autoChooser.addObject("Right", AutoModes.RIGHT);
        autoChooser.addObject("Middle", AutoModes.MIDDLE);
        
        //SmartDashboard.putData(autoChooser);

        boolean limitReached = false;

        drivetrain = new Drivetrain();
        intake = new Intake();
        lift = new Lift();

        timer = new Timer();

		pdp = new PowerDistributionPanel();

		limitSwitchC = new DigitalInput(0);
		cam = CameraServer.getInstance().startAutomaticCapture();
		cam.setResolution(320, 240);
		//CameraServer.getInstance().startAutomaticCapture();
	}

	@Override
	public void autonomousInit() {
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        drivetrain.resetGyro();
        timer.reset();
        timer.start();
    }

	@Override
	public void autonomousPeriodic() {
        double seconds = timer.get();
        double gyroAngle = drivetrain.getGyroAngle();
        if (seconds < 3.2)
        {
            drivetrain.arcadeDrive(-0.65, 0);
        }
        else
        {
            drivetrain.arcadeDrive(0, 0.0);
        }
        updateSmartDashboard();
    }
	@Override
	public void teleopInit() {
		drivetrain.resetGyro();
	}

	@Override
	public void teleopPeriodic() {

		drivetrain.arcadeDrive((joystick.getY() * (-joystick.getThrottle() + 1.0) / 2.0), joystick.getX() * (-joystick.getThrottle() + 1.0) / 2.0);

		if (Controller.getXButton()) { // move carriage up---- this might not work
            lift.raise();
        }
        else if (Controller.getBButton()) { // move carriage down---- this might not work
            lift.lower();
		}
        else  {
		    lift.stop();
		}
		if (Controller.getAButton()) {
			intake.intake();
		}
        else if (Controller.getYButton()) { // Eject
            intake.eject();
		}
        else {
            intake.stop();
		}
	//if ( !liftLowerLimit.get() && joystick.getY () > 0 ) {
	//	drivebase.arcadeDrive(joystick.getY()*0.85, joystick.getX()*0.85);
	//}
	//else {
	//	drivebase.arcadeDrive(joystick.getY(), joystick.getX());

        updateSmartDashboard();
	}
    public void updateSmartDashboard()
    {
        drivetrain.updateSmartDashboard();
        intake.updateSmartDashboard();
        lift.updateSmartDashboard();
        //SmartDashboard.putNumber("Intake Speed", joystick.getY());
    }
}