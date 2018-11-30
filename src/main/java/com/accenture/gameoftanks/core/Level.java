package com.accenture.gameoftanks.core;

import com.accenture.gameoftanks.core.primitives.Shape;
import com.accenture.gameoftanks.core.primitives.Vertex;

import java.util.LinkedList;
import java.util.List;

public class Level {
    //Level boundaries
    //public float leftBoundary;
    //public float rightBoundary;
    //public float topBoundary;
    //public float bottomBoundary;

    private List<Entity> content;
    private List<String> textures;

    //Generate constructor
    public Level(float leftBoundary, float rightBoundary, float topBoundary, float bottomBoundary) {

        /*
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
        */
        loadContent();
        loadTextures();
    }

    //Doesn't allow tank to ride behind the level boundaries
    public void computeColision(Tank tank) {
        /*
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
        */
    }

    /*
    public float getWidth() {
        return rightBoundary - leftBoundary;
    }

    public float getHeight() {
        return topBoundary - bottomBoundary;
    }
    */

    private void loadContent() {
        // STUB - it's better to load level from file

        content = new LinkedList<>();
        Entity entity;
        float [] vertices;
        Shape topology;

        // create level boundaries
        entity = new Entity(true, 0.0f);

        vertices = new float[] {
                0.0f, 0.0f,
                100.0f, 0.0f,
                100.0f, 100.0f,
                0.0f, 100.0f
        };

        topology = new Shape(vertices);
        entity.setTopology(topology);
        content.add(entity);
    }

    public List<Entity> getContent() {
        return content;
    }

    public float [] getExtents() {
        float minX = 1.0e6f;
        float maxX = -1.0e6f;
        float minY = 1.0e6f;
        float maxY = -1.0e6f;

        for (Entity entity: content) {
            Vertex [] vertices = entity.getTopology();

            for (Vertex vertex: vertices) {
                minX = Math.min(vertex.x, minX);
                maxX = Math.max(vertex.x, maxX);
                minY = Math.min(vertex.y, minY);
                maxY = Math.max(vertex.y, maxY);
            }
        }
        return new float[] {minX, maxX, minY, maxY};
    }

    private void loadTextures() {
        // STUB  - it's better to load texture list from level file
        textures = new LinkedList<>();
        textures.add("battleGround01.bmp");
        textures.add("tank01.bmp");
    }

    public List<String> getTextures() {
        return textures;
    }

    public int getTextureID(String textureName) {
        int id = 0;

        for (String name: textures) {
            if (name.equals(textureName)) {
                return id;
            }
            id++;
        }
        return id;
    }
}
