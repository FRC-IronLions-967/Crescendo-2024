// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Utils.Values;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class VisionSubsystem extends SubsystemBase {
  /** Creates a new VisionSubsystem. */

  private PhotonCamera aprilTagCamera;

  private PhotonPipelineResult result;
  private PhotonTrackedTarget speakerAimTarget;
  private boolean speakerAimTargetValid;
  private PhotonPoseEstimator visionPose;

  private static NavigableMap<Double,Double> speakerLookup = new TreeMap<Double,Double>();
  static { //pitch, angle
    speakerLookup.put(12.0,0.82);
    speakerLookup.put(8.5,0.793);
    speakerLookup.put(0.0, 0.755);
    speakerLookup.put(-1.8,0.73);
  }
  

  public VisionSubsystem() {
    aprilTagCamera = new PhotonCamera("April_Tag_Camera");
    //Cam mounted facing backward, 0.298 meters behind center, 0.58 meters up from center.
    Transform3d robotToCam = new Transform3d(new Translation3d(-0.298, 0.0, 0.58), new Rotation3d(0,0.349,Math.PI)); 

    visionPose = new PhotonPoseEstimator(AprilTagFields.k2024Crescendo.loadAprilTagLayoutField(), PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, robotToCam);
  }

  /**
   * Check if there is a speaker target to shoot at
   * @return if a target exists
   */
  public boolean hasShotTarget() {
    return speakerAimTargetValid;
  }

  /**
   * 
   * @return
   */
  public boolean isAimed() {
    return speakerAimTargetValid && Math.abs(speakerAimTarget.getYaw()) < 0.5;
  }

  /**
   * Computes shooter angle based on the speaker april tag location
   * @return PID reference point for the current target
   */
  public double getScorerPositionBasedOnPitch() {
    double maxPosition = Values.getInstance().getDoubleValue("kScorerMaxPosition");
    if (speakerAimTargetValid) {
      double pitch = speakerAimTarget.getPitch();
      Entry<Double,Double> highEntry = speakerLookup.higherEntry(pitch);
      Entry<Double,Double> lowEntry = speakerLookup.lowerEntry(pitch);

      //bounded linear regression between data lookup points
      if(highEntry != null && lowEntry != null) {
        return lowEntry.getValue() + ((pitch - lowEntry.getKey()) * (highEntry.getValue() - lowEntry.getValue()) / (highEntry.getKey() - lowEntry.getKey()));
      } else if ( highEntry == null) {
        return lowEntry.getValue();
      } else {
        return maxPosition;
      }
    } else {
      return maxPosition;
    }
  }  

  /**
   * 
   * @return yaw error to speaker target
   */
  public double getSpeakerYaw() {
    if (speakerAimTargetValid) {
      return speakerAimTarget.getYaw();
    } else {
      return 0.0;
    }
  }

  /**
   * Processes the input of the april tag camera pipeline to identify the speaker april tag
   */
  private void getSpeakerTarget() {
    //Get alliance
    var alliance = DriverStation.getAlliance();
    int shotTargetId = -1;
    if (alliance.isPresent()) {
      if(alliance.get() == DriverStation.Alliance.Blue) {
          shotTargetId = 8;
      } else {
          shotTargetId = 4;
      }
    }
    speakerAimTargetValid = false;
    if (result.hasTargets()) {
      List<PhotonTrackedTarget> targets = result.getTargets();
      PhotonTrackedTarget aimTarget = result.getBestTarget();
      for (java.util.Iterator<PhotonTrackedTarget> iter = targets.iterator(); iter.hasNext(); ) {
        aimTarget = iter.next();
        if (aimTarget.getFiducialId() == shotTargetId){
          speakerAimTarget = aimTarget;
          speakerAimTargetValid = true;
        }
      }
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    result = aprilTagCamera.getLatestResult();
    visionPose.update(result);
    getSpeakerTarget();
    
    if(speakerAimTargetValid) {
      SmartDashboard.putNumber("Pitch", speakerAimTarget.getPitch());
      SmartDashboard.putNumber("Yaw", speakerAimTarget.getYaw());
    }
    SmartDashboard.putBoolean("Has Target", hasShotTarget());
  }
}
