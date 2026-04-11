package frc.robot.commands;

//import java.lang.invoke.ClassSpecializer.SpeciesData;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeArm;

public class AutoCommands {
        //spins the shooter motors and waits until full speed before activating the index
        public static Command Shoot(Shooter shooter, Index index, double shooterSpeed, double indexSpeed) {

                return Commands.parallel(
                        shooter.spinShooterMotorsAuto(shooterSpeed),
                        Commands.waitSeconds(1).andThen(
                                index.spinIndexAuto(indexSpeed, "AutoCommands::Shoot")));

        }

        public static Command StopShoot(Shooter shooter, Index index){
                return Commands.parallel(
                
                );
        }

        //For no one that asked, I am NOT coding this with the Active limit switch in mind lol. 
        //If it breaks the switch it's not my problem (IT MOST DEFINITELY IS MY PROBLEM T^T ). -AZ

        //spins the intake motor to intake while moving the arm down to counter balance the movement..?
        public static Command Intake(Intake intake, IntakeArm intakeArm, double intakeSpeed, double armSpeed) {
                return Commands.parallel(
                        intakeArm.spinArmDown(armSpeed), 
                        intake.spinIntake(intakeSpeed)   
                );  
        }
        

}
