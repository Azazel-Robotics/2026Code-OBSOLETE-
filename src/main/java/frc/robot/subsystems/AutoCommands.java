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
    private static boolean beenDelayed;

    public AutoCommands() {
        beenDelayed = false;
    }

    public static Command Shoot(Shooter shooter, Index index) {
        if (!beenDelayed) {
            beenDelayed = true;
            DriverStation.reportWarning("Calling AutoCommands::Shoot with Delay", false);

            return Commands.parallel(
                    Commands.run(() -> shooter.spinShooterMotorAuto(0.75, "AutoCommands::Shoot"), shooter),
                    Commands.run( () -> shooter.spinShooterNeckAuto(0.75, "AutoCommands::Shoot"), shooter),
                    Commands.waitSeconds(1).andThen(
                            Commands.run(() -> index.spinIndexAuto(0.25, "AutoCommands::Shoot"), index))
            );
        } else {
            DriverStation.reportWarning("Calling AutoCommands::Shoot without delay", false);
            return Commands.parallel(
                    Commands.run(() -> shooter.spinShooterMotorAuto(0.75, "AutoCommands::Shoot"), shooter),
                    Commands.run(() -> index.spinIndexAuto(0.25, "AutoCommands::Command::Shoot"), index));
        }

        // .alongWith();

        // addCommands(

        // //Command Group will (hopefully) finish after 10 seconds -AZ
        // //new WaitCommand(10.0), //check if this is right -AZ
        // new RunCommand ( () -> shooter.spinShooterMotor(0.25), shooter),
        // new RunCommand ( () -> shooter.spinNeckMotor(0.25), shooter),
        // new RunCommand( () ->
        // Commands.waitSeconds(1.5).andThen(index.spinIndex(0.3)), index)

        // //once completed, add command for the "jiggle"/feeder mechanism -AZ

        // );
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
