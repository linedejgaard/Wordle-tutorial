import java.awt.event.*;

public class GameState {
    private Dictionary dictionary;
    private Word currentGuess;
    private Word secretWord;
    private Response[] letterResponses; //is guess: gray, yellow, green
    private Response[][] guessResponses; //is guess: gray, yellow, green
    private char[][] board; // row x col
    private int numGuess;
    private final int totalGuesses = 6;
    private boolean isInvalid = false;

    private boolean isFinished = false;


    public GameState() {
        dictionary = new Dictionary("dictionary/word-list.txt");
        currentGuess = new Word();
        letterResponses = new Response[26];
        secretWord = new Word(dictionary.getRandomStringWord());
        System.out.println(secretWord.toString());

        numGuess = 0;
        guessResponses = new Response[totalGuesses][secretWord.length()];

        this.board = new char[totalGuesses][secretWord.length()];

        //initialize responses
        for (int i = 0; i < letterResponses.length; i++) {
            letterResponses[i] = Response.UNKNOWN;
        }

        for (int row = 0; row < guessResponses.length; row++) {
            for (int col = 0; col < guessResponses[row].length; col++) {
                guessResponses[row][col] = Response.UNKNOWN;
            }
        }
    }

    private boolean isValidGuess() {
        return currentGuess.isFiveChars() && dictionary.contains(currentGuess.toString());
    }

    public boolean isCorrectGuess() {
        return (secretWord.toString().equals(currentGuess.toString()));
    }

    private void evaluateGuess() {

        //System.out.println("Evaluate guess");
        
        if (isCorrectGuess()) {
            isFinished = true;
            updateResponses();
            // System.out.println("The guess is correct");
        }
        else if (isValidGuess()) {
            if (numGuess < totalGuesses - 1) {
                updateResponses();
                numGuess++;
                currentGuess = new Word();                
            } else { //last guess is when numGuess == totalGuesses - 1, because of index 0
                updateResponses();
                isFinished = true;
                // System.out.println("The game is finish, and you did not manage to guess the word");
            }

        } else {
            isInvalid = true;
            // System.out.println("This is not a valid guess");
        }
    }

    public void updateResponses() {
        String currWord = currentGuess.toString();
        String currSecret = secretWord.toString();

        for (int i = 0; i < currWord.length(); i++) {

            char letter = currWord.charAt(i);
            int asciicode = letter;

            printLetterResponses();

            // update key responses
            if (currSecret.charAt(i) == letter) {
                letterResponses[asciicode-65] = Response.CORRECT;
            } else if (secretWord.contains("" + letter) && (letterResponses[asciicode-65] != Response.CORRECT)) { //green cannot be yellow again
                letterResponses[asciicode-65] = Response.ALMOST_CORRECT;
            } else if (letterResponses[asciicode-65] == Response.UNKNOWN){
                letterResponses[asciicode-65] = Response.WRONG;
            } 
            
            guessResponses[numGuess] = secretWord.getLetterResponses(currentGuess);
            
        }
        printResponses();
    }

    public void updateBoard() {
        String word = currentGuess.toString();

        for (int i = 0; i < word.length(); i++) {
            this.board[numGuess][i] = word.charAt(i);

        }
        for (int i = word.length(); i < secretWord.length(); i++) {
            this.board[numGuess][i] = '-';
        }
    }

    public char[][] getBoard() {
        return this.board;
    }

    /**
     * 
     * @param letter
     * @return true if the length of the word is changed
     */
    public boolean writeLetter(char letter) {
        int previousLength = currentGuess.length();
        if (!currentGuess.isFiveChars()) {
            if (Character.isLetter(letter)) {
                currentGuess.addLetter(Character.toUpperCase(letter));
                updateBoard();
            }
        }
        return previousLength != currentGuess.length();
    }

    /**
     * 
     * @param keycode
     * @return true if the length of the word is changed
     */
    public void modifyGuess(int keycode) {
        if (keycode == KeyEvent.VK_BACK_SPACE) {
            isInvalid = false;
            currentGuess.deleteLetter();
            updateBoard();
        }

        if (keycode == KeyEvent.VK_ENTER && currentGuess.isFiveChars()) {
            evaluateGuess();
            updateBoard();
        }
    }

    public Response[] getLetterResponses() {
        return letterResponses;
    }

    public Response[][] getGussResponses() {
        return guessResponses;
    }

    public int getNumGuess() {
        return numGuess;
    }

    public void printResponses() {
        System.out.println("\nBOARD RESPONSES:");
        for (int i = 0; i < guessResponses.length; i++) {
            for (int j = 0; j < guessResponses[i].length; j++) {
                System.out.print(guessResponses[i][j].name() + " ");
            }
            System.out.println();
        }
    }

    public void printLetterResponses() {
        System.out.println("\nLETTER RESPONSES:");
        int letter = 65;
        for (Response r : letterResponses) {
            System.out.print(((char) letter) +": " + r.name() + " ");
            letter++;
        }
        System.out.println();
    }

    public void printboard() {
        System.out.println("BOARD");

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                System.out.print(this.board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isInvalid() {
        return isInvalid;
    }
}