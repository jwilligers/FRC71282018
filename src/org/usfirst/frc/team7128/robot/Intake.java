package org.usfirst.frc.team7128.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Created by Joshua on 16/04/2018.
 */

public class Intake implements Subsystem{

    Victor intakeMotorL;
    Victor intakeMotorR;

    public void Intake()
    {
        intakeMotorR = new Victor(5);
        intakeMotorL = new Victor(4);
    }

    public void setSpeed(double speed)
    {
        intakeMotorL.set(speed);
        intakeMotorR.set(-speed);
    }
    public void intake()
    {
        setSpeed(Constants.intakeSpeed);
    }
    public void eject()
    {
        setSpeed(Constants.ejectSpeed);
    }
    public void stop()
    {
        setSpeed(0);
    }
    public void updateSmartDashboard()
    {
        SmartDashboard.putNumber("Intake Motor Output", intakeMotorL.get());
    }
}
