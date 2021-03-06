import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener, FocusListener{
  
  JFrame parent;
  Ball ball;
  Platform platform;
  Timer timer;
  Graphics gg;
  BrickReader brickReader;
  ArrayList<Brick> bricks;
  private int levelNum;
  private BufferedImage bg;
  
  public GamePanel(JFrame parent){
    this.parent = parent;
    setPreferredSize(parent.getSize());
    setBackground(Color.WHITE);
    try{
      bg = ImageIO.read(new File("Graphics/Background.png"));
    }catch(IOException e){System.out.println(e.getMessage());return;}
    ball = new Ball(this);
    platform = new Platform(this, ball);
    ball.setPlatform(platform);    
    timer = new Timer(30,this);
    brickReader = new BrickReader("level1");
    levelNum = 1;
    addKeyListener(this);
    Player.setParent(this);
    start();
  }
  
  public void newLevel(){
    timer.stop();
    levelNum++;
    ball = new Ball(this);
    platform = new Platform(this, ball);
    ball.setPlatform(platform); 
    brickReader.setLevel("level"+levelNum);
    start();
  }
  
  public void start(){
    setFocusable(true);
    bricks = brickReader.readBricks();
    ball.setBricks(bricks);
    requestFocus();
    timer.start();
  }
  
  public void pause(){
    timer.stop();
  }
  
  public void resume(){
    timer.start();
  }
  
  public void checkIfWin(){
    if(bricks.size() < 1)
      newLevel();
  }
  
  public void paintComponent(Graphics g){
    gg = g;
    super.paintComponent(g);    
    drawBg(g);
    ball.draw(g);
    platform.draw(g);
    drawBricks(g);
    drawScore(g);
  }
  
  public void drawBg(Graphics g){
    g.drawImage(bg,0,0,null);
  }
  
  public void drawScore(Graphics g){
    g.setColor(Color.BLACK);
    g.drawString("SCORE: " + Player.getScore() + "     LEVEL      " + levelNum + "        LIVES: " + Player.getLives(), 10,10);
  }
  
  public void drawBricks(Graphics g){
    for(Brick b: bricks)
      b.draw(g);
  }
  
  public ArrayList<Brick> getBricks(){
    return bricks;
  }
  
  public void gameOver(){
    //ScreenTransition.fade(parent,new GameOverPanel(parent),this);
    parent.add(new GameOverPanel(parent));
    parent.remove(this);
    parent.pack();
  }
  
  public void actionPerformed(ActionEvent evt){
    if(evt.getSource() == timer){
      repaint();
      checkIfWin();
    }
  }
  
  public void keyPressed(KeyEvent evt){
    switch(evt.getKeyCode()){
      case KeyEvent.VK_SPACE:
        System.out.println("SPACE");
        break;
      case KeyEvent.VK_LEFT:
        platform.moveLeft();
        break;
      case KeyEvent.VK_RIGHT:
        platform.moveRight();
        break;
    }
  }
  
  public void keyReleased(KeyEvent evt){
    switch(evt.getKeyCode()){
      case KeyEvent.VK_SPACE:
        ball.release();
        break;
      case KeyEvent.VK_LEFT:
        platform.stopLeft();
        break;
      case KeyEvent.VK_RIGHT:
        platform.stopRight();
        break;
    }
  }
  public void keyTyped(KeyEvent evt){}
  
  public void focusGained(FocusEvent evt){}
  public void focusLost(FocusEvent evt){}
}