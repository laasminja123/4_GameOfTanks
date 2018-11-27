package com.accenture.gameoftanks.core;
import java.lang.Math;
public class Intent {
    private boolean onMove;
    private boolean onShot;
    private float moveAngle;
    private float shotAngle;

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
    public void computeIntent(boolean left, boolean top, boolean right, boolean bottom) {
        //Angle when player press top and bottom or left and right at the same time
        if (top == true && bottom == true && right == false && left == false || left == true && right == true && top == false && bottom == false) {
            this.moveAngle = 0;
        }
        //Angle when player press top right
        if (top == true && bottom == false && left == false && right == true) {
            this.moveAngle = (float)Math.toRadians(45);
        }
        //Angle when player press top
        if (top == true && bottom == false && left == false && right == false) {
            this.moveAngle = (float)Math.toRadians(90);
        }
        //Angle when player press top and left
        if (top == true && bottom == false && left == true && right == false) {
            this.moveAngle = (float)Math.toRadians(135);
        }
        //Angle when player press left
        if (left == true && bottom == false && top == false && right == false) {
            this.moveAngle = (float)Math.toRadians(180);
        }
        //Angle when player press left and bottom
        if (bottom == true && top == false && left == true && right == false) {
            this.moveAngle = (float)Math.toRadians(220);
        }

        //Angle when player press bottom
        if (bottom == true && top == false && left == false && right == false) {
            this.moveAngle = (float)Math.toRadians(270);
        }
        //Angle when player press bottom and right
        if (bottom == true && top == false && left == false && right == true) {
            this.moveAngle = (float)Math.toRadians(315);
        }
        //Angle when player press right
        if (right == true && bottom == false && top == false && left == false) {
            this.moveAngle = (float)Math.toRadians(360);
        }


    }


}
