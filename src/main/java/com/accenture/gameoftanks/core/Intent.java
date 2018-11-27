package com.accenture.gameoftanks.core;

public class Intent {
    private boolean onMove;
    private boolean onShot;
    private float moveAngle;
    private float shotAngle;

    public Intent(boolean onMove, boolean onShot, float moveAngle, float shotAngle) {
        this.onMove = onMove;
        this.onShot = onShot;
        this.moveAngle = moveAngle;
        this.shotAngle = shotAngle;
    }

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

    public float getMoveAngle() {
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
}
