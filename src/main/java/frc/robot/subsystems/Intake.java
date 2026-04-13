
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


public class Intake extends SubsystemBase {

    private SparkMax intakeMotor = new SparkMax(Constants.Intake.kIntakeMotor, MotorType.kBrushless);

    IntakeArm passiveLimit;

    //---------- Class Instance Creation ----------//
    public static final Intake intake;

    static {
        intake = new Intake();
    }

    public static Intake getInstance() {
        return intake;
    }

    public Intake() {

        passiveLimit = IntakeArm.getInstance();

        //---------- Intake Motor Configuration and Set Current Limit ----------//
        SparkMaxConfig intakeConfig = new SparkMaxConfig();
        intakeConfig.smartCurrentLimit(Constants.kMaxCurrent);
        intakeConfig.idleMode(IdleMode.kBrake);
        intakeMotor.configure(intakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }
    /*
     * @Override
     * public void periodic() {
     * SmartDashboard.putBoolean("passiveLimit", passiveLimit.getLimitUp());
     * }
     */

    //---------- Teleop and Auto Routine Intake Motor Command ----------//
    public Command spinIntake(double speed) {
        return this.runOnce(() -> intakeMotor.set(speed));
    }

}
