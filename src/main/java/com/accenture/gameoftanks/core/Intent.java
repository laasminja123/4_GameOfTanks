package com.accenture.gameoftanks.core;

import java.io.Serializable;

public class Intent implements Serializable {
    public volatile boolean onForward;
    public volatile boolean onBackWard;
    public volatile boolean onTurnLeft;
    public volatile boolean onTurnRight;

    public volatile boolean onShoot;
    public volatile boolean onAdjustShootingAngle;
    public volatile float shootingAngle;

    Intent() {}

    public void update(boolean onTurnLeft,
                       boolean onTurnRight,
                       boolean onForward,
                       boolean onBackWard,
                       boolean onShoot,
                       boolean onAdjustShootingAngle,
                       float shootingAngle) {
        this.onForward   = onForward;
        this.onBackWard  = onBackWard;
        this.onTurnLeft  = onTurnLeft;
        this.onTurnRight = onTurnRight;
        this.onShoot     = onShoot;
        this.onAdjustShootingAngle = onAdjustShootingAngle;
        this.shootingAngle = shootingAngle;
    }

    void copy(Intent intent) {
        this.onForward   = intent.onForward;
        this.onBackWard  = intent.onBackWard;
        this.onTurnLeft  = intent.onTurnLeft;
        this.onTurnRight = intent.onTurnRight;
        this.onShoot     = intent.onShoot;
        this.onAdjustShootingAngle = intent.onAdjustShootingAngle;
        this.shootingAngle = intent.shootingAngle;
    }
}
