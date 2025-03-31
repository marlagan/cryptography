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
    private Button generateKeysButton, doEncrypt, doDecrypt, cleanLeftButton, cleanRightButton, openFileText, openFileCrypt, saveFileText, saveFileCrypt;

    @FXML
    private TextArea textInput, textOutput;

    @FXML
    private RadioButton typeWindow, typeFile;

    private DES3 des3;
    private ToggleGroup encryptionMode;

    @FXML
    public void initialize() {
        des3 = new DES3();

        encryptionMode = new ToggleGroup();
        typeWindow.setToggleGroup(encryptionMode);
        typeFile.setToggleGroup(encryptionMode);
        typeWindow.setSelected(true);

        generateKeysButton.setOnAction(e -> generateKeys());
        doEncrypt.setOnAction(e -> encryptText());
        doDecrypt.setOnAction(e -> decryptText());
        openFileText.setOnAction(e -> openFile(textInput));
        openFileCrypt.setOnAction(e -> openFile(textOutput));
        saveFileText.setOnAction(e -> saveFile(textInput));
        saveFileCrypt.setOnAction(e -> saveFile(textOutput));
        cleanLeftButton.setOnAction(e -> cleanLeft());
        cleanRightButton.setOnAction(e -> cleanRight());
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
        for (int i = 0; i < 8; i++) {
            sb.append(characters.charAt(rand.nextInt(characters.length())));
        }
        return sb.toString();
    }

    private void encryptText() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(key1Field.getText());
        keys.add(key2Field.getText());
        keys.add(key3Field.getText());

        if (typeWindow.isSelected()) {
            String plainText = textInput.getText();
            String encrypted = des3.encryptDES3(plainText, keys);
            textOutput.setText(encrypted);
        } else if (typeFile.isSelected()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz plik do szyfrowania");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                try {
                    String content = ReadWriteFile.readText(file.getName());
                    String encrypted = des3.encryptDES3(content, keys);
                    ReadWriteFile.writeText(file.getName() + ".enc", encrypted);
                    showAlert("Sukces", "Plik zaszyfrowano i zapisano jako " + file.getName() + ".enc");
                } catch (IOException e) {
                    showAlert("Błąd", "Nie udało się przetworzyć pliku.");
                }
            }
        }
    }

    private void decryptText() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(key1Field.getText());
        keys.add(key2Field.getText());
        keys.add(key3Field.getText());

        if (typeWindow.isSelected()) {
            String cipherText = textOutput.getText();
            String decrypted = des3.decryptDES3(cipherText, keys);
            textInput.setText(decrypted);
        } else if (typeFile.isSelected()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wybierz plik do deszyfrowania");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                try {
                    String content = ReadWriteFile.readText(file.getName());
                    String decrypted = des3.decryptDES3(content, keys);
                    ReadWriteFile.writeText(file.getName().replace(".enc", "") + "_decrypted.txt", decrypted);
                    showAlert("Sukces", "Plik odszyfrowano i zapisano jako " + file.getName().replace(".enc", "") + "_decrypted.txt");
                } catch (IOException e) {
                    showAlert("Błąd", "Nie udało się przetworzyć pliku.");
                }
            }
        }
    }

    private void openFile(TextArea targetArea) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik");

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                String content = ReadWriteFile.readText(file.getName());
                targetArea.setText(content);
            } catch (IOException e) {
                showAlert("Błąd", "Nie udało się wczytać pliku.");
            }
        }
    }

    private void saveFile(TextArea sourceArea) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Zapisz plik");

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                ReadWriteFile.writeText(file.getName(), sourceArea.getText());
            } catch (IOException e) {
                showAlert("Błąd", "Nie udało się zapisać pliku.");
            }
        }
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
}
