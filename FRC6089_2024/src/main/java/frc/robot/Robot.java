// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// Not using PWM: import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

// View > Command Pallette > WpiLib: Manage Vendor Libs > Install New Libs (online)
// https://docs.revrobotics.com/brushless/spark-max/revlib#java-api-documentation
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
  private final CANSparkMax m_leftDriveFront = new CANSparkMax(1, MotorType.kBrushed);
  private final CANSparkMax m_leftDriveRear = new CANSparkMax(2, MotorType.kBrushed);
  private final CANSparkMax m_rightDriveFront = new CANSparkMax(3, MotorType.kBrushed);
  private final CANSparkMax m_rightDriveRear = new CANSparkMax(4, MotorType.kBrushed);

  private final DifferentialDrive m_robotDrive =
      new DifferentialDrive(m_leftDriveFront::set, m_rightDriveFront::set);
  private final XboxController m_controller = new XboxController(0);
  private final Timer m_timer = new Timer();

  public Robot() {
    SendableRegistry.addChild(m_robotDrive, m_leftDriveFront);
    SendableRegistry.addChild(m_robotDrive, m_rightDriveFront);
  }

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_rightDriveFront.setInverted(true);
    
    // Make the rear motors follow the fronts
    m_leftDriveRear.follow(m_leftDriveFront);
    m_rightDriveRear.follow(m_rightDriveFront);
  }

  /** This function is run once each time the robot enters autonomous mode. */
  @Override
  public void autonomousInit() {
    m_timer.restart();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    // Drive for 2 seconds
    if (m_timer.get() < 2.0) {
      // Drive forwards half speed, make sure to turn input squaring off
      m_robotDrive.arcadeDrive(0.5, 0.0, false);
    } else {
      m_robotDrive.stopMotor(); // stop robot
    }
  }

  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during teleoperated mode. */
  @Override
  public void teleopPeriodic() {
    m_robotDrive.arcadeDrive(-m_controller.getLeftY(), -m_controller.getRightX());
  }

  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
