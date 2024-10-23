package org.firstinspires.ftc.teamcode.robotverticalslides.HorizontalSlide;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.robotverticalslides.constants.ConfigConstants;

public class HorizontalSlideActions {

    public DcMotorEx HorizontalSlide1 = null;
    //public DcMotorEx HorizontalSlide2 = null;
    private Telemetry telemetry;

    public HorizontalSlideActions(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        HorizontalSlide1 = hardwareMap.get(DcMotorEx.class, ConfigConstants.HORIZONTAL_SLIDE1);
        HorizontalSlide1.setDirection(DcMotorSimple.Direction.FORWARD);
        HorizontalSlide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HorizontalSlide1.setTargetPosition(0);
        HorizontalSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

//        HorizontalSlide2 = hardwareMap.get(DcMotorEx.class, ConfigConstants.HORIZONTAL_SLIDE2);
//        HorizontalSlide2.setDirection(DcMotorSimple.Direction.REVERSE);
//        HorizontalSlide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        HorizontalSlide2.setTargetPosition(0);
//        HorizontalSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void resetSlide() {
        HorizontalSlide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        HorizontalSlide1.setTargetPosition(0);
        HorizontalSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

//        HorizontalSlide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        HorizontalSlide2.setTargetPosition(0);
//        HorizontalSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    double prevTime = System.currentTimeMillis();

    public void teleOpHorizontalSlide(double power, double liftSpeedMultiplier) { //  controls the lifty uppy (viper slides) which is being extended and retracted
        double time = System.currentTimeMillis();
        if (power != 0) {
            if (HorizontalSlide1.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
                HorizontalSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                HorizontalSlide1.setPower(1.0);

//                HorizontalSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//                HorizontalSlide2.setPower(1.0);
            }
//            double time = System.currentTimeMillis();

            double total = SlidePosition + power * (time - prevTime) * liftSpeedMultiplier;
            total = Range.clip(total, -3000, 100);
            setSlidePosition((int) total, 3000 * liftSpeedMultiplier);
//            prevTime = time;
            RobotLog.dd("LiftyUppy", "Target Position %f, time %f", SlidePosition, time);
        }
        prevTime = time;
        telemetry.addData("target position", SlidePosition);
        telemetry.addData("liftyPower", HorizontalSlide1.getPower());
        telemetry.addData("liftyCurrent mA", HorizontalSlide1.getCurrent(CurrentUnit.MILLIAMPS));

        double maxCurrent = 0;

        if (HorizontalSlide1.getCurrent(CurrentUnit.MILLIAMPS) > maxCurrent) {
            maxCurrent = HorizontalSlide1.getCurrent(CurrentUnit.MILLIAMPS);
        }

        telemetry.addData("liftyMax mA", maxCurrent);
        telemetry.addData("current position", HorizontalSlide1.getCurrentPosition());
    }

//    boolean downTo1 = false;
//    int preset1 = 0;
//    int preset2 = -1100;
//    int preset3 = -1500;
//    int preset4 = -2500;
//
//    public void goToPreset(boolean bottomRung, boolean bottomBasket, boolean topRung, boolean topBasket) {
//        if ((bottomRung || bottomBasket || topRung) && HorizontalSlide.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
//            HorizontalSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//            HorizontalSlide.setPower(1.0);
//        }
//        if (bottomRung) {
//            setLiftyUppyPosition(preset1, 1800);
//            downTo1 = true;
//            if (bottomBasket) {
//                setLiftyUppyPosition(preset2, 2500);
//            } else if (topRung) {
//                setLiftyUppyPosition(preset3, 2500);
//            } else if (topBasket) {
//                setLiftyUppyPosition(preset4, 2500);
//            }
//        }
//    }

    double SlidePosition = 0;

    public void setSlidePosition(int position, double velocity) {
        HorizontalSlide1.setTargetPosition(position);
        HorizontalSlide1.setVelocity(velocity);

//        HorizontalSlide2.setTargetPosition(position);
//        HorizontalSlide2.setVelocity(velocity);
        SlidePosition = position;
    }
}
