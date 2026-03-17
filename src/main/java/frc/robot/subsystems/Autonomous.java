package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Index;

public class Autonomous extends ParallelCommandGroup{

    public Autonomous(Shooter shooter, Index index){
        addCommands(

        new RunCommand ( () -> shooter.spinShooterMotor(0.4), shooter),
        new RunCommand (() -> index.spinIndex(.4), index)

        );
    }

}
