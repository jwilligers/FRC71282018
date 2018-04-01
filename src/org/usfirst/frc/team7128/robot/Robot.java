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
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.UsbCamera;

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
	UsbCamera cam;
	private Joystick joystick = new Joystick(1);
	private XboxController Controller = new XboxController(2);
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
		LEFT,MIDDLE,RIGHT,SIMPLE
	}
	public AutoModes AutoMode(AutoModes autonomous) {
		this.autonomous = autonomous;
		return autonomous;
	}
		
	
	@Override
	public void robotInit() {
		
		/*gameData = DriverStation.getInstance().getGameSpecificMessage();
		autoChooser = new SendableChooser();
        
        autoChooser.addDefault("Left", AutoModes.LEFT);
        autoChooser.addObject("Right", AutoModes.RIGHT);
        autoChooser.addObject("Middle", AutoModes.MIDDLE);
        
        SmartDashboard.putData(autoChooser); */
        
		leftSpark = new Spark(0);
		rightSpark = new Spark(1);
		drivebase = new DifferentialDrive(leftSpark, rightSpark);
		
		gyro = new ADXRS450_Gyro();
		gyro.calibrate();
		gyro.reset();
		
		pdp = new PowerDistributionPanel();

		limitSwitchA = new DigitalInput(2);
		limitSwitchB = new DigitalInput(1);
		limitSwitchC = new DigitalInput(0);
		cam = CameraServer.getInstance().startAutomaticCapture();
		cam.setResolution(320, 240);
		//CameraServer.getInstance().startAutomaticCapture();
		
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
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        
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
	        //currentVoltage = pdp.getVoltage();
	        if (seconds < 3.2)
	        {
	           drivebase.arcadeDrive(-0.65, 0);
	        }
	        else
	        {
	           drivebase.arcadeDrive(0, 0.0);
	        }
	        
	        /*        
	        switch(autonomous) {
	
	        //left code
	            case LEFT:
	                if(gameData.length() > 0) {
	                    if(gameData.charAt(0) == 'L') {
	                        if (seconds < 2) {
	                            drivebase.arcadeDrive(-6/currentVoltage, 0.04*(gyroAngle - 0)); //drive forward
	                        }
	                        else if(seconds < 2.2) {
	                            drivebase.arcadeDrive(0, 0);
	                        }
	                        else if(seconds < 2.5) {
	                            drivebase.arcadeDrive(8/currentVoltage, 0.04*(gyroAngle - 0));//drive backwards
	                        }
	                        else if(seconds < 4) {
	                            drivebase.arcadeDrive(-7/currentVoltage, 0.04*(gyroAngle - 0)); //pick up cube
	                            intakeMotorL.set(-.5);
	                            intakeMotorR.set(.5);
	                        }
	                        else if (seconds < 6) {
	                            drivebase.arcadeDrive(10/currentVoltage, 0.04*(gyroAngle-0)); //drive next to switch
	                            
	                        }
	                        else if (seconds < 8) { //turn towards switch
	                            drivebase.arcadeDrive(0, -0.04*(gyroAngle + 90));
	                        }
	                        else if(seconds <9) {
	                            liftMotor1.set(1);
	                            liftMotor1.set(2);
	                        }
	                        else if (seconds < 9.5 ) {
	                            drivebase.arcadeDrive(6/currentVoltage, -0.08*(gyroAngle + 90));
	                            liftMotor1.set(0);
	                            liftMotor1.set(0);
	                                                    }
	                        else if (seconds < 10) {
	                            intakeMotorL.set(-.5);
	                            intakeMotorR.set(.5);
	                            drivebase.arcadeDrive(0, 0);
	                        }
	                        else if (seconds < 10.5) {
	                            liftMotor1.set(0);
	                            liftMotor2.set(0);
	                            
	                        }
	                    }
	                    else {
	                        if (seconds < 5) {
	                            drivebase.arcadeDrive(10/currentVoltage, 0.8*(gyroAngle-0)); //change so it goes next to the switch (120")
	                        }
	                    }
	                }
	            break;
	            //middle code
	            case MIDDLE:
	                if(gameData.length() > 0) {
	            if(gameData.charAt(0) == 'L') {
	                if (seconds < 2) {
	                    drivebase.arcadeDrive(-6/currentVoltage, 0.03*(gyroAngle - 0)); //drive forward
	                }
	                else if(seconds < 2.2) {
	                    drivebase.arcadeDrive(0, 0);
	                }
	                else if(seconds < 2.5) {
	                    drivebase.arcadeDrive(8/currentVoltage, 0.06*(gyroAngle - 0));//drive backwards
	                }
	                else if(seconds < 4) {
	                    drivebase.arcadeDrive(-7/currentVoltage, 0.03*(gyroAngle - 0)); //pick up cube
	                    intakeMotorL.set(-.5);
	                    intakeMotorR.set(.5);
	                }
	                else if(seconds < 5) {
	                    intakeMotorL.set(-.2);
	                    intakeMotorR.set(.2);
	                    drivebase.arcadeDrive(0, -0.03*(gyroAngle + 90));//turn 90 degrees
	                    
	                }
	                else if(seconds < 7) {
	                    drivebase.arcadeDrive(-6/currentVoltage, -0.04*(gyroAngle + 90));
	                }
	                else if(seconds < 9) {
	                    liftMotor1.set(1);//drive forward and turn on lift
	                    liftMotor2.set(1);//drive forward and turn on lift
	                }
	                else if(seconds < 9.5) {
	                    liftMotor1.set(0);
	                    liftMotor2.set(0);
	                    drivebase.arcadeDrive(0, -0.02*(gyroAngle - 0)/1.9); //straighten up with switch
	                }
	                else if(seconds < 13) {
	                    drivebase.arcadeDrive(-6/currentVoltage, -0.02*(gyroAngle - 0)); //drive into the switch
	                }
	                else if(seconds < 14 ) { 
	                    drivebase.arcadeDrive(0, 0);
	                    intakeMotorL.set(.5);//dispense cube(but not a cube because FIRST doesn't know how to define a cube)
	                    intakeMotorR.set(-.5);
	                }
	            }
	        }
	        
	        else if(gameData.length() > 0) {
	            if(gameData.charAt(0) == 'R') {
	                if (seconds < 1) {
	                    drivebase.arcadeDrive(-10/currentVoltage, 0.08*(gyroAngle - 0)); //drive forward
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
	                    drivebase.arcadeDrive(5/currentVoltage, 0.04*(gyroAngle - 90));//turn 90 degrees
	                }
	                else if(seconds < 6) {
	                    liftMotor1.set(1);//drive forward and turn on lift
	                    liftMotor2.set(1);
	                    drivebase.arcadeDrive(9.5/currentVoltage, 0.04*(gyroAngle - 90));
	                }
	                else if(seconds < 7.5) {
	                    drivebase.arcadeDrive(0, 0.8*(gyroAngle - 0)); //straighten up with switch
	                    liftMotor1.set(0);
	                    liftMotor2.set(0);
	                }
	                else if(seconds < 8) {
	                    drivebase.arcadeDrive(8/currentVoltage, 0.04*(gyroAngle - 90)); //drive into the switch
	                }
	                else if(seconds < 10 ) { 
	                    drivebase.arcadeDrive(0, 0);
	                    intakeMotorL.set(-.5);//dispense cube(but not a cube because FIRST doesn't know how to define a cube)
	                    intakeMotorR.set(.5);
	                }
	            }
	            }
	            break;
	            
	            
	            //right code
	            case RIGHT:
	                if(gameData.length() > 0) {
	                    if(gameData.charAt(0) == 'R') {
	                        if (seconds < 2) {
	                            drivebase.arcadeDrive(-6/currentVoltage, 0.04*(gyroAngle - 0)); //drive forward
	                        }
	                        else if(seconds < 2.2) {
	                            drivebase.arcadeDrive(0, 0);
	                        }
	                        else if(seconds < 2.5) {
	                            drivebase.arcadeDrive(8/currentVoltage, 0.04*(gyroAngle - 0));//drive backwards
	                        }
	                        else if(seconds < 4) {
	                            drivebase.arcadeDrive(-7/currentVoltage, 0.04*(gyroAngle - 0)); //pick up cube
	                            intakeMotorL.set(-.5);
	                            intakeMotorR.set(.5);
	                        }
	                        else if (seconds < 6) {
	                            drivebase.arcadeDrive(10/currentVoltage, 0.04*(gyroAngle-0)); //drive next to switch
	                            
	                        }
	                        else if (seconds < 8) { //turn towards switch
	                            drivebase.arcadeDrive(0, 0.04*(gyroAngle - 90));
	                        }
	                        else if(seconds <9) {
	                            liftMotor1.set(1);
	                            liftMotor1.set(2);
	                        }
	                        else if (seconds < 9.5 ) {
	                            drivebase.arcadeDrive(6/currentVoltage, 0.04*(gyroAngle - 90));
	                            liftMotor1.set(0);
	                            liftMotor1.set(0);
	                                                    }
	                        else if (seconds < 10) {
	                            intakeMotorL.set(-.5);
	                            intakeMotorR.set(.5);
	                            drivebase.arcadeDrive(0, 0);
	                        }
	                        else if (seconds < 10.5) {
	                            liftMotor1.set(0);
	                            liftMotor2.set(0);
	                            
	                        }
	                    }
	                    else {
	                        if (seconds < 5) {
	                            drivebase.arcadeDrive(10/currentVoltage, 0.4*(gyroAngle-0)); //change so it goes next to the switch (120")
	                        }
	                    }
	                }
	            break; 
	        }
	        */
	    }
	@Override
	public void teleopInit() {
		gyro.reset();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	boolean limitReached = false;
	@Override
	public void teleopPeriodic() {
		
		double intakeSpeed = (0.8);
		System.out.println(limitSwitchB.get());
		System.out.println("intake speed = " + joystick.getY());
		drivebase.arcadeDrive((joystick.getY() * (-joystick.getThrottle() + 1.0) / 2.0), joystick.getX() * (-joystick.getThrottle() + 1.0) / 2.0);

		if(limitSwitchA.get() == true)
		{
			limitReached = true;
			System.out.println("limitReached");
		}
		if(Controller.getXButton() && !limitReached) { // move carriage up---- this might not work
		
			
			liftMotor1.set(-1);
			liftMotor2.set(-1);}
			else if(Controller.getBButton()&& !limitSwitchB.get()) { // move carriage down---- this might not work
				
				limitReached = false;
				liftMotor1.set(.3);
				liftMotor2.set(.3);
		} else {
			liftMotor1.set(0);
			liftMotor2.set(0);
		}
	    
		
		if(Controller.getAButton()  ) {  
			
			intakeMotorR.set(intakeSpeed); // intake
			intakeMotorL.set(-intakeSpeed);
		
		} else if(Controller.getYButton()) { // outake
			
			intakeMotorR.set(-intakeSpeed);
			intakeMotorL.set(intakeSpeed);
		
		} else {
			
			intakeMotorR.set(0);
			intakeMotorL.set(0);
		
		}
	//if ( !limitSwitchB.get() && joystick.getY () > 0 ) {
	//	drivebase.arcadeDrive(joystick.getY()*0.85, joystick.getX()*0.85);
	//}
	//else {
	//	drivebase.arcadeDrive(joystick.getY(), joystick.getX());
	
	
	
	}
}


	
		

