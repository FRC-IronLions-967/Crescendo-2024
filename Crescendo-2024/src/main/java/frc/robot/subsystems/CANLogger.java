// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.Enumeration;
import java.util.Hashtable;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkBase.FaultID;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CANLogger extends SubsystemBase {

  private int rateLimiter;
  private final int rateLimit = 50;

  private final boolean outputEnabled = false;
  private Hashtable<Integer, CANSparkBase> sparkList = new Hashtable<Integer, CANSparkBase>(13);

  /** Creates a new CANLogger. */
  public CANLogger() {

  }

  public void registerSpark(int canId, CANSparkBase spark) {
    if (outputEnabled) {
      sparkList.put(canId, spark);
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (outputEnabled) {
      if (rateLimiter == 0) {
        String[] statusArray = new String[13];
        for (Enumeration<CANSparkBase> e = sparkList.elements(); e.hasMoreElements();) {
          CANSparkBase spark = e.nextElement();
          int canId = spark.getDeviceId();
          statusArray[canId - 1] = "Spark " + Integer.toString(canId) + ": TX / RX Fault " + Boolean.toString(spark.getFault(FaultID.kCANTX)) + " / " + Boolean.toString(spark.getFault(FaultID.kCANRX));

        }
        SmartDashboard.putStringArray("CAN_Status", statusArray);
      }
      rateLimiter = (rateLimiter + 1) % rateLimit;
    }
  }
}
