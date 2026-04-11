package frc.robot.commands;

import com.pathplanner.lib.commands.PathPlannerAuto;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.subsystems.*;

public class AutoLeftStartScoring extends SequentialCommandGroup {
   public  AutoLeftStartScoring(){
        //Moves Robot backward 
     addCommands(
      
     //1st path to shoot into hub
      new PathPlannerAuto("Backwards"),
        
      //RD: Shoot all 8 balls in Hopper! I Hope
      new WaitCommand(3).deadlineFor(AutoCommands.Shoot(Shooter.getInstance(), Index.getInstance(), .75, .5)),
      
      new WaitCommand(.1).deadlineFor(AutoCommands.Shoot(Shooter.getInstance(), Index.getInstance(), 0, 0)),
      /* 
      //Get in Position to collect Fuel from Mid
      new PathPlannerAuto("Mid"),

      //Arm Down, Arm down takes about 1.3 seconds
      new WaitCommand(1.5).deadlineFor(IntakeArm.getInstance().spinArmDown(.65)),
      */

      //Trying to Combine 1: Get in Poisition to collect fuel form mid, 2: IntakeArm Down
      //According to Path Planner, It clear the trench at 1.9 seconds
      new ParallelCommandGroup(
        new PathPlannerAuto("Mid"), 
        new WaitCommand(1.9).andThen(new WaitCommand(1.5).deadlineFor(IntakeArm.getInstance().spinArmDown(.65)))),
      
      new PathPlannerAuto("Intake").deadlineFor(Intake.getInstance().spinIntake(1)),
      
      new WaitCommand(.1).deadlineFor(Intake.getInstance().spinIntake(0)),

      new WaitCommand(.5).deadlineFor(IntakeArm.getInstance().spinArmUp(.75)),

      new PathPlannerAuto("Hub 1"),

      new WaitCommand(8.5).deadlineFor(AutoCommands.Shoot(Shooter.getInstance(), Index.getInstance(), .65, .5))
        
        );
    



    }
}
