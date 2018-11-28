package com.accenture.gameoftanks.core;

public class Level {
    //Level boundaries
    public float leftBoundary;
    public float rightBoundary;
    public float topBoundary;
    public float bottomBoundary;

    //Generate constructor
    public Level(float leftBoundary, float rightBoundary, float topBoundary, float bottomBoundary) {
        //Verify if right boundary coordinate is bigger than left
        if (rightBoundary > leftBoundary) {
            this.leftBoundary = leftBoundary;
            this.rightBoundary = rightBoundary;
        }
        //Verify if top boundary coordinate is bigger than bottom
        if (topBoundary > bottomBoundary) {
            this.topBoundary = topBoundary;
            this.bottomBoundary = bottomBoundary;
        }
    }

    //Doesn't allow tank to ride behind the level boundaries
    public void computeColision(Tank tank) {
        //Create reference to Position class
        Position position = tank.getPosition();

        //Get tankFullPosition (tank center coordinate +  tank length)
        float tankFullPositionX = position.posX + tank.length/2;
        float tankFullPositionY = position.posY + tank.width/2;

        if (tankFullPositionX > rightBoundary) {
            position.posX = rightBoundary - tank.length/2;
        }


        if (tankFullPositionX < leftBoundary) {
            position.posX = leftBoundary + tank.length/2;
            position.vx = 0;
        }


        if (tankFullPositionY > topBoundary) {
            position.posY = topBoundary - tank.width/2;
        }


        if (tankFullPositionY < bottomBoundary) {
            position.posY = bottomBoundary + tank.width/2;
            position.vy = 0;
        }

    }

    public float getWidth() {
        return rightBoundary - leftBoundary;
    }

    public float getHeight() {
        return topBoundary - bottomBoundary;
    }
}
