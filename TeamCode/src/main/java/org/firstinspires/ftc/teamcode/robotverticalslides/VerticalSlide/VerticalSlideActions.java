package org.firstinspires.ftc.teamcode.robotverticalslides.VerticalSlide;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.robotverticalslides.constants.ConfigConstants;

public class VerticalSlideActions {

    public DcMotorEx VerticalSlide1 = null;
    public DcMotorEx VerticalSlide2 = null;
    private Telemetry telemetry;

    public VerticalSlideActions(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        VerticalSlide1 = hardwareMap.get(DcMotorEx.class, ConfigConstants.VERTICAL_SLIDE1);
        VerticalSlide1.setDirection(DcMotorSimple.Direction.REVERSE);
        VerticalSlide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        VerticalSlide1.setTargetPosition(0);
        VerticalSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        VerticalSlide2 = hardwareMap.get(DcMotorEx.class, ConfigConstants.VERTICAL_SLIDE2);
        VerticalSlide2.setDirection(DcMotorSimple.Direction.FORWARD);
        VerticalSlide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        VerticalSlide2.setTargetPosition(0);
        VerticalSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void resetSlide() {
        VerticalSlide1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        VerticalSlide1.setTargetPosition(0);
        VerticalSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        VerticalSlide2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        VerticalSlide2.setTargetPosition(0);
        VerticalSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    double prevTime = System.currentTimeMillis();

    public void teleOpVerticalSlide(double power, double liftSpeedMultiplier, boolean cancel) { //  controls the lifty uppy (viper slides) which is being extended and retracted
        double time = System.currentTimeMillis();
        if (power != 0) {
            if (VerticalSlide1.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
                VerticalSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                VerticalSlide1.setPower(1.0);

                VerticalSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                VerticalSlide2.setPower(1.0);
            }
//            double time = System.currentTimeMillis();

            double total = SlidePosition + power * (time - prevTime) * liftSpeedMultiplier;
            if (!cancel) {
                total = Range.clip(total, -3000, 100);
            }
            setSlidePosition((int) total, 3000 * liftSpeedMultiplier);
//            prevTime = time;
            RobotLog.dd("LiftyUppy", "Target Position %f, time %f", SlidePosition, time);
        }
        prevTime = time;
        telemetry.addData("target position", SlidePosition);
        telemetry.addData("liftyPower", VerticalSlide1.getPower());
        telemetry.addData("liftyCurrent mA", VerticalSlide1.getCurrent(CurrentUnit.MILLIAMPS));

        double maxCurrent = 0;

        if (VerticalSlide1.getCurrent(CurrentUnit.MILLIAMPS) > maxCurrent) {
            maxCurrent = VerticalSlide1.getCurrent(CurrentUnit.MILLIAMPS);
        }

        telemetry.addData("liftyMax mA", maxCurrent);
        telemetry.addData("current position", VerticalSlide1.getCurrentPosition());
    }

    boolean downTo1 = false;
    int preset1 = 0;
    int preset2 = -1100;
    int preset3 = -1500;
    int preset4 = -2500;

    public void goToPreset(boolean bottomRung, boolean bottomBasket, boolean topRung, boolean topBasket) {
        if ((bottomRung || bottomBasket || topRung) && VerticalSlide1.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
            VerticalSlide1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            VerticalSlide1.setPower(1.0);

            VerticalSlide2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            VerticalSlide2.setPower(1.0);
        }
        if (bottomRung) {
            setSlidePosition(preset1, 1800);
            downTo1 = true;
            if (bottomBasket) {
                setSlidePosition(preset2, 2500);
            } else if (topRung) {
                setSlidePosition(preset3, 2500);
            } else if (topBasket) {
                setSlidePosition(preset4, 2500);
            }
        }
    }

    double SlidePosition = 0;

    public void setSlidePosition(int position, double velocity) {
        VerticalSlide1.setTargetPosition(position);
        VerticalSlide1.setVelocity(velocity);
        SlidePosition = position;
    }

    boolean rungWasSet = false;
    public void setOnRung(boolean setRung) {
        if (setRung &! rungWasSet) {
            setSlidePosition((int) (SlidePosition + 100), 1800);
        }
        rungWasSet = setRung;
    }
}
