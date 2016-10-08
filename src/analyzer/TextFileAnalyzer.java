package analyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
 /**
  * This class provides the funcionality to read data like the count of rows, sentences and word as well as<br>
  * the average length of one word. In addition to that, it allows you to copy the file described by an object<br>
  * of this class, with a length for each line given by the user of the Testclass. If no length for the lines<br>
  * is specified, the old line length is inherited.<br>
  * <br>
  * This Application is reading the files it gets all line by line, so that Bufferoverflows cannot occur.<br>
  * also it got tested with text files with the size of 200mb and it needs only abput two seconbds to analyze<br>
  * it´s details and another 3 seconds to copy it without changing of the line length.<br>
  * If you change the line length it takes a little bit longer depending on the length sou specified,<br>
  * but with lengths in the range of 30-100 it also only takes a few seconds to copy.
  * 
  * @author Gabriel Pawlowsky
  * @version 2012-04-23
  */
public class TextFileAnalyzer {
	//The file that is described and analyzed by the object
	private File file;
	
	//The details about the file
	public int rowCount;
	public int sentenceCount;
	public int wordCount;
	public int averageWordLength;
	public long charCount;
	
	/**
	 * A constructor that sets the passed file to our main file.<br>
	 * In addition to that it starts the method analyzeFile, which calculates the details about the file.
	 * 
	 * @param fileName name of the file that will become the main file of this object
	 * @throws IOException is thrown if the file is not found, or the details are not calculatable
	 */
	public TextFileAnalyzer(String fileName) throws IOException{
		this.rowCount = 0;
		this.sentenceCount = 0;
		this.wordCount = 0;
		this.averageWordLength = 0;
		this.charCount = 0;
		
		this.file = new File(fileName);
		
		analyzeFile();
	}
	
	/**
	 * A method that returns a string with all the details about the main file.
	 * @return all the details about the main file as String
	 */
	public String getFileInformation(){
		return "Die Anzahl der Zeilen in der Datei betraegt: " + this.rowCount + "\n" +
				"Die Anzahl der Saetze in der Datei betraegt: " + this.sentenceCount + "\n" +
				"Die Anzahl der Woerter in der Datei betraegt: " + this.wordCount + "\n" +
				"Die Durchschnittliche Anzahl an Buchstaben in einem Wort der Datei betraegt: " + this.averageWordLength;
	}
	
	/**
	 * Calculates all the details about the main file.
	 * @throws IOException is thrown if of of the details cannot be calculated
	 */
	public void analyzeFile() throws IOException{
		//A LineNumberReader is created to easily count the number of lines while reading the file and getting the other details
		LineNumberReader lnReader = new LineNumberReader(new FileReader(this.file));
		
		//Now every line is getting read
		String line;
		while ((line = lnReader.readLine()) != null)   {
			//To get the count of sentences every sentence mark has to be counted
			for(int i =0; i < line.length(); i++)
			    if(line.charAt(i) == '.' || line.charAt(i) == '!' || line.charAt(i) == '?')
			        this.sentenceCount++;
			
			//To get the word count the line gets splitted by a space and the remaining words are counted
			String[] words = line.split(" ");
			
			this.wordCount += words.length;
			
			//In order to calculate the average length of a word lateron, the characters of the whole document
			//get counted without spaces(we count the words because we dont want the spaces and here they are splitted away)			
			for(String word : words){
				this.charCount += word.length();
			}
		}
		//Now we can get the number of the last line because we read the whole file
		this.rowCount = lnReader.getLineNumber();
		//and we can calculate the average length of a word by dividing the count of chars in the whole file
		//through the count of words. Afterwars the result is rounded to an integer value.
		this.averageWordLength = (int)(this.charCount / this.wordCount);
	}
	
	/**
	 * Copies the main file into an new file specified by the first parameter.<br>
	 * It also allows the user to set a maximum line length.<br>
	 * If the maximum line length given by the user is zero, the line length is ignored.
	 * 
	 * @param pathToNewFile the path where the file should be copied to
	 * @param lineLength the line length requested by the user
	 * @return an output, that the file was copied successfully.
	 * @throws IOException thrown if one of the files aren´t accessable
	 */
	public String copyFile(String pathToNewFile, int lineLength) throws IOException{
		//Create a BufferedReader and a BufferedWriter with the main and the new file
		//to be able to get the data from the main file into the new one.
		BufferedReader bufReader = new BufferedReader(new FileReader(this.file));
		BufferedWriter newFileWriter = new BufferedWriter(new FileWriter(pathToNewFile));

		//A query that checks if a specific line length is requested
		String line;
		if(lineLength == 0){
			//If its not then copy every line from the old into the new file and flush after each of them
			while ((line = bufReader.readLine()) != null)   {
				newFileWriter.write(line);
				newFileWriter.newLine();
				newFileWriter.flush();
			}
		} else {
			//The charBuffer reads the specified count of characters for each line
			char[] charBuffer = new char[lineLength];
			//This int saves how much chars were read, but it is only used to determine where the end of the file is located
			int numbChars = 0;
			//When we are at the end of the file numbChars is -1, so this is executed until the end of the file
			while(numbChars != -1){
				//read the specified number of chars
				numbChars = bufReader.read(charBuffer);
				//find where the last word ends that fits inside the line
				int indexOfSpace = (new String(charBuffer)).lastIndexOf(" ");
				//If there is a space left in the read bytes
				if(indexOfSpace != -1){
					//the write everything until the space
					newFileWriter.write(charBuffer, 0, indexOfSpace);
					//jump into the next line
					newFileWriter.newLine();
					//and write the rest of the line into the next one in order to not destroying the words
					newFileWriter.write(charBuffer, indexOfSpace+1, lineLength-indexOfSpace-1);
				}else{
					//Otherwise just write down the line and jump into the next one
					newFileWriter.write(charBuffer, 0, lineLength);
					newFileWriter.newLine();
				}
				//flush the BufferedWriter after every line
				newFileWriter.flush();
			}
		}
		//close the Writer and Write a success-Message back to the user
		newFileWriter.close();
		return "Erfolgreich kopiert!";
	}
}
