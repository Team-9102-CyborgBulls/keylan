// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  XboxController xbox_k = new XboxController(0);
  WPI_TalonSRX leftmotorfront = new WPI_TalonSRX(4);
 WPI_TalonSRX leftmotorfollow = new WPI_TalonSRX(3);
 WPI_TalonSRX rightmotorfollow = new WPI_TalonSRX(2);
 WPI_TalonSRX rightmotorfront = new WPI_TalonSRX(1);
 MotorControllerGroup m_left = new  MotorControllerGroup(leftmotorfront,leftmotorfollow);
 MotorControllerGroup m_right = new  MotorControllerGroup(rightmotorfront,rightmotorfollow);
 DifferentialDrive m_RobotDrive = new DifferentialDrive(m_left, m_right);
 CANSparkMax m_ArmMotor = new CANSparkMax(6, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless);
 CANSparkMax intakemotor = new CANSparkMax(7, com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushed);
 Compressor phCompressor = new Compressor(10, PneumaticsModuleType.REVPH);
DoubleSolenoid doublePH = new DoubleSolenoid(10, PneumaticsModuleType.REVPH, 1, 2);
//RelativeEncoder arm_encoder = m_ArmMotor.getEncoder();
DutyCycleEncoder arm_encoder = new DutyCycleEncoder(1);
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    m_ArmMotor.enableVoltageCompensation(8);

    m_ArmMotor.setIdleMode(IdleMode.kBrake);
    intakemotor.setIdleMode(IdleMode.kBrake);
    arm_encoder.reset();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("right trigger", xbox_k.getRightTriggerAxis());
    SmartDashboard.putNumber("left trigger", xbox_k.getLeftTriggerAxis());
    double outpout = arm_encoder.get();
    
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    //valeur
    double intakepower;
    double armpower;
    double speed = xbox_k.getRightX();
    double turn = xbox_k.getLeftY();
    m_RobotDrive.setMaxOutput(0.6);
    //base pilotable
    m_RobotDrive.arcadeDrive (speed,turn);

    //bras principale
    if(xbox_k.getXButton() == true ){

      armpower = -0.7;

    }

    else if(xbox_k.getAButton() == true){

      armpower = 0.4;

    }
    else{
      armpower = 0;
    }

    m_ArmMotor.set(armpower);
    //bras intake
    if(xbox_k.getYButton() == true){

      intakepower = -0.7;
    }
      else if(xbox_k.getBButton() == true){

        intakepower = 0.5;

      }
      else{
        intakepower = 0;
      }
      intakemotor.set(intakepower);
    //intake
    if(xbox_k.getRightBumper()== true){
      doublePH.set(Value.kForward);
    }else{
      doublePH.set(Value.kReverse);
    }
    
  }
  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}

//il n'y a plus rien passer cette ligne