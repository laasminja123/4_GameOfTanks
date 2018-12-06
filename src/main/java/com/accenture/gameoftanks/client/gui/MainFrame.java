package com.accenture.gameoftanks.client.gui;

import com.accenture.gameoftanks.client.net.PlayerConnection;
import com.accenture.gameoftanks.core.Player;
import com.accenture.gameoftanks.core.Intent;
import com.accenture.gameoftanks.core.Level;
import com.accenture.gameoftanks.core.Player;
import com.accenture.gameoftanks.core.Vehicle;
import com.accenture.gameoftanks.net.Data;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.border.Border;

public class MainFrame extends JFrame implements KeyListener, MouseListener, MouseMotionListener {

    private static final int RENDERER_TIME_STEP_MSEC = 40;

    // GUI
    private JTextField portEdit;
    private JTextField addressEdit;
    private JTextField nickInput;
    private JLabel nickText;
    private JLabel portText;
    private JLabel connectionText;
    private JLabel randomStuffText;

    private JPanel emptyspace;
    private JPanel base;
    private JPanel bottom;
    private JPanel bottomfirst;
    private JPanel bottomsecond;
    private JPanel forthleft;
    private JPanel forthmiddle;
    private JPanel forthright;
    private JPanel textInfoPanel;
    private JPanel textInputPanel;
    private JPanel conDiscPanel;
    private JPanel tankControlPanel;
    private JPanel tankStatusPanel;
    private JPanel tankInfoPanel;

    private JButton moveUp;
    private JButton moveRight;
    private JButton moveLeft;
    private JButton moveDown;
    private JButton shoot;
    private JButton connect;
    private JButton disconnect;

    private JProgressBar hp;
    private JProgressBar turretReload;

    private boolean onLeft;
    private boolean onTop;
    private boolean onRight;
    private boolean onBottom;
    private boolean onShoot;
    private boolean onAdjustShootingAngle;
    public boolean reloadBlock;
    private float shootingAngle;

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
    private int count = 50;

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
        renderArea.addKeyListener(new KeyProcessor());

        this.addKeyListener(new KeyProcessor());
        setFocusable(true);
        //setFocusTraversalKeysEnabled(false);

        //base layout
        getContentPane().add(base = new JPanel(), BorderLayout.CENTER);
        getContentPane().add(bottom = new JPanel(), BorderLayout.SOUTH);
        bottom.setPreferredSize(new Dimension(200, 100));
        base.add(renderArea);


        bottom.setLayout(new GridLayout(1 , 2, 0, 0));
        bottom.add(bottomfirst = new JPanel());
        bottom.add(bottomsecond = new JPanel());



        //bottom left panel with connection info buttons & text messages & movement button
        bottomfirst.setLayout(new GridLayout(1, 4, 5,0));

        bottomfirst.add(textInfoPanel = new JPanel());
        bottomfirst.add(textInputPanel = new JPanel());
        bottomfirst.add(conDiscPanel = new JPanel());
        bottomfirst.add(tankControlPanel = new JPanel());

        textInfoPanel.setLayout(new GridLayout(3,1,0,0));
        textInfoPanel.add(nickText = new JLabel("Your nickname here:"));
        textInfoPanel.add(connectionText = new JLabel("Enter SERVER address:"));
        textInfoPanel.add(portText = new JLabel("Enter SERVER port:"));

        textInputPanel.setLayout(new GridLayout(3,1,0,5));
        textInputPanel.add(nickInput = new JTextField("Player")); //TODO delete "player" word
        nickInput.addKeyListener(new KeyProcessor());
        textInputPanel.add(addressEdit = new JTextField("localhost"));
        addressEdit.addKeyListener(new KeyProcessor());
        textInputPanel.add(portEdit = new JTextField("9999"));
        portEdit.addKeyListener(new KeyProcessor());

        conDiscPanel.setLayout(new GridLayout(3,1,0,5));
        conDiscPanel.add(connect = new JButton("CONNECT"));
        connect.addKeyListener(new KeyProcessor());
        conDiscPanel.add(disconnect = new JButton("DISCONNECT"));
        disconnect.setEnabled(false);
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

                connect.setEnabled(true);
                disconnect.setEnabled(false);
            }
        });

        tankControlPanel.setLayout(new GridLayout(1,3,5,5));

        tankControlPanel.add(forthleft = new JPanel());
        tankControlPanel.add(forthmiddle = new JPanel());
        tankControlPanel.add(forthright = new JPanel());

        emptyspace = new JPanel();
        forthleft.setLayout(new GridLayout(3,1,5,5));
        forthleft.add(emptyspace);
        forthleft.add(moveLeft = new JButton(new ImageIcon("src/main/resources/buttonSkins/left.png")));
        moveLeft.addKeyListener(new KeyProcessor());
        moveLeft.addMouseListener(this);

        forthmiddle.setLayout(new GridLayout(3,1,1,5));
        forthmiddle.add(moveUp = new JButton(new ImageIcon("src/main/resources/buttonSkins/up.png")));
        moveUp.addKeyListener(new KeyProcessor());
        forthmiddle.add(shoot = new JButton(new ImageIcon("src/main/resources/buttonSkins/bullet.png")));
        shoot.addKeyListener(new KeyProcessor());
        forthmiddle.add(moveDown = new JButton(new ImageIcon("src/main/resources/buttonSkins/down.png")));
        moveDown.addKeyListener(new KeyProcessor());
        moveUp.addMouseListener(this);
        moveUp.addKeyListener(new KeyProcessor());
        moveDown.addMouseListener(this);
        shoot.addMouseListener(this);
        shoot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                onShoot = true;
                updateIntent();
            }
        });

        emptyspace = new JPanel();
        forthright.setLayout(new GridLayout(3,1,1,5));
        forthright.add(emptyspace);
        forthright.add(moveRight = new JButton(new ImageIcon("src/main/resources/buttonSkins/right.png")));
        moveRight.addKeyListener(new KeyProcessor());
        moveRight.addMouseListener(this);


        // bottom right panel
        bottomsecond.setLayout(new BorderLayout());
        bottomsecond.add(tankStatusPanel = new JPanel(), BorderLayout.CENTER);
        bottomsecond.add(tankInfoPanel = new JPanel(), BorderLayout.SOUTH);
        bottomsecond.setBackground(Color.red);
        tankInfoPanel.setPreferredSize(new Dimension(200,80));

        reloadBlock = new Boolean(false);

        tankStatusPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 1,0));
        tankStatusPanel.add(randomStuffText = new JLabel("Tank Healpoints:"));
        tankStatusPanel.add(hp = new JProgressBar());
        hp.setStringPainted(true);
        hp.setPreferredSize(new Dimension(150,25));
        hp.setBackground(Color.RED);
        hp.setForeground(Color.GREEN);
        hp.setString("50 / 100");
        hp.setValue(count);

        tankStatusPanel.add(randomStuffText = new JLabel("Turret Reload"));
        tankStatusPanel.add(turretReload = new JProgressBar());
        turretReload.setStringPainted(true);
        turretReload.setPreferredSize(new Dimension(150,25));
        turretReload.setBackground(Color.RED);
        turretReload.setForeground(Color.GREEN);
        turretReload.setValue(100);

        tankInfoPanel.setLayout(new GridLayout(2,2,0,0));
        tankInfoPanel.add(new JLabel("Your lives: "));
        tankInfoPanel.add(new JLabel("Your ammo"));
        tankInfoPanel.add(new JLabel("Total Players alive:"));
        tankInfoPanel.add(new JLabel("Ping - 13ms"));



        //font
        Font fontype1 = new Font("Courier New", Font.PLAIN, 10);
        Font fontype2 = new Font("Courier New", Font.PLAIN, 20);
        Font fontype3 = new Font("Courier New", Font.BOLD, 14);
        //buttons
//        moveUp.setFont(fontype1);
//        moveDown.setFont(fontype1);
//        moveRight.setFont(fontype1);
//        moveLeft.setFont(fontype1);
//        shoot.setFont(fontype1);
        connect.setFont(fontype2);
        disconnect.setFont(fontype2);
        //info texts
        nickText.setFont(fontype3);
        connectionText.setFont(fontype3);
        portText.setFont(fontype3);
        //input text
        nickInput.setFont(fontype2);
        addressEdit.setFont(fontype2);
        portEdit.setFont(fontype2);

        // button colors
        shoot.setBackground(Color.white);
        moveLeft.setBackground(Color.white);
        moveRight.setBackground(Color.white);
        moveUp.setBackground(Color.white);
        moveDown.setBackground(Color.white);
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
        } else if (event.getSource() == shoot){
            if (reloadBlock == false) { onShoot = true; reload(); }

        } else if (event.getSource() == renderArea) {
            mousePos1[0] = event.getX();
            mousePos1[1] = event.getY();

            if (SwingUtilities.isLeftMouseButton(event)) {
                onAdjustShootingAngle = true;
            }
        }
        updateIntent();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (event.getSource() == moveLeft) {
            onLeft = false;
        } else if (event.getSource() == moveUp) {
            onTop = false;
        } else if (event.getSource() == moveRight) {
            onRight = false;
        } else if (event.getSource() == moveDown) {
            onBottom = false;
        } else if (event.getSource() == shoot){
            onShoot = false;
        } else if (event.getSource() == renderArea) {
            if (SwingUtilities.isLeftMouseButton(event)) {
                onAdjustShootingAngle = false;
            }
            Renderer renderer = (Renderer) renderArea.getGLEventListener(0);
            renderer.dragLine(renderArea.getWidth(), renderArea.getHeight(), event.getX(), event.getY(), false);
            renderArea.display();
        }
        updateIntent();
    }

    @Override
    public void mouseEntered(MouseEvent event) {}

    @Override
    public void mouseExited(MouseEvent event) {}

    @Override
    public void mouseDragged(MouseEvent event) {
        if (event.getSource() == renderArea) {
            mousePos2[0] = event.getX();
            mousePos2[1] = event.getY();
            Renderer renderer = (Renderer) renderArea.getGLEventListener(0);

            if (SwingUtilities.isRightMouseButton(event)) {
                renderer.processMouseDrag(mousePos2[0] - mousePos1[0],
                        mousePos2[1] - mousePos1[1]);

                mousePos1[0] = event.getX();
                mousePos1[1] = event.getY();
            } else {
                renderer.dragLine(renderArea.getWidth(), renderArea.getHeight(), mousePos2[0], mousePos2[1], true);
            }
            renderArea.display();
        }
    }

    @Override
    public void mouseMoved(MouseEvent event) {}

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
        level = new Level();

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
            connect.setEnabled(false);
            disconnect.setEnabled(true);
            startRendererLoop();
            this.requestFocus();
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
                Renderer renderer = (Renderer) renderArea.getGLEventListener(0);

                while (onRender) {
                    try {
                        Thread.sleep(RENDERER_TIME_STEP_MSEC);
                    } catch (InterruptedException exc) {
                        //
                    }
                    updateControlPanel();

                    if (onAdjustShootingAngle) {
                        shootingAngle = renderer.getShootingAngle();
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

    private void updateControlPanel() {
        Player currentPlayer = playerData.getPlayer(nickName);
        Vehicle vehicle = currentPlayer.getVehicle();
        int currentHp = vehicle.getCurrentHp();
        int maxHp = vehicle.getStartingHp();

        hp.setString(currentHp + " / " + maxHp);
        hp.setValue((currentHp / maxHp) * 100);

        // todo
        // label.setText ...
        currentPlayer.getKills();
        currentPlayer.getDeaths();
        currentPlayer.getShoots();
    }

    private void updateIntent() {
        if (playerData == null) {
            return;
        }
        Player player = playerData.getPlayer(nickName);

        if (player != null) {
            Intent intent = player.getVehicle().getIntent();
            intent.update(onLeft, onRight, onTop, onBottom, onShoot, onAdjustShootingAngle, shootingAngle);
        }
    }

    private void printMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    private class KeyProcessor implements KeyListener {
        public void keyTyped(KeyEvent e) {}

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode()== KeyEvent.VK_D) {
                onRight = true;
            } else if (e.getKeyCode()== KeyEvent.VK_A) {
                onLeft = true;
            } else if (e.getKeyCode()== KeyEvent.VK_S) {
                onBottom = true;
            } else if (e.getKeyCode()== KeyEvent.VK_W) {
                onTop = true;
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                onShoot = true;
            } else {
                return;
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
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                onShoot = false;
            } else {
                return;
            }
            updateIntent();
        }

    }

        private void reload() {

        Timer timer = new Timer(20, new ActionListener() {

            int i = 0;

            public void actionPerformed(ActionEvent evt) {
                reloadBlock = true;
                i++;
                turretReload.setValue(i);
                getContentPane().repaint();
                if (i == 100) {
                    reloadBlock = false;
                    ((Timer) evt.getSource()).stop();
                }
            }
        });
        if (reloadBlock == false) {
            timer.start();
        } else {
            return;
        }
    }
}