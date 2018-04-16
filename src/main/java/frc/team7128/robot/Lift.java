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

    public Lift() {
        liftMotor1 = new Jaguar(2);
        liftMotor2 = new Jaguar(3);

        liftLowerLimit = new DigitalInput(1);
        liftUpperLimit = new DigitalInput(2);
    }

    private void setSpeed(double speed) {
        liftMotor1.set(speed);
        liftMotor2.set(speed);
    }
    public void raise() {
        if (!liftUpperLimit.get()) {
            setSpeed(-1);
        }
    }
    public void lower()
    {
        if (!liftLowerLimit.get()) {
            setSpeed(0.3);
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
