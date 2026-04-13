package frc.robot;

public class Constants {
    
    //---------- Maximum Current of SparkMax and Falcon Motors ----------//
    public static final int kMaxCurrent = 40;
    
    public static class Intake{
        //ID of the Intake Motor
        public static final int kIntakeMotor = 21;
    }
    
    public static class Shooter{
        //IDs of the Shooter Motors
        public static final int kShooterMotor = 17;
        public static final int kShooterNeckMotor = 18;
    }

    public static class IntakeArm{
        //ID of the Intake Arm Motor
        public static final int kIntakeArmMotor = 22;
       
        //IDs of the Limit Switches
        public static final int kArmPassive = 0;
        public static final int kArmActive = 1;
    }

    public static class Index{
        //ID of the Index Motor
        public static final int kIntakeIndexMotor = 20;
    }
}
