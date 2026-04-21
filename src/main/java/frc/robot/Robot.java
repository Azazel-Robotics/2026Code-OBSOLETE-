// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.HootAutoReplay;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Index;

public class Robot extends TimedRobot {
    private Command m_autonomousCommand;

    private final RobotContainer m_robotContainer;

    /* log and replay timestamp and joystick data */
    private final HootAutoReplay m_timeAndJoystickReplay = new HootAutoReplay()
        .withTimestampReplay()
        .withJoystickReplay();

    public Robot() {
        m_robotContainer = new RobotContainer();
    }

    @Override
    public void robotInit() {
        SmartDashboard.putData(CommandScheduler.getInstance());
    }

    @Override
    public void robotPeriodic() {
        m_timeAndJoystickReplay.update();
        CommandScheduler.getInstance().run(); 
    }

    @Override
    public void disabledInit() {
        SmartDashboard.putString("Auto State", "");
    }

    @Override
    public void disabledPeriodic() {}

    @Override
    public void disabledExit() {}

    @Override
    public void autonomousInit() {
        DriverStation.reportError("Report::autonomousInit: Started!", isAutonomous());

        //Calls the Selected Manual Auto
        // m_autonomousCommand = m_robotContainer.getSelectedAuto();
        
        //Calls the Selected Path Planner Auto
        m_autonomousCommand = m_robotContainer.getPathPlannerAuto();

        if (m_autonomousCommand != null) {
            CommandScheduler.getInstance().schedule(m_autonomousCommand);
            SmartDashboard.putString("Auto State", "Start");
        }
       
    }

    @Override
    public void autonomousPeriodic() {
        //DriverStation.reportError("Report::autonomousPeriodic: Periodic()", isAutonomous());
        //SmartDashboard.putString("Auto State", "running");
    }

    @Override
    public void autonomousExit() {
        DriverStation.reportWarning("Report::autonomousExit: Done", isAutonomous());
        SmartDashboard.putString("Auto State", "end");
    }

    @Override
    public void teleopInit() {
        if (m_autonomousCommand != null) {
            CommandScheduler.getInstance().cancel(m_autonomousCommand);
        }
        
        Shooter.getInstance().spinShooterZero();
        Index.getInstance().spinIndexZero();
        SmartDashboard.putString("Auto State", "End");
    }

    @Override
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {}

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {}

    @Override
    public void testExit() {}

    @Override
    public void simulationInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void simulationPeriodic() {}
}
