package com.accenture.gameoftanks.core;

import java.io.Serializable;

public class Intent implements Serializable {
    public volatile boolean onForward;
    public volatile boolean onBackWard;
    public volatile boolean onTurnLeft;
    public volatile boolean onTurnRight;

    Intent() {}

    public void update(boolean onTurnLeft,
                       boolean onTurnRight,
                       boolean onForward,
                       boolean onBackWard) {
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
