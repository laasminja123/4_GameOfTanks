package com.accenture.gameoftanks.core;

import java.io.Serializable;
import java.lang.Math;

public class Intent implements Serializable {
    public volatile boolean onForward;
    public volatile boolean onBackWard;
    public volatile boolean onTurnLeft;
    public volatile boolean onTurnRight;

    public Intent() {}

    //Method that computes player intention
    //Method that computes player intention
    public void computeIntent(boolean onTurnLeft, boolean onTurnRight, boolean onForward, boolean onBackWard) {
        /*
        //Angle when player press right
        if (left == false && top == false && right && bottom == false ) {
            this.moveAngle = (float) Math.toRadians(0);
        }
        //Angle when player press top right
        if (left == false && top && right && bottom == false) {
            this.moveAngle = (float) Math.toRadians(45);
        }
        //Angle when player press top
        if (left == false && top && right == false && bottom == false ) {
            this.moveAngle = (float) Math.toRadians(90);
        }
        //Angle when player press top and left
        if (left && top && right == false && bottom == false) {
            this.moveAngle = (float) Math.toRadians(135);
        }
        //Angle when player press left
        if (left && top == false && right == false && bottom == false) {
            this.moveAngle = (float) Math.toRadians(180);
        }
        //Angle when player press left and bottom
        if (left && top == false && right == false && bottom) {
            this.moveAngle = (float) Math.toRadians(225);
        }
        //Angle when player press bottom
        if (left == false && top == false && left == false && bottom) {
            this.moveAngle = (float) Math.toRadians(270);
        }
        //Angle when player press bottom and right
        if (left == false && top == false && right && bottom) {
            this.moveAngle = (float) Math.toRadians(315);
        }
        */

        this.onForward   = onForward;
        this.onBackWard  = onBackWard;
        this.onTurnLeft  = onTurnLeft;
        this.onTurnRight = onTurnRight;
    }

    void copy(Intent intent) {
        this.onForward   = intent.onForward;
        this.onBackWard  = intent.onBackWard;
        this.onTurnLeft  = intent.onTurnLeft;
        this.onTurnRight = intent.onTurnRight;
    }
}
