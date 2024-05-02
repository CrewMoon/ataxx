package ataxx;

// Optional Task: The GUI for the Ataxx Game

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.Timer;

class GUI extends JPanel implements View, CommandSource, Reporter, MouseListener{

    private Board board;
    private JFrame jFrame;
    private static final int WIDTH =  900;
    private static final int HEIGHT = 800;
    private static final Object MONITOR = new Object();
    private static final int BOARD_OFFSET_HEIGHT = 190;
    private static final int BOARD_OFFSET_WIDTH = 100;
    private static final int CELL_SIZE = 50;
    private static final int ROW = 7;
    private static final int COL = 7;
    private static final int ITEM_SIZE = 25;
    private static final  int MIDDLE = (CELL_SIZE-ITEM_SIZE)/2;
    private int[] fromLocation;
    private volatile String command;
    private String message;
    private PieceState winner;

    private static final char[] COLS = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
    private static final char[] ROWS = {'7', '6', '5', '4', '3', '2', '1'};
    Font f1=new Font("TimesRoman",Font.BOLD,24);
    Font f2=new Font("TimesRoman",Font.BOLD,50);
    Font f3=new Font("TimesRoman",Font.BOLD,18);
    Color pale = new Color(250,235,215);
    Color chessboardColor = new Color(178, 196, 171);
    Color chose = new Color(255, 125, 16,126);

    private JPanel jPanelAnnounce = new JPanel();
    private JPanel jPanel_R = new JPanel();
    private JPanel jPanel1=new JPanel(new GridLayout(3,1,1,0));
    private JPanel jPanel2=new JPanel(new GridLayout(3,1,1,0));
    private JRadioButton RedHuman = new JRadioButton("Human");
    private JRadioButton RedAI = new JRadioButton("AI");
    private  JRadioButton BlueHuman = new JRadioButton("Human");
    private JRadioButton BlueAI = new JRadioButton("AI");


    // Complete the codes here
    GUI(String ataxx) {
        jFrame = new JFrame();
        jFrame.setTitle(ataxx);
        jFrame.setSize(WIDTH,HEIGHT);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.addMouseListener(this);
        fromLocation = new int[]{-1, -1};

        jPanel1.setPreferredSize(new Dimension(140,150));
        JLabel jLabel1 = new JLabel();
        jLabel1.setText("Red Player:");
        jLabel1.setFont(f1);
        RedHuman.setSelected(true);
        BlueAI.setSelected(true);
        jPanel1.add(jLabel1,BorderLayout.LINE_START);
        jPanel1.add(RedHuman,BorderLayout.AFTER_LINE_ENDS);
        jPanel1.add(RedAI,BorderLayout.AFTER_LAST_LINE);
        RedHuman.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (RedAI.isSelected() && BlueAI.isSelected() && board.getWinner() == null) {
                    RedHuman.setSelected(false);
                } else if (RedHuman.isSelected()){
                    RedAI.setSelected(false);
                    command = "manual red";
                    fromLocation[0] = -1;
                    message = null;
                    synchronized (MONITOR){
                        MONITOR.notifyAll();
                    }
                } else if (!RedAI.isSelected()) {
                    RedHuman.setSelected(true);
                }
            }
        });
        RedAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (RedAI.isSelected()){
                    RedHuman.setSelected(false);
                    command = "ai red";
                    fromLocation[0] = -1;
                    message = null;
                    synchronized (MONITOR){
                        MONITOR.notifyAll();
                    }
                } else if (!RedHuman.isSelected()) {
                    RedAI.setSelected(true);
                }

            }
        });

        jPanel2.setPreferredSize(new Dimension(140,150));
        JLabel jLabel2 = new JLabel();
        jLabel2.setText("Blue Player:");
        jLabel2.setFont(f1);
        jPanel2.add(jLabel2,BorderLayout.LINE_START);
        jPanel2.add(BlueHuman,BorderLayout.AFTER_LINE_ENDS);
        jPanel2.add(BlueAI,BorderLayout.AFTER_LAST_LINE);
        BlueHuman.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (RedAI.isSelected() && BlueAI.isSelected() && board.getWinner() == null) {
                    BlueHuman.setSelected(false);
                } else if (BlueHuman.isSelected()) {
                    BlueAI.setSelected(false);
                    command = "manual blue";
                    fromLocation[0] = -1;
                    message = null;
                    synchronized (MONITOR){
                        MONITOR.notifyAll();
                    }
                } else if (!BlueAI.isSelected()) {
                    BlueHuman.setSelected(true);
                }
            }
        });
        BlueAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (BlueAI.isSelected()){
                    BlueHuman.setSelected(false);
                    command = "ai blue";
                    fromLocation[0] = -1;
                    message = null;
                    synchronized (MONITOR){
                        MONITOR.notifyAll();
                    }
                } else if (!BlueHuman.isSelected()) {
                    BlueAI.setSelected(true);
                }

            }
        });


        JPanel jPanel = new JPanel(new FlowLayout());
        jPanel.setPreferredSize(new Dimension(140,150));
        JButton passBtn = new JButton("Pass");
        passBtn.setFont(f3);
        passBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                command = "-";
                fromLocation[0] = -1;
                synchronized (MONITOR){
                    MONITOR.notifyAll();
                }
            }
        });
        jPanel.add(passBtn,BorderLayout.NORTH);
        JButton newBtn = new JButton("New Game");
        newBtn.setFont(f3);;
        newBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                command = "new";
                fromLocation[0] = -1;
                message = null;

                jPanel1.repaint();
                jPanel2.repaint();
                jPanel_R.remove(jPanelAnnounce);
                jPanel_R.repaint();
                synchronized (MONITOR) {
                    MONITOR.notifyAll();
                }
            }

        });
        JLabel jLabel = new JLabel();
        jLabel.setText("Game");
        jLabel.setFont(f1);
        jPanel.setLayout(new GridLayout(3,1,20,10));
        jPanel.add(jLabel);
        jPanel.add(passBtn);
        jPanel.add(newBtn);


        jPanel_R.setPreferredSize(new Dimension(150,500));
        jPanel_R.setLayout(new GridLayout(4,1,100,70));
        jPanel_R.add(jPanel2);
        jPanel_R.add(jPanel1);
        jPanel_R.add(jPanel);


        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,this,jPanel_R);
        jSplitPane.setDividerLocation(630);
        jSplitPane.setOneTouchExpandable(false);
        jSplitPane.setDividerSize(0);
        jSplitPane.setEnabled(false);
        jFrame.add(jSplitPane);
        jFrame.setVisible(true);
    }

    // Add some codes here

    // These methods could be modified
    @Override
    public void update(Board board) {
        this.board = board;
        fromLocation[0] = -1;
        if (winner==null)  repaint();
    }

    private void pentagram(Graphics g) {
        double xA = 2, yA = 2, c = 2, j36, j18, j54;
        double xB, yB, xC, yC, xD, yD, xE, yE, xf, yf, xg, yg;
        j36 = Math.toRadians(36);
        j18 = Math.toRadians(18);
        j54 = Math.toRadians(54);
        xB = xA + c * Math.cos(j36);
        yB = yA - c * Math.sin(j36);
        xC = xA + 2 * c * Math.cos(j36);
        yC = yA;
        xD = xA + c * Math.sin(j18);
        yD = yA + c * Math.cos(j18);
        xE = xC - c * Math.sin(j18);
        yE = yD;
        xf = xD + c / 2;
        yf = yD - (c / 2) * Math.tan(j36);
        xg = xE - c / (2 * Math.sin(j54)) * Math.sin(j18);
        yg = yE - c / (2 * Math.sin(j54)) * Math.cos(j18);
        int xpoint[] = {(int)(xB*10),(int)(xD*10),(int)(xE*10),(int)(xB*10)};
        int ypoint[] = {(int)(yB*10),(int)(yD*10),(int)(yE*10),(int)(yB*10)};
        int xpoint1[] = {(int)(xA*10), (int)(xC*10),(int)(xE*10),(int)(xA*10)};
        int ypoint1[] = {(int)(yA*10),(int)(yC*10),(int)(yE*10),(int)(yA*10)};
        int xpoint2[] = {(int)(xD*10),(int)(xf*10),(int)(xE*10),(int)(xD*10)};
        int ypoint2[] = {(int)(yD*10),(int)(yf*10),(int)(yE*10),(int)(yD*10)};
        int xpoint3[] = {(int)(xE*10),(int)(xg*10),(int)(xC*10),(int)(xE*10)};
        int ypoint3[] = {(int)(yE*10),(int)(yg*10),(int)(yC*10),(int)(yE*10)};
        Map<Integer,int[]> map = new HashMap<>();
        map.put(0,xpoint);
        map.put(1,ypoint);
        map.put(2,xpoint1);
        map.put(3,ypoint1);
        map.put(4,xpoint2);
        map.put(5,ypoint2);
        map.put(6,xpoint3);
        map.put(7,ypoint3);
        g.setColor(Color.orange);
        g.fillPolygon(xpoint,ypoint,4);
        g.fillPolygon(xpoint1,ypoint1,4);
        g.setColor(pale);
        g.fillPolygon(xpoint2,ypoint2,4);
        g.fillPolygon(xpoint3,ypoint3,4);
    }


    @Override
    protected void paintComponent(Graphics g) {
        if (board==null){return;}
        super.paintComponent(g);
        Color color = g.getColor();
        g.setFont(f2);
        g.drawString(" Ataxx",BOARD_OFFSET_WIDTH-50,80);
        g.setFont(f1);
        g.setColor(chessboardColor);
        g.fillRect(BOARD_OFFSET_WIDTH-50,BOARD_OFFSET_HEIGHT-50,450,450);
        g.setColor(pale);
        g.fillRect(BOARD_OFFSET_WIDTH,BOARD_OFFSET_HEIGHT,350,350);
        g.setColor(Color.RED);
        g.setFont(f3);
        g.drawString("Red number >> "+String.format("%d",board.getColorNums(PieceState.RED)),50,BOARD_OFFSET_HEIGHT+430);
        g.setFont(f3);
        g.setColor(Color.BLUE);
        g.drawString("Blue number >> "+String.format("%d",board.getColorNums(PieceState.BLUE)),300,BOARD_OFFSET_HEIGHT+430);
        g.setFont(f3);
        g.setColor(board.nextMove()==PieceState.RED?Color.RED:Color.BLUE);
        g.fillOval(290,BOARD_OFFSET_HEIGHT+450,ITEM_SIZE,ITEM_SIZE);
        g.setColor(Color.black);
        g.drawString("Now the moving piece is : ",50,BOARD_OFFSET_HEIGHT+470);
        g.drawString("·Block set: Click the right mouse button.",50,BOARD_OFFSET_HEIGHT+530);
        g.drawString("·Choose blue player mode first.",50,BOARD_OFFSET_HEIGHT+550);
        if (message!=null){
            g.drawString(message,50,BOARD_OFFSET_HEIGHT+500);
        }

        for(int i=-1;i<ROW+1;i++){
            for(int u=-1;u<COL+1;u++){
                PieceState pieceState = board.getContent((char) (u+'a'),(char) ((ROW-i-1)+'1'));
                if (pieceState==PieceState.BLOCKED){
                    g.drawRect(BOARD_OFFSET_WIDTH+u*CELL_SIZE,BOARD_OFFSET_HEIGHT+i*CELL_SIZE,CELL_SIZE,CELL_SIZE);
                    pentagram(g.create(BOARD_OFFSET_WIDTH+u*CELL_SIZE-MIDDLE,BOARD_OFFSET_HEIGHT+i*CELL_SIZE,CELL_SIZE,CELL_SIZE));
                }
                else if (pieceState == PieceState.EMPTY){
                    g.setColor(Color.black);
                    g.drawRect(BOARD_OFFSET_WIDTH+u*CELL_SIZE,BOARD_OFFSET_HEIGHT+i*CELL_SIZE,CELL_SIZE,CELL_SIZE);
                    if ((i==-1||i==7 )&& 0<=u &&u<7){
                        g.drawString(String.valueOf(ROWS[u]),BOARD_OFFSET_WIDTH+u*CELL_SIZE+MIDDLE,BOARD_OFFSET_HEIGHT+i*CELL_SIZE+MIDDLE+f1.getSize());
                    }
                    else if((u==-1||u==7 )&& 0<=i &&i<7){
                        g.drawString(String.valueOf(COLS[i]),BOARD_OFFSET_WIDTH+u*CELL_SIZE+MIDDLE,BOARD_OFFSET_HEIGHT+i*CELL_SIZE+MIDDLE+f1.getSize());
                    }
                }
                else if (pieceState == PieceState.BLUE){
                    if (fromLocation[0]==u&&fromLocation[1]==i){
                        g.setColor(chose);
                        g.fillRect(BOARD_OFFSET_WIDTH+u*CELL_SIZE,BOARD_OFFSET_HEIGHT+i*CELL_SIZE,CELL_SIZE,CELL_SIZE);
                    }
                    g.setColor(Color.black);
                    g.drawRect(BOARD_OFFSET_WIDTH+u*CELL_SIZE,BOARD_OFFSET_HEIGHT+i*CELL_SIZE,CELL_SIZE,CELL_SIZE);
                    g.setColor(Color.BLUE);
                    g.fillOval(BOARD_OFFSET_WIDTH+u*CELL_SIZE+MIDDLE,BOARD_OFFSET_HEIGHT+i*CELL_SIZE+MIDDLE,ITEM_SIZE,ITEM_SIZE);
                }
                else {
                    if (fromLocation[0]==u&&fromLocation[1]==i){
                        g.setColor(chose);
                        g.fillRect(BOARD_OFFSET_WIDTH+u*CELL_SIZE,BOARD_OFFSET_HEIGHT+i*CELL_SIZE,CELL_SIZE,CELL_SIZE);
                    }
                    g.setColor(Color.black);
                    g.drawRect(BOARD_OFFSET_WIDTH+u*CELL_SIZE,BOARD_OFFSET_HEIGHT+i*CELL_SIZE,CELL_SIZE,CELL_SIZE);
                    g.setColor(Color.RED);
                    g.fillOval(BOARD_OFFSET_WIDTH+u*CELL_SIZE+MIDDLE,BOARD_OFFSET_HEIGHT+i*CELL_SIZE+MIDDLE,ITEM_SIZE,ITEM_SIZE);
                }
                g.setColor(color);
            }
        }

    }

    @Override
    public String getCommand(String prompt) {
        if (winner != null){
            winner = null;
            return "new";
        }
        synchronized (MONITOR) {
            try {
                MONITOR.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return command;
    }

    @Override
    public void announceWinner(PieceState state) {
        JTextArea jTextArea = new JTextArea(state == PieceState.RED ? "Red wins" : state == PieceState.EMPTY ? "draw" : "Blue wins");
        jTextArea.setFont(f2);
        jTextArea.setEditable(false);
        jPanelAnnounce.removeAll();
        jPanelAnnounce.setBounds(0, 200, 50, 300);
        jPanelAnnounce.add(jTextArea);

        SwingUtilities.invokeLater(() -> {
            jPanel_R.add(jPanelAnnounce);
            jPanel_R.revalidate();
            jPanel_R.repaint();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    jPanel1.revalidate();
                    jPanel1.repaint();
                    jPanel2.revalidate();
                    jPanel2.repaint();
                    jPanel_R.revalidate();
                    jPanel_R.repaint();
                }
            };
            Timer timer1 = new Timer();
            timer1.schedule(task, 1000);
        });

    }

    @Override
    public void announceMove(Move move, PieceState player) {
        message = String.format("%s moves %s",player==PieceState.RED?"RED":"BLUE",move.toString(),"Winner");
    }

    @Override
    public void message(String format, Object... args) {
        JTextArea jTextArea = new JTextArea(String.format(format,args));
        jTextArea.setBounds(0, 0, 169, 300);
        jTextArea.setFont(f3);
        jTextArea.setLineWrap(true);

        jTextArea.setEditable(false);
        jPanelAnnounce.removeAll();
        jPanelAnnounce.setBounds(0, 200, 50, 300);
        jPanelAnnounce.add(jTextArea);

        SwingUtilities.invokeLater(() -> {
            jPanel_R.add(jPanelAnnounce);
            jPanel_R.revalidate();
            jPanel_R.repaint();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    jPanel_R.remove(jPanelAnnounce);
                    jPanel_R.revalidate();
                    jPanel_R.repaint();
                }
            };
            Timer timer1 = new Timer();
            timer1.schedule(task, 10000);
        });

        synchronized (MONITOR) {
            try {
                MONITOR.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void error(String format, Object... args) {
        JTextArea jTextArea1 = new JTextArea(String.format(format,args));
        jTextArea1.setBounds(0, 0, 169, 300);
        jTextArea1.setFont(f3);
        jTextArea1.setLineWrap(true);
        jTextArea1.setEditable(false);
        jPanelAnnounce.removeAll();
        jPanelAnnounce.setBounds(0, 200, 50, 300);
        jPanelAnnounce.add(jTextArea1);


        SwingUtilities.invokeLater(() -> {
            jPanel_R.add(jPanelAnnounce);
            jPanel_R.revalidate();
            jPanel_R.repaint();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    jPanel_R.remove(jPanelAnnounce);
                    jPanel_R.revalidate();
                    jPanel_R.repaint();
                }
            };
            Timer timer1 = new Timer();
            timer1.schedule(task, 10000);

        });

        synchronized (MONITOR) {
            try {
                MONITOR.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setVisible(boolean b) {
        jFrame.setVisible(b);
        super.setVisible(b);
    }

    public void pack() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // check if click on board
        if (e.getY()<=BOARD_OFFSET_HEIGHT||e.getY()>=ROW*CELL_SIZE+BOARD_OFFSET_HEIGHT||e.getX()<=BOARD_OFFSET_WIDTH||e.getX()>=BOARD_OFFSET_WIDTH+COL*CELL_SIZE
                ||(e.getY()-BOARD_OFFSET_HEIGHT)%CELL_SIZE==0||(e.getX()-BOARD_OFFSET_WIDTH)%CELL_SIZE==0){
            return;
        }
        if (e.getButton()==MouseEvent.BUTTON2){return;}
        //Set block
        if (e.getButton()==MouseEvent.BUTTON3){
            int[] location = getLocation(e.getX()-BOARD_OFFSET_WIDTH,e.getY()-BOARD_OFFSET_HEIGHT);
            command = String.format("block %c%c",(char)(location[0]+'a'),(char)((ROW-1-location[1])+'1'));
            synchronized (MONITOR){
                MONITOR.notifyAll();
            }
            return;
        }

        int[] clickLocation = getLocation(e.getX()-BOARD_OFFSET_WIDTH,e.getY()-BOARD_OFFSET_HEIGHT);
        char[] boardLocations = getBoardLocation(clickLocation[0],clickLocation[1]);
        PieceState pieceState = board.getContent(boardLocations[0],boardLocations[1]);
        // cannot click block
        if (pieceState == PieceState.BLOCKED || pieceState==(board.nextMove()==PieceState.RED?PieceState.BLUE:PieceState.RED)||(fromLocation[0]<0&&pieceState==PieceState.EMPTY)){
            return;
        }
        if (fromLocation[0]>=0&&pieceState==PieceState.EMPTY&&(Math.abs(clickLocation[0]-fromLocation[0])>2||Math.abs(clickLocation[1]-fromLocation[1])>2)){
            return;
        }
        if (fromLocation[0]<0||pieceState == board.nextMove()){
            fromLocation[0] = clickLocation[0];
            fromLocation[1] = clickLocation[1];
            jFrame.repaint();
            return;
        }

        char[] fromBoardLocation = getBoardLocation(fromLocation[0],fromLocation[1]);
        command = String.format("%c%c-%c%c",fromBoardLocation[0],fromBoardLocation[1],boardLocations[0],boardLocations[1]);
        fromLocation[0] = -1;
        synchronized (MONITOR){
            MONITOR.notifyAll();
        }

    }

    private static int[] getLocation(int x,int y){
        return new int[]{x/CELL_SIZE,y/CELL_SIZE};
    }
    private static char[] getBoardLocation(int col,int row){
        return new char[]{
                (char)(col+'a'),
                (char)(ROW-1-row+'1')
        };
    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
