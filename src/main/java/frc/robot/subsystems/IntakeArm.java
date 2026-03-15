package frc.robot.subsystems;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import frc.robot.Constants;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class IntakeArm  extends SubsystemBase{

    private SparkMax intakeArmMotor;

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

    public Command spinIntakeArmUp(double speed) {

        return this.runOnce( () -> intakeArmMotor.set(speed));

    }

    public Command spinIntakeArmDown(double speed) {

        return this.runOnce( () -> intakeArmMotor.set(-speed));

    }

    public boolean isUpperLimitPressed() {

        return armUpperLimit.get();
    

    }
    public Command intakeArmJiggle() {
        return switch (isLimitedPressed()) {
            case true -> this.InstantCommand( () -> intakeArmMotor.set(Constants.IntakeArm.kSTOP));
            case false -> new InstantCommand( () -> intakeArmMotor.set(0.2));
        }
    }



}
