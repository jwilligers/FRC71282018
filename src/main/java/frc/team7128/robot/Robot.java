/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.team7128.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;

public class Robot extends IterativeRobot {
    private UsbCamera cam;
    private Joystick joystick = new Joystick(1);
    private XboxController Controller = new XboxController(2);

    // Subsystems
    private Drivetrain drivetrain;
    private Intake intake;
    private Lift lift;

    // Other
    private Timer timer;
    //private String gameData;

	@Override
	public void robotInit() {
        drivetrain = new Drivetrain();
        intake = new Intake();
        lift = new Lift();

        timer = new Timer();

		cam = CameraServer.getInstance().startAutomaticCapture();
		cam.setResolution(320, 240);
		//CameraServer.getInstance().startAutomaticCapture();
	}

	@Override
	public void autonomousInit() {
        //gameData = DriverStation.getInstance().getGameSpecificMessage();
        drivetrain.resetGyro();
        timer.reset();
        timer.start();
    }

	@Override
	public void autonomousPeriodic() {
        double seconds = timer.get();
        if (seconds < 3.2) {
            drivetrain.arcadeDrive(-0.65, 0);
        }
        else {
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
        updateSmartDashboard();
	}
    private void updateSmartDashboard()
    {
        drivetrain.updateSmartDashboard();
        intake.updateSmartDashboard();
        lift.updateSmartDashboard();
    }
}