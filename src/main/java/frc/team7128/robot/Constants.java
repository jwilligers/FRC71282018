package frc.team7128.robot;

public class Constants {

    // PWM Ports
    public static final int pwmLeftDrivetrain = 0;
    public static final int pwmRightDrivetrain = 1;
    public static final int pwmLiftA = 2;
    public static final int pwmLiftB = 3;
    public static final int pwmLeftIntake = 4;
    public static final int pwmRightIntake = 5;

    // DIO Ports
    public static final int dioLiftLowerLimit = 1;
    public static final int dioLiftUpperLimit = 2;

    // Lift Variables
    public static final double liftLowerSpeed = 0.3;
    public static final double liftRaiseSpeed = -1;

    // Intake Variables
    public static final double intakeSpeed = 0.85;
    public static final double ejectSpeed = -0.85;
}
