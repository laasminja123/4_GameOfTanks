package com.accenture.gameoftanks.core;

import com.accenture.gameoftanks.core.primitives.Shape;
import com.accenture.gameoftanks.core.primitives.Vertex;
import sun.awt.image.ImageWatched;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Level {

    private List<Entity> content;
    private List<String> textures;
    private float[] respawns;

    private Queue<Integer> deadObjects;


    //Generate constructor
    public Level() {
        loadContent();
        loadTextures();

        initRespawns();
        deadObjects = new ConcurrentLinkedQueue<>();
    }

    private void loadContent() {
        // STUB - it's better to load level from file

        content = new LinkedList<>();
        Entity entity;
        float [] vertices;
        Shape topology;

        // create level boundaries
        entity = new Entity(true, false, 0.0f, 0.0f, 100);

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
        textures.add("tank-turret01.bmp");
        textures.add("tank-gun01.bmp");
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

    public void copyDeadObjects(Queue<Integer> data) {
        for (int id: data) {
            addDeadObject(id);
        }
    }

    public void addDeadObject(int id) {
        if (!deadObjects.contains(id)) {
            deadObjects.add(id);
        }
    }

    public Queue<Integer> getLevelDeadObjects() {
        return deadObjects;
    }

    //---------------------------------------

    private void initRespawns() {
        int size = 5;
        respawns = new float[3 * size];

        respawns[0] = 10.0f;
        respawns[1] = 10.0f;
        respawns[2] = 0.0f;
        respawns[3] = 10.0f;
        respawns[4] = 90.0f;
        respawns[5] = 0.0f;
        respawns[6] = 90.0f;
        respawns[7] = 10.0f;
        respawns[8] = 0.0f;
        respawns[9] = 90.0f;
        respawns[10] = 90.0f;
        respawns[11] = 0.0f;
        respawns[12] = 50.0f;
        respawns[13] = 50.0f;
        respawns[14] = 0.0f;

    }

    /**
     *
     * @param vehicles all existing vehicles on field
     * @return respawn coords (float x 3)
     */
   /* public float [] getRespawns(List<Vehicle> vehicles) {
         //Position pos = vehicles.getPosition();
        //float x  = pos.posX;
        // float y = pos.posY;
        return 0;
    }

    */


}
