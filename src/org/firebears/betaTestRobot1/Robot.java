package org.firebears.betaTestRobot1;

import static java.lang.Math.abs;

import java.io.PrintStream;
import java.util.TreeSet;

import org.firebears.util.CANTalon;

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
		
		// Cause the roboRIO to print out a lot of things about it's JRE
		out.println("::: robotInit : " + this.getClass());
		out.printf(":::     availableProcessors : %d%n", Runtime.getRuntime().availableProcessors());
		out.printf(":::     totalMemory : %d%n", Runtime.getRuntime().totalMemory());
		out.printf(":::     maxMemory : %d%n", Runtime.getRuntime().maxMemory());
		out.printf(":::     freeMemory : %d%n", Runtime.getRuntime().freeMemory());
		for (String propName : (new TreeSet<String>(System.getProperties().stringPropertyNames()))) {
			if (propName.equals(" line.separator"))  continue;
			out.printf(":::     %s : %s%n", propName, System.getProperty(propName));
		}
		for (String varName : (new TreeSet<String>(System.getenv().keySet()))) {
			out.printf(":::     %s : %s%n", varName, System.getenv(varName));
		}
	}

	@Override
	public void teleopInit() {
		// Verify that we can connect to components and to the driver's station
		out.printf("::: alliance = %s%n", driverStation.getAlliance());
		out.printf("::: voltage = %5.2f%n", pdp.getVoltage());
		out.printf("::: current = %5.2f%n", pdp.getTotalCurrent());
		out.printf("::: PDP temp = %5.2f%n", pdp.getTemperature());
		out.printf("::: motor temp = %5.2f%n", motor.getTemperature());
		out.println(":::");
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
