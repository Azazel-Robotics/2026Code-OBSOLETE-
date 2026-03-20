package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    private SparkMax indexMotor = new SparkMax(Constants.Index.kIntakeIndexMotor, MotorType.kBrushless);
    private boolean motorOn; //used for toggle method

    public Index() {
        //configs and sets limits for index motor
        SparkMaxConfig indexConfig = new SparkMaxConfig();
        indexConfig.smartCurrentLimit(Constants.kMaxCurrent);
        indexConfig.idleMode(IdleMode.kBrake); //stops motor from moving when button is not pressed
        indexMotor.configure(indexConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        motorOn = false;
    }

    //@override and periodic() means it is always running
    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Index Motor On", motorOn); //words that show up on dashboard screen :D
    }

    //Hold down button to spin motor
    public Command spinIndex(double speed) 
    {
        return this.runOnce( () -> indexMotor.set(-speed));
    }

    public Command spinIndex(double speed,String caller) 
    {
        SmartDashboard.putString("Index Motor Call", caller);
        return spinIndex(speed);
    }

    public Command spinIndexReverse(double speed) 
    {
        if(speed != 0)
        {
            SmartDashboard.putBoolean("Index Motor On", true);
        }
        else SmartDashboard.putBoolean("Index Motor On", false);
        return this.runOnce( () -> indexMotor.set(speed));
    }

    public Command spinIndexAuto(double autoSpeed)
    {
        return this.run( () -> indexMotor.set(-autoSpeed));
    }

    public Command spinIndexAuto(double autoSpeed, String caller)
    {
        SmartDashboard.putString("Index Motor Call Auto", caller);
        return spinIndexAuto(autoSpeed);
    }


    //press button to toggle motor on/off
    public Command toggleIndexMotor(double speed)
    {
        if(motorOn)
        {
            motorOn = false;
            return this.runOnce( () -> indexMotor.set(0));
        }
        else 
        {
            motorOn = true;
            return this.runOnce( () -> indexMotor.set(speed));
        }
    }
}
