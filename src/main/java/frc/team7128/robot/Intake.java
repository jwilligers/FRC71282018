package frc.team7128.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake implements Subsystem{

    private Victor intakeMotorL;
    private Victor intakeMotorR;

    public Intake()
    {
        intakeMotorL = new Victor(Constants.pwmLeftIntake);
        intakeMotorR = new Victor(Constants.pwmRightIntake);
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
