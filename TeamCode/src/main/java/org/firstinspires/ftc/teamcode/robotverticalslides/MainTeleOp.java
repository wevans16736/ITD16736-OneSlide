
package org.firstinspires.ftc.teamcode.robotverticalslides;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robotverticalslides.HorizontalSlide.HorizontalIntakeActions;
import org.firstinspires.ftc.teamcode.robotverticalslides.HorizontalSlide.HorizontalSlideActions;
import org.firstinspires.ftc.teamcode.robotverticalslides.HorizontalSlide.HorizontalWristActions;
import org.firstinspires.ftc.teamcode.robotverticalslides.VerticalSlide.VerticalGrabberActions;
import org.firstinspires.ftc.teamcode.robotverticalslides.VerticalSlide.VerticalSlideActions;
import org.firstinspires.ftc.teamcode.robotverticalslides.VerticalSlide.VerticalWristActions;

@TeleOp(name = "Tele Op 2 Slide Robot", group = "Linear Opmode")
public class MainTeleOp extends HelperActions {
    private DcMotor slide = null;
    private DriveActions driveActions = null;
//    private HorizontalSlideActions horizontalSlide = null;
//    private HorizontalWristActions horizontalWrist = null;
//    private HorizontalIntakeActions horizontalIntake = null;
//    private VerticalSlideActions verticalSlide = null;
//    private VerticalWristActions verticalWrist = null;
//    private VerticalGrabberActions verticalGrabber = null;


    boolean correctRotation = false;
    double rotationPosition = 0;
    double rotation = 0;
    double liftSpdMult = 0.8 ;
    double slideMax = -3000;
    double slideMin = 0;

    @Override
    public void runOpMode() {

        driveActions = new DriveActions(telemetry, hardwareMap);
        slide  = hardwareMap.get(DcMotor.class, "slide");
        slide.setDirection(DcMotorSimple.Direction.FORWARD);
        //slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.getCurrentPosition();

        telemetry.addData("Slide Encoder Tick", slide.getCurrentPosition());

//        horizontalSlide = new HorizontalSlideActions(hardwareMap, telemetry);
        Servo supportSlide = hardwareMap.get(Servo.class, "supportSlide");
//        horizontalWrist = new HorizontalWristActions(telemetry, hardwareMap);
//       horizontalIntake = new HorizontalIntakeActions(telemetry, hardwareMap);
//        verticalSlide = new VerticalSlideActions(hardwareMap, telemetry);
//        verticalWrist = new VerticalWristActions(telemetry, hardwareMap);
//        verticalGrabber = new VerticalGrabberActions(telemetry, hardwareMap);

        supportSlide.setPosition(.5);
        //Set Speed for teleOp. Mecannum wheel speed.
        //driveActions.setSpeed(1.0);

        // Wait for the game to start (driver presses PLAY)

        waitForStart();
        driveActions.setPowerMax();
        driveActions.drive(0,0,0);
        sleep(500);
        while (opModeIsActive()) {

            /** Gamepad 1 **/

            driveActions.drive(
                    (gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x)),      //joystick controlling strafe
                    (-gamepad1.left_stick_y * Math.abs(gamepad1.left_stick_y)),     //joystick controlling forward/backward
                    driveStraight(gamepad1.right_stick_x));    //joystick controlling rotation
            telemetry.addData("Left stick x", gamepad1.left_stick_x);
            telemetry.addData("left stick y", gamepad1.left_stick_y);
            telemetry.addData("right stick x", gamepad1.right_stick_x);

            telemetry.addData("Joystick", gamepad2.left_stick_y);

            changeSpeed(driveActions, gamepad1.dpad_up, gamepad1.dpad_down, false, false);
            toggleSpeed(gamepad1.a);


            /** Gamepad 2 **/
            //horizontalSlide.teleOpHorizontalSlide(gamepad2.left_stick_y, 1400);
//            horizontalWrist.flipping(gamepad2.left_bumper);
//            horizontalIntake.intake(gamepad2.right_trigger);
//            horizontalIntake.outtake(gamepad2.left_trigger);

//            verticalSlide.goToPreset(gamepad2.dpad_left, gamepad2.dpad_down, gamepad2.dpad_right, gamepad2.dpad_up);
//            verticalSlide.setOnRung(gamepad2.a);
//            verticalWrist.flipping(gamepad2.right_bumper);
//            verticalGrabber.teleOp(gamepad2.y, gamepad2.x);
            slide.setPower(0);

            setSlide();

            telemetry.addData("Slide Encoder Tick", slide.getCurrentPosition());
            telemetry.update();





            if(gamepad2.a) {
                supportSlide.setPosition(.75);
                //wait(5000);
            }
            else if(gamepad2.b){
                supportSlide.setPosition(.5);

            }

        }

        telemetry.addData("[ROBOTNAME] ", "Going");
        telemetry.update();
        idle();

    }

    private void setSlide() {
        if(gamepad2.left_stick_y < 0){
            if(slide.getCurrentPosition() > slideMax) {
                slide.setPower(gamepad2.left_stick_y);
            }
        }
        else if(gamepad2.left_stick_y > 0){
            if(slide.getCurrentPosition() < slideMin) {
                slide.setPower(gamepad2.left_stick_y);
            }
        }
        else{
            slide.setPower(0);
        }
    }

    // Code to make it drive straight
    double gainMultiplier = 0.0005;
    private double driveStraight(double rightStickX) {
        if(Math.abs(rightStickX) > 0.01){ // Only correct position when not rotating
            rotation = rightStickX * Math.abs(rightStickX); // Rotating voluntarily
            correctRotation = false;
        } else if (!correctRotation){ // If not rotating, get the position rotationally once when the turn is done
            if (!isTurning()) {
                correctRotation = true;
                rotationPosition = driveActions.getRawHeading();
            }
            rotation = 0;
        } else { // Correct rotation when not turning
            double gain = driveActions.leftFront.getVelocity() + driveActions.rightFront.getVelocity() + driveActions.leftRear.getVelocity() + driveActions.rightRear.getVelocity();
            rotation = -driveActions.getSteeringCorrection(rotationPosition, gain * gainMultiplier);
        }
        return rotation;
//        return  rightStickX * Math.abs(rightStickX);
    }
    private boolean isTurning() {
        return Math.abs((driveActions.leftFront.getVelocity() + driveActions.leftRear.getVelocity()) - (driveActions.rightFront.getVelocity() + driveActions.rightRear.getVelocity())) < 1;
    }
}
