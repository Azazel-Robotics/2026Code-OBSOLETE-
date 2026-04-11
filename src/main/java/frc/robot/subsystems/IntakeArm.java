package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.spark.SparkLimitSwitch;

public class IntakeArm extends SubsystemBase {
    private static final IntakeArm instance;

    static {
        instance = new IntakeArm();
    }

    public static IntakeArm getInstance() {
        return instance;
    }

    private SparkMax intakeArmMotor;

    // private RelativeEncoder encoder; //was this the one that works..? -AZ
    // private SparkClosedLoopController armController;

    //limit switchs
    private SparkLimitSwitch limitUp;
    private SparkLimitSwitch limitDown;

    public IntakeArm() {

        // configs for intake arm speed
        intakeArmMotor = new SparkMax(Constants.IntakeArm.kIntakeArmMotor, MotorType.kBrushless);
        SparkMaxConfig intakeArmConfig = new SparkMaxConfig();
        intakeArmConfig.smartCurrentLimit(Constants.kMaxCurrent);
        intakeArmConfig.idleMode(IdleMode.kBrake);
        intakeArmMotor.configure(intakeArmConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        
        //initializing limit switches
        limitDown = intakeArmMotor.getForwardLimitSwitch();
        limitUp = intakeArmMotor.getReverseLimitSwitch();


        // //initalizing encoder and stuff
        // encoder = intakeArmMotor.getEncoder(); //idk what this was for..? -AZ
        // armController = intakeArmMotor.getClosedLoopController();
        
        // //we probably need to change the setpoint value..? I just put it on zero for now. -AZ
        // armController.setSetpoint(0, ControlType.kPosition);

        // //I put zeros as the values for now -> we should figure out the correct tuning for the constants class -AZ
        // intakeArmConfig.closedLoop
        // .p(0)
        // .i(0) //this is not recommended for FRC so maybe we should use the feedforward method instead..? -AZ
        // .d(0)
        // .outputRange(0, 0);
        

    }
    public SparkMax getIntakeArmMotor(){
        return intakeArmMotor;
    }

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

    public boolean getLimitUp() {
        return limitUp.isPressed();
    }

    public boolean getLimitDown() {
        return limitDown.isPressed();
    }

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

    // // POSITIONS OF INTAKE ARM
    // // encoders stuff
    // public static enum armStates {
    //     // change parameters to match positions of arm
    //     START(0),
    //     JIGGLE1(20),
    //     JIGGLE2(40),
    //     FLOOR(100);

    //     private final double armPosition;

    //     private armStates(double position) {
    //         this.armPosition = position;
    //     }

    //     public double getArmPosition() {
    //         return this.armPosition;
    //     }
    // }

    //testing limit switches
    // // is limit switch pressed
    // boolean isUpperPressed = !armPassive.get(); // .get() returns false when button is pressed
    // boolean isLowerPressed = !armActive.get();

    // // MANUAL INTAKE ARM UP AND DOWN limit switches
    // public Command spinIntakeArmUp(double speed) {
    //     // account for limit switches
    //     if (isUpperPressed) {
    //         return this.runOnce(() ->intakeArmMotor.set(0));
    //     } else {
    //         return this.runOnce(() ->intakeArmMotor.set(speed));
    //     }
    // }

    // public Command spinIntakeArmDown(double speed) {
    //     // account for limit switches
    //     if (isLowerPressed) {
    //         return this.runOnce(() -> intakeArmMotor.set(0));
    //     } else {
    //         return this.runOnce(() -> intakeArmMotor.set(-speed));
    //     }
    // }

    /*
    // JIGGLE FEATURES LOL
    boolean goUp = true; // goUp = true, motor is going positive direction

    // public void setReferencePoint //what???? I forgot what we were going to put here??? -AZ

    // this is for limit switches
    public Command intakeArmJiggle() {
        if (isUpperPressed) {
            goUp = false;
            return this.runOnce(() -> intakeArmMotor.set(-0.1));
        } else if (isLowerPressed) {
            goUp = true;
            return this.runOnce(() -> intakeArmMotor.set(0.1));
        } else if (goUp) {
            return this.runOnce(() -> intakeArmMotor.set(0.1));
        } else {
            return this.runOnce(() -> intakeArmMotor.set(-0.1));
        }
    } */

    // to reset position of intake arm to floor, PLEASE CHANGE IDK IF WORK -JA
    // public Command intakeArmToFloor() {
    //     if (isLowerPressed) {
    //         return this.runOnce(() -> intakeArmMotor.set(0));
    //     } else {
    //         return this.runOnce(() -> intakeArmMotor.set(-0.1));
    //     }

    // }

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