public class Keyboard {
    private char[] row1 = {'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I','O', 'P'};
    private char[] row2 = {'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'};
    private char[] row3 = {'Z', 'X', 'C', 'V', 'B', 'N', 'M'};
    private char[][] letters = {row1, row2, row3};


    public Keyboard() {
    }

    public char[][] getLetters() {
        return letters;
    }

    public int getNumberInAlphabet(char letter) {
        return ((int) letter) - 65;
    }

}