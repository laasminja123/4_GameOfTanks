package com.accenture.gameoftanks.core;

import com.accenture.gameoftanks.core.primitives.Shape;
import com.accenture.gameoftanks.core.primitives.Vertex;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.lang.Math.*;

public class Level {

    private List<Entity> content;
    private List<String> textures;
    private float [] respawns;

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
        Position position;
        int id = 0;

        // create level boundaries ----------------------------------------------------------------
        entity = new Entity(true, false, 0.0f, 0.0f, 100, null);
        entity.setId(id++);
        vertices = new float[] {
                0.0f, 0.0f,
                100.0f, 0.0f,
                100.0f, 100.0f,
                0.0f, 100.0f
        };
        topology = new Shape(vertices);
        entity.setTopology(topology);
        content.add(entity);

        // create buildings -----------------------------------------------------------------------

        // building 1
        entity = new Entity(true, false, 1.0f, 1.0f, 100, "roof03.bmp");
        entity.setId(id++);
        vertices = new float[] {
                -20.0f, -12.0f,
                -20.0f,  12.0f,
                 20.0f,  12.0f,
                 20.0f, -12.0f
        };
        topology = new Shape(vertices);
        entity.setTopology(topology);
        position = entity.getPosition();
        position.posX = 35.0f;
        position.posY = 35.0f;
        position.alpha = (float) PI / 2.0f;
        content.add(entity);

        // building 2
        entity = new Entity(true, false, 1.0f, 1.0f, 100, "roof02.bmp");
        entity.setId(id++);
        vertices = new float[] {
                -10.0f, -6.0f,
                -10.0f,  6.0f,
                10.0f,  6.0f,
                10.0f, -6.0f
        };
        topology = new Shape(vertices);
        entity.setTopology(topology);
        position = entity.getPosition();
        position.posX = 44.0f;
        position.posY = 75.0f;
        content.add(entity);

        // building 3
        entity = new Entity(true, false, 1.0f, 1.0f, 100, "roof01.bmp");
        entity.setId(id++);
        vertices = new float[] {
                -14.0f, -7.0f,
                -14.0f,  7.0f,
                14.0f,  7.0f,
                14.0f, -7.0f
        };
        topology = new Shape(vertices);
        entity.setTopology(topology);
        position = entity.getPosition();
        position.posX = 80.0f;
        position.posY = 45.0f;
        position.alpha = 2.0f;
        content.add(entity);

        // building 4
        entity = new Entity(true, false, 1.0f, 1.0f, 100, "roof02.bmp");
        entity.setId(id++);
        vertices = new float[] {
                -10.0f, -6.0f,
                -10.0f,  6.0f,
                10.0f,  6.0f,
                10.0f, -6.0f
        };
        topology = new Shape(vertices);
        entity.setTopology(topology);
        position = entity.getPosition();
        position.posX = 86.0f;
        position.posY = 80.0f;
        position.alpha = (float) PI / 2.0f;
        content.add(entity);

        // create breakable objects ---------------------------------------------------------------

        // crate 1
        entity = new Entity(true, true, 1.0f, 1.0f, 100, "crate01.bmp");
        entity.setId(id++);
        vertices = new float[] {
                -1.5f, -1.5f,
                -1.5f,  1.5f,
                1.5f,  1.5f,
                1.5f, -1.5f
        };
        topology = new Shape(vertices);
        entity.setTopology(topology);
        position = entity.getPosition();
        position.posX = 40.0f;
        position.posY = 4.0f;
        position.alpha = (float) PI / 6.0f;
        content.add(entity);

        // crate 2
        entity = new Entity(true, true, 1.0f, 1.0f, 100, "crate01.bmp");
        entity.setId(id++);
        vertices = new float[] {
                -1.5f, -1.5f,
                -1.5f,  1.5f,
                1.5f,  1.5f,
                1.5f, -1.5f
        };
        topology = new Shape(vertices);
        entity.setTopology(topology);
        position = entity.getPosition();
        position.posX = 57.0f;
        position.posY = 12.0f;
        content.add(entity);

        // crate 3
        entity = new Entity(true, true, 1.0f, 1.0f, 100, "crate01.bmp");
        entity.setId(id++);
        vertices = new float[] {
                -1.5f, -1.5f,
                -1.5f,  1.5f,
                1.5f,  1.5f,
                1.5f, -1.5f
        };
        topology = new Shape(vertices);
        entity.setTopology(topology);
        position = entity.getPosition();
        position.posX = 15.0f;
        position.posY = 22.0f;
        position.alpha = -.57f;
        content.add(entity);

        // crate 4
        entity = new Entity(true, true, 1.0f, 1.0f, 100, "crate01.bmp");
        entity.setId(id++);
        vertices = new float[] {
                -1.5f, -1.5f,
                -1.5f,  1.5f,
                1.5f,  1.5f,
                1.5f, -1.5f
        };
        topology = new Shape(vertices);
        entity.setTopology(topology);
        position = entity.getPosition();
        position.posX = 57.0f;
        position.posY = 22.0f;
        position.alpha = -.57f;
        content.add(entity);

        // crate 5
        entity = new Entity(true, true, 1.0f, 1.0f, 100, "crate01.bmp");
        entity.setId(id++);
        vertices = new float[] {
                -1.5f, -1.5f,
                -1.5f,  1.5f,
                1.5f,  1.5f,
                1.5f, -1.5f
        };
        topology = new Shape(vertices);
        entity.setTopology(topology);
        position = entity.getPosition();
        position.posX = 54.0f;
        position.posY = 19.4f;
        position.alpha = .0f;
        content.add(entity);

        // crate 6
        entity = new Entity(true, true, 1.0f, 1.0f, 100, "crate01.bmp");
        entity.setId(id++);
        vertices = new float[] {
                -1.5f, -1.5f,
                -1.5f,  1.5f,
                1.5f,  1.5f,
                1.5f, -1.5f
        };
        topology = new Shape(vertices);
        entity.setTopology(topology);
        position = entity.getPosition();
        position.posX = 53.2f;
        position.posY = 23.0f;
        position.alpha = .3f;
        content.add(entity);

        // long truck
        entity = new Entity(true, false, 1.0f, 1.0f, 100, "truck01.bmp");
        entity.setId(id++);
        vertices = new float[] {
                -12.0f, -2.2f,
                -12.0f,  2.2f,
                12.0f,  2.2f,
                12.0f, -2.2f
        };
        topology = new Shape(vertices);
        entity.setTopology(topology);
        position = entity.getPosition();
        position.posX = 13.0f;
        position.posY = 40.0f;
        position.alpha = -(float) PI / 2.0f - .04f;
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
            if (!entity.hasConvexTopology()) {
                Vertex[] vertices = entity.getTopology();

                for (Vertex vertex : vertices) {
                    minX = Math.min(vertex.x, minX);
                    maxX = Math.max(vertex.x, maxX);
                    minY = Math.min(vertex.y, minY);
                    maxY = Math.max(vertex.y, maxY);
                }
                break;
            }
        }
        return new float[] {minX, maxX, minY, maxY};
    }

    private void loadTextures() {
        // STUB  - it's better to load texture list from level file
        textures = new LinkedList<>();
        textures.add("battleGround01.bmp");
        textures.add("battleGround02.bmp");
        textures.add("battleGround03.bmp");
        textures.add("tank01.bmp");
        textures.add("tank-turret01.bmp");
        textures.add("tank-gun01.bmp");
        textures.add("crate01.bmp");
        textures.add("roof01.bmp");
        textures.add("roof02.bmp");
        textures.add("roof03.bmp");
        textures.add("truck01.bmp");
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

    public boolean containsDeadObject(int id) {
        return deadObjects.contains(id);
    }

    //---------------------------------------

    private void initRespawns() {
        int size = 5;
        respawns = new float[3 * size];

        // 1
        respawns[0] = 10.0f;
        respawns[1] = 10.0f;
        respawns[2] = 0.0f;

        // 2
        respawns[3] = 10.0f;
        respawns[4] = 90.0f;
        respawns[5] = -.5f;

        // 3
        respawns[6] = 90.0f;
        respawns[7] = 10.0f;
        respawns[8] = (float) PI / 2.0f;

        // 4
        respawns[9] = 90.0f;
        respawns[10] = 60.0f;
        respawns[11] = -(float) PI;

        // 5
        respawns[12] = 60.0f;
        respawns[13] = 60.0f;
        respawns[14] = -2.0f;
    }

    /**
     * computes best respawn which has maximal distance to all existing player current positions
     * @param players all existing players on field
     * @return respawn coords (float x 3)
     */
    public float [] getRespawn(Queue<Player> players) {
        int size = respawns.length / 3;
        float [] respawn = new float[3];
        System.arraycopy(respawns, 0, respawn, 0, 3);

        if (!players.isEmpty()) {
            float [] distances = new float[size];
            float maxDistance = 0.0f;
            int bestRespIndex = 0;

            // compute min distances from respawn to player position
            for (int i = 0; i < size; i++) {
                float dist = 1.0e5f;

                float x = i * 3;
                float y = i * 3 + 1;

                for (Player player : players) {
                    Vehicle vehicle = player.getVehicle();
                    Position position = vehicle.getPosition();

                    float dx = position.posX - x;
                    float dy = position.posY - y;

                    dist = min(dist, (float) sqrt(dx * dx + dy * dy));
                }
                distances[i] = dist;

                if (dist > maxDistance) {
                    maxDistance = dist;
                    bestRespIndex = i;
                }
            }
            System.arraycopy(respawns, bestRespIndex * 3, respawn, 0, 3);
        }
        return respawn;
    }
}
