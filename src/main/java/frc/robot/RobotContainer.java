// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.IntakeArm;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.AutoCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class RobotContainer {
    
    //SWERVE STUFF
    private double MaxSpeed = 1.0 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity
    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
    private final Telemetry logger = new Telemetry(MaxSpeed);
    public final CommandSwerveDrivetrain m_robotDrive = TunerConstants.createDrivetrain();


    private final CommandXboxController Driver = new CommandXboxController(0);
    private final CommandXboxController Operator = new CommandXboxController(1);

    //public final IntakeSubsystem intake = new IntakeSubsystem();
    //public final IntakeArm intakeArm = new IntakeArm();
    public final Shooter shooter = new Shooter();
    public final Index index = new Index();
    //public final Autonomous auto = new Autonomous(shooter, index);
    //public final AutoCommands AutoCommands = new AutoCommands(shooter, index);
    

    public RobotContainer() {
        configureBindings();
    }
    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        m_robotDrive.setDefaultCommand(
            // Drivetrain will execute this command periodically
            m_robotDrive.applyRequest(() ->
                drive.withVelocityX(-Driver.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-Driver.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-Driver.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );
        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            m_robotDrive.applyRequest(() -> idle).ignoringDisable(true)
        );
        Driver.a().whileTrue(m_robotDrive.applyRequest(() -> brake));
        Driver.b().whileTrue(m_robotDrive.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-Driver.getLeftY(), -Driver.getLeftX()))
        ));
        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        Driver.back().and(Driver.y()).whileTrue(m_robotDrive.sysIdDynamic(Direction.kForward));
        Driver.back().and(Driver.x()).whileTrue(m_robotDrive.sysIdDynamic(Direction.kReverse));
        Driver.start().and(Driver.y()).whileTrue(m_robotDrive.sysIdQuasistatic(Direction.kForward));
        Driver.start().and(Driver.x()).whileTrue(m_robotDrive.sysIdQuasistatic(Direction.kReverse));
        
        //INTAKE & INTAKE ARM
        //Operator.x().onTrue(intake.spinIntake(0.3)).onFalse(intake.spinIntake(0));
        //Operator.y().onTrue(intakeArm.spinIntakeArmUp(0.1)).onFalse(intakeArm.spinIntakeArmUp(0));
        //Operator.a().onTrue(intakeArm.spinIntakeArmDown(0.1)).onFalse(intakeArm.spinIntakeArmDown(0));
        //Driver.leftBumper().onTrue(intakeArm.intakeArmJiggle()).onFalse(intakeArm.intakeArmToFloor()); //arm jiggle


        //index
        Driver.rightBumper().onTrue(index.spinIndex(-0.5)).onFalse(index.spinIndex(0));

        //shooter short range
        Driver.leftTrigger().onTrue(shooter.spinShooterMotors(0.50)).onFalse(shooter.spinShooterMotors(0));
        Driver.leftTrigger().onTrue(shooter.spinShooterNeck(.50)).onFalse(shooter.spinShooterNeck((0)));

        //shooter mid range
        Driver.leftBumper().onTrue(shooter.spinShooterMotors(0.75)).onFalse(shooter.spinShooterMotors(0));


        //shooter long range
        Driver.rightTrigger().onTrue(shooter.spinShooterMotors(0.85)).onFalse(shooter.spinShooterMotors(0));
       


        // Reset the field-centric heading on X button press.
        Driver.x().onTrue(m_robotDrive.runOnce(m_robotDrive::seedFieldCentric));

        m_robotDrive.registerTelemetry(logger::telemeterize);

        //Driver.y().toggleOnTrue(AutoCommands.Shoot(shooter, index)).toggleonFalse(shooter.spinShooterMotors(0), index.spinIndex(0));
    }



    public Command getAutonomousCommand() {
        // Simple drive forward auton
        final var idle = new SwerveRequest.Idle();
        return Commands.sequence(
            // Reset our field centric heading to match the robot
            // facing away from our alliance station wall (0 deg).
            m_robotDrive.runOnce(() -> m_robotDrive.seedFieldCentric(Rotation2d.kZero)),
            // Then slowly drive forward (away from us) for 5 seconds.
            m_robotDrive.applyRequest(() ->
                drive.withVelocityX(-0.5)
                    .withVelocityY(0)
                    .withRotationalRate(0)
            )
            .withTimeout(3.0),
            
            //Activate Mid Distance Shooter and Index
            AutoCommands.Shoot(shooter, index),
            
            // Finally idle for the rest of auton
            m_robotDrive.applyRequest(() -> idle)
        );
    } 

    //edited Auto to just be activating shooter and index -> mid range
    // public Command getAutonomousCommand () {
    //     return AutoCommands.Shoot(shooter, index);
        //return new Autonomous(shooter, index).withTimeout(2.0);
        // return new ParallelCommandGroup(
        //     new InstantCommand( () -> shooter.spinShooterMotor(0.5)),
        //     new InstantCommand( () -> shooter.spinNeckMotor(0.5)),
        //     // new WaitCommand(1),
        //     new InstantCommand(() -> index.spinIndex(-0.25))
        // );
    }
}
