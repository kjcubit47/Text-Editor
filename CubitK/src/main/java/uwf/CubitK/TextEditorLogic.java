package uwf.CubitK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class TextEditorLogic {
	
	private String fileLocation = null;
	private File currentFile = null;
	private File dictionaryFile = null;
	private HashSet<String> dictionary;
	
	public TextEditorLogic() {
		dictionary = new HashSet<String>();
	}
	
	public void createDictionary(String fileName) {
		Scanner dictionaryScanner = null;
		File dictionaryFile = new File(fileName);
		try {
			dictionaryScanner = new Scanner(dictionaryFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(dictionaryScanner.hasNext()) {
			dictionary.add(dictionaryScanner.next().toLowerCase());
		}

		dictionaryScanner.close();
	}
	
	
	public void setSaveAs(MenuItem item, TextArea text, Stage stage) {
		
		item.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			PrintWriter writeFile = null;
    			FileChooser saveChooser = new FileChooser();
    			File newFile = saveChooser.showSaveDialog(stage);
    			try {
    				writeFile = new PrintWriter(newFile);
    			}
    			catch(IOException e) {
    				e.printStackTrace();
    			}
    			writeFile.print(text.getText());
    			writeFile.close();
    		}
    	});
	}
	
	public void setSave(MenuItem item, TextArea text, Stage stage) {
		item.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent event) {
    			if(currentFile == null) {
    				setSaveAs(item, text, stage);
    			}
    			else {
    				PrintWriter saveWriter = null;
    				try {
    					saveWriter = new PrintWriter(currentFile);
    				} catch (FileNotFoundException e) {
    					e.printStackTrace();
    				}
    				saveWriter.print(text.getText());
    				saveWriter.close();
    			}
    		}
    	});

	}
	
	public void setLoad(MenuItem item, TextArea text, Stage stage) {
		item.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				FileChooser loadChooser = new FileChooser();
				Scanner fileScanner = null;
				String fileWords = "";
				currentFile = loadChooser.showOpenDialog(stage);

				try {
					fileScanner = new Scanner(currentFile);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				while (fileScanner.hasNext()) {
					fileWords += fileScanner.nextLine() + "\n";
				}
				text.setText(fileWords);
			}
		
		});
		
	}
	
	public void setSpellCheck(MenuItem item, TextArea text) {
		item.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent e) {
    			String allText  = text.getText();
    			String[] wordErrors = new String[100];
    			String[] wordSuggestions = new String[100];
    			int errorCount = 0;
    			allText = allText.replaceAll("[\\.]*[,]*", "");
    			Scanner textScanner = new Scanner(allText);
    			while(textScanner.hasNext()) {
    				String currentWord = textScanner.next().toLowerCase();
    				if (dictionary.contains(currentWord))
    					continue;
    				else {
    					
    					StringBuffer currWordBuffer = new StringBuffer(currentWord);
    					for (int i = 0; i < currentWord.length()+1; i++) {
    						for(char letter = 'a'; letter <= 'z'; letter++ ) {
    							currWordBuffer.insert(i, letter);
    							if (dictionary.contains(currWordBuffer.toString())){
    								wordErrors[errorCount] = currentWord;
    								wordSuggestions[errorCount] = currWordBuffer.toString();
    								errorCount++;
    							}	
    	    					currWordBuffer = new StringBuffer(currentWord);
    						}
    					}
    					for (int i = 0; i < currentWord.length(); i++) {
    						currWordBuffer.deleteCharAt(i);
    						if (dictionary.contains(currWordBuffer.toString())) {
    							wordErrors[errorCount] = currentWord;
    							wordSuggestions[errorCount] = currWordBuffer.toString();
    							errorCount++;
    						}
    						currWordBuffer = new StringBuffer(currentWord);
    					}
    					for (int i = 0; i < currentWord.length() - 1; i++) {
    						char temp = currentWord.charAt(i);
    						currWordBuffer.setCharAt(i, currWordBuffer.charAt(i + 1));
    						currWordBuffer.setCharAt(i + 1, temp);
    						if (dictionary.contains(currWordBuffer.toString())) {
    							wordErrors[errorCount] = currentWord;
    							wordSuggestions[errorCount] = currWordBuffer.toString();
    							errorCount++;
    						}
    						currWordBuffer = new StringBuffer(currentWord);
    					}
    				}
    			}
    			textScanner.close();
    			
    			Alert resultBox = new Alert(AlertType.INFORMATION);
    			resultBox.setTitle("Spell Checker Results");
    			if (errorCount == 0) {
    				resultBox.setHeaderText("No errors found.");
    				resultBox.showAndWait();

    			}
    			else {
    				for (int i = 0; i < errorCount; i++) {
    					resultBox.setHeaderText(wordErrors[i]);
    					resultBox.setContentText(wordSuggestions[i]);
    	    			resultBox.showAndWait();

    				}
    			}
    		
    		}
    		
    	});
    	
	}
	
}
