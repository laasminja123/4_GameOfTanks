package com.accenture.gameoftanks.client.gui;

import com.accenture.gameoftanks.client.net.PlayerConnection;
import com.accenture.gameoftanks.core.Intent;
import com.accenture.gameoftanks.core.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MainFrame extends JFrame implements WindowListener, ActionListener, KeyListener {

    // GUI
    private JTextField portEdit;
    private JTextField addressEdit;
    private JTextField nickInput;
    private JLabel nickText;
    private JLabel portText;
    private JLabel connectionText;
    Player player;

    private boolean onLeft;
    private boolean onTop;
    private boolean onRight;
    private boolean onBottom;


    // Network
    private PlayerConnection playerConnection;

    private enum Actions {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        SHOOT,
        PORT,
        CONNECT,
    }

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

        //f0.setFocusable(true);
        //f0.requestFocusInWindow();

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

        JButton movementUp = new JButton();
        JLabel moveUpText = new JLabel("UP");
        moveUpText.setSize(40, 15);
        moveUpText.setLocation(3, 1);
        movementUp.setSize(85, 40);
        movementUp.setBackground(Color.green);
        movementUp.setLocation(600, 460);
        movementUp.add(moveUpText);
        getContentPane().add(movementUp);
        movementUp.setActionCommand(Actions.UP.name());
        movementUp.addActionListener(this);

        JButton movementRight = new JButton();
        JLabel moveRightText = new JLabel("RIGHT");
        moveRightText.setSize(40, 15);
        moveRightText.setLocation(3, 1);
        movementRight.setSize(85, 40);
        movementRight.setBackground(Color.green);
        movementRight.setLocation(510, 505);
        movementRight.add(moveRightText);
        getContentPane().add(movementRight);
        movementRight.setActionCommand(Actions.RIGHT.name());
        movementRight.addActionListener(this);

        JButton movementLeft = new JButton();
        JLabel moveLeftText = new JLabel("LEFT");
        moveLeftText.setSize(40, 15);
        moveLeftText.setLocation(3, 1);
        movementLeft.setSize(85, 40);
        movementLeft.setBackground(Color.green);
        movementLeft.setLocation(690, 505);
        movementLeft.add(moveLeftText);
        getContentPane().add(movementLeft);
        movementLeft.setActionCommand(Actions.LEFT.name());
        movementLeft.addActionListener(this);

        JButton movementDown = new JButton();
        JLabel moveDownText = new JLabel("DOWN");
        moveDownText.setSize(40, 15);
        moveDownText.setLocation(3, 1);
        movementDown.setSize(85, 40);
        movementDown.setBackground(Color.green);
        movementDown.setLocation(600, 550);
        movementDown.add(moveDownText);
        getContentPane().add(movementDown);
        movementDown.setActionCommand(Actions.DOWN.name());
        movementDown.addActionListener(this);


        JButton shootBtn = new JButton();
        JLabel shotTxt = new JLabel("SHOOT");
        shotTxt.setSize(40, 15);
        shotTxt.setLocation(3, 1);
        shootBtn.setSize(85, 40);
        shootBtn.setBackground(Color.red);
        shootBtn.setLocation(600, 505);
        shootBtn.add(shotTxt);
        getContentPane().add(shootBtn);
        shootBtn.setActionCommand(Actions.SHOOT.name());
        shootBtn.addActionListener(this);

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
        connBtn.setActionCommand(Actions.CONNECT.name());
        connBtn.addActionListener(this);
    }

    public void windowClosing(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}



    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals(Actions.UP.name())) {
            System.out.println("Driving UP");
            updateIntent();
        } else if (evt.getActionCommand().equals(Actions.DOWN.name())) {
            System.out.println("Driving Down");
        } else if (evt.getActionCommand().equals(Actions.RIGHT.name())) {
            System.out.println("Driving Right");
        } else if (evt.getActionCommand().equals(Actions.LEFT.name())) {
            System.out.println("Driving Left");
        } else if (evt.getActionCommand().equals(Actions.SHOOT.name())) {
            System.out.println("Im shooting BITCH !");
        } else if (evt.getActionCommand().equals(Actions.CONNECT.name())) {
            connect();
            System.out.println("connection here we go");
//        } else if (evt.getActionCommand() == Actions.PORT.name()) {
//            System.out.println("gimme that port");
        }
    }


    private void connect() {
        String address = addressEdit.getText();
        String nickname = nickInput.getText();
        int port;
        if (nickname.trim().isEmpty()){
            printMessage("Enter Nickname!");
        } else {

            try {
                port = Integer.parseInt(portEdit.getText());
            } catch (NumberFormatException exc) {
                printMessage("Bad Port Number!");
                return;
            }

            if (playerConnection == null) {
                player = new Player(nickname);
                playerConnection = new PlayerConnection(address, port, player);

                try {
                    playerConnection.init();
                } catch (RuntimeException exc) {
                    printMessage(exc.getMessage());
                }
            }
        }

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