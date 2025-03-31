package GUI;

import Model.DES3;
import Model.ReadWriteFile;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Controller {

    @FXML
    private TextField key1Field, key2Field, key3Field;

    @FXML
    private Button generateKeysButton, doEncrypt, doDecrypt, cleanLeftButton, cleanRightButton, openFileText, openFileCrypt, saveFileText, saveFileCrypt, pickFileToEncrypt, pickFileToDecrypt;

    @FXML
    private TextArea textInput, textOutput;

//    @FXML
//    private RadioButton typeWindow, typeFile; skurwysynstwo

//    @FXML
//    private Label fileNameText, fileNameCrypt;

    private DES3 des3;
//    private ToggleGroup encryptionMode;

    @FXML
    public void initialize() {
        des3 = new DES3();

//        encryptionMode = new ToggleGroup();
//        typeWindow.setToggleGroup(encryptionMode);
//        typeFile.setToggleGroup(encryptionMode);
//        typeWindow.setSelected(true);

        generateKeysButton.setOnAction(e -> generateKeys());
        doEncrypt.setOnAction(e -> encryptText());
        doDecrypt.setOnAction(e -> decryptText());

        openFileText.setOnAction(e -> openFile(textInput));
        openFileCrypt.setOnAction(e -> openFile(textOutput));
        saveFileText.setOnAction(e -> saveFile(textInput));
        saveFileCrypt.setOnAction(e -> saveFile(textOutput));

        pickFileToEncrypt.setOnAction(e -> pickFile(true));
        pickFileToDecrypt.setOnAction(e -> pickFile(false));

        cleanLeftButton.setOnAction(e -> cleanLeft());
        cleanRightButton.setOnAction(e -> cleanRight());
    }

    private void encryptText() {
        ArrayList<String> keys = getKeys();
        if (keys == null) return;

        String plainText = textInput.getText();
        textOutput.setText(des3.encryptDES3(plainText, keys));

    }

    private void decryptText() {
        ArrayList<String> keys = getKeys();
        if (keys == null) return;

        String encryptedText = textOutput.getText();
        textInput.setText(des3.decryptDES3(encryptedText, keys));
    }

    private void openFile(TextArea targetArea) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                targetArea.setText(ReadWriteFile.readText(file.getName()));
            } catch (IOException e) {
                showAlert("Błąd", "Nie udało się wczytać pliku.");
            }
        }
    }

    private void saveFile(TextArea sourceArea) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                ReadWriteFile.writeText(file.getName(), sourceArea.getText());
                showAlert("Sukces", "Plik zapisano pomyślnie.");
            } catch (IOException e) {
                showAlert("Błąd", "Nie udało się zapisać pliku.");
            }
        }
    }

    private void pickFile(boolean isEncrypt) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            ArrayList<String> keys = getKeys();
            if (keys == null) return;

            try {
                String content = ReadWriteFile.readText(file.getName());
                String result = isEncrypt ? des3.encryptDES3(content, keys) : des3.decryptDES3(content, keys);
                String newFilePath = (isEncrypt ? "encrypted.txt" : "decrypted.txt");
                ReadWriteFile.writeText(newFilePath, result);
                showAlert("Sukces", "Plik zapisano jako " + newFilePath);
            } catch (IOException e) {
                showAlert("Błąd", "Nie udało się przetworzyć pliku.");
            }
        }
    }


    private ArrayList<String> getKeys() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(key1Field.getText());
        keys.add(key2Field.getText());
        keys.add(key3Field.getText());
        if (keys.contains("") || keys.contains(null)) {
            showAlert("Błąd", "Wprowadź wszystkie klucze.");
            return null;
        }
        return keys;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void cleanLeft() {
        textInput.setText(null);
    }

    private void cleanRight() {
        textOutput.setText(null);
    }

    private void generateKeys() {
        key1Field.setText(generateRandomKey());
        key2Field.setText(generateRandomKey());
        key3Field.setText(generateRandomKey());
    }

    private String generateRandomKey() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            sb.append(characters.charAt(rand.nextInt(characters.length())));
        }
        return sb.toString();
    }
}
