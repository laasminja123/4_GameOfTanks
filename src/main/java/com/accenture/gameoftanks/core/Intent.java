package com.accenture.gameoftanks.core;

import java.io.Serializable;
import java.lang.Math;

public class Intent implements Serializable {
    private boolean onMove;
    private boolean onShot;
    private float moveAngle;
    private float shotAngle;

    public Intent() {
    }

    //Generate constructor
    public Intent(boolean onMove, boolean onShot, float moveAngle, float shotAngle) {
        this.onMove = onMove;
        this.onShot = onShot;
        this.moveAngle = moveAngle;
        this.shotAngle = shotAngle;
    }

    //Generate getters and setters
    public boolean isOnMove() {
        return onMove;
    }

    public void setOnMove(boolean onMove) {
        this.onMove = onMove;
    }

    public boolean isOnShot() {
        return onShot;
    }

    public void setOnShot(boolean onShot) {
        this.onShot = onShot;
    }

    public double getMoveAngle() {
        return moveAngle;
    }

    public void setMoveAngle(float moveAngle) {
        this.moveAngle = moveAngle;
    }

    public float getShotAngle() {
        return shotAngle;
    }

    public void setShotAngle(float shotAngle) {
        this.shotAngle = shotAngle;
    }

    //Method that computes player intention
    //Method that computes player intention
    public void computeIntent(boolean left, boolean top, boolean right, boolean bottom) {
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


    }


}
