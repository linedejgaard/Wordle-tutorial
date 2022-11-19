import java.util.ArrayList;

public class Word {
    private String currentWord;
    private int wordlength;

    public Word() {
        currentWord = "";
        wordlength = 5;
    }

    public Word(String word) {
        currentWord = word;
        wordlength = word.length();
    }

    public void addLetter(char letter) {
        currentWord = currentWord + letter;
    }

    public void deleteLetter() {
        if (currentWord.length() > 0) {
            currentWord = currentWord.substring(0, currentWord.length() - 1);
        }
    }

    public boolean isFiveChars() {
        return currentWord.length() == wordlength;
    }

    @Override
    public String toString() {
        return currentWord;
    }

    public boolean contains(String letter) {
        return currentWord.contains(letter);
    }

    public int length() {
        return currentWord.length();
    }

    //  MORE OCCURENCES
    private boolean letterIsUnique(char letter){
        return numberOfEqualLetters(letter) <= 1;
    }

    private int numberOfEqualLetters(char letter) {
        int count = 0;
        for (int index = 0; index < length(); index++) {
            if (currentWord.charAt(index) == letter) {
                count++;
            }
        }
        return count;
    }

    /* Returns an array list with all the indexes where letter is f.eks. sunn: n: -> return {2, 3} */
    private ArrayList<Integer> getIndexOf(char letter) {
        int index = currentWord.indexOf(letter);
        ArrayList<Integer> occurrences = new ArrayList<>();

        while (index >= 0) {
            occurrences.add(index);
            index = currentWord.indexOf(letter, index + 1);
        }

        return occurrences;
    }

    private Response[] getInitResponses() {
        Response[] responses = new Response[wordlength];

        //init response
        for (int i = 0; i < responses.length; i++) {
            responses[i] = Response.UNKNOWN;
        }

        return responses;
    }

    /* Letter is unique in secret word */
    private Response getResponseLetterIsUnique(char secretLetter, char guessLetter, String secretWord) {
        //if the guess letter is unique in the guess, and if the sec letter is unique in the secret word
        if (secretLetter == guessLetter) {
            return Response.CORRECT;
            } else if (secretWord.contains("" + guessLetter)) {
                return Response.ALMOST_CORRECT;
            } else {
                return Response.WRONG;
            }
    }

    private ArrayList<Integer> completelyCorrectIndexes(ArrayList<Integer> indexesOfGuess, ArrayList<Integer> indexesOfSec, Response[] responses) {
        ArrayList<Integer> completelyCorrectIndexes = new ArrayList<>();
        
        for (Integer indexOfGuess : indexesOfGuess) {
            for (Integer indexOfSecret : indexesOfSec) {
                if (indexOfGuess == indexOfSecret) {
                    responses[indexOfGuess] = Response.CORRECT;
                    completelyCorrectIndexes.add(indexOfSecret);
                }
            }
        }
        return completelyCorrectIndexes;
    }

    private Response[] setRemainingResponses(ArrayList<Integer> indexesOfGuess, ArrayList<Integer> indexesOfSec, Response[] responses) {
        int numOfSecretLettersBack = indexesOfSec.size();

        //flere indexes tilbage: skal enten sættes til wrong eller til almost_correct
        for (Integer i : indexesOfGuess) { 
            if (numOfSecretLettersBack > 0) {
                numOfSecretLettersBack = numOfSecretLettersBack - 1;
                responses[i] = Response.ALMOST_CORRECT;
            } else {
                responses[i] = Response.WRONG;
            }
        }
        return responses;
    }

    /* Only called on secret word */
    public Response[] getLetterResponses(Word guess) { //int[] can also contain -1
        String secretWord = currentWord;
        String guessWord = guess.toString();
        Response[] responses = getInitResponses();

        for (int index = 0; index < wordlength; index++) {
            char secretLetter = secretWord.charAt(index);
            char guessLetter = guessWord.charAt(index);
            Response currentResponse = responses[index];
            
            if (currentResponse == Response.UNKNOWN) {

                // GÆTTET INDEHOLDER KUN ET BOGSTAV AF DEN TYPE
                if (guess.letterIsUnique(guessLetter)) { 
                    responses[index] = getResponseLetterIsUnique(secretLetter, guessLetter, secretWord);
            
                } else { //letter is not unique, der er flere af dem i secret word: class, guess: issue (begge skal være gule), class, grass (begge skal være grønne), lasso, grass (en grøn og en gul)
                    ArrayList<Integer> indexesOfSec = this.getIndexOf(guessLetter);
                    ArrayList<Integer> indexesOfGuess = guess.getIndexOf(guessLetter);

                    //find all that is correct
                    ArrayList<Integer> completelyCorrectIndexes = completelyCorrectIndexes(indexesOfGuess, indexesOfSec, responses);

                    indexesOfSec.removeAll(completelyCorrectIndexes);
                    indexesOfGuess.removeAll(completelyCorrectIndexes);

                    responses = setRemainingResponses(indexesOfGuess, indexesOfSec, responses);
                }
            }
        }       
        return responses;
    }

}