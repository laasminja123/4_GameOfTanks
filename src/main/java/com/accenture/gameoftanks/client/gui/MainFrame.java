package com.accenture.gameoftanks.client.gui;

import com.accenture.gameoftanks.client.net.PlayerConnection;
import com.accenture.gameoftanks.core.Intent;
import com.accenture.gameoftanks.core.Level;
import com.accenture.gameoftanks.core.Player;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MainFrame extends JFrame implements KeyListener, MouseListener, MouseMotionListener {

    private static final int RENDERER_TIME_STEP_MSEC = 40;

    // GUI
    private JTextField portEdit;
    private JTextField addressEdit;
    private JTextField nickInput;
    private JLabel nickText;
    private JLabel portText;
    private JLabel connectionText;

    private JPanel base;
    private JPanel center;
    private JPanel bottom;
    private JPanel bottomfirst;
    private JPanel bottomsecond;
    private JPanel bottomrightfirst;
    private JPanel bottomrightsecond;
    private JPanel bottomrightthird;
    private JPanel bottomleftfirst;
    private JPanel bottomleftsecond;
    private JPanel bottomleftthird;

    private JButton moveUp;
    private JButton moveRight;
    private JButton moveLeft;
    private JButton moveDown;
    private JButton shoot;
    private JButton connect;
    private JButton disconnect;

    private boolean onLeft;
    private boolean onTop;
    private boolean onRight;
    private boolean onBottom;

    // GL Viewport
    private GLCanvas renderArea;
    private Thread rendererLoop;
    private boolean onRender;

    // Network
    private PlayerConnection playerConnection;
    private Player player;
    private Level level;

    // mouse motion processing
    private int [] mousePos1;
    private int [] mousePos2;

    public MainFrame() {
        this.setFocusable(true);
        initComponents();
        setupGLMouseHandler();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        portEdit = new JTextField();
        addressEdit = new JTextField();
        nickInput = new JTextField();
        nickText = new JLabel();
        portText = new JLabel();
        connectionText = new JLabel();

        //JFrame f0 = new JFrame("Games Of Tanks");
        setSize(800, 650);
        setTitle("Game Of Tanks");

        //getContentPane.requestFocusInWindow();   // must try this to check about key listeneres !!!

        getContentPane().setLayout(new BorderLayout());

        // OPEN GL panel
        renderArea = new GLCanvas();
        renderArea.addGLEventListener(new Renderer(null, null));
        renderArea.setSize(1780, 1450);

        //base layout next --->
        getContentPane().add(base = new JPanel(), BorderLayout.CENTER);
        getContentPane().add(bottom = new JPanel(), BorderLayout.SOUTH);
        bottom.setPreferredSize(new Dimension(200, 200));
        base.add(renderArea);


        bottom.setLayout(new GridLayout(1 , 2, 0, 0));
        bottom.add(bottomfirst = new JPanel());
        bottom.add(bottomsecond = new JPanel());

        bottomsecond.setLayout(new GridLayout(1, 3, 0,0));
        bottomfirst.setLayout(new GridLayout(1, 3, 0,0));

        bottomfirst.add(bottomleftfirst = new JPanel());
        bottomfirst.add(bottomleftsecond = new JPanel());
        bottomfirst.add(bottomleftthird = new JPanel());

        bottomleftfirst.setLayout(new GridLayout(3,1,0,0));
        bottomleftfirst.add(nickText = new JLabel("Your nickname here:"));
        bottomleftfirst.add(connectionText = new JLabel("Enter SERVER adres:"));
        bottomleftfirst.add(portText = new JLabel("Enter SERVER port:"));

        bottomleftsecond.setLayout(new GridLayout(3,1,0,0));
        bottomleftsecond.add(nickInput = new JTextField());
        bottomleftsecond.add(addressEdit = new JTextField("localhost"));
        bottomleftsecond.add(portEdit = new JTextField("9999"));

        bottomleftthird.setLayout(new GridLayout(3,1,0,0));
        bottomleftthird.add(connect = new JButton("CONNECT"));
        bottomleftthird.add(disconnect = new JButton("DISCONNECT"));
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                connect();
            }
        });

        //TODO disconnect script

        bottomsecond.add(bottomrightfirst = new JPanel());
        bottomsecond.add(bottomrightsecond = new JPanel());
        bottomsecond.add(bottomrightthird = new JPanel());

        bottomrightfirst.setLayout(new BorderLayout());
        bottomrightfirst.add(moveLeft = new JButton("LEFT"), BorderLayout.CENTER);
        moveLeft.addMouseListener(this);

        bottomrightsecond.setLayout(new GridLayout(3, 1, 0, 0));
        bottomrightsecond.add(moveUp = new JButton("UP"));
        bottomrightsecond.add(shoot = new JButton("SHOOT"));
        bottomrightsecond.add(moveDown = new JButton("DOWN"));
        moveUp.addMouseListener(this);
        moveDown.addMouseListener(this);
        shoot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                processShootAction();
            }
        });

        bottomrightthird.setLayout(new BorderLayout());
        bottomrightthird.add(moveRight = new JButton("RIGHT"), BorderLayout.CENTER);
        moveRight.addMouseListener(this);


        getContentPane().addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_RIGHT) {
                    System.out.println("right"); }
                if(e.getKeyCode()== KeyEvent.VK_LEFT) {
                    System.out.println("left"); }
                if(e.getKeyCode()== KeyEvent.VK_DOWN) {
                    System.out.println("down"); }
                if(e.getKeyCode()== KeyEvent.VK_UP) {
                    System.out.println("up"); }
            }

            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_RIGHT) {
                    System.out.println("right"); }
                if(e.getKeyCode()== KeyEvent.VK_LEFT) {
                    System.out.println("left"); }
                if(e.getKeyCode()== KeyEvent.VK_DOWN) {
                    System.out.println("down"); }
                if(e.getKeyCode()== KeyEvent.VK_UP) {
                    System.out.println("up"); }
            }

            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_RIGHT) {
                    System.out.println("right"); }
                else if(e.getKeyCode()== KeyEvent.VK_LEFT) {
                    System.out.println("left"); }
                else if(e.getKeyCode()== KeyEvent.VK_DOWN) {
                    System.out.println("down"); }
                else if(e.getKeyCode()== KeyEvent.VK_UP) {
                    System.out.println("up"); }
            }
        });
    }

    private void setupGLMouseHandler() {
        renderArea.addMouseListener(this);
        renderArea.addMouseMotionListener(this);
        renderArea.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                int notches = event.getWheelRotation();

                if (notches < 0) {
                    updateRendererScale(1);
                } else {
                    updateRendererScale(2);
                }
            }
        });
        mousePos1 = new int[2];
        mousePos2 = new int[2];
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        //
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (event.getSource() == moveLeft) {
            onLeft = true;
        } else if (event.getSource() == moveUp) {
            onTop = true;
        } else if (event.getSource() == moveRight) {
            onRight = true;
        } else if (event.getSource() == moveDown) {
            onBottom = true;
        } else if (event.getSource() == renderArea) {
            mousePos1[0] = event.getX();
            mousePos1[1] = event.getY();
        }
        updateIntent();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == moveLeft) {
            onLeft = false;
        } else if (mouseEvent.getSource() == moveUp) {
            onTop = false;
        } else if (mouseEvent.getSource() == moveRight) {
            onRight = false;
        } else if (mouseEvent.getSource() == moveDown) {
            onBottom = false;
        } else if (mouseEvent.getSource() == renderArea) {
            // todo
        }
        updateIntent();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    @Override
    public void mouseDragged(MouseEvent event) {
        mousePos2[0] = event.getX();
        mousePos2[1] = event.getY();
        updateRendererPosition();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    private void updateRendererPosition() {
        Renderer renderer = (Renderer) renderArea.getGLEventListener(0);
        int dx = (mousePos2[0] - mousePos1[0]) / 5;
        int dy = -(mousePos2[1] - mousePos1[1]) / 5;

        if (renderer != null) {
            renderer.setPosition(dx, dy);
        }
    }

    private void updateRendererScale(int action) {
        // action 1: up
        // action 2: down
        Renderer renderer = (Renderer) renderArea.getGLEventListener(0);

        if (renderer != null) {
            if (action == 1) {
                renderer.scaleIn();
            } else if (action == 2) {
                renderer.scaleOut();
            }
        }
    }


    private void connect() {
        if (playerConnection != null) {
            printMessage("Already connected!");
            return;
        }

        String address = addressEdit.getText();

        if (address.trim().isEmpty()) {
            printMessage("Enter Server IP address!");
            return;
        }

        String portS = portEdit.getText();

        if (portS.trim().isEmpty()) {
            printMessage("Enter Server port!");
            return;
        }
        int port;

        try {
            port = Integer.parseInt(portEdit.getText());
        } catch (NumberFormatException exc) {
            printMessage("Bad Port Number!");
            return;
        }

        String nickname = nickInput.getText();

        if (nickname.trim().isEmpty()) {
            printMessage("Enter Nickname!");
            return;
        }

        // create data
        player = new Player(nickname);
        level = new Level(0.0f, 100.0f, 100.0f, 0.0f);

        // update renderer
        Renderer renderer = (Renderer) renderArea.getGLEventListener(0);

        if (renderer != null) {
            renderer.setLevel(level);
            renderer.setPlayer(player);
        }
        renderArea.reshape(0, 0, renderArea.getWidth(), renderArea.getHeight());
        renderArea.display();

        // create connection
        playerConnection = new PlayerConnection(address, port, player);

        try {
            playerConnection.init();
            startRendererLoop();
        } catch (RuntimeException exc) {
            printMessage(exc.getMessage());
        }
    }

    private void startRendererLoop() {
        if (isRendererAlive()) {
            return;
        }

        rendererLoop = new Thread(new Runnable() {
            @Override
            public void run() {
                while (onRender) {
                    try {
                        Thread.sleep(RENDERER_TIME_STEP_MSEC);
                    } catch (InterruptedException exc) {
                        //
                    }
                    // TEST
                    //player.getTank().getPosition().posX += 1.0f;

                    renderArea.display();
                }
                rendererLoop = null;
            }
        });
        onRender = true;
        rendererLoop.start();
    }

    private boolean isRendererAlive() {
        return rendererLoop != null && rendererLoop.isAlive();
    }

    private void processShootAction() {
        // TODO
    }

    private void updateIntent() {
        if (player == null) {
            return;
        }

        Intent intent = player.getTank().getIntent();
        intent.computeIntent(onLeft, onTop, onRight, onBottom);
    }

    private void printMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}