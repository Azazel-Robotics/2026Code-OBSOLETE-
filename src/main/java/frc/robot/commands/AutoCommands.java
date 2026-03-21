package frc.robot.commands;

//import java.lang.invoke.ClassSpecializer.SpeciesData;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Index;

public class AutoCommands {
        //spins the shooter motors and waits until full speed before activating the index
        public static Command Shoot(Shooter shooter, Index index, double shooterSpeed) {

                return Commands.parallel(
                                shooter.spinShooterMotors(shooterSpeed),
                                Commands.waitSeconds(1).andThen(
                                                index.spinIndexAuto(0.25, "AutoCommands::Shoot")));

        }

        //Testing for Auto
        

}
