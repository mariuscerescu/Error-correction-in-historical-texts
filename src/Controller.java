import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller implements Initializable{

    FileChooser fileChooser = new FileChooser();

    @FXML
    private ToggleGroup century;

    @FXML
    private RadioButton sec17RB;

    @FXML
    private RadioButton sec18RB;

    @FXML
    private TextArea CorrectedTextTA;

    @FXML
    private TextArea OriginalTextTA;

    @FXML
    private TextArea VocabularyTA;

    public ArrayList<String> vocabularyList;

    public ArrayList<String> vocabularyListSec17 = new ArrayList<>();

    public ArrayList<String> vocabularyListSec18 = new ArrayList<>();

    private ArrayList<String> correctedWords = new ArrayList<>();

    VocabController vocabController = null;
    Stage vocabStage = null;
    
    CorrectedWordsController CorrectedWordsController = null;
    Stage CorrectedWordsStage = null;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        File vocabularyFile = new File("Vocabular.txt");
        File vocabularyFileSec17 = new File("Vocabular(sec XVII).txt");
        fileChooser.setInitialDirectory(new File("C:\\Users\\Asus\\Desktop\\Universitate\\Anul 2 sem 2\\Inv Aut\\Corectarea erorilor"));

        try{
            Scanner scanner = new Scanner(vocabularyFile);
            while(scanner.hasNextLine()){
                vocabularyListSec18.add(scanner.nextLine());
               }
               scanner.close();
       }catch(Exception e){}

       try{
        Scanner scanner = new Scanner(vocabularyFileSec17);
        while(scanner.hasNextLine()){
            vocabularyListSec17.add(scanner.nextLine());
           }
           scanner.close();
        }catch(Exception e){}

        vocabularyList = vocabularyListSec18;

       FXMLLoader VocabfxmlLoader = new FXMLLoader(getClass().getResource("VocabularyScene.fxml"));
       Parent VocabRoot = null;
    try {
        VocabRoot = (Parent)VocabfxmlLoader.load();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
       vocabStage = new Stage();
       vocabStage.setResizable(false);
       vocabStage.setTitle("Vocabular");
       vocabStage.setScene(new Scene(VocabRoot));

       vocabController = VocabfxmlLoader.getController();

       vocabController.VocabularyTAsec18.setEditable(false);
       vocabController.VocabularyTAsec17.setEditable(false);

       for(int i = 0; i < vocabularyList.size(); i++){
        vocabController.VocabularyTAsec18.appendText(i + 1 + "." + " " + vocabularyListSec17.get(i) + "\n");
       }

       for(int i = 0; i < vocabularyList.size(); i++){
        vocabController.VocabularyTAsec17.appendText(i + 1 + "." + " " + vocabularyListSec18.get(i) + "\n");
       }

       vocabController.VocabularyTAsec18.setOpacity(0);

       FXMLLoader CorrectedWordsfxmlLoader = new FXMLLoader(getClass().getResource("CorrectedWordsScene.fxml"));
       Parent CorrectedWordsRoot = null;
    try {
        CorrectedWordsRoot = (Parent)CorrectedWordsfxmlLoader.load();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
        CorrectedWordsStage = new Stage();
        CorrectedWordsStage.setResizable(false);
        CorrectedWordsStage.setTitle("Cuvintele corectate");
        CorrectedWordsStage.setScene(new Scene(CorrectedWordsRoot));

        CorrectedWordsController = CorrectedWordsfxmlLoader.getController();

        CorrectedWordsController.CorrectedWordsTA.setEditable(false);

       for(String word : correctedWords){
        CorrectedWordsController.CorrectedWordsTA.appendText(word + "\n");
       }
    }

    @FXML
    void loadFileButton(ActionEvent event){
      File OriginalTextFile =  fileChooser.showOpenDialog(new Stage());
       try{
            Scanner scanner = new Scanner(OriginalTextFile);
            while(scanner.hasNextLine()){
                OriginalTextTA.appendText(scanner.nextLine() + "\n");
               }
               scanner.close();
       }catch(Exception e){

       }
    }

    @FXML
    void changeCenturyButton(ActionEvent event) {
      if(sec17RB.isSelected()){
        vocabularyList = vocabularyListSec17;
        vocabController.VocabularyTAsec17.setOpacity(0);
        vocabController.VocabularyTAsec18.setOpacity(1);

      }else if(sec18RB.isSelected()){
        vocabularyList = vocabularyListSec18;
        vocabController.VocabularyTAsec17.setOpacity(1);
        vocabController.VocabularyTAsec18.setOpacity(0);
      }

    }

    @FXML
    void showCorrectedWordsButton(ActionEvent event) {
        CorrectedWordsStage.show();
    }

    @FXML
    void showVocabularyButton(ActionEvent event) throws IOException {
        vocabStage.show();
    }

    @FXML
    void CorrectButton(ActionEvent event) {

      CorrectedWordsController.CorrectedWordsTA.clear();

        if(!OriginalTextTA.getText().isBlank()){
            String OriginalText = OriginalTextTA.getText();

            String regex = "[^\\p{L}\\s]";

            String OriginalTextOnlyLettersLowerCase = OriginalText.replaceAll(regex, "").toLowerCase();

            String[] listOfWords = OriginalTextOnlyLettersLowerCase.split("\\s+");

            HashSet<String> listOfUniqueWords = new HashSet<>();

            for(String word : listOfWords){
                listOfUniqueWords.add(word);
            }

            for(String word : listOfUniqueWords){

                if(!vocabularyList.contains(word)){
                    String separatedWord = checkMergedWord(word);
                    if(separatedWord != null){
                      // create a pattern with case-insensitive flag
                      Pattern pattern = Pattern.compile(word, Pattern.CASE_INSENSITIVE);
                      // create a matcher for the string
                      Matcher matcher = pattern.matcher(OriginalText);
                      // create a string buffer to store the result
                      StringBuffer sb = new StringBuffer();
                      // loop through the matches
                      while (matcher.find()) {
                        // get the matched substring
                        String match = matcher.group();
                        // check the case of the first character
                        if (Character.isUpperCase(match.charAt(0))) {
                          // make the first letter of the replacement uppercase
                          separatedWord = Character.toUpperCase(separatedWord.charAt(0)) + separatedWord.substring(1);
                        } else {
                          // make the first letter of the replacement lowercase
                          separatedWord = Character.toLowerCase(separatedWord.charAt(0)) + separatedWord.substring(1);
                        }
                        // append the replacement with the same case for the first letter
                        matcher.appendReplacement(sb, separatedWord);
                      }
                      // append the remaining part of the string
                      matcher.appendTail(sb);
                      // convert the string buffer to string
                      OriginalText = sb.toString();
                      CorrectedWordsController.CorrectedWordsTA.appendText(word + " -> " + separatedWord + "\n");
                    }else{
                      String mostSimilarWord = mostSimilarWord(word);
                      if(mostSimilarWord != null){
                        // create a pattern with case-insensitive flag
                        Pattern pattern = Pattern.compile(word, Pattern.CASE_INSENSITIVE);
                        // create a matcher for the string
                        Matcher matcher = pattern.matcher(OriginalText);
                        // create a string buffer to store the result
                        StringBuffer sb = new StringBuffer();
                        // loop through the matches
                        while (matcher.find()) {
                          // get the matched substring
                          String match = matcher.group();
                          // check the case of the first character
                          if (Character.isUpperCase(match.charAt(0))) {
                            // make the first letter of the replacement uppercase
                            mostSimilarWord = Character.toUpperCase(mostSimilarWord.charAt(0)) + mostSimilarWord.substring(1);
                          } else {
                            // make the first letter of the replacement lowercase
                            mostSimilarWord = Character.toLowerCase(mostSimilarWord.charAt(0)) + mostSimilarWord.substring(1);
                          }
                          // append the replacement with the same case for the first letter
                          matcher.appendReplacement(sb, mostSimilarWord);
                        }
                        // append the remaining part of the string
                        matcher.appendTail(sb);
                        // convert the string buffer to string
                        OriginalText = sb.toString();
                        CorrectedWordsController.CorrectedWordsTA.appendText(word + " -> " + mostSimilarWord + "\n");
                      }
                    }
                  }
                  
                  
            }

            OriginalText = correctPunctuation(OriginalText);
            CorrectedTextTA.setText(OriginalText);

        }

    }


// A method that returns the most similar word from a vocabulary list to a given test word
// The similarity is measured by the Levenshtein distance between the words
public String mostSimilarWord(String testWord){
    // Initialize the prediction variable
    String prediction = null;
    
    // If the test word is already in the vocabulary list, return it as the prediction
    if(vocabularyList.contains(testWord)){
      prediction = testWord;
    }else{
      // Otherwise, initialize a variable to store the minimum distance
      int minDistance = Integer.MAX_VALUE;
      // Loop through each word in the vocabulary list and compare it with the test word
      for(String word : vocabularyList){
        // Compute the Levenshtein distance between the test word and the current word
        int distance = levenshteinDistance(testWord, word);
        // If the distance is smaller than the minimum distance, update the minimum distance and prediction variables
        if(distance < minDistance){
          minDistance = distance;
          prediction = word;
        }
      }
    }
    // Return the prediction or null if no similar word was found
    return prediction;
  }
  
  // A helper method that computes the Levenshtein distance between two words
  public int levenshteinDistance(String s1, String s2){
    // Get the lengths of the words
    int len1 = s1.length();
    int len2 = s2.length();
    
    // Create a matrix to store the distances
    int[][] matrix = new int[len1 + 1][len2 + 1];
    
    // Initialize the first row and column of the matrix with increasing numbers
    for(int i = 0; i <= len1; i++){
      matrix[i][0] = i;
    }
    
    for(int j = 0; j <= len2; j++){
      matrix[0][j] = j;
    }
    
    // Fill up the rest of the matrix using dynamic programming
    for(int i = 1; i <= len1; i++){
      for(int j = 1; j <= len2; j++){
        // If the characters at the current positions are equal, use the diagonal value
        if(s1.charAt(i - 1) == s2.charAt(j - 1)){
          matrix[i][j] = matrix[i - 1][j - 1];
        }else{
          // Otherwise, use the minimum of the left, top, and diagonal values plus one
          matrix[i][j] = Math.min(Math.min(matrix[i - 1][j], matrix[i][j - 1]), matrix[i - 1][j - 1]) + 1;
        }
      }
    }
    
    // Return the bottom right value of the matrix as the final distance
    return matrix[len1][len2];
  }
  

// A method that returns the split of a test word into two words from a vocabulary list, if possible
public String checkMergedWord(String testWord){

    HashSet<String> vocabularySet = new HashSet<>(vocabularyList); // convert list to set
    String leftWord;
    String rightWord;
  
    // If the test word is already in the vocabulary list, return null
    if(vocabularySet.contains(testWord)){
      return null;
    }else{
      // Loop through each possible split of the test word
      for(int i = 0; i < testWord.length() - 1; i++){
        // Get the left and right parts of the split
        leftWord = testWord.substring(0, i+1);
        rightWord = testWord.substring(i+1);
  
        // If both parts are in the vocabulary set, return them separated by a space
        if(vocabularySet.contains(leftWord) && vocabularySet.contains(rightWord)){
          return leftWord + " " + rightWord;
        }
      }
    }
  
    // If no valid split was found, return null
    return null;
  }

  public String correctPunctuation(String input) {
    // Add a space after each comma, unless it is followed by a digit
    input = input.replaceAll(",(?!\\d)", ", ");
    // Add a space after each colon or semicolon, unless it is followed by a space
    input = input.replaceAll("([:;])(?!\\s)", "$1 ");
    // Remove any space before a period, comma, colon, semicolon, question mark or exclamation mark
    input = input.replaceAll("\\s+([.,:;?!])", "$1");
    // Return the corrected string
    return input;
  }
  
  
}
