package test;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import analyzer.TextFileAnalyzer;
/**
 * A Testclass, that uses the file specified by the command line arguments and analyzes it. Afterwards the user<br>
 * gets asked if he wants to copy the file with a specified line length of if he wants to see the details that have<br>
 * been analyzed by this application. 
 * 
 * @author Gabriel Pawlowsky
 * @version 2012-04-23
 */
public class Testclass {
	/**
	 * Main-Method that is prompting the user to do what he wants.
	 * @param args in the command linbe arguments only the filename for the main file that should be analyzed is required.
	 */
	public static void main(String[] args){
		//Synopsis, this shows up if the user didn´t specify the right command line argument
		//it also shows the user how to do it right
		if (args.length != 1) {
			System.err
					.println("Works like this: \nfile.jar textFileToBeAnalyzed");
			System.exit(1);
		}
				
		try {
			//This is for big files only. It tells the user he has to wait a little bit until the main file is analyzed
			System.out.println("Analysieren der Datei laeuft. Bitte warten...");
			
			//specifieing and analyzing the main file
			TextFileAnalyzer tfa = new TextFileAnalyzer(args[0]);
			
			//showing a help screen
			System.out.println("Sie sind nun in der Konsole des TextFileAnalyzers angelangt.\n" +
					"Mit der Eingabe von 'copy pathToNewFile [lineLength]' koennen sie die Datei mit den\n" +
					"angegebenen Spezifikationen kopieren. Ohne Angabe einer Zeilenlaenge wird diese ignoriert.\n" +
					"Mit der Eingabe von 'getInfo' koennen sie sich die Grundinformationen ueber ihre Datei\n" +
					"ausgeben lassen.\n" +
					"Mit der Eingabe von 'help' koennen sie sich diese Hilfe erneut ausgeben lassen.");
			
			//an endless loop which allows the user to do everything specified in the help screen
			while(true){
				//gets the input in the comman dline from the user
				String command = new BufferedReader(new InputStreamReader(System.in))
				.readLine();
				
				//This query tests if the input from the user and does what he asked to
				if(command.toLowerCase().startsWith("copy")){
					//If the user specified only 1 parameter at the copy clause, the line length is set to 0
					String[] commandParts = command.toLowerCase().split(" ");
					if(commandParts.length == 2)
						System.out.println(tfa.copyFile(commandParts[1], 0));
					//else the copy clause is set to the value specified by the user
					else if(commandParts.length == 3)
						System.out.println(tfa.copyFile(commandParts[1], Integer.parseInt(commandParts[2])));
					//if the users input could not be recognized, a synopsis gets printed
					else
						System.err
							.println("Works like this: \ncopy pathToNewFile [lineLength]");
					//if the user writes 'getinfo' the details of the main file get printed
				} else if(command.toLowerCase().startsWith("getinfo")){
					System.out.println(tfa.getFileInformation());
					//and if the user writes 'help' the help screen gets printed again
				} else if(command.toLowerCase().startsWith("help")){
					System.out.println("Sie sind nun in der Konsole des TextFileAnalyzers angelangt.\n" +
							"Mit der Eingabe von 'copy pathToNewFile [lineLength]' koennen sie die Datei mit den\n" +
							"angegebenen Spezifikationen kopieren. Ohne Angabe einer Zeilenlaenge wird diese ignoriert.\n" +
							"Mit der Eingabe von 'getInfo' koennen sie sich die Grundinformationen ueber ihre Datei\n" +
							"ausgeben lassen.\n" +
							"Mit der Eingabe von 'help' koennen sie sich diese Hilfe erneut ausgeben lassen.");
				//If the user printed nothing of the above, the application tells him taht he made a mistake
				} else {
					System.err.println("Ihre Eingabe ist inkorrekt und kann nicht verarbeitet werden!");
				}
			}
		//If some exceptions occur during the whole process they are catched and the user is notified
		} catch (FileNotFoundException e) {
			System.err.println("Die angegebene Datei konnte nicht gefunden werden!");
		} catch (IOException e) {
			System.err.println("Fehler beim Auslesen der Eckdaten der Dateien!");
		} catch (Exception e){
			System.err.println("Ein Fehler ist aufgetreten!");
		}
	}
}
