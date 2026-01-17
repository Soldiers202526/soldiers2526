/*   MIT License
 *   Copyright (c) [2025] [Base 10 Assets, LLC]
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:

 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.

 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package org.firstinspires.ftc.teamcode;


import static org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver.LayerHeight;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Prism.Color;
import org.firstinspires.ftc.teamcode.Prism.Direction;
import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver;
import org.firstinspires.ftc.teamcode.Prism.PrismAnimations;

import java.util.concurrent.TimeUnit;

/*
 * This example file shows how to create a couple of different Animations on the Prism, and save
 * them to an Artboard on the device.
 * The example file called "GoBildaPrismConfigurator" is designed to create a user-interface
 * through your driver's station. This is a great way to create Artboards from Animations, but
 * doesn't let you control quite everything about the available Animations. If you'd like to
 * do something more complex, or would just prefer to create your Artboard in Java, this
 * program shows you how.
 * The example file called "GoBildaPrismArtboardExample" shows how to recall different Artboards
 * that you have already saved to the device. That file shows the code you should consider adding
 * to your OpMode if you would like to dynamically change the Artboard shown by your LEDs during
 * the match.
 *
 * Core to understanding how to use this product is knowing these three terms:
 * Animations: (Like RAINBOW or BLINK) - These have properties you can configure, like their color.
 * They can have unique start and end points!
 * Layers: There are 10 layers, each of which can store an animation. These are hierarchical.
 * So an Animation on layer 5 will cover an animation on layer 2 if they overlap.
 * You can use start and end points to have layers overlap to create new patterns! Or show multiple
 * animations at once on different LEDs.
 * Artboards: An Artboard is a set of 10 layers which is stored on the Prism.
 * you can have up to 8 unique Artboards. Artboards are easy and computationally fast to switch between.
 */

@TeleOp(name="Prism Animations Example", group="Linear OpMode")
//@Disabled

public class LED_test extends LinearOpMode {

    GoBildaPrismDriver Right;
    GoBildaPrismDriver Left;

    PrismAnimations.Solid solid = new PrismAnimations.Solid(new Color(0,50,0));
    PrismAnimations.Snakes snake = new PrismAnimations.Snakes();

    @Override
    public void runOpMode() {
        /*
         * Initialize the hardware variables. Note that the strings used here must correspond
         * to the names assigned during the robot configuration step on the driver's station.
         */
        Left = hardwareMap.get(GoBildaPrismDriver.class,"Left");
        Right = hardwareMap.get(GoBildaPrismDriver.class, "Right");

        /*
         * Here you can customize the specifics of different animations. Each animation has it's
         * own set of parameters that you can customize to create something unique! Each animation
         * has carefully selected default parameters. So you do not need to set each parameter
         * for every animation!
         */
        solid.setBrightness(50);
        solid.setStartIndex(0);
        solid.setStopIndex(12);




       snake.setBackgroundColor(new Color(0,50,0));
       snake.setSnakeLength(8);
       snake.setDirection(Direction.Forward);
       snake.setColors(Color.GREEN);
       snake.setSpacingBetween(2);
       snake.setSpeed(0.5f);

        telemetry.addData("Device ID: ", Right.getDeviceID());
        telemetry.addData("Firmware Version: ", Right.getFirmwareVersionString());
        telemetry.addData("Hardware Version: ", Right.getHardwareVersionString());
        telemetry.addData("Power Cycle Count: ", Right.getPowerCycleCount());
        telemetry.addData("Device ID: ", Left.getDeviceID());
        telemetry.addData("Firmware Version: ", Left.getFirmwareVersionString());
        telemetry.addData("Hardware Version: ", Left.getHardwareVersionString());
        telemetry.addData("Power Cycle Count: ", Left.getPowerCycleCount());
        telemetry.update();

        boolean state = true;

//        Right.insertAndUpdateAnimation(LayerHeight.LAYER_0, solid);
//        Left.insertAndUpdateAnimation(LayerHeight.LAYER_0, solid);
//        Right.insertAndUpdateAnimation(LayerHeight.LAYER_1, greenDroid);
//        Left.insertAndUpdateAnimation(LayerHeight.LAYER_1, greenDroid);
        // Wait for the game to start (driver presses START)
        waitForStart();
        resetRuntime();


        Right.insertAndUpdateAnimation(LayerHeight.LAYER_1, snake);
        Left.insertAndUpdateAnimation(LayerHeight.LAYER_1, snake);




        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {




//            if (state) {

//                state = false;
               // Time = System.currentTimeMillis();
//            }
//
//            if (!state && System.currentTimeMillis() - Time > 400 ) {
//                state = true;
//
//            }

//            Right.insertAndUpdateAnimation(LayerHeight.LAYER_1, greenDroid);
//            Left.insertAndUpdateAnimation(LayerHeight.LAYER_1, greenDroid);
                /*
                 * Here we insert and update the animation to the Prism, this by default does not
                 * save it to an Artboard, it just starts the Animation playing. If you have
                 * already inserted an animation at a layer height, you can instead call
                 * .updateAnimationFromIndex(LayerHeight.LAYER_0) to update an animation at a
                 * specific layer height without overwriting it completely.
                 */



            if(gamepad1.xWasPressed()){
                /*
                 * Clearing the animation doesn't erase any saved Artboards, but it removes all the
                 * currently displayed animations.
                 */

                Right.clearAllAnimations();
                Left.clearAllAnimations();

            }

            if(gamepad1.dpadDownWasPressed()){
                /*
                 * Here we save the animation we are currently displaying to Artboard 0.
                 */
                Left.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);
                Right.saveCurrentAnimationsToArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_0);
            }

            telemetry.addLine("Press A to insert and update the created animations.");
            telemetry.addLine("Press X to clear current animations.");
            telemetry.addLine("Press D-Pad Down to save current animations to Artboard #0");
            telemetry.addLine();
//            telemetry.addData("Run Time (Hours): ",Left.getRunTime(TimeUnit.HOURS));
//            telemetry.addData("Run Time (Minutes): ",Left.getRunTime(TimeUnit.MINUTES));
            telemetry.addData("Number of LEDS: ", Left.getNumberOfLEDs());
            telemetry.addData("Current FPS: ", Left.getCurrentFPS());
//            telemetry.addData("Run Time (Hours): ",Right.getRunTime(TimeUnit.HOURS));
//            telemetry.addData("Run Time (Minutes): ",Right.getRunTime(TimeUnit.MINUTES));
            telemetry.addData("Number of LEDS: ", Right.getNumberOfLEDs());
            telemetry.addData("Current FPS: ", Right.getCurrentFPS());
            telemetry.update();
            sleep(50);
        }
    }

}