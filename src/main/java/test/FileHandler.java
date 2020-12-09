package test;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileHandler {

	public static String openFromFile() {
		File file = askForOpenFile();
		if (file == null) {
			return null;
		}

		return loadFromFile(file);
	}

	public static String loadFromFile(File file) {
		String input = "";
		try (Scanner s = new Scanner(file)) {
			while (s.hasNextLine()) {
				String line = s.nextLine();
				input += line;
			}
		} catch (FileNotFoundException e) {
			// should never happen since we return on null file
			// so if we end up here it's something really bad
			// and so we let it blow up to runtime
			throw new RuntimeException(e);
		}
		return input;
	}

	public static void saveToFile(String output) {
		File file = askForSaveFile();
		if (file == null) {
			return;
		}
		try (FileWriter fileWriter = new FileWriter(file)) {
			fileWriter.write(output);
			fileWriter.close();
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	private static File askForSaveFile() {
		return getFileChooser().showSaveDialog(new Stage());
	}


	private static File askForOpenFile() {
		return getFileChooser().showOpenDialog(new Stage());
	}

	private static FileChooser getFileChooser() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Plik z danymi testowymi.");
		fileChooser.getExtensionFilters().
				  add(new ExtensionFilter("Text files (*.txt)", "*.txt"));

		return fileChooser;
	}
}
