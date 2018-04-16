package frc.team7128.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Lift {
    // Motors
    private Jaguar liftMotor1;
    private Jaguar liftMotor2;

    //Sensors
    private DigitalInput liftLowerLimit;
    private DigitalInput liftUpperLimit;

    public Lift() { // Constructor called when creating a new lift
        liftMotor1 = new Jaguar(Constants.pwmLiftA);
        liftMotor2 = new Jaguar(Constants.pwmLiftB);
        liftLowerLimit = new DigitalInput(Constants.dioLiftLowerLimit);
        liftUpperLimit = new DigitalInput(Constants.dioLiftUpperLimit);
    }

    private void setSpeed(double speed) {
        liftMotor1.set(speed);
        liftMotor2.set(speed);
    }
    public void raise() {
        if (!liftUpperLimit.get()) {
            setSpeed(-Constants.liftRaiseSpeed);
        }
    }
    public void lower()
    {
        if (!liftLowerLimit.get()) {
            setSpeed(Constants.liftLowerSpeed);
        }
    }
    public void holdPosition()
    {
        setSpeed(-0.1);
    }
    public void stop()
    {
        setSpeed(0);
    }
    public void updateSmartDashboard()
    {
        SmartDashboard.putNumber("Lift Motor Output", liftMotor1.get());
        SmartDashboard.putBoolean("Lift Lower Limit", liftLowerLimit.get());
        SmartDashboard.putBoolean("Lift Upper Limit", liftUpperLimit.get());
    }
}
