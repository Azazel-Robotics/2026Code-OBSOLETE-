package frc.robot.commands;

import com.pathplanner.lib.commands.PathPlannerAuto;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.subsystems.*;

public class middleAuto  extends SequentialCommandGroup{
    public middleAuto() {
        
        addCommands(
            new PathPlannerAuto("Middle Move Back"),

            new WaitCommand(3).deadlineFor(AutoCommands.Shoot(Shooter.getInstance(), Index.getInstance(), .75, .5)),
      
            new WaitCommand(.1).deadlineFor(AutoCommands.StopShoot(Shooter.getInstance(), Index.getInstance()))
        
    );

    }
}
