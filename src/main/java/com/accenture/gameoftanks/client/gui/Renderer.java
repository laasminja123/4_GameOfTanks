package com.accenture.gameoftanks.client.gui;

import com.accenture.gameoftanks.core.Level;
import com.accenture.gameoftanks.core.Player;
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

        // TODO: make transformations if needed ...

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
    }

    public void init(GLAutoDrawable gLDrawable) {
        final GL2 gl = gLDrawable.getGL().getGL2();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(0.0f);
    }

    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
        final GL2 gl = gLDrawable.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        float offset = 5.0f;
        gl.glOrtho(level.leftBoundary - offset, level.rightBoundary + offset,
                level.bottomBoundary - offset, level.topBoundary + offset, -1.0f, 1.0f);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public void dispose(GLAutoDrawable arg0) {
        //
    }
}
