package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

import frc.robot.Constants;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class Shooter extends SubsystemBase {

    private TalonFX shooterMotor = new TalonFX(Constants.Shooter.kShooterMotor);
    private TalonFX shooterNeckMotor = new TalonFX(Constants.Shooter.kShooterNeckMotor);

    public Shooter() {

        // resets motor to factory default config
        shooterMotor.getConfigurator().apply(new TalonFXConfiguration());
        shooterNeckMotor.getConfigurator().apply(new TalonFXConfiguration());

        var currentConfig = new CurrentLimitsConfigs();
        TalonFXConfiguration config = new TalonFXConfiguration();

        // configs current of motor
        currentConfig.StatorCurrentLimit = Constants.kMaxCurrent;
        currentConfig.StatorCurrentLimitEnable = true;

        // refreshes and applies current config
        shooterMotor.getConfigurator().refresh(currentConfig);
        shooterMotor.getConfigurator().apply(currentConfig);

        shooterMotor.setControl(new Follower(shooterNeckMotor.getDeviceID(), MotorAlignmentValue.Aligned));

        shooterNeckMotor.getConfigurator().refresh(currentConfig);
        shooterNeckMotor.getConfigurator().apply(currentConfig);
    }

    // set speed of shooter motors
    public Command spinShooterMotor(double speed) {
        return this.runOnce(() -> shooterMotor.set(speed));
    }

    public Command spinShooterNeck(double speed) {

        return this.runOnce(() -> shooterNeckMotor.set(speed));
    }

    public Command spinShooterMotorAuto(double autoSpeed) {
        return this.run(() -> shooterMotor.set(autoSpeed));
    }

    public Command spinShooterMotors(double autoSpeed) {
        return this.run(() -> {
            shooterNeckMotor.set(autoSpeed);
            shooterMotor.set(autoSpeed);
        });
    }

}
