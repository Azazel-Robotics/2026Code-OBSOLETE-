package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.DigitalInput;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.spark.SparkClosedLoopController;

public class IntakeArm extends SubsystemBase{

    private SparkMax intakeArmMotor;

    //declaring limit switches if we use them..?
    private DigitalInput armUpperLimit;
    private DigitalInput armLowerLimit;

    private RelativeEncoder encoder = intakeArmMotor.getEncoder();
    private SparkClosedLoopController armController;
    

    public IntakeArm() {

        //configs for intake arm speed
        intakeArmMotor = new SparkMax(Constants.IntakeArm.kIntakeArmMotor, MotorType.kBrushless);
        SparkMaxConfig intakeArmConfig = new SparkMaxConfig();
        intakeArmConfig.smartCurrentLimit(Constants.kMaxCurrent);
        intakeArmConfig.idleMode(IdleMode.kBrake);
        intakeArmMotor.configure(intakeArmConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        //initilaizing limit switches
        armUpperLimit = new DigitalInput(Constants.IntakeArm.kArmUpperLimit);
        armLowerLimit = new DigitalInput(Constants.IntakeArm.kArmLowerLimit);

    }

    //POSITIONS OF INTAKE ARM
    //encoders stuff
    public static enum armStates{
        //change parameters to match positions of arm
        START(0),
        JIGGLE1(20),
        JIGGLE2(40),
        FLOOR(100);

        private final double armPosition;

        private armStates(double position) {
            this.armPosition = position;
        }

        public double getArmPosition() {
            return this.armPosition;
        }
    }

    //is limit switch pressed
    boolean isUpperPressed = !armUpperLimit.get(); //.get() returns false when button is pressed
    boolean isLowerPressed = !armLowerLimit.get();

   //MANUAL INTAKE ARM UP AND DOWN limit switches
    public Command spinIntakeArmUp(double speed) {
        //account for limit switches
        if (isUpperPressed) {
            return this.runOnce( () -> intakeArmMotor.set(0));
        } else {
        return this.runOnce( () -> intakeArmMotor.set(speed));
        }
    }
    public Command spinIntakeArmDown(double speed) {
        //account for limit switches
        if (isLowerPressed) {
            return this.runOnce( () -> intakeArmMotor.set(0));
        } else {
        return this.runOnce( () -> intakeArmMotor.set(-speed)); 
        }
    }


    //JIGGLE FEATURES LOL
    boolean goUp = true; //goUp = true, motor is going positive direction
    
    //public void setReferencePoint //what???? I forgot what we were going to put here??? -AZ

    public Command intakeArmJiggle() {
        if (isUpperPressed) {
            goUp = false;
            return this.runOnce( () -> intakeArmMotor.set(-0.1));
        } else if (isLowerPressed) {
            goUp = true;
            return this.runOnce( () -> intakeArmMotor.set(0.1));
        } else if (goUp) {
            return this.runOnce( () -> intakeArmMotor.set(0.1));
        } else {
            return this.runOnce( () -> intakeArmMotor.set(-0.1));
        }
    }
    //to reset position of intake arm to floor, PLEASE CHANGE IDK IF WORK -JA
    public Command intakeArmToFloor() {
        if (isLowerPressed) {
            return this.runOnce( () -> intakeArmMotor.set(0));
        } else {
            while (!isLowerPressed) {
                return this.runOnce( () -> intakeArmMotor.set(-0.1));
            }
            return this.runOnce( () -> intakeArmMotor.set(0));
        }
        
    }

    /*
    //SMARTDASHBOARD THINGS
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Position", encoder.getPosition());
        SmartDashboard.putNumber("Velocity", encoder.getVelocity());

        SmartDashboard.putNumber("Arm Motor Current", intakeArmMotor.getOutputCurrent());
        SmartDashboard.putNumber("Arm Motor Output", intakeArmMotor.getAppliedOutput());
    }
    */
        
}