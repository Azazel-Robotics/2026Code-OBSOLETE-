package frc.robot;
public class Constants {
    
    public static final int kMaxCurrent = 40;
    public static final double kSTOP = 0;
    
    public static class Intake{
        public static final int kIntakeMotor = 1;
        
    }
    
    public static class Shooter{
        //MAKE SURE TO RECHECK PORTS -> THE NECK SPAWNED IN -AZ
        public static final int kShooterMotor = 17;
        public static final int kShooterNeckMotor = 18;
    }

    public static class IntakeArm{
        //CHANGE THIS -> WE DIDNT HAVE PORT # AT THE TIME OF CREATION -AZ
        public static final int kIntakeArmMotor = 2;
        //CHANGE THESE
        public static final int kArmUpperLimit = 0;
        public static final int kArmLowerLimit = 1;
    }

    public static class Index{
        //CHANGE THIS -> WE DIDNT HAVE PORT # AT THE TIME OF CREATION -AZ
        public static final int kIntakeIndexMotor = 3;
    }
}
