package com.accenture.gameoftanks.client.gui;

import com.accenture.gameoftanks.core.*;
import com.accenture.gameoftanks.core.primitives.Vertex;
import com.accenture.gameoftanks.net.Data;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Renderer  implements GLEventListener {

    private Level level;
    private Data data;
    private String clientName;

    private float posX;
    private float posY;
    private float translationSensitivity;

    private float clientVehiclePosX;
    private float clientVehiclePosY;

    private float viewPortSizeX;
    private float viewPortSizeY;

    private float aimPosX;
    private float aimPosY;
    private boolean onDrag;

    private float scale;
    private float scaleFactor;

    private List<Texture> usedTextures;
    private boolean needToReloadTextures;
    private int [] textures;

    private boolean onRender;

    Renderer() {
        scale = 1.0f;
        translationSensitivity = .2f;
        scaleFactor = 1.1f;

        usedTextures = new LinkedList<>();
    }

    void scaleIn() {
        scale *= scaleFactor;
    }

    void scaleOut() {
        scale /= scaleFactor;
    }

    public void display(GLAutoDrawable gLDrawable) {
        if (onRender) {
            return;
        }
        onRender = true;

        final GL2 gl = gLDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        loadTextures(gl);

        if (level == null || data == null || clientName == null) {
            onRender = false;
            return;
        }

        gl.glTranslatef(-posX, -posY, 0.0f);

        gl.glScalef(scale, scale, scale);

        drawLevel(gl);
        drawVehicles(gl);
        drawBullets(gl);
        onRender = false;
    }

    private void drawLevel(GL2 gl) {
        // draw level background
        float[][] uvCoords = getDefaultTextureCoordinates();
        int id = level.getTextureID("battleGround03.bmp");
        Texture texture = usedTextures.get(id);
        texture.enable(gl);
        texture.bind(gl);
        float [] extents = level.getExtents();

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL2.GL_QUADS);
        {
            gl.glTexCoord2f(uvCoords[3][0], uvCoords[3][1]);
            gl.glVertex2f(extents[0], extents[2]);

            gl.glTexCoord2f(uvCoords[2][0], uvCoords[2][1]);
            gl.glVertex2f(extents[1], extents[2]);

            gl.glTexCoord2f(uvCoords[1][0], uvCoords[1][1]);
            gl.glVertex2f(extents[1], extents[3]);

            gl.glTexCoord2f(uvCoords[0][0], uvCoords[0][1]);
            gl.glVertex2f(extents[0], extents[3]);
        }
        gl.glEnd();
        texture.disable(gl);

        // draw static objects
        List<Entity> entities = level.getContent();

        for (Entity entity: entities) {
            if (entity.hasConvexTopology() && !level.containsDeadObject(entity.getId())) {
                String textureName = entity.getTextureName();
                Vertex [] vertices = transformEntityPosition(entity);

                if (textureName != null && vertices.length == 4) {  // only quads for now
                    id = level.getTextureID(textureName);
                    texture = usedTextures.get(id);
                    texture.enable(gl);
                    texture.bind(gl);

                    gl.glColor3f(1.0f, 1.0f, 1.0f);
                    gl.glBegin(GL2.GL_QUADS);
                    {
                        gl.glTexCoord2f(uvCoords[0][0], uvCoords[0][1]);
                        gl.glVertex2f(vertices[0].xt, vertices[0].yt);

                        gl.glTexCoord2f(uvCoords[3][0], uvCoords[3][1]);
                        gl.glVertex2f(vertices[1].xt, vertices[1].yt);

                        gl.glTexCoord2f(uvCoords[2][0], uvCoords[2][1]);
                        gl.glVertex2f(vertices[2].xt, vertices[2].yt);

                        gl.glTexCoord2f(uvCoords[1][0], uvCoords[1][1]);
                        gl.glVertex2f(vertices[3].xt, vertices[3].yt);
                    }
                    gl.glEnd();
                    texture.disable(gl);
                }
            }
        }
    }

    private void drawVehicles(GL2 gl) {
        List<Vehicle> vehicles = data.getVehicles();
        int vehicleId = data.getVehicleId(clientName);
        float[][] uvCoords = getDefaultTextureCoordinates();

        for (Vehicle vehicle: vehicles) {
            Vertex[] vertices = transformEntityPosition(vehicle);

            if (vehicle instanceof Tank) {
                Turret turret = ((Tank) vehicle).getTurret();
                float offset = turret.getOffset();
                float angle =  turret.getAngle();
                Position pos = vehicle.getPosition();

                // handle client's vehicle
                if (vehicle.getId() == vehicleId && vehicle.isAlive()) {
                    // drag line
                    if (onDrag) {
                        Position position = vehicle.getPosition();
                        clientVehiclePosX = position.posX;
                        clientVehiclePosY = position.posY;

                        // compute current shooting direction
                        float [] displacement1 = new float[] {offset, 0.0f};
                        MATH.rotate(position.alpha, displacement1);

                        float [] displacement2 = new float[] {20.0f, 0.0f};
                        MATH.rotate(position.alpha + angle, displacement2);

                        gl.glColor3f(1.0f, 1.0f, 1.0f);
                        gl.glBegin(GL2.GL_LINES);
                        {
                            // aiming line
                            gl.glVertex2f(clientVehiclePosX + displacement1[0],
                                    clientVehiclePosY + displacement1[1]);
                            gl.glVertex2f(aimPosX, aimPosY);
                        }
                        gl.glEnd();

                        gl.glColor3f(1.0f, 0.0f, 0.0f);
                        gl.glBegin(GL2.GL_LINES);
                        {
                            // current fire line
                            gl.glVertex2f(clientVehiclePosX + displacement1[0],
                                    clientVehiclePosY + displacement1[1]);
                            gl.glVertex2f(clientVehiclePosX + displacement1[0] + displacement2[0],
                                    clientVehiclePosY + displacement1[1] + displacement2[1]);
                        }
                        gl.glEnd();
                    }
                }

                if (vehicle.isAlive()) {
                    gl.glColor3f(1.0f, 1.0f, 1.0f);
                } else {
                    gl.glColor3f(.5f, .5f, .5f);
                }

                // draw body
                int id = level.getTextureID(vehicle.getTextureName());
                Texture texture = usedTextures.get(id);
                texture.enable(gl);
                texture.bind(gl);

                gl.glBegin(GL2.GL_QUADS);
                {
                    gl.glTexCoord2f(uvCoords[0][0], uvCoords[0][1]);
                    gl.glVertex2f(vertices[0].xt, vertices[0].yt);

                    gl.glTexCoord2f(uvCoords[1][0], uvCoords[1][1]);
                    gl.glVertex2f(vertices[1].xt, vertices[1].yt);

                    gl.glTexCoord2f(uvCoords[2][0], uvCoords[2][1]);
                    gl.glVertex2f(vertices[2].xt, vertices[2].yt);

                    gl.glTexCoord2f(uvCoords[3][0], uvCoords[3][1]);
                    gl.glVertex2f(vertices[3].xt, vertices[3].yt);
                }
                gl.glEnd();
                texture.disable(gl);


                // draw turret
                id = level.getTextureID(turret.getTextureName());
                texture = usedTextures.get(id);
                texture.enable(gl);
                texture.bind(gl);
                vertices = turret.getTurretTopology();
                MATH.transform2d(pos.posX, pos.posY, offset, pos.alpha, angle, vertices);

                gl.glBegin(GL2.GL_QUADS);
                {
                    gl.glTexCoord2f(uvCoords[0][0], uvCoords[0][1]);
                    gl.glVertex2f(vertices[0].xt, vertices[0].yt);

                    gl.glTexCoord2f(uvCoords[1][0], uvCoords[1][1]);
                    gl.glVertex2f(vertices[1].xt, vertices[1].yt);

                    gl.glTexCoord2f(uvCoords[2][0], uvCoords[2][1]);
                    gl.glVertex2f(vertices[2].xt, vertices[2].yt);

                    gl.glTexCoord2f(uvCoords[3][0], uvCoords[3][1]);
                    gl.glVertex2f(vertices[3].xt, vertices[3].yt);
                }
                gl.glEnd();
                texture.disable(gl);

                // draw gun
                id = level.getTextureID(turret.getGunTextureName());
                texture = usedTextures.get(id);
                texture.enable(gl);
                texture.bind(gl);
                vertices = turret.getGunTopology();
                MATH.transform2d(pos.posX, pos.posY, offset, pos.alpha, angle, vertices);

                gl.glBegin(GL2.GL_QUADS);
                {
                    gl.glTexCoord2f(uvCoords[0][0], uvCoords[0][1]);
                    gl.glVertex2f(vertices[0].xt, vertices[0].yt);

                    gl.glTexCoord2f(uvCoords[1][0], uvCoords[1][1]);
                    gl.glVertex2f(vertices[1].xt, vertices[1].yt);

                    gl.glTexCoord2f(uvCoords[2][0], uvCoords[2][1]);
                    gl.glVertex2f(vertices[2].xt, vertices[2].yt);

                    gl.glTexCoord2f(uvCoords[3][0], uvCoords[3][1]);
                    gl.glVertex2f(vertices[3].xt, vertices[3].yt);
                }
                gl.glEnd();
                texture.disable(gl);
            }
        }
    }

    private void drawBullets(GL2 gl) {
        Queue<Bullet> bullets = data.getBullets();
        float radius = .5f;

        gl.glColor3f(1.0f, 1.0f, 0.0f);
        gl.glBegin(GL2.GL_QUADS);
        {
            for (Bullet bullet: bullets) {
                gl.glVertex2f(bullet.posX - radius, bullet.posY);
                gl.glVertex2f(bullet.posX, bullet.posY - radius);
                gl.glVertex2f(bullet.posX + radius, bullet.posY);
                gl.glVertex2f(bullet.posX, bullet.posY + radius);
            }
        }
        gl.glEnd();
    }

    private float[][] getDefaultTextureCoordinates() {
        float[][] coords = new float[4][2];
        coords[0][0] = 0.0f;
        coords[0][1] = 0.0f;

        coords[1][0] = 1.0f;
        coords[1][1] = 0.0f;

        coords[2][0] = 1.0f;
        coords[2][1] = 1.0f;

        coords[3][0] = 0.0f;
        coords[3][1] = 1.0f;
        return coords;
    }

    private Vertex [] transformEntityPosition(Entity entity) {
        Position position = entity.getPosition();
        Vertex [] topology = entity.getTopology();

        // transform vertices
        MATH.transform2d(position, topology);
        return topology;
    }

    public void init(GLAutoDrawable gLDrawable) {
        final GL2 gl = gLDrawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(0.0f);

    }

    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
        final GL2 gl = gLDrawable.getGL().getGL2();

        if (height <= 0) {
            height = 1;
        }
        float ratio = (float) width / (float) height;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        if (level != null) {
            float [] extents = level.getExtents();
            viewPortSizeY = Math.max(extents[1] - extents[0], extents[3] - extents[2]);
            viewPortSizeX = viewPortSizeY * ratio;

            gl.glOrtho(-viewPortSizeX / 2.0f, viewPortSizeX / 2.0f,
                    -viewPortSizeY / 2.0f, viewPortSizeY / 2.0f, -100.0f, 100.0f);
        }

        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public void dispose(GLAutoDrawable arg0) {}

    void setupGameData(Level level, Data data, String clientName) {
        this.level = level;
        this.data = data;
        this.clientName = clientName;
        float [] extents = level.getExtents();
        posX = .5f * (extents[0] + extents[1]);
        needToReloadTextures = true;
    }

    void removeGameData() {
        this.level = null;
        this.data = null;
        this.clientName = null;
    }

    void processMouseDrag(int dx, int dy) {
        posX -= dx * translationSensitivity;
        posY += dy * translationSensitivity;
    }

    void dragLine(float width, float height, int x, int y, boolean onDrag) {
        aimPosX = x;
        aimPosY = y;
        transformToWorldCs(width, height, x, y);
        this.onDrag = onDrag;
    }

    float getShootingAngle() {
        return (float) Math.atan2(aimPosY - clientVehiclePosY, aimPosX - clientVehiclePosX);
    }

    private void transformToWorldCs(float width, float height, float mx1, float my1) {
        float [] p1 = new float[2];

        // horizontal physical bounds
        p1[0] = (mx1 / width) * viewPortSizeX - viewPortSizeX / 2 + posX;
        p1[0] /= scale;

        // vertical physical bounds
        p1[1] = (-my1 / height) * viewPortSizeY + viewPortSizeY / 2 + posY;
        p1[1] /= scale;

        aimPosX = p1[0];
        aimPosY = p1[1];
    }

    private void loadTextures(GL2 gl) {
        if (level == null || data == null || clientName == null) {
            return;
        }

        if (needToReloadTextures) {
            List<String> usedTextureNames = level.getTextures();
            usedTextures.clear();
            textures = new int[usedTextureNames.size()];

            for (String name: usedTextureNames) {
                // create texture file
                String fileName = System.getProperty("user.dir") + "/src/main/resources/textures/" + name;
                File image = new File(fileName);
                Texture texture;

                try {
                    texture = TextureIO.newTexture(image, true);
                    texture.setTexParameteri(gl, GL2ES2.GL_TEXTURE_MAG_FILTER, GL2ES2.GL_LINEAR);
                    texture.setTexParameteri(gl, GL2ES2.GL_TEXTURE_MIN_FILTER, GL2ES2.GL_LINEAR);
                    texture.setTexParameteri(gl, GL2ES2.GL_TEXTURE_WRAP_S, GL2ES2.GL_REPEAT);
                    texture.setTexParameteri(gl, GL2ES2.GL_TEXTURE_WRAP_T, GL2ES2.GL_REPEAT);
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(null, "Texture \"" + name + "\" is not available!",
                            "ERROR!", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                usedTextures.add(texture);
            }

            if (usedTextureNames.size() != 0) {
                gl.glGenTextures(textures.length, textures, 0);
                gl.glEnable(GL.GL_TEXTURE_2D);
            }
            needToReloadTextures = false;
        }
    }
}
