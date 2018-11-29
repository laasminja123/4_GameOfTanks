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
            gl.glVertex3f(level.leftBoundary, level.bottomBoundary, 0.0f);

            gl.glTexCoord2f(uvCoords[1][0], uvCoords[1][1]);
            gl.glVertex3f(level.rightBoundary, level.bottomBoundary, 0.0f);

            gl.glTexCoord2f(uvCoords[2][0], uvCoords[2][1]);
            gl.glVertex3f(level.rightBoundary, level.topBoundary, 0.0f);

            gl.glTexCoord2f(uvCoords[3][0], uvCoords[3][1]);
            gl.glVertex3f(level.leftBoundary, level.topBoundary, 0.0f);
        }
        gl.glEnd();

        // draw tank
        Tank tank = player.getTank();
        Vertex [] vertices = computeTankPosition(tank);
        id = level.getTextureID("tank01.bmp");
        texture = usedTextures.get(id);
        texture.enable(gl);
        texture.bind(gl);

        //gl.glColor3f(0.0f, 1.0f, 1.0f);
        gl.glBegin(GL2.GL_QUADS);
        {
            gl.glTexCoord2f(uvCoords[0][0], uvCoords[0][1]);
            gl.glVertex3f(vertices[0].x, vertices[0].y, vertices[0].z);

            gl.glTexCoord2f(uvCoords[1][0], uvCoords[1][1]);
            gl.glVertex3f(vertices[1].x, vertices[1].y, vertices[1].z);

            gl.glTexCoord2f(uvCoords[2][0], uvCoords[2][1]);
            gl.glVertex3f(vertices[2].x, vertices[2].y, vertices[2].z);

            gl.glTexCoord2f(uvCoords[3][0], uvCoords[3][1]);
            gl.glVertex3f(vertices[3].x, vertices[3].y, vertices[3].z);
        }
        gl.glEnd();
        //System.out.println("Position in renderer is: " + position.posX);
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

    private Vertex [] computeTankPosition(Tank tank) {
        Position position = tank.getPosition();
        float length = tank.getLength();
        float width  = tank.getWidth();

        // create new vertices in coordinate zero position
        Vertex v1 = new Vertex(-length / 2.0f, -width / 2.0f);
        Vertex v2 = new Vertex( length / 2.0f, -width / 2.0f);
        Vertex v3 = new Vertex( length / 2.0f,  width / 2.0f);
        Vertex v4 = new Vertex(-length / 2.0f,  width / 2.0f);

        // rotate vertices
        MATH.rotate2d(position.alpha, v1, v2, v3, v4);

        // translate vertices in actual position
        v1.x += position.posX;
        v1.y += position.posY;

        v2.x += position.posX;
        v2.y += position.posY;

        v3.x += position.posX;
        v3.y += position.posY;

        v4.x += position.posX;
        v4.y += position.posY;

        return new Vertex[] {v1, v2, v3, v4};
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
