package frc.robot.mechanisms;

import org.team4159.commandsv3backport.command3.Command;
import org.team4159.commandsv3backport.command3.Mechanism;
import org.team4159.commandsv3backport.command3.button.CommandNiDsXboxController;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

public class Arm implements Mechanism {

   private final SparkMax armMotor = new SparkMax(3, MotorType.kBrushless);
   private final SparkMaxConfig armMotorConfig = new SparkMaxConfig();

   public Arm() {
    armMotorConfig.inverted(false);
    armMotorConfig.smartCurrentLimit(40);
    armMotorConfig.secondaryCurrentLimit(80);
    armMotor.configure(armMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
   }

   private void lift(double armSpeed) {
    armMotor.set(armSpeed);
   }
   private void stop() {
    armMotor.stopMotor();
   }

   public Command liftCommand(double speed) {
    return run(coroutine -> {
        lift(speed);
        coroutine.park();
    })
    .whenExited(this::stop)
    .named("Lift Arm");
   }
}
