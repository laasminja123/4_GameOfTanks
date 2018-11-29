package com.accenture.gameoftanks.core;

import org.junit.Test;

import static org.junit.Assert.*;


public class IntentTest {

    @Test
    public void computeIntent() {
        int angleDeg = 0;
        float angle = (float) Math.toRadians(angleDeg);
        Intent intent = new Intent();
        float delta = 1.e-7f;

        intent.computeIntent(false, false, true, false);

        /*

        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }
        angleDeg += 45;
        angle = (float) Math.toRadians(angleDeg);


        intent.computeIntent(false, true, true, false);

        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }
        angleDeg += 45;
        angle = (float) Math.toRadians(angleDeg);


        intent.computeIntent(false, true, false, false);

        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }
        angleDeg += 45;
        angle = (float) Math.toRadians(angleDeg);


        intent.computeIntent(true, true, false, false);

        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }
        angleDeg += 45;
        angle = (float) Math.toRadians(angleDeg);


        intent.computeIntent(true, false, false, false);

        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }
        angleDeg += 45;
        angle = (float) Math.toRadians(angleDeg);


        intent.computeIntent(true, false, false, true);

        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }

        angleDeg += 45;
        angle = (float) Math.toRadians(angleDeg);

        intent.computeIntent(false, false, false, true);
        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }

        angleDeg += 45;
        angle = (float) Math.toRadians(angleDeg);

        intent.computeIntent(false, false, true, true);
        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }

        */

    }
}