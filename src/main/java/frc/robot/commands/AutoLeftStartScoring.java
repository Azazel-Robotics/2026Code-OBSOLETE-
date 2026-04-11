package frc.robot.commands;

import com.pathplanner.lib.commands.PathPlannerAuto;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.subsystems.*;

public class AutoLeftStartScoring extends SequentialCommandGroup {
   public  AutoLeftStartScoring(){
        //Moves Robot backward 
     addCommands(
      
     //1st path to shoot into hub
      new PathPlannerAuto("Move Back, Rotate"),
        
        //RD: Shoot all 8 balls in Hopper! I Hope
      new WaitCommand(4.5).deadlineFor(AutoCommands.Shoot(Shooter.getInstance(), Index.getInstance(), .75, .5))
        .andThen(AutoCommands.Shoot(Shooter.getInstance(), Index.getInstance(), 0, .0)),
        
      new PathPlannerAuto("Enter Mid"),

      new WaitCommand(1.5).deadlineFor(IntakeArm.getInstance().autoSpinArmDown(.65)),

      new PathPlannerAuto("Move to Intake").deadlineFor(Intake.getInstance().spinIntake(1))
      .andThen(Intake.getInstance().spinIntake(0)),

      new WaitCommand(1.5).deadlineFor(IntakeArm.getInstance().autoSpinArmUp(.65)),

      new PathPlannerAuto("Move to Hub to Score"),

      new WaitCommand(8.5).deadlineFor(AutoCommands.Shoot(Shooter.getInstance(), Index.getInstance(), .65, .5))
        
        );
    



    }
}
