package org.firebears.betaTestRobot1;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

import static java.lang.Math.abs;

import org.firebears.util.CANTalon;

import com.ctre.phoenix.MotorControl.CAN.TalonSRX;

public class Robot extends IterativeRobot {

	Joystick joystick = null;
	CANTalon motor = null;
	PowerDistributionPanel pdp = null;
	DriverStation driverStation = null;

	@Override
	public void robotInit() {
		joystick = new Joystick(0) {
			public int getAxisCount() {
				return 5;
			}
		};
		motor = new CANTalon(2);
		motor.clearStickyFaults();
		driverStation = DriverStation.getInstance();
		pdp = new PowerDistributionPanel();
		pdp.clearStickyFaults();
		(new Compressor()).clearAllPCMStickyFaults();
		System.out.println("::: robotInit : " + this.getClass());
	}

	@Override
	public void teleopInit() {
		System.out.println("::: alliance = " + driverStation.getAlliance());
		System.out.println("::: voltage = " + pdp.getVoltage());
		System.out.println("::: current = " + pdp.getTotalCurrent());
		System.out.println("::: PDP temp = " + pdp.getTemperature());
		System.out.println("::: motor temp = " + motor.getTemperature());
		System.out.println(":::");
	}

	@Override
	public void teleopPeriodic() {
		double value = joystick.getY();
		if (abs(value) > 0.2) {
			System.out.println("\t::: value = " + value + " : " + motor.getOutputCurrent());
		}
		motor.set(value);
	}

}
