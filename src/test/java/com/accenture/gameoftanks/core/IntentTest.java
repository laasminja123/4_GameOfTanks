package com.accenture.gameoftanks.core;

import org.junit.Test;


public class IntentTest {

    @Test
    public void computeIntent() {
        int angleDeg = 0;
        float angle = (float) Math.toRadians(angleDeg);
        Intent intent = new Intent();
        float delta = 1.e-7f;

        intent.update(false, false, true, false, true, false, 0.0f);

        /*

        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }
        angleDeg += 45;
        angle = (float) Math.toRadians(angleDeg);


        intent.update(false, true, true, false);

        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }
        angleDeg += 45;
        angle = (float) Math.toRadians(angleDeg);


        intent.update(false, true, false, false);

        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }
        angleDeg += 45;
        angle = (float) Math.toRadians(angleDeg);


        intent.update(true, true, false, false);

        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }
        angleDeg += 45;
        angle = (float) Math.toRadians(angleDeg);


        intent.update(true, false, false, false);

        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }
        angleDeg += 45;
        angle = (float) Math.toRadians(angleDeg);


        intent.update(true, false, false, true);

        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }

        angleDeg += 45;
        angle = (float) Math.toRadians(angleDeg);

        intent.update(false, false, false, true);
        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }

        angleDeg += 45;
        angle = (float) Math.toRadians(angleDeg);

        intent.update(false, false, true, true);
        if (intent.getMoveAngle() < (angle - delta) || intent.getMoveAngle() > (angle + delta)) {
            assertEquals("Incorrect angle value for right button", angle, intent.getMoveAngle(), delta);
        }

        */

    }
}