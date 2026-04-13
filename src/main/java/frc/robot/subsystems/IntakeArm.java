package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.spark.SparkLimitSwitch;

public class IntakeArm extends SubsystemBase {
    
    private SparkMax intakeArmMotor;

    //---------- Declaring Limit Switches ----------//
    private SparkLimitSwitch limitUp;
    private SparkLimitSwitch limitDown;

    //---------- Class Instance Creation ----------//
    private static final IntakeArm instance;

    static {
        instance = new IntakeArm();
    }

    public static IntakeArm getInstance() {
        return instance;
    }


    public IntakeArm() {

        // configs for intake arm speed
        intakeArmMotor = new SparkMax(Constants.IntakeArm.kIntakeArmMotor, MotorType.kBrushless);

        //---------- Intake Arm Motor Configuration and Set Current Limit ----------//
        SparkMaxConfig intakeArmConfig = new SparkMaxConfig();
        intakeArmConfig.smartCurrentLimit(Constants.kMaxCurrent);
        intakeArmConfig.idleMode(IdleMode.kBrake);
        intakeArmMotor.configure(intakeArmConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        
        //---------- Initializing Limit Switches ----------//
        limitDown = intakeArmMotor.getForwardLimitSwitch();
        limitUp = intakeArmMotor.getReverseLimitSwitch();

    }


    public SparkMax getIntakeArmMotor(){
        return intakeArmMotor;
    }

    //many of the if-else constraint/parameters did not work as the motor stopped once the switch was hit
    //switches were hard coded to stop -> seen in REV Client -AZ

    //---------- Initial Teleop Commands for Intake Arm ----------//
    //Commands did not work in some cases -> sometimes the arm would go down but never back up and vice versa

    /* 
    //manual up and down, limit switches work HOORAY -JA
    //DOUBLE CHECK which limit switch is up and down -JA
    public Command spinArmUp(double speed) {
        if(limitUp.isPressed()){
            return this.runOnce(() -> intakeArmMotor.set(0));
        } else if (!(limitUp.isPressed() && limitDown.isPressed())){
            return this.runOnce(() -> intakeArmMotor.set(-speed)); 
        } else {
            return this.runOnce(() -> intakeArmMotor.set(-speed));
        }
    }
    public Command spinArmDown(double speed) {
        if(limitDown.isPressed()){
            return this.runOnce(() -> intakeArmMotor.set(0));
        } else if (!(limitUp.isPressed() && limitDown.isPressed())){
            return this.runOnce(() -> intakeArmMotor.set(speed)); 
        }else{
            return this.runOnce(() -> intakeArmMotor.set(speed));
        }
    }
        
    */

    //---------- Teleop Intake Arm Commands ----------//
    //we got rid of the conditions as the motor was hard coded to stop in REV Client

    public Command spinArmUp(double speed) {
        return  this.runOnce(() -> intakeArmMotor.set(-speed));    
    }
    
    public Command spinArmDown(double speed) {
        return this.runOnce(() -> intakeArmMotor.set(speed));
    }

    //---------- Limit Switches' Accessors ----------//
    //returns true if the limit switch is pressed
    
    public boolean getLimitUp() {
        return limitUp.isPressed();
    }

    public boolean getLimitDown() {
        return limitDown.isPressed();
    }

    //---------- Testing Intake Arm Commands for Auto Routines ----------//

    //added by AZ -> ASK before touching; there is a reason I wrote parameters this way! -AZ
    public Command autoSpinArm(double speed) {
        if(!limitDown.isPressed() && limitUp.isPressed()){
            //arm moves down to Active Limit
            while (!limitDown.isPressed()){
                return this.run( () -> intakeArmMotor.set(speed));
            }
            return this.runOnce( () -> intakeArmMotor.set(0));

        } else if (limitDown.isPressed() && !limitUp.isPressed()){
            //arm move up to Passive Limit
            while(!limitUp.isPressed()) {
                return this.run( () -> intakeArmMotor.set(-speed));
            }
            return this.runOnce( () -> intakeArmMotor.set(0));

        } else {
            //if neither limit is pressed the arm goes back to Passive limit
            while (!limitUp.isPressed()) {
                return this.run( () -> intakeArmMotor.set(-speed));
            }
            return this.runOnce( () -> intakeArmMotor.set(0));
        }
    }
    
    //safer versions of autoSpinArm that is more guaranteed with the results -AZ
    public Command autoSpinArmUp(double speed) {
            while(!limitUp.isPressed()) {
                return this.run( () -> intakeArmMotor.set(-speed));
            }
            return this.runOnce( () -> intakeArmMotor.set(0));
        }

        public Command autoSpinArmDown(double speed) {
            while(!limitDown.isPressed()) {
                return this.run( () -> intakeArmMotor.set(speed));
            }
            return this.runOnce( () -> intakeArmMotor.set(0));
        }

    /*
     * //SMARTDASHBOARD THINGS
     * 
     * @Override
     * public void periodic() {
     * SmartDashboard.putNumber("Position", encoder.getPosition());
     * SmartDashboard.putNumber("Velocity", encoder.getVelocity());
     * 
     * SmartDashboard.putNumber("Arm Motor Current",
     * intakeArmMotor.getOutputCurrent());
     * SmartDashboard.putNumber("Arm Motor Output",
     * intakeArmMotor.getAppliedOutput());
     * }
     */

}