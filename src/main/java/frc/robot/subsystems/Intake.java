
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
import frc.robot.subsystems.IntakeArm;

public class Intake extends SubsystemBase {
    
    private SparkMax intakeMotor = new SparkMax(Constants.Intake.kIntakeMotor, MotorType.kBrushless);

    IntakeArm passiveLimit = new IntakeArm();

    public Intake() {
        
        SparkMaxConfig intakeConfig = new SparkMaxConfig();
        intakeConfig.smartCurrentLimit(Constants.kMaxCurrent);
        intakeConfig.idleMode(IdleMode.kBrake);
        intakeMotor.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public Command spinIntake(double speed) {
        if (passiveLimit.getLimitUp()){
            return this.runOnce( () -> intakeMotor.set(0));
        } else {
            return this.runOnce( () -> intakeMotor.set(speed));
        }
    }
    

}
