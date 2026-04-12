package frc.robot;
import edu.wpi.first.wpilibj.DataLogManager;

public class LogManager {
    public void WriteToLog(String subsystem, String data){

        DataLogManager.log("DataLogger");
    }
    
}
