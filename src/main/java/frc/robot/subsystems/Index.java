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

public class Index extends SubsystemBase{

    private SparkMax indexMotor;

    public Index() {

        indexMotor = new SparkMax(Constants.Index.kIntakeIndexMotor, MotorType.kBrushless);

        SparkMaxConfig indexConfig = new SparkMaxConfig();

        indexConfig.smartCurrentLimit(Constants.kMaxCurrent);
        indexConfig.idleMode(IdleMode.kBrake);

        indexMotor.configure(indexConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public Command spinIndex(double speed) {

        return this.runOnce( () -> indexMotor.set(speed));

    }
}
