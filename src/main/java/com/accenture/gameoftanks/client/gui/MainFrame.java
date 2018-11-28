package com.accenture.gameoftanks.client.gui;

import com.accenture.gameoftanks.client.net.PlayerConnection;
import com.accenture.gameoftanks.core.Intent;
import com.accenture.gameoftanks.core.Level;
import com.accenture.gameoftanks.core.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MainFrame extends JFrame implements KeyListener, MouseListener {

    // GUI
    private JTextField portEdit;
    private JTextField addressEdit;
    private JTextField nickInput;
    private JLabel nickText;
    private JLabel portText;
    private JLabel connectionText;

    private JButton moveUp;
    private JButton moveRight;
    private JButton moveLeft;
    private JButton moveDown;

    private boolean onLeft;
    private boolean onTop;
    private boolean onRight;
    private boolean onBottom;


    // Network
    private PlayerConnection playerConnection;
    private Player player;
    private Level level;

    public MainFrame() {
        initComponents();
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
        addKeyListener(new KeyListener() {

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

        getContentPane().setLayout(null);

        JLabel text = new JLabel("openGL please here");

        JPanel openGLhere = new JPanel();
        openGLhere.add(text);
        openGLhere.setSize(780, 450);
        openGLhere.setLocation(10, 0);
        openGLhere.setVisible(true);
        openGLhere.setBackground(Color.gray);
        getContentPane().add(openGLhere, BorderLayout.PAGE_START);

//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setSize(780, 300);
//        buttonPanel.setLocation(10, 360);
//        buttonPanel.setBackground(Color.cyan);
//        getContentPane().add(buttonPanel);

        moveUp = new JButton();
        JLabel moveUpText = new JLabel("UP");
        moveUpText.setSize(40, 15);
        moveUpText.setLocation(3, 1);
        moveUp.setSize(85, 40);
        moveUp.setBackground(Color.green);
        moveUp.setLocation(600, 460);
        moveUp.add(moveUpText);
        getContentPane().add(moveUp);
        moveUp.addMouseListener(this);

        moveRight = new JButton();
        JLabel moveRightText = new JLabel("RIGHT");
        moveRightText.setSize(40, 15);
        moveRightText.setLocation(3, 1);
        moveRight.setSize(85, 40);
        moveRight.setBackground(Color.green);
        moveRight.setLocation(510, 505);
        moveRight.add(moveRightText);
        getContentPane().add(moveRight);
        moveRight.addMouseListener(this);

        moveLeft = new JButton();
        JLabel moveLeftText = new JLabel("LEFT");
        moveLeftText.setSize(40, 15);
        moveLeftText.setLocation(3, 1);
        moveLeft.setSize(85, 40);
        moveLeft.setBackground(Color.green);
        moveLeft.setLocation(690, 505);
        moveLeft.add(moveLeftText);
        getContentPane().add(moveLeft);
        moveLeft.addMouseListener(this);

        moveDown = new JButton();
        JLabel moveDownText = new JLabel("DOWN");
        moveDownText.setSize(40, 15);
        moveDownText.setLocation(3, 1);
        moveDown.setSize(85, 40);
        moveDown.setBackground(Color.green);
        moveDown.setLocation(600, 550);
        moveDown.add(moveDownText);
        getContentPane().add(moveDown);
        moveDown.addMouseListener(this);


        JButton shootBtn = new JButton();
        JLabel shotTxt = new JLabel("SHOOT");
        shotTxt.setSize(40, 15);
        shotTxt.setLocation(3, 1);
        shootBtn.setSize(85, 40);
        shootBtn.setBackground(Color.red);
        shootBtn.setLocation(600, 505);
        shootBtn.add(shotTxt);
        getContentPane().add(shootBtn);
        shootBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                processShootAction();
            }
        });

        //instance.requestFocusInWindow();
//        JButton portBtn = new JButton();
//        JLabel portTxt = new JLabel("CONNECT PORT");
//        portTxt.setSize(40, 15);
//        portTxt.setLocation(3, 1);
//        portBtn.setSize(65 , 30);
//        portBtn.setBackground(Color.blue);
//        portBtn.setLocation(320, 495);
//        portBtn.add(portTxt);
//        getContentPane().add(portBtn);
//        portBtn.setActionCommand(Actions.PORT.name());
//        portBtn.addActionListener(this);


        nickInput.setSize(100, 40);
        nickInput.setLocation(160, 465);
//        nickInput.setText("your nick here");
        getContentPane().add(nickInput);

        nickText.setText("Your nickname here:");
        nickText.setLocation(10, 465);
        nickText.setSize(250, 40);
        getContentPane().add(nickText);


        portEdit.setSize(100, 40);
        portEdit.setLocation(160, 545);
        portEdit.setText("9999");
        getContentPane().add(portEdit);

        portText.setText("Enter SERVER port:");
        portText.setLocation(10, 545);
        portText.setSize(250, 40);
        getContentPane().add(portText);


        addressEdit.setSize(100, 40);
        addressEdit.setLocation(160, 505);
        addressEdit.setText("localhost");
        getContentPane().add(addressEdit);

        connectionText.setText("Enter SERVER adress:");
        connectionText.setLocation(8, 505);
        connectionText.setSize(250, 40);
        getContentPane().add(connectionText);

        JButton connBtn = new JButton();
        JLabel connTxt = new JLabel("CONNECT !");
        connTxt.setSize(35, 10);
        connTxt.setLocation(1, 1);
        connBtn.setSize(130 , 40);
        connBtn.setBackground(Color.blue);
        connBtn.setLocation(265, 505);
        connBtn.add(connTxt);
        getContentPane().add(connBtn);
        connBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                connect();
            }
        });
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {}

    public void keyReleased(KeyEvent e) {}

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        //
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == moveLeft) {
            onLeft = true;
        } else if (mouseEvent.getSource() == moveUp) {
            onTop = true;
        } else if (mouseEvent.getSource() == moveRight) {
            onRight = true;
        } else if (mouseEvent.getSource() == moveDown) {
            onBottom = true;
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
        }
        updateIntent();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}


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

        player = new Player(nickname);
        level = new Level(0.0f, 100.0f, 100.0f, 0.0f);
        playerConnection = new PlayerConnection(address, port, player);

        try {
            playerConnection.init();
        } catch (RuntimeException exc) {
            printMessage(exc.getMessage());
        }
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