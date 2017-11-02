package org.firebears.util;

import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeSet;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class RoboReport {
	
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

	public static void print(PrintStream out) {
		out.printf("# RoboReport, as of %s%n", dateFormat.format(new Date()));
		out.println();
		
		out.println("## DriverStation");
		out.println();
		DriverStation driverStation = DriverStation.getInstance();
		out.printf("* voltage = %5.2f%n", driverStation.getBatteryVoltage());
		out.printf("* alliance = %s%n", driverStation.getAlliance());
		out.println();
		
		out.println("## Power Distribution Panel");
		out.println();
		PowerDistributionPanel pdp = new PowerDistributionPanel();
		out.printf("* voltage = %5.2f%n", pdp.getVoltage());
		out.printf("* current = %5.2f%n", pdp.getTotalCurrent());
		out.printf("* PDP temp = %5.2f%n", pdp.getTemperature());
		out.println();
		
//		out.println("## CAN");
//		out.println();
//		for (int deviceNumber = 1; deviceNumber < 63; deviceNumber++) {
//			Object device = testCAN(deviceNumber);
//			if (device != null) {
//				out.printf("* %d : %s%n", deviceNumber, device.toString());
//			}
//		}
//		out.println();
		
		out.println("## System Properties");
		out.println();
		for (String varName : (new TreeSet<String>(System.getenv().keySet()))) {
			if (varName.equals("line.separator")) continue;
			out.printf("* %s = %s%n", varName, System.getenv(varName));
		}
		out.println();
		
		out.println("## Environment Variables");
		out.println();
		for (String varName : (new TreeSet<String>(System.getenv().keySet()))) {
			out.printf("* %s = %s%n", varName, System.getenv(varName));
		}
		out.println();
		
		out.println("## Java Runtime");
		out.println();
		out.printf("* availableProcessors : %d%n", Runtime.getRuntime().availableProcessors());
		out.printf("* totalMemory : %d%n", Runtime.getRuntime().totalMemory());
		out.printf("* maxMemory : %d%n", Runtime.getRuntime().maxMemory());
		out.printf("* freeMemory : %d%n", Runtime.getRuntime().freeMemory());
		out.println();
		
		out.println("## Java classpath");
		out.println();
		URLClassLoader cl = (URLClassLoader)ClassLoader.getSystemClassLoader();
		for (URL url : cl.getURLs())  {
			out.printf("* %s%n", url.getFile());
		}
		out.println();
		
		out.flush();
	}
	
	private static Object testCAN(int deviceNumber)   {
		try {
			return new CANTalon(deviceNumber);
		} catch (Exception e) {
			return null;
		}
	}
}
