package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;


public abstract class Subsystem extends SubsystemBase {

    public String baseSmartDashboardKey;

    public Subsystem(String key){
        this.baseSmartDashboardKey = key;
    }
    

    public void putNumber(String key, double value) {

    }
}
