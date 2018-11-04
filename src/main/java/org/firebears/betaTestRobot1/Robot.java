package org.firebears.betaTestRobot1;

import java.io.PrintStream;

import org.firebears.util.RobotReport;

import java.io.File;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Simple program to verify that we can load code into the roboRIO and that we
 * can communicate to other parts of the robot.
 */
public class Robot extends TimedRobot {

	public static final boolean DEBUG = true;
	public static final int MOTOR_CAN_ID = 2;

	Joystick joystick = null;
	SpeedController motor = null;
	PowerDistributionPanel pdp = null;
	DriverStation driverStation = null;
	PrintStream out = System.out;
	AHRS navXBoard = null;

	@Override
	public void robotInit() {

		RobotReport report = new RobotReport("betaTestRobot1");
		report.setDescription("IterativeRobot for testing the 2018 beta software.");

		// All robot components should be created in the robotInit method
		joystick = new Joystick(0);
		report.addJoystick(0, "Main Joystick", joystick);

		motor = new WPI_TalonSRX(MOTOR_CAN_ID);
		((WPI_TalonSRX)motor).setName("Motor");
		report.addCAN(MOTOR_CAN_ID, "Motor", motor);

		// try {
		// 	navXBoard = new AHRS(Port.kUSB);
		// } catch (RuntimeException ex) {
		// 	DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
		// }

		driverStation = DriverStation.getInstance();
		pdp = new PowerDistributionPanel();
	    LiveWindow.disableTelemetry(pdp);

		// Programmatically clear out faults in the components
		pdp.clearStickyFaults();
		// (new Compressor()).clearAllPCMStickyFaults();

		 report.write(System.out);
		 report.write(new File(System.getProperty("user.home"), "robotReport.md"), DEBUG);
	}

	@Override
	public void robotPeriodic() {
		if (navXBoard!=null) {
			SmartDashboard.putNumber("navx angle", navXBoard.getAngle());
		}
		if (pdp!=null) {
			SmartDashboard.putNumber("pdp voltage", pdp.getVoltage());
		}
	}

	@Override
	public void teleopPeriodic() {
		// Verify that we can send values to the motor controller
		double value = joystick.getY();
		if (joystick.getTrigger()) {
			motor.stopMotor();
		} else {
			motor.set(value);
		}
		//  SmartDashboard.putNumber("NavX Angle", navXBoard.getAngle());
		 if ((++t)%20 == 0)  {
		//  System.out.println("angle=" + navXBoard.getAngle());
		 }
	}
	long t = 0;

}
