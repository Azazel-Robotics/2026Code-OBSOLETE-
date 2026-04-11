// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.AutoCommands;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeArm;
import frc.robot.subsystems.Shooter;

public class RobotContainer {

        // SWERVE STUFF
        private double MaxSpeed = 0.75 * TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired
                                                                                             // top
                                                                                             // speed
        private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per
                                                                                          // second
                                                                                          // max angular velocity
        /* Setting up bindings for necessary control of the swerve drive platform */
        private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
                        .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
                        .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive
                                                                                 // motors
        private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
        private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
        private final Telemetry logger = new Telemetry(MaxSpeed);
        public final CommandSwerveDrivetrain m_robotDrive = TunerConstants.createDrivetrain();

        private final CommandXboxController Driver = new CommandXboxController(0);
        private final CommandXboxController Operator = new CommandXboxController(1);
        public final IntakeArm intakeArm = IntakeArm.getInstance();
        public final Intake intake = Intake.getInstance();
        public final Shooter shooter = Shooter.getInstance();
        public final Index index = Index.getInstance();

        // testing for multiple autos
        SendableChooser<Command> m_autoChooser = new SendableChooser<>();
        SendableChooser<Command> m_autoChooserPathPlanner;

        public RobotContainer() {
                configureBindings();
                
                m_autoChooserPathPlanner = AutoBuilder.buildAutoChooser();

                // Auto Option Displayer found on Smart Dashboard
                //SmartDashboard.putData("Auto Position", m_autoChooser);
                SmartDashboard.putData("PathPlanner Auto Position", m_autoChooserPathPlanner);

                //Non Path Planner Auto Options
                // m_autoChooser.setDefaultOption("Starting in the Middle", getAutonomousCommandMiddle());
                // m_autoChooser.addOption("Starting on Right Side", getAutonomousCommandRight());
                // m_autoChooser.addOption("Starting on Left Side", getAutonomousCommandLeft());

                //Path Planner Auto Options
                m_autoChooserPathPlanner.addOption("Path Planner Option: Only Movement", getAutonomousPathPlanner());
                m_autoChooserPathPlanner.addOption("Left Side Ferry", new PathPlannerAuto("Auto Left Start Ferrying"));
                m_autoChooserPathPlanner.addOption("Left Side Shoot", new PathPlannerAuto("Auto Left Start Shooting"));
                
                
        }

        private void configureBindings() {
                // Note that X is defined as forward according to WPILib convention,
                // and Y is defined as to the left according to WPILib convention.
                m_robotDrive.setDefaultCommand(
                                // Drivetrain will execute this command periodically
                                m_robotDrive.applyRequest(() -> drive.withVelocityX(-Driver.getLeftY() * MaxSpeed) // Drive
                                                                                                                   // forward
                                                                                                                   // with
                                                                                                                   // negative
                                                                                                                   // Y
                                                                                                                   // (forward)
                                                .withVelocityY(-Driver.getLeftX() * MaxSpeed) // Drive left with
                                                                                              // negative X (left)
                                                .withRotationalRate(-Driver.getRightX() * MaxAngularRate) // Drive
                                                                                                          // counterclockwise
                                                                                                          // with
                                                                                                          // negative X
                                                                                                          // (left)
                                ));
                // Idle while the robot is disabled. This ensures the configured
                // neutral mode is applied to the drive motors while disabled.
                final var idle = new SwerveRequest.Idle();
                RobotModeTriggers.disabled().whileTrue(
                                m_robotDrive.applyRequest(() -> idle).ignoringDisable(true));
                Driver.a().whileTrue(m_robotDrive.applyRequest(() -> brake));
                Driver.b().whileTrue(m_robotDrive
                                .applyRequest(() -> point.withModuleDirection(
                                                new Rotation2d(-Driver.getLeftY(), -Driver.getLeftX()))));
                // Run SysId routines when holding back/start and X/Y.
                // Note that each routine should be run exactly once in a single log.
                Driver.back().and(Driver.y()).whileTrue(m_robotDrive.sysIdDynamic(Direction.kForward));
                Driver.back().and(Driver.x()).whileTrue(m_robotDrive.sysIdDynamic(Direction.kReverse));
                Driver.start().and(Driver.y()).whileTrue(m_robotDrive.sysIdQuasistatic(Direction.kForward));
                Driver.start().and(Driver.x()).whileTrue(m_robotDrive.sysIdQuasistatic(Direction.kReverse));


                //Operator's Controls

                //THIS CONTROL SHOULD BE THE ONE BEING USED -AZ
                //IDK IF THIS IS SAFE WITH THE LIMIT SWITCH BTW! DONT BREAK THE SWITCH /srs -AZ
                //Intake forward while Arm moves down
                //April 10 -> control not really needed as arm going up wasn't really an issue -AZ
                //Operator.a().onTrue(AutoCommands.Intake(intake, intakeArm,1.0 , 0.5));

                // INTAKE forward..? CHECK IF POSITIVE OR NEGATIVE -AZ
                //forward = negative :D  -JA
                Operator.b().onTrue(intake.spinIntake(-1)).onFalse(intake.spinIntake(0));

                // INTAKE reverse..? CHECK IF POSITIVE OR NEGATIVE -AZ
                Operator.x().onTrue(intake.spinIntake(1)).onFalse(intake.spinIntake(0));

                //manual intake arm with limits
                Operator.leftTrigger().onTrue(intakeArm.spinArmUp(0.4)).onFalse(intakeArm.spinArmUp(0));
                Operator.rightTrigger().onTrue(intakeArm.spinArmDown(0.4)).onFalse(intakeArm.spinArmDown(0));


                //Driver's Controls

                // index forward
                Driver.rightBumper().onTrue(index.spinIndex(0.5)).onFalse(index.spinIndex(0));

                // index reverse
                Driver.y().onTrue(index.spinIndexReverse(0.5)).onFalse(index.spinIndexReverse(0));

                // shooter short range -> Best used 5-6ft..? from Hub
                //Driver.leftTrigger().onTrue(shooter.spinShooterMotors(0.65)).onFalse(shooter.spinShooterMotors(0));
                Driver.leftTrigger().onTrue(AutoCommands.Shoot(shooter, index, .65, .5))
                                .onFalse(AutoCommands.Shoot(shooter, index, 0, 0));

                // shooter mid range -> Best used 8ft from Hub
                //Driver.leftBumper().onTrue(shooter.spinShooterMotors(0.75)).onFalse(shooter.spinShooterMotors(0));
                Driver.leftBumper().onTrue(AutoCommands.Shoot(shooter, index, .75, .5))
                                .onFalse(AutoCommands.Shoot(shooter, index, 0, 0));

                // shooter long range -> use for Ferrying
                //Driver.rightTrigger().onTrue(shooter.spinShooterMotors(0.80)).onFalse(shooter.spinShooterMotors(0));
                Driver.rightTrigger().onTrue(AutoCommands.Shoot(shooter, index, .8, .5))
                                .onFalse(AutoCommands.Shoot(shooter, index, 0, 0));
                
                //intake arm up -> added control for Driver autonomy
                 Driver.x().onTrue(intakeArm.spinArmUp(0.4)).onFalse(intakeArm.spinArmUp(0));

                
                // Driver.povUp().onTrue(AutoCommands.Shoot(shooter, index, .75, .5))
                //                 .onFalse(AutoCommands.Shoot(shooter, index, 0, 0));

                // Reset the field-centric heading on X button press.
                // Driver.x().onTrue(m_robotDrive.runOnce(m_robotDrive::seedFieldCentric));

                m_robotDrive.registerTelemetry(logger::telemeterize);

                //Commands for PathPlanner

                //Adjust Values to fit -AZ
                NamedCommands.registerCommand("Intake Start", AutoCommands.Intake(intake, intakeArm, 1, 0.2));
                NamedCommands.registerCommand("Intake End", AutoCommands.Intake(intake, intakeArm, 0, 0));

                NamedCommands.registerCommand("Spin Intake Arm Up/Down", intakeArm.autoSpinArm(0.5));

                NamedCommands.registerCommand("First Shoot Run", AutoCommands.Shoot(shooter, index, 0.75, .25));
                NamedCommands.registerCommand("Shoot Close Run", AutoCommands.Shoot(shooter, index, 0.65, .25));
                
                NamedCommands.registerCommand("Shoot End", AutoCommands.Shoot(shooter, index, 0, 0));

                NamedCommands.registerCommand("Ferrying Run", AutoCommands.Shoot(shooter, index, 0.8, 0.25));
                NamedCommands.registerCommand("Ferrying End", AutoCommands.Shoot(shooter, index, 0., 0));

                

        }        

        //returns the selected path planner auto command
        //this method HAS to be used inside the Robot class (autonomousInIt) for it to work -AZ
        public Command getPathPlannerAuto(){
                return m_autoChooserPathPlanner.getSelected();
        }

        public Command getAutonomousPathPlanner() {
                // This is pulling a path that will make the command go Forward and then back.
                return new PathPlannerAuto("BackAndForth");
        }

        public Command getAutonomousCommandLeft() {

                return Commands.sequence(
                                m_robotDrive.applyRequest(() -> {
                                        return drive.withVelocityX(-2)
                                                        .withVelocityY(0)
                                                        .withRotationalRate(0);
                                })
                                                .withTimeout(3.0),

                                m_robotDrive.applyRequest(() -> {
                                        return drive.withVelocityX(0)
                                                        .withVelocityY(0)
                                                        .withRotationalRate(-0.48);
                                })
                                                .withTimeout(2.0),
                                m_robotDrive.applyRequest(() -> {
                                        return drive.withVelocityX(0)
                                                        .withVelocityY(0)
                                                        .withRotationalRate(0);
                                }),

                                // Activate Shooter and Index
                                AutoCommands.Shoot(shooter, index, .75, .25)
                                );

        }

        public Command getAutonomousCommandRight() {

                return Commands.sequence(
                                m_robotDrive.applyRequest(() -> 
                                        drive.withVelocityX(-2)
                                                        .withVelocityY(0)
                                                        .withRotationalRate(0)
                                )
                                                .withTimeout(3.0),

                                m_robotDrive.applyRequest(() -> drive.withVelocityX(0)
                                                .withVelocityY(0)
                                                .withRotationalRate(1.48))
                                                .withTimeout(2.0),
                                m_robotDrive.applyRequest(() -> drive.withVelocityX(0)
                                                        .withVelocityY(0)
                                                        .withRotationalRate(0))
                                                        .withTimeout(1.0),

                                // Activate Shooter and Index
                                AutoCommands.Shoot(shooter, index, .75, .25)
                                );

        }

        public Command getAutonomousCommandMiddle() {

                return Commands.sequence(
                                m_robotDrive.applyRequest(() -> drive.withVelocityX(-2)
                                                .withVelocityY(0)
                                                .withRotationalRate(0))
                                                .withTimeout(1.3),
                                m_robotDrive.applyRequest(() -> drive.withVelocityX(0)
                                                                .withVelocityY(0)
                                                                .withRotationalRate(0)).withTimeout(0.1),

                                // Activate Shooter and Index
                                AutoCommands.Shoot(shooter, index, .75, .25)
                                );

        }
}
