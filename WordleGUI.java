import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import java.awt.event.KeyListener;

import java.awt.Image;

public class WordleGUI extends JComponent implements KeyListener {

    // Images: letters: a-z (0-25)
    private Image[] letterImages;
    
    // Images: keys: a-z (0-25)
    private Image[] keyImages;

    private GameState gameState;

    private Image correctLetter, almostCorrectLetter, wrongLetter, unknownLetter;
    private Image correctKey, almostCorrectKey, wrongKey, unknownKey;
    private Image correctMessage, invalidMessage, finishMessage;

    private int KEY_WIDTH = 92/2;
    private int KEY_HEIGHT = 138/2;

    private int LETTER_WIDTH = 75;
    private int LETTER_HEIGHT = 75;
    private Response[] letterResponses;
    private Response[][] guessResponses;

    // LETTERS
    private Keyboard keyboard;
    
    public WordleGUI() throws Exception {
        gameState = new GameState();
        keyboard = new Keyboard();
        letterResponses = gameState.getLetterResponses();
        guessResponses = gameState.getGussResponses();

        this.setFocusable(true);
        this.addKeyListener(this);
        setLetters();
        setImages();
        setKeys();
        setLetterImages();
        setKeyImages();
    }

    public void setImages() throws IOException {

        correctMessage = ImageIO.read(new File("messages/Correct.png"));
        finishMessage = ImageIO.read(new File("messages/Finish.png"));
        invalidMessage = ImageIO.read(new File("messages/Invalid.png"));
    }
    
    public void setKeyImages() throws IOException {
        correctKey = ImageIO.read(new File("keys/Correct.png")).getScaledInstance(KEY_WIDTH, KEY_HEIGHT, ABORT);      
        almostCorrectKey = ImageIO.read(new File("keys/Almost_correct.png")).getScaledInstance(KEY_WIDTH, KEY_HEIGHT, ABORT);        
        wrongKey = ImageIO.read(new File("keys/Wrong.png")).getScaledInstance(KEY_WIDTH, KEY_HEIGHT, ABORT);      
        unknownKey = ImageIO.read(new File("keys/Unknown.png")).getScaledInstance(KEY_WIDTH, KEY_HEIGHT, ABORT);      
    }

    public void setLetterImages() throws IOException {
        correctLetter = ImageIO.read(new File("letters/Correct.png")).getScaledInstance(LETTER_WIDTH, LETTER_HEIGHT, ABORT);      
        almostCorrectLetter = ImageIO.read(new File("letters/Almost_correct.png")).getScaledInstance(LETTER_WIDTH, LETTER_HEIGHT, ABORT);   
        wrongLetter = ImageIO.read(new File("letters/Wrong.png")).getScaledInstance(LETTER_WIDTH, LETTER_HEIGHT, ABORT);   
        unknownLetter = ImageIO.read(new File("letters/Unknown.png")).getScaledInstance(LETTER_WIDTH, LETTER_HEIGHT, ABORT);   
    }

    /* Save letter pictures in array */
    private void setLetters() throws IOException {
        this.letterImages = new Image[26];
        for (int i = 0; i < letterImages.length; i++) {
            int asciicode = i+65;
            String path = "letters/" + (char) asciicode + ".png";
            letterImages[i] = ImageIO.read(new File(path)).getScaledInstance(LETTER_WIDTH, LETTER_HEIGHT, ABORT);;      
        }
    }

    /* Save key pictures in array */
    private void setKeys() throws Exception {
        this.keyImages = new Image[26];
        for (int i = 0; i < keyImages.length; i++) {
            int asciicode = i+65;
            String path = "keys/" + (char) asciicode + ".png";
            keyImages[i] = ImageIO.read(new File(path)).getScaledInstance(KEY_WIDTH, KEY_HEIGHT, ABORT);   
        }
    }

    @Override
    public void paint(Graphics g){ 

        int imSize = 90;
        int ins = 1680/2-(3*imSize-40); //1680/2-((5/2)*90): width in pixels / 2 - (5*imSize) //google: how many pixels is my screen

        g.setColor(Color.black);
        drawKeys(g);
        drawBoard(g);
        
        if (gameState.isFinished()) {
            if (gameState.isCorrectGuess()) {
                System.out.println("The game is finish, and the guess is correct");
                g.drawImage(correctMessage, imSize*1+ins-20, imSize*2+50, this);
            } else {
                System.out.println("The game is finish, and the guess is not correct");
                g.drawImage(finishMessage, imSize*1+ins-20, imSize*2+50, this);
            }            
        }
        if (gameState.isInvalid()) { 
            System.out.println("The guess is unvalid");
            g.drawImage(invalidMessage, imSize*1+ins-20, imSize*2+50, this);
        }
    }

    public void drawBoard(Graphics g) {
        int imSize = 90;
        int ins = 1680/2-(3*imSize-40); //1680/2-((5/2)*90): width in pixels / 2 - (5*imSize) //google: how many pixels is my screen

        char[][] board = gameState.getBoard();
        for (int row = 0; row < board.length; row++){
            for (int col = 0; col < board[row].length; col++){
                char letter = board[row][col];
                if (Character.isLetter(letter)) {
                    int index = keyboard.getNumberInAlphabet(letter);
                    if (index<letterImages.length) {
                        Response response = guessResponses[row][col];
                        g.drawImage(getLetterImgFromResponse(response), imSize*col+ins, imSize*row+50, this);
                        g.drawImage(letterImages[index], imSize*col+ins, imSize*row+50, this);
                    }

                } else {
                    g.drawImage(getLetterImgFromResponse(Response.UNKNOWN), imSize*col+ins, imSize*row+50, this);
                }
                
            }
        }
    }
    public void drawKeys(Graphics g) {
        char[][] letters = keyboard.getLetters();
        int imSize = 50;
        int ins = 590; //380; //1680/2-(5*50): width in pixels / 2 - (5*imSize) //google: how many pixels is my screen

    	for (int row = 0; row < letters.length; row++){
            int rowLength = letters[row].length;

            for (int col = 0; col < letters[row].length; col++){

                char letter = letters[row][col];
                int index = keyboard.getNumberInAlphabet(letter);
                Response response = letterResponses[index];
                
                g.drawImage(getKeyImgFromResponse(response), imSize*col+ins+(15-rowLength)*row*5, imSize*row*2+ins+50, this); //Prøvede mig bare frem. Lægger rowlength og row til, fordi de forskellige rækker skal have forskellige indtryk
                g.drawImage(keyImages[index], imSize*col+ins+(15-rowLength)*row*5, imSize*row*2+ins+50, this); //Prøvede mig bare frem. Lægger rowlength og row til, fordi de forskellige rækker skal have forskellige indtryk
            }
        }
    }

    public Image getKeyImgFromResponse(Response response) {
        switch (response) {
            case CORRECT:
                return correctKey;
            case ALMOST_CORRECT:
                return almostCorrectKey;
            case WRONG:
                return wrongKey;
            case UNKNOWN:
                return unknownKey;
        }
        return null;
    }

    public Image getLetterImgFromResponse(Response response) {
        switch (response) {
            case CORRECT:
                return correctLetter;
            case ALMOST_CORRECT:
                return almostCorrectLetter;
            case WRONG:
                return wrongLetter;
            case UNKNOWN:
                return unknownLetter;
        }
        return null;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        int keycode = e.getKeyCode();
        
        if (keycode == KeyEvent.VK_UNDEFINED) {
            char keychar = e.getKeyChar();
            boolean isModified = gameState.writeLetter(keychar);
            if (isModified) {
                repaint();
            }
        }
        if (gameState.isFinished()) {
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int keycode = e.getKeyCode();
        gameState.modifyGuess(keycode);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

}