package frc.robot.mechanisms;

import org.team4159.commandsv3backport.command3.Command;
import org.team4159.commandsv3backport.command3.Mechanism;
import org.team4159.commandsv3backport.command3.button.CommandNiDsXboxController;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.MathUtil;

public class Drivetrain implements Mechanism {
    //Declare motor controllers
    private final SparkMax leftMotor = new SparkMax(1, MotorType.kBrushless);
    private final SparkMax rightMotor = new SparkMax(2, MotorType.kBrushless);
    private final SparkMaxConfig leftMotorConfig = new SparkMaxConfig();
    private final SparkMaxConfig rightMotorConfig = new SparkMaxConfig();

    // Constructor
    public Drivetrain() {
        // One motor has to spin in reverse for the robot to go straight
        leftMotorConfig.inverted(false);
        leftMotorConfig.smartCurrentLimit(40);
        leftMotorConfig.secondaryCurrentLimit(80);
        leftMotor.configure(leftMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

        rightMotorConfig.inverted(true);
        rightMotorConfig.smartCurrentLimit(40);
        rightMotorConfig.secondaryCurrentLimit(80);
        rightMotor.configure(rightMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    }
    private void drive(double leftSpeed, double rightSpeed) {
        leftMotor.set(leftSpeed);
        rightMotor.set(rightSpeed);
    }
    private void stop() {
        leftMotor.stopMotor();
        
        rightMotor.stopMotor();
    }
    public Command driveCommand(CommandNiDsXboxController controller) {
        return runRepeatedly(() -> {
                drive(MathUtil.applyDeadband(controller.getLeftY(), 0.1) * 0.5, MathUtil.applyDeadband(controller.getRightY(), 0.1) * 0.5);
        })
        .whenExited(this::stop)
        .named("Drive");
    }

    public Command autoDriveCommand(double leftSpeed, double rightSpeed) {
        return run(coroutine -> {
            drive(leftSpeed, rightSpeed);
            coroutine.park();
        })
        .whenExited(this::stop)
        .named("Auto Drive");
    }
}

    
