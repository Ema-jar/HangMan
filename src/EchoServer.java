// File Name GreetingServer.java

import java.net.*;
import java.io.*;
import java.util.*;

public class EchoServer extends Thread {

    int port, numberOfTry;
    String wordToGuess, hint;

    public EchoServer(int port) {
        this.port = port;
        String[] couple = generateWordToGuess();
        this.wordToGuess = couple[0];
        this.hint = couple[1];
        this.numberOfTry = 1;
    }

    public void run() {

        generateWordToGuess();


        String[] positionArray = initArray(wordToGuess);

        //shows the blank world, a lot of underscores
        System.out.println("Guess the word ");
        System.out.println("This is a hint: " + hint);
        System.out.println(Arrays.toString(positionArray));

        while(true) {
            try {
                //asks the client for a letter
                ServerSocket ss  =  new ServerSocket(port);
                System.out.println("Tell me a letter: ");

                //gets the letter and shows a message to the client
                Socket s = ss.accept();//establishes connection
                DataInputStream dis = new DataInputStream(s.getInputStream());
                String letter = (String)dis.readUTF();
                System.out.println("The letter received is:  " + letter);

                //if there is a match it is notified to the client
                if(wordToGuess.indexOf(letter) != -1){
                    System.out.println("HIT!!");
                    positionArray = insertChar(positionArray, letter);

                    //if no blank positions remain the player wins the game
                    if(!Arrays.toString(positionArray).contains("_ ")){
                        System.out.println("<---------------->");
                        System.out.println("YOU WIN!!");
                        System.out.println("The word to guess was: " + wordToGuess);
                        System.out.println("Number of tries: " + numberOfTry);
                        System.out.println("<---------------->");
                        ss.close();
                        return;
                    }
                } else{
                    System.out.println("No, your letter is not present");
                }

                numberOfTry++;

                //shows the new string with characters and underscores
                System.out.println(Arrays.toString(positionArray));

                ss.close();
            }catch(SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            }catch(IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private String[] generateWordToGuess(){

        String[] couple = new String[2];
        String filename = "words_hints.properties";
        Properties prop = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream(filename);

        Map<String, String> wordMap = new HashMap<String, String>();

        if(input == null){
            System.out.println("Error");
            return couple;
        }

        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Enumeration<?> e = prop.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = prop.getProperty(key);
            wordMap.put(key, value);
        }

        String[] keys = wordMap.keySet().toArray(new String[0]);
        int randomRow = new Random().nextInt(keys.length);

        couple[0] = keys[randomRow].toLowerCase();
        couple[1] = wordMap.get(couple[0]);

        return couple;
    }


    private String[] initArray(String word){
        int wordLength = word.length();
        String[] returnArray = new String[wordLength];

        for(int i = 0 ; i < wordLength ; i++){
            returnArray[i] = "_ ";
        }

        return returnArray;
    }

    private String[] insertChar(String[] positionArray, String c){
        for(int i = 0 ; i < wordToGuess.length() ; i++){
            if(wordToGuess.toLowerCase().charAt(i) == c.toLowerCase().charAt(0)){
                positionArray[i] = c;
            }
        }
        return positionArray;
    }
}