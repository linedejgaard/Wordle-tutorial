import java.util.ArrayList;
import java.util.List;

public class Word {
    private StringBuilder currentWord;
    private int wordlength;

    public Word() {
        currentWord = new StringBuilder();
        wordlength = 5;
    
    }

    public Word(String word) {
        currentWord = new StringBuilder(word);        
    }

    public void addLetter(char letter) {
        currentWord.append(letter);
    }

    public void deleteLetter() {
        if (currentWord.length() > 0) {
            currentWord.deleteCharAt(currentWord.length()-1);
        }
    }

    public boolean isFiveChars() {
        return currentWord.length() == wordlength;
    }

    @Override
    public String toString() {
        return currentWord.toString();
    }

    public boolean contains(String letter) {
        return currentWord.toString().contains(letter);
    }

    public int length() {
        return currentWord.length();
    }

    //  MORE OCCURENCES

    public boolean letterIsUnique(char letter){
        return numberOfEqualLetters(letter) <= 1;
    }

    public int numberOfEqualLetters(char letter) {
        int count = 0;
        String word = toString();
        for (int index = 0; index < length(); index++) {
            if (word.charAt(index) == letter) {
                count++;
            }
        }
        return count;
    }

    public ArrayList<Integer> getIndexOf(char letter) {
        String word = toString();
        int index = word.indexOf(letter);
        ArrayList<Integer> occurrences = new ArrayList<>(wordlength);
        while (index >= 0) {
            occurrences.add(index);
            index = word.indexOf(letter, index + 1);
        }
        return occurrences;
    }

    public Response[] getLetterResponses(Word guess) { //int[] can also contain -1
        String secretWord = toString();
        String guessWord = guess.toString();
        int wordlength = secretWord.length();
        Response[] responses = new Response[wordlength];

        //init response
        for (int i = 0; i < responses.length; i++) {
            responses[i] = Response.UNKNOWN;
        }

        for (int index = 0; index < secretWord.length(); index++) {
            char secretLetter = secretWord.charAt(index);
            char guessLetter = guessWord.charAt(index);
            Response currentResponse = responses[index];
            
            if (currentResponse == Response.UNKNOWN) {

                // GÆTTET INDEHOLDER KUN ET BOGSTAV AF DEN TYPE
                if (guess.letterIsUnique(guessLetter)) { //if the guess letter is unique in the guess, and if the sec letter is unique in the secret word
                    if (secretLetter == guessLetter) {
                      responses[index] = Response.CORRECT;
                    } else if (secretWord.contains("" + guessLetter)) {
                        responses[index] = Response.ALMOST_CORRECT;
                    } else {
                        responses[index] = Response.WRONG;
                    }
                
                
                // GÆTTET INDEHOLDER FLERE BOGSTAVER AF DEN TYPE, MEN DET HEMMELIGE ORD INDEHOLDER MAX ET AF DET BOGSTAV
                // } else if (this.letterIsUnique(guessLetter)) { //gætter f.eks. flere ss f.eks. class, men der er kun et s i house, som kunne være secret. 
                //     //Så skal man finde ud af, om der er et af guessLettersne eller flere, der er placeret korrekt.
                //     //Husk at tjekke, om der allerede et response
                
                //         int indexOfLetter = secretWord.indexOf(guessLetter);
                //         boolean letterIsInSecretWord = indexOfLetter != -1;
                        
                //         if (letterIsInSecretWord) { //så er en af s'erne rigtige, den rigtige plads har finder vi ved index of, alle andre steder skal sættes til wrong
                //             List<Integer> allIndexesOfLetterGuess = guess.getIndexOf(guessLetter);
                //             for (int i : allIndexesOfLetterGuess) {
                //                     if (indexOfLetter == i) {
                //                         responses[i] = Response.CORRECT;
                //                     } else {
                //                         responses[i] = Response.WRONG;
                //                     }   
                //             }
                //         } else { //letter is not in secret word
                //             responses[index] = Response.WRONG;
                //         }

                // GÆTTET INDEHOLDER FLERE AF BOGSTAVET I BEGGE ORD
                } else { //letter is not unique, der er flere af dem i secret word: class, guess: issue (begge skal være gule), class, grass (begge skal være grønne), lasso, grass (en grøn og en gul)
                    List<Integer> indexesOfSec = this.getIndexOf(guessLetter);
                    List<Integer> indexesOfGuess = guess.getIndexOf(guessLetter);
                    List<Integer> indexesOfDelSec = new ArrayList<>(wordlength);
                    List<Integer> indexesOfDelGuess = new ArrayList<>(wordlength);

                    //der kan også være 3 af de samme bogstaver i et ord og 2 i et andet
                    // 2 i gæt og 3 i sec
                    // 3 i sec og 2 i gæt

                    //find all that is correct
                    for (Integer indexOfGuess : indexesOfGuess) {
                        for (Integer indexOfSecret : indexesOfSec) {
                            if (indexOfGuess == indexOfSecret) {
                                responses[indexOfGuess] = Response.CORRECT;
                                indexesOfDelGuess.add(indexOfGuess);
                                indexesOfDelSec.add(indexOfSecret);
                            }
                        }
                    }

                    indexesOfSec.removeAll(indexesOfDelSec);
                    indexesOfGuess.removeAll(indexesOfDelGuess);
                    int numOfSecretLettersBack = indexesOfSec.size();

                    for (Integer i : indexesOfGuess) { //flere indexes tilbage: skal enten sættes til wrong eller til almost_correct
                        if (numOfSecretLettersBack > 0) {
                            numOfSecretLettersBack--;
                            //indexes in indexesOfGuess should be unknown
                            responses[i] = Response.ALMOST_CORRECT;
                        } else {
                            responses[i] = Response.WRONG;
                        }
                    }
                }
            }
        }       
        return responses;
    }

}