package com.accenture.gameoftanks.client.gui;

import com.accenture.gameoftanks.client.net.PlayerConnection;
import com.accenture.gameoftanks.core.Player;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MainFrame extends JFrame implements WindowListener, ActionListener, KeyListener {

    // GUI
    private JTextField portEdit;
    private JTextField addressEdit;

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

        JLabel text = new JLabel("openGL please here");

        JPanel f1 = new JPanel();
        getContentPane().setLayout(null);
        f1.add(text);
        f1.setSize(780, 450);
        f1.setLocation(10, 0);
        f1.setVisible(true);
        f1.setBackground(Color.gray);
        getContentPane().add(f1);

        JButton mvmU = new JButton();
        JLabel lblu = new JLabel("Up");
        lblu.setSize(40, 15);
        lblu.setLocation(3, 1);
        mvmU.setSize(45, 20);
        mvmU.setBackground(Color.green);
        mvmU.setLocation(560, 470);
        mvmU.add(lblu);
        getContentPane().add(mvmU);
        mvmU.setActionCommand(Actions.UP.name());
        mvmU.addActionListener(this);

        JButton mvmR = new JButton();
        JLabel lblr = new JLabel("Right");
        lblr.setSize(40, 15);
        lblr.setLocation(3, 1);
        mvmR.setSize(45, 20);
        mvmR.setBackground(Color.green);
        mvmR.setLocation(610, 495);
        mvmR.add(lblr);
        getContentPane().add(mvmR);
        mvmR.setActionCommand(Actions.RIGHT.name());
        mvmR.addActionListener(this);

        JButton mvmL = new JButton();
        JLabel lbll = new JLabel("Left");
        lbll.setSize(40, 15);
        lbll.setLocation(3, 1);
        mvmL.setSize(45, 20);
        mvmL.setBackground(Color.green);
        mvmL.setLocation(510, 495);
        mvmL.add(lbll);
        getContentPane().add(mvmL);
        mvmL.setActionCommand(Actions.LEFT.name());
        mvmL.addActionListener(this);

        JButton mvmD = new JButton();
        JLabel lbld = new JLabel("Down");
        lbld.setSize(40, 15);
        lbld.setLocation(3, 1);
        mvmD.setSize(45, 20);
        mvmD.setBackground(Color.green);
        mvmD.setLocation(560, 495);
        mvmD.add(lbld);
        getContentPane().add(mvmD);
        mvmD.setActionCommand(Actions.DOWN.name());
        mvmD.addActionListener(this);


        JButton shootBtn = new JButton();
        JLabel shotTxt = new JLabel("SHOOT ME PLEASE");
        shotTxt.setSize(40, 15);
        shotTxt.setLocation(3, 1);
        shootBtn.setSize(65, 30);
        shootBtn.setBackground(Color.green);
        shootBtn.setLocation(360, 495);
        shootBtn.add(shotTxt);
        getContentPane().add(shootBtn);
        shootBtn.setActionCommand(Actions.SHOOT.name());
        shootBtn.addActionListener(this);

        //instance.requestFocusInWindow();

        portEdit.setSize(100, 40);
        portEdit.setLocation(60, 525);
        portEdit.setText("9999");
        getContentPane().add(portEdit);

        JButton portBtn = new JButton();
        JLabel portTxt = new JLabel("CONNECT PORT");
        portTxt.setSize(40, 15);
        portTxt.setLocation(3, 1);
        portBtn.setSize(65 , 30);
        portBtn.setBackground(Color.blue);
        portBtn.setLocation(320, 495);
        portBtn.add(portTxt);
        getContentPane().add(portBtn);
        portBtn.setActionCommand(Actions.PORT.name());
        portBtn.addActionListener(this);


        addressEdit.setSize(100, 40);
        addressEdit.setLocation(60, 465);
        addressEdit.setText("localhost");
        getContentPane().add(addressEdit);

        JButton connBtn = new JButton();
        JLabel connTxt = new JLabel("CONNECTION");
        connTxt.setSize(40, 15);
        connTxt.setLocation(3, 1);
        connBtn.setSize(65 , 30);
        connBtn.setBackground(Color.blue);
        connBtn.setLocation(320, 465);
        connBtn.add(connTxt);
        getContentPane().add(connBtn);
        connBtn.setActionCommand(Actions.CONNECT.name());
        connBtn.addActionListener(this);
    }

    public void windowClosing(WindowEvent e) {
        System.out.println("win exit");
        dispose();
        System.exit(0);
    }


    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {
        System.out.println("win exit");
        dispose();
        System.exit(0);
    }


    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand() == Actions.UP.name()) {
            System.out.println("Driving UP");
        } else if (evt.getActionCommand() == Actions.DOWN.name()) {
            System.out.println("Driving Down");
        } else if (evt.getActionCommand() == Actions.RIGHT.name()) {
            System.out.println("Driving Right");
        } else if (evt.getActionCommand() == Actions.DOWN.name()) {
            System.out.println("Driving Left");
        } else if (evt.getActionCommand() == Actions.SHOOT.name()) {
            System.out.println("Im shooting BITCH !");
        } else if (evt.getActionCommand() == Actions.PORT.name()) {
            System.out.println("gimme that port");
        } else if (evt.getActionCommand() == Actions.CONNECT.name()) {
            connect();
            System.out.println("connection here we go");
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

    private void connect() {
        String address = addressEdit.getText();
        int port;

        try {
            port = Integer.parseInt(portEdit.getText());
        } catch (NumberFormatException exc) {
            printMessage("Bad Port Number!");
            return;
        }
        String nick = null; // TODO nick field

        // TODO check values
        if (playerConnection == null) {
            Player player = new Player(nick);
            playerConnection = new PlayerConnection(address, port, player);

            try {
                playerConnection.init();
            } catch (RuntimeException exc) {
                printMessage(exc.getMessage());
            }
        }
    }

    private void printMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

}