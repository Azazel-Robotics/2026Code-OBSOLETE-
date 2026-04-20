package frc.robot.subsystems;

import frc.robot.Constants;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;

//---------- Offseason Testing Kraken Motors for Subsystems ----------//

public class KrakenConfig extends SubsystemBase {

    //---------- Class Instance Creation ----------//

    public static final KrakenConfig instance;

    static {
        instance = new KrakenConfig();
    }

    public static KrakenConfig getInstance() {
        return instance;
    }
    
    //---------- Declaring and Initializing Kraken Motor ----------//
    private TalonFX krakenMotor = new TalonFX(Constants.KrakenConfig.kKrakenMotor);


    public KrakenConfig() {

        //reset to factory default config
        krakenMotor.getConfigurator().apply(new TalonFXConfiguration());
        var currentConfig = new CurrentLimitsConfigs();

        //config of motor current
        currentConfig.StatorCurrentLimit = Constants.kMaxCurrent;
        currentConfig.StatorCurrentLimitEnable = true;

        //refresh and apply current configs
        krakenMotor.getConfigurator().refresh(currentConfig);
        krakenMotor.getConfigurator().apply(currentConfig);
    }

    public Command spinKrakenMotor(double speed) {
        return this.runOnce( () -> krakenMotor.set(speed));
    }
}
