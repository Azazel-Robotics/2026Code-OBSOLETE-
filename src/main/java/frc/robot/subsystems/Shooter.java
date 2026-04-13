package frc.robot.subsystems;

import frc.robot.Constants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;


public class Shooter extends SubsystemBase {

    //---------- Class Instance Creation ----------//
    public static final Shooter instance;

    static {
        instance = new Shooter();
    }

    public static Shooter getInstance() {
        return instance;
    }

    //---------- Declaring and Initializing Shooter Motors ----------//
    private TalonFX shooterMotor = new TalonFX(Constants.Shooter.kShooterMotor);
    private TalonFX shooterNeckMotor = new TalonFX(Constants.Shooter.kShooterNeckMotor);

    public Shooter() {

        //---------- Reseting Motors to Factory Default Configs ----------//
        shooterMotor.getConfigurator().apply(new TalonFXConfiguration());
        shooterNeckMotor.getConfigurator().apply(new TalonFXConfiguration());

        var currentConfig = new CurrentLimitsConfigs();
        TalonFXConfiguration config = new TalonFXConfiguration();

        //---------- Configuration of Motors' Current ----------//
        currentConfig.StatorCurrentLimit = Constants.kMaxCurrent;
        currentConfig.StatorCurrentLimitEnable = true;

        //---------- Refreshing and Applying Current Configuration ----------//
        shooterMotor.getConfigurator().refresh(currentConfig);
        shooterMotor.getConfigurator().apply(currentConfig);

        shooterNeckMotor.getConfigurator().refresh(currentConfig);
        shooterNeckMotor.getConfigurator().apply(currentConfig);
    }

    //---------- Controls for Setting the Speeds of the Motors (Separately) ----------//

    // // set speed of shooter motor
    // public Command spinShooterMotor(double speed) {
    //     return this.runOnce(() -> shooterMotor.set(speed));
    // }

    // //set speed of shooter neck motor
    // public Command spinShooterNeck(double speed) {

    //     return this.runOnce(() -> shooterNeckMotor.set(speed));
    // }

    //---------- Teleop Commands to Set Speeds for Both Motors ----------//
    
    public Command spinShooterMotors(double speed) {
        return this.runOnce(() -> {
            shooterNeckMotor.set(speed);
            shooterMotor.set(speed);
        });
    }

    public Command spinShooterZero() {
        return this.runOnce(() -> {
            shooterNeckMotor.set(0);
            shooterMotor.set(0); 
        });
    }

    //---------- Auto Routine Command to Set Speeds for Both Motors ----------//

    //sets speed of the shooter and shooter neck motor during auto
    public Command spinShooterMotorsAuto(double autoSpeed) {
        return this.run(() -> {
            shooterNeckMotor.set(autoSpeed);
            shooterMotor.set(autoSpeed);
        });
    }

}
