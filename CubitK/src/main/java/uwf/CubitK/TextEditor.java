package uwf.CubitK;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.FileChooser;


public class TextEditor extends Application {

	private String fileLocation = null;
	private File currentFile = null;
	private File dictionaryFile = null;
	private TextArea text = null;
	private Stage stage = null;
	private HashSet<String> dictionary = null;
	private TextEditorLogic logic = null;
	
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	
	public String getFileLocation() {
		return fileLocation;
	}
	
	public void setDictionaryFile(File dictionary) {
		this.dictionaryFile = dictionary;
	}
	
	public File getDictionaryFile() {
		return dictionaryFile;
	}
	
	public void setCurrentFile(File currentFile) {
		this.currentFile = currentFile;
	}
	
	public File getCurrentFile() {
		return currentFile;
	}
	
	public void createDictionary() {
		dictionary = new HashSet<String>();
		Scanner dictionaryScanner = null;
		try {
			dictionaryScanner = new Scanner(getDictionaryFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(dictionaryScanner.hasNext()) {
			dictionary.add(dictionaryScanner.next().toLowerCase());
		}

		dictionaryScanner.close();
	}
	
	public void saveAsLogic() {
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
	
	public void saveLogic() {
		PrintWriter saveWriter = null;
		try {
			saveWriter = new PrintWriter(currentFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		saveWriter.print(text.getText());
		saveWriter.close();
	}
	
	public void loadLogic() {
		FileChooser loadChooser = new FileChooser();
		Scanner fileScanner = null;
		String fileWords = "";
		setCurrentFile(loadChooser.showOpenDialog(stage));
		setFileLocation(getCurrentFile().getAbsolutePath());

		try {
			fileScanner = new Scanner(getCurrentFile());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (fileScanner.hasNext()) {
			fileWords += fileScanner.nextLine() + "\n";
		}
		text.setText(fileWords);
		
	}
	
//	public void spellCheckLogic() {
//		String allText  = text.getText();
//		String[] wordErrors = new String[100];
//		String[] wordSuggestions = new String[100];
//		int errorCount = 0;
//		allText = allText.replaceAll("[\\.]*[,]*", "");
//		Scanner textScanner = new Scanner(allText);
//		while(textScanner.hasNext()) {
//			String currentWord = textScanner.next().toLowerCase();
//			if (dictionary.contains(currentWord))
//				continue;
//			else {
//				
//				StringBuffer currWordBuffer = new StringBuffer(currentWord);
//				for (int i = 0; i < currentWord.length()+1; i++) {
//					for(char letter = 'a'; letter <= 'z'; letter++ ) {
//						currWordBuffer.insert(i, letter);
//						if (dictionary.contains(currWordBuffer.toString())){
//							wordErrors[errorCount] = currentWord;
//							wordSuggestions[errorCount] = currWordBuffer.toString();
//							errorCount++;
//						}	
//    					currWordBuffer = new StringBuffer(currentWord);
//					}
//				}
//				for (int i = 0; i < currentWord.length(); i++) {
//					currWordBuffer.deleteCharAt(i);
//					if (dictionary.contains(currWordBuffer.toString())) {
//						wordErrors[errorCount] = currentWord;
//						wordSuggestions[errorCount] = currWordBuffer.toString();
//						errorCount++;
//					}
//					currWordBuffer = new StringBuffer(currentWord);
//				}
//				for (int i = 0; i < currentWord.length() - 1; i++) {
//					char temp = currentWord.charAt(i);
//					currWordBuffer.setCharAt(i, currWordBuffer.charAt(i + 1));
//					currWordBuffer.setCharAt(i + 1, temp);
//					if (dictionary.contains(currWordBuffer.toString())) {
//						wordErrors[errorCount] = currentWord;
//						wordSuggestions[errorCount] = currWordBuffer.toString();
//						errorCount++;
//					}
//					currWordBuffer = new StringBuffer(currentWord);
//				}
//			}
//		}
//		textScanner.close();
//		
//		Alert resultBox = new Alert(AlertType.INFORMATION);
//		resultBox.setTitle("Spell Checker Results");
//		if (errorCount == 0) {
//			resultBox.setHeaderText("No errors found.");
//			resultBox.showAndWait();
//
//		}
//		else {
//			for (int i = 0; i < errorCount; i++) {
//				resultBox.setHeaderText(wordErrors[i]);
//				resultBox.setContentText(wordSuggestions[i]);
//    			resultBox.showAndWait();
//
//			}
//		}
//	
//	}
	
    @Override
    public void start(Stage stage) {
    	
    	this.stage = stage;
    	logic = new TextEditorLogic();
    	logic.createDictionary("dictionary.txt");

    	BorderPane root = new BorderPane();
    	setDictionaryFile(new File("dictionary.txt"));
    	createDictionary();
    	text = new TextArea();
    	text.setWrapText(true);
    	text.setEditable(true);
    	root.setCenter(text);
    	MenuBar bar = new MenuBar();
    	Menu file = new Menu("File");
    	Menu edit = new Menu("Edit");
    	MenuItem save = new MenuItem("Save");
//    	save.setOnAction(new EventHandler<ActionEvent>() {
//    		public void handle(ActionEvent event) {
//    			if(getFileLocation() == null) {
//    				saveAsLogic();
//    			}
//    			else {
//    				saveLogic();
//    			}
//    		}
//    	});
//    	
    	logic.setSave(save, text, stage);
    	
    	MenuItem saveAs = new MenuItem("Save As");
//    	saveAs.setOnAction(new EventHandler<ActionEvent>() {
//    		public void handle(ActionEvent event) {
//    			saveAsLogic();
//    		}
//    	});
//    	
    	logic.setSaveAs(saveAs, text, stage);
    	
    	MenuItem load = new MenuItem("Load");
//    	load.setOnAction(new EventHandler<ActionEvent>(){
//    		@Override
//    		public void handle(ActionEvent event) {
//    			loadLogic();
//    		}
//    	});
    	
    	logic.setLoad(load, text, stage);
    	
    	MenuItem spellCheck = new MenuItem("Spell Check"); 
//    	spellCheck.setOnAction(new EventHandler<ActionEvent>() {
//    		@Override
//    		public void handle(ActionEvent e) {
//    			spellCheckLogic();
//    		}
//    	});
    	
    	logic.setSpellCheck(spellCheck, text);
//    	
    	file.getItems().addAll(save, saveAs, load);
    	edit.getItems().addAll(spellCheck);
    	bar.getMenus().addAll(file, edit);
    	root.setTop(bar);
    	

    	
    	
    	Scene scene = new Scene(root, 600, 500);
    	stage.setTitle("Text Editor");
    	
    
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
   
}