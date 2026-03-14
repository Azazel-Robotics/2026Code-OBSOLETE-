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

public class IntakeArm  extends SubsystemBase{

    private SparkMax intakeArmMotor;

      public IntakeArm() {

        intakeArmMotor = new SparkMax(Constants.IntakeArm.kIntakeArmMotor, MotorType.kBrushless);

        SparkMaxConfig intakeConfig = new SparkMaxConfig();

        intakeConfig.smartCurrentLimit(Constants.kMaxCurrent);
        intakeConfig.idleMode(IdleMode.kBrake);

        intakeArmMotor.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    }

    public Command spinIntakeArmUp(double speed) {

        return this.runOnce( () -> intakeArmMotor.set(speed));

    }

    public Command spinIntakeArmDown(double speed) {

        return this.runOnce( () -> intakeArmMotor.set(-speed));

    }
    
}
