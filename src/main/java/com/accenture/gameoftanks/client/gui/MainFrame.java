package com.accenture.gameoftanks.client.gui;

import com.accenture.gameoftanks.client.net.PlayerConnection;
import com.accenture.gameoftanks.core.Intent;
import com.accenture.gameoftanks.core.Level;
import com.accenture.gameoftanks.core.Player;
import com.accenture.gameoftanks.net.Data;
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
    private JPanel emptyspace;
    private JPanel bottom;
    private JPanel bottomfirst;
    private JPanel bottomsecond;
    private JPanel rightfirst;
    private JPanel rightsecond;
    private JPanel rightthird;
    private JPanel leftfirst;
    private JPanel leftsecond;
    private JPanel leftthird;

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
    private Data playerData;
    private Level level;
    private String nickName;

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
        setSize(1100, 850);
        setTitle("Game Of Tanks");

        //getContentPane.requestFocusInWindow();   // must try this to check about key listeneres !!!

        getContentPane().setLayout(new BorderLayout());

        // OPEN GL panel
        renderArea = new GLCanvas();
        renderArea.addGLEventListener(new Renderer());
        renderArea.setSize(1780, 1450);
//        renderArea.setFocusable(true);
        renderArea.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_D) {
                    onRight = true;
                } else if(e.getKeyCode()== KeyEvent.VK_A) {
                    onLeft = true;
                } else if(e.getKeyCode()== KeyEvent.VK_S) {
                    onBottom = true;
                } else if(e.getKeyCode()== KeyEvent.VK_W) {
                    onTop = true;
                }
                updateIntent();
            }

            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    onRight = false;
                } else if (e.getKeyCode() == KeyEvent.VK_A) {
                    onLeft = false;
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    onBottom = false;
                } else if (e.getKeyCode() == KeyEvent.VK_W) {
                    onTop = false;
                }
                updateIntent();
            }

        });

        //base layout
        getContentPane().add(base = new JPanel(), BorderLayout.CENTER);
        getContentPane().add(bottom = new JPanel(), BorderLayout.SOUTH);
        bottom.setPreferredSize(new Dimension(200, 100));
        base.add(renderArea);


        bottom.setLayout(new GridLayout(1 , 2, 100, 0));
        bottom.add(bottomfirst = new JPanel());
        bottom.add(bottomsecond = new JPanel());

        bottomsecond.setLayout(new GridLayout(1, 3, 5,0));
        bottomfirst.setLayout(new GridLayout(1, 3, 5,0));


        //bottom left panel with connection info buttons & text messages
        bottomfirst.add(leftfirst = new JPanel());
        bottomfirst.add(leftsecond = new JPanel());
        bottomfirst.add(leftthird = new JPanel());

        leftfirst.setLayout(new GridLayout(3,1,0,0));
        leftfirst.add(nickText = new JLabel("Your nickname here:"));
        leftfirst.add(connectionText = new JLabel("Enter SERVER address:"));
        leftfirst.add(portText = new JLabel("Enter SERVER port:"));

        leftsecond.setLayout(new GridLayout(3,1,0,5));
        leftsecond.add(nickInput = new JTextField("Player"));
        leftsecond.add(addressEdit = new JTextField("localhost"));
        leftsecond.add(portEdit = new JTextField("9999"));

        leftthird.setLayout(new GridLayout(3,1,0,5));
        leftthird.add(connect = new JButton("CONNECT"));
        leftthird.add(disconnect = new JButton("DISCONNECT"));
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                connect();
            }
        });

        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                playerConnection.disconnect();

                // update renderer
                Renderer renderer = (Renderer) renderArea.getGLEventListener(0);

                if (renderer != null) {
                    renderer.removeGameData();
                }
            }
        });

        // bottom right panel with control buttons
        bottomsecond.add(rightfirst = new JPanel());
        bottomsecond.add(rightsecond = new JPanel());
        bottomsecond.add(rightthird = new JPanel());

        emptyspace = new JPanel();
        rightfirst.setLayout(new GridLayout(3,1,0,5));
        rightfirst.add(emptyspace);
        rightfirst.add(moveLeft = new JButton("TURN LEFT"));
        moveLeft.addMouseListener(this);

        rightsecond.setLayout(new GridLayout(3, 1, 0, 5));
        rightsecond.add(moveUp = new JButton("FORWARD"));
        rightsecond.add(shoot = new JButton("SHOOT"));
        rightsecond.add(moveDown = new JButton("BACKWARD"));
        moveUp.addMouseListener(this);
        moveDown.addMouseListener(this);
        shoot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                processShootAction();
            }
        });

        emptyspace = new JPanel();
        rightthird.setLayout(new GridLayout(3,1,0,5));
        rightthird.add(emptyspace);
        rightthird.add(moveRight = new JButton("TURN RIGHT"));
        moveRight.addMouseListener(this);

        // button colors
        shoot.setBackground(Color.decode("#e91640"));
        moveLeft.setBackground(Color.decode("#80e916"));
        moveRight.setBackground(Color.decode("#80e916"));
        moveUp.setBackground(Color.decode("#80e916"));
        moveDown.setBackground(Color.decode("#80e916"));
        connect.setBackground(Color.decode("#164be9"));
        disconnect.setBackground(Color.decode("#164be9"));

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

        nickName = nickInput.getText();

        if (nickName.trim().isEmpty()) {
            printMessage("Enter Nickname!");
            return;
        }

        // create data
        Player player = new Player(nickName);
        playerData = new Data(player);
        level = new Level(0.0f, 100.0f, 100.0f, 0.0f);

        // update renderer
        Renderer renderer = (Renderer) renderArea.getGLEventListener(0);

        if (renderer != null) {
            renderer.setupGameData(level, playerData, nickName);
        }
        renderArea.reshape(0, 0, renderArea.getWidth(), renderArea.getHeight());
        renderArea.display();

        // create connection
        playerConnection = new PlayerConnection(address, port, playerData);

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
        Player player = playerData.getPlayer(nickName);

        if (player != null) {
            Intent intent = player.getVehicle().getIntent();
            intent.update(onLeft, onRight, onTop, onBottom);
        }
    }

    private void printMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

}