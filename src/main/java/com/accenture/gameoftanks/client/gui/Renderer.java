package com.accenture.gameoftanks.client.gui;

import com.accenture.gameoftanks.core.*;
import com.accenture.gameoftanks.core.primitives.Vertex;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Renderer  implements GLEventListener {

    private Level level;
    private Player player;

    private int posX;
    private int posY;

    private float scale;
    private float scaleFactor;

    private List<Texture> usedTextures;
    private boolean needToReloadTextures;
    private int [] textures;

    public Renderer(Level level, Player player) {
        this.level = level;
        this.player = player;

        scale = 1.0f;
        scaleFactor = 1.1f;

        usedTextures = new LinkedList<>();
    }

    void setPosition(int x, int y) {
        posX = x;
        posY = y;
    }

    void scaleIn() {
        scale *= scaleFactor;
    }

    void scaleOut() {
        scale /= scaleFactor;
    }

    public void display(GLAutoDrawable gLDrawable) {
        final GL2 gl = gLDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        loadTextures(gl);

        if (level == null || player == null) {
            return;
        }

        //gl.glTranslatef(-level.getWidth() / 2.0f, -level.getHeight() / 2.0f, 0.0f);
        gl.glTranslatef(posX, posY, 0.0f);
        //gl.glTranslatef(-5.0f, 0.0f, 0.0f);

        gl.glScalef(scale, scale, scale);

        // draw level boundaries
        float[][] uvCoords = getDefaultTextureCoordinates();
        int id = level.getTextureID("battleGround01.bmp");
        Texture texture = usedTextures.get(id);
        texture.enable(gl);
        texture.bind(gl);

        //gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL2.GL_QUADS);
        {
            gl.glTexCoord2f(uvCoords[0][0], uvCoords[0][1]);
            gl.glVertex2f(level.leftBoundary, level.bottomBoundary);

            gl.glTexCoord2f(uvCoords[1][0], uvCoords[1][1]);
            gl.glVertex2f(level.rightBoundary, level.bottomBoundary);

            gl.glTexCoord2f(uvCoords[2][0], uvCoords[2][1]);
            gl.glVertex2f(level.rightBoundary, level.topBoundary);

            gl.glTexCoord2f(uvCoords[3][0], uvCoords[3][1]);
            gl.glVertex2f(level.leftBoundary, level.topBoundary);
        }
        gl.glEnd();

        // draw vehicle
        Vehicle vehicle = player.getVehicle();
        Vertex [] vertices = transformVehiclePosition(vehicle);

        if (vehicle instanceof Tank) {
            id = level.getTextureID("tank01.bmp");
            texture = usedTextures.get(id);
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
        }
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

    private Vertex [] transformVehiclePosition(Vehicle vehicle) {
        Position position = vehicle.getPosition();
        Vertex [] topology = vehicle.getTopology();

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
            float offset = 5.0f;
            float extent = Math.max(level.getWidth(), level.getHeight());
            float w = extent * ratio;
            float h = extent;
            gl.glOrtho(-w / 2.0f - offset, w / 2.0f + offset,
                    -h / 2.0f - offset, h / 2.0f + offset, -100.0f, 100.0f);
        }

        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public void dispose(GLAutoDrawable arg0) {
        //
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setPlayer(Player player) {
        this.player = player;
        needToReloadTextures = true;
    }

    private void loadTextures(GL2 gl) {
        if (level == null || player == null) {
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
