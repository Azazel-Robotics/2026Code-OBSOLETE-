package frc.robot;
public class Constants {
    
    //sets the maximum current of SparkMax and Falcon motors
    public static final int kMaxCurrent = 40;
    
    public static class Intake{
        //ID of the Intake Motor
        public static final int kIntakeMotor = 21;
    }
    
    public static class Shooter{
        //Ids of the shooter motors
        public static final int kShooterMotor = 17;
        public static final int kShooterNeckMotor = 18;
    }

    public static class IntakeArm{
        //CHECK THIS
        public static final int kIntakeArmMotor = 19;
       
        //CHANGE/CHECK THESE
        public static final int kArmPassive = 0;
        public static final int kArmActive = 1;
    }

    public static class Index{
        //ID of the Index motor
        public static final int kIntakeIndexMotor = 20;
    }
}
