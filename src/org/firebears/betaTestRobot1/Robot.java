package org.firebears.betaTestRobot1;

import static java.lang.Math.abs;

import java.io.PrintStream;

import org.firebears.util.CANTalon;
import org.firebears.util.RoboReport;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * Simple program to verify that we can load code into the roboRIO and 
 * that we can communicate to other parts of the robot.
 */
public class Robot extends IterativeRobot {

	Joystick joystick = null;
	CANTalon motor = null;
	PowerDistributionPanel pdp = null;
	DriverStation driverStation = null;
	PrintStream out = System.out;

	@Override
	public void robotInit() {
		
		// All robot components should be created in the robotInit method
		joystick = new Joystick(0) {
			public int getAxisCount() {
				// temporary hack to get around bug in Joystick class.
				int superCount = super.getAxisCount();
				return 5;
			}
		};
		motor = new CANTalon(2);
		driverStation = DriverStation.getInstance();
		pdp = new PowerDistributionPanel();

		// Programmatically clear out faults in the components
		motor.clearStickyFaults();
		pdp.clearStickyFaults();
		(new Compressor()).clearAllPCMStickyFaults();
		
		RoboReport.print(System.out);
	}

	@Override
	public void teleopPeriodic() {
		// Verify that we can send values to the motor controller
		double value = joystick.getY();
		if (abs(joystick.getX()) > 0.2 || joystick.getY() > 0.2 || joystick.getZ() > 0.2) {
			out.printf("\t::: value = %4.2f : current=%5.2f : %5.2f,%5.2f,%5.2f  %5.2f,%5.2f%n", value,
					motor.getOutputCurrent(), joystick.getX(), joystick.getY(), joystick.getZ(), joystick.getTwist(),
					joystick.getThrottle());
		}
		motor.set(value);
	}

}
