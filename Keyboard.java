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
        if (letter == 'A') {
            return 0;
        } else if (letter == 'B') {
            return 1;
        } else if (letter == 'C') {
            return 2;
        } else if (letter == 'D') {
            return 3;
        } else if (letter == 'E') {
            return 4;
        } else if (letter == 'F') {
            return 5;
        } else if (letter == 'G') {
            return 6;
        } else if (letter == 'H') {
            return 7;
        } else if (letter == 'I') {
            return 8;
        } else if (letter == 'J') {
            return 9;
        } else if (letter == 'K') {
            return 10;
        } else if (letter == 'L') {
            return 11;
        } else if (letter == 'M') {
            return 12;
        } else if (letter == 'N') {
            return 13;
        } else if (letter == 'O') {
            return 14;
        } else if (letter == 'P') {
            return 15;
        } else if (letter == 'Q') {
            return 16;
        } else if (letter == 'R') {
            return 17;
        } else if (letter == 'S') {
            return 18;
        } else if (letter == 'T') {
            return 19;
        } else if (letter == 'U') {
            return 20;
        } else if (letter == 'V') {
            return 21;
        } else if (letter == 'W') {
            return 22;
        }else if (letter == 'X') {
            return 23;
        } else if (letter == 'Y') {
            return 24;
        } else if (letter == 'Z') {
            return 25;
        }
        return -1;
        //return ((int) letter) - 65;
    }

}
