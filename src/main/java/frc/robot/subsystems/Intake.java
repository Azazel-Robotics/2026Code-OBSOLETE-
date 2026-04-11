
package frc.robot.subsystems;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import frc.robot.Constants;
import frc.robot.RobotContainer;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.subsystems.IntakeArm;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends SubsystemBase {

    public static final Intake intake;
    static {
        intake = new Intake();
    }
    public static Intake getInstance() {
        return intake;
    }
    
    private SparkMax intakeMotor = new SparkMax(Constants.Intake.kIntakeMotor, MotorType.kBrushless);

    IntakeArm passiveLimit;

    public Intake() {
        passiveLimit = IntakeArm.getInstance();
        SparkMaxConfig intakeConfig = new SparkMaxConfig();
        intakeConfig.smartCurrentLimit(Constants.kMaxCurrent);
        intakeConfig.idleMode(IdleMode.kBrake);
        intakeMotor.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }
/* 
    @Override
    public void periodic() {
        SmartDashboard.putBoolean("passiveLimit", passiveLimit.getLimitUp());
    }*/

    public Command spinIntake(double speed) {
        if (passiveLimit.getLimitUp()){
           return this.runOnce( () -> intakeMotor.set(0));
        } else /*if (passiveLimit.getLimitDown())*/ {
            return this.runOnce( () -> intakeMotor.set(speed));
        }
    }
    

}
