// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Utils.Values;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class VisionSubsystem extends SubsystemBase {
  /** Creates a new VisionSubsystem. */

  private PhotonCamera aprilTagCamera;

  private double pitch;
  private double yaw;
  private PhotonPipelineResult result;

  public VisionSubsystem() {
    
    aprilTagCamera = new PhotonCamera("April_Tag_Camera");
  }

  public boolean lookForTargets() {
    setResult();
    return result.hasTargets();
  }

  public double getPitch() {
    lockOntoTarget();
    return pitch;
  }

  public double getYaw() {
    lockOntoTarget();
    return yaw;
  }

  public void setResult() {
    result = aprilTagCamera.getLatestResult();
  }

  public void lockOntoTarget() {
    setResult();
    pitch = 0.0;
    if (result.hasTargets()) {
      List<PhotonTrackedTarget> targets = result.getTargets();
      PhotonTrackedTarget aimTarget = result.getBestTarget();
      for (java.util.Iterator<PhotonTrackedTarget> iter = targets.iterator(); iter.hasNext(); ) {
        aimTarget = iter.next();
        if (aimTarget.getFiducialId() == 8 || aimTarget.getFiducialId() == 4){
          break;
        }
      }
      pitch = aimTarget.getPitch();
      yaw = aimTarget.getYaw();
    }
  }

  public boolean isAimed() {
    return Math.abs(yaw) < 0.5;
  }

  public double getScorerPositionBasedOnPitch() {
    getPitch();
    if (pitch > 12) {
      return 0.82;
    } else if (pitch > 8.5) {
     return 0.793;
    } else if (pitch > 0.0) {
     return 0.785;
    } else if (pitch > -1.8) {
     return 0.77;
    }
    else {
      return Values.getInstance().getDoubleValue("kScorerMaxPosition");
    }
  }  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

   


    SmartDashboard.putNumber("Pitch", pitch);
    SmartDashboard.putNumber("Yaw", yaw);
    SmartDashboard.putBoolean("Has Target", lookForTargets());
  }
}
