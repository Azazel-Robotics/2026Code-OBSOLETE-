package frc.robot.subsystems;

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


    public static Command Shoot(Shooter shooter, Index index) {


            DriverStation.reportWarning("Calling AutoCommands::Shoot with Delay", false);

            return Commands.parallel(
                    shooter.spinShooterMotorsAuto(0.75),
                    Commands.waitSeconds(1).andThen(
                            index.spinIndexAuto(0.25, "AutoCommands::Shoot"))
            );

    }

    // figure out if a Parallel Deadline Group will work better -AZ

    // public Autonomous() {
    // addCommands (

    // new WaitCommand (10.0),
    // new RunCommand ( () -> shooter.spinShooterMotor(0.4)),
    // new RunCommand ( () -> shooter.spinNeckMotor(0.4)),
    // new RunCommand (() -> index.spinIndex(0.4))

    // );
    // }

}
