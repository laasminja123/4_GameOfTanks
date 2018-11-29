package com.accenture.gameoftanks.client.gui;

import com.accenture.gameoftanks.core.Level;
import com.accenture.gameoftanks.core.Player;
import com.accenture.gameoftanks.core.Position;
import com.accenture.gameoftanks.core.Tank;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class Renderer  implements GLEventListener {

    private Level level;
    private Player player;

    public Renderer(Level level, Player player) {
        this.level = level;
        this.player = player;
    }

    public void display(GLAutoDrawable gLDrawable) {
        final GL2 gl = gLDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        if (level == null || player == null) {
            return;
        }

        gl.glTranslatef(-level.getWidth() / 2.0f, -level.getHeight() / 2.0f, 0.0f);
        //gl.glTranslatef(-5.0f, 0.0f, 0.0f);

        // draw level boundaries
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL2.GL_LINES);
        {
            // left
            gl.glVertex3f(level.leftBoundary, level.bottomBoundary, 0.0f);
            gl.glVertex3f(level.leftBoundary, level.topBoundary, 0.0f);

            // right
            gl.glVertex3f(level.rightBoundary, level.bottomBoundary, 0.0f);
            gl.glVertex3f(level.rightBoundary, level.topBoundary, 0.0f);

            // top
            gl.glVertex3f(level.leftBoundary, level.bottomBoundary, 0.0f);
            gl.glVertex3f(level.rightBoundary, level.bottomBoundary, 0.0f);

            // left
            gl.glVertex3f(level.leftBoundary, level.topBoundary, 0.0f);
            gl.glVertex3f(level.rightBoundary, level.topBoundary, 0.0f);
        }
        gl.glEnd();

        // draw tank
        Tank tank = player.getTank();
        Position position = tank.getPosition();
        float length = tank.getLength();
        float width  = tank.getWidth();

        gl.glColor3f(0.0f, 1.0f, 1.0f);
        gl.glBegin(GL2.GL_QUADS);
        {
            // left
            gl.glVertex3f(position.posX - length / 2.0f, position.posY - width / 2.0f, 0.0f);
            gl.glVertex3f(position.posX + length / 2.0f, position.posY - width / 2.0f, 0.0f);
            gl.glVertex3f(position.posX + length / 2.0f, position.posY + width / 2.0f, 0.0f);
            gl.glVertex3f(position.posX - length / 2.0f, position.posY + width / 2.0f, 0.0f);
        }
        gl.glEnd();
        //System.out.println("Position in renderer is: " + position.posX);
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
    }
}
