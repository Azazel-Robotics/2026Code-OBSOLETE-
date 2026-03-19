package frc.robot;
public class Constants {
    
    public static final int kMaxCurrent = 40;
    
    public static class Intake{
        public static final int kIntakeMotor = 21;
    }
    
    public static class Shooter{
        public static final int kShooterMotor = 17;
        public static final int kShooterNeckMotor = 18;
    }

    public static class IntakeArm{
        public static final int kIntakeArmMotor = 19;
        //CHANGE THESE
        public static final int kArmUpperLimit = 0;
        public static final int kArmLowerLimit = 1;
    }

    public static class Index{
        public static final int kIntakeIndexMotor = 20;
    }
}
