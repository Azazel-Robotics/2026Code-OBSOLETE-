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

public class IntakeArm extends SubsystemBase{

    private SparkMax intakeArmMotor;

    //declaring limit switches
    private DigitalInput armUpperLimit;
    private DigitalInput armLowerLimit;
    

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

    //is limit switch pressed
    boolean isUpperPressed = !armUpperLimit.get(); //.get() returns false when button is pressed
    boolean isLowerPressed = !armLowerLimit.get();

   
    public Command spinIntakeArmUp(double speed) {

        //account for limit switches
        if (isUpperPressed) {
            return this.runOnce( () -> intakeArmMotor.set(Constants.kSTOP));
        }

        return this.runOnce( () -> intakeArmMotor.set(speed)); //manually move intake arm up
    }
    
    public Command spinIntakeArmDown(double speed) {

        //account for limit switches
        if (isLowerPressed) {
            return this.runOnce( () -> intakeArmMotor.set(Constants.kSTOP));
        }

        return this.runOnce( () -> intakeArmMotor.set(-speed)); //manually move intake arm down
    }

    //jiggle features lol
    boolean goUp = true; //goUp = true, motor is going positive direction
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
            return this.runOnce( () -> intakeArmMotor.set(Constants.kSTOP));
        } else {
            while (!isLowerPressed) {
                return this.runOnce( () -> intakeArmMotor.set(-0.1));
            }
            return this.runOnce( () -> intakeArmMotor.set(Constants.kSTOP));
        }
        
    }
        
}
//testing commit thing AZ
//hiiii