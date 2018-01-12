package org.firebears.util;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * Extend {@link TalonSRX} to better implement the 2017 API.
 * 
 * @author Keith Rieck
 */
public class CANTalon extends WPI_TalonSRX {

  private final int timeoutMs = 10;
  private ControlMode controlMode = null;
  private int encoderMultiplier = 1;
  private int pidIdx = 0;

  public CANTalon(int deviceNumber) {
    super(deviceNumber);
  }

  public void changeControlMode(ControlMode talonControlMode) {
    this.controlMode = talonControlMode;
  }
  
  public void clearStickyFaults() {
    this.clearStickyFaults(timeoutMs);
  }

  public void configEncoderCodesPerRev(int ticks) {
    this.encoderMultiplier = ticks;
  }

  public void enable() {
    set(controlMode, get());
  }

  public void enableBrakeMode(boolean brakeEnabled) {
    setNeutralMode(brakeEnabled ? NeutralMode.Brake : NeutralMode.Coast);
  }

  /**
   * @return Position of selected sensor (in Raw Sensor Units).
   */
  public int getSelectedSensorPosition() {
    return this.getSelectedSensorPosition(pidIdx);
  }

  /**
   * @return Velocity of selected sensor (in Raw Sensor Units per 100 ms).
   */
  public int getSelectedSensorVelocity() {
    return this.getSelectedSensorVelocity(pidIdx);
  }

  public String getSmartDashboardType() {
    return "Speed Controller";
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("Speed Controller");
    builder.setSafeState(() -> disable());
    builder.addDoubleProperty("Value", () -> get(), (value) -> set(value));
  }

  public void reverseSensor(boolean isReversed) {
    this.setSensorPhase(isReversed);
  }

  @Override
  public void set(double speed) {
    ControlMode mode = (controlMode != null) 
        ? controlMode
        : (encoderMultiplier > 1.0 ? ControlMode.Velocity : ControlMode.PercentOutput);
    super.set(mode, speed * encoderMultiplier);
  }

  public void setFeedbackDevice(FeedbackDevice feedbackDevice) {
    this.configSelectedFeedbackSensor(feedbackDevice, pidIdx, timeoutMs);
  }

  public void setPID(double pidP, double pidI, double pidD, double pidF, int pidIZone,
      double pidRampRate, int slotIdx) {
    this.config_kP(slotIdx, pidP, timeoutMs);
    this.config_kI(slotIdx, pidI, timeoutMs);
    this.config_kD(slotIdx, pidD, timeoutMs);
    this.config_kF(slotIdx, pidF, timeoutMs);
    this.config_IntegralZone(slotIdx, pidIZone, timeoutMs);
    this.configClosedloopRamp(pidRampRate, timeoutMs);
    this.selectProfileSlot(slotIdx, pidIdx);
  }

  @Override
  public String toString() {
    return "CANTalon(" + getDeviceID() + (getSubsystem() != null ? "," + getSubsystem() : "")
        + (getName() != null ? "," + getName() : "") + ")";
  }
}
