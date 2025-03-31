package GUI;

import Model.DES3;
import Model.Tools;
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
    private Button generateKeysButton, doEncrypt, doDecrypt, cleanLeftButton, cleanRightButton, openFileText, openFileCrypt, saveFileText, saveFileCrypt, pickFileToEncrypt, pickFileToDecrypt, pickFileToEncryptOther, pickFileToDecryptOther;

    @FXML
    private TextArea textInput, textOutput;


    private DES3 des3;

    @FXML
    public void initialize() {
        des3 = new DES3();

        generateKeysButton.setOnAction(e -> generateKeys());
        doEncrypt.setOnAction(e -> encryptText());
        doDecrypt.setOnAction(e -> decryptText());

        openFileText.setOnAction(e -> openFile(textInput));
        openFileCrypt.setOnAction(e -> openFile(textOutput));
        saveFileText.setOnAction(e -> saveFile(textInput));
        saveFileCrypt.setOnAction(e -> saveFile(textOutput));

        pickFileToEncrypt.setOnAction(e -> pickFile(true));
        pickFileToDecrypt.setOnAction(e -> pickFile(false));
        pickFileToEncryptOther.setOnAction(e -> pickFileOther(true));
        pickFileToDecryptOther.setOnAction(e -> pickFileOther(false));

        cleanLeftButton.setOnAction(e -> cleanLeft());
        cleanRightButton.setOnAction(e -> cleanRight());
    }

    private void encryptText() {
        ArrayList<String> keys = getKeys();
        if (keys == null) return;

        String plainText = textInput.getText();
        System.out.println(plainText);
        System.out.println(plainText.length());
        System.out.println(keys);
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
        key1Field.setText(generateRandomKey(8));
        key2Field.setText(generateRandomKey(8));
        key3Field.setText(generateRandomKey(8));
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

    private String generateRandomKey(int length){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


//    private void pickFileOther(boolean isEncrypt) {
//        FileChooser fileChooser = new FileChooser();
//        Tools tools = new Tools();
//        File file = fileChooser.showOpenDialog(null);
//        if (file != null) {
//            ArrayList<String> keys = getKeys();
//            if (keys == null) return;
//
//            try {
//                byte[] content = ReadWriteFile.readOtherFile(file.getAbsolutePath());
//                ArrayList<byte[]> bits = tools.otherToBits(content);
//
//                // Przekształcamy bajty na string do szyfrowania
//                String result = isEncrypt ? des3.encryptDES3File(bits, keys) : des3.decryptDES3File(bits, keys);
//                byte[] resultBytes = result.getBytes(); // Zamiana na bajty do zapisu
//                String newFilePath = file.getAbsolutePath().replaceAll("\\.\\w+$", "") + (isEncrypt ? "-encrypted" : "-decrypted");
//                ReadWriteFile.writeOtherFile(newFilePath, resultBytes);
//                showAlert("Sukces", "Plik zapisano jako " + newFilePath);
//            } catch (IOException e) {
//                showAlert("Błąd", "Nie udało się przetworzyć pliku.");
//            }
//        }
//    }

    private void pickFileOther(boolean isEncrypt) {
        Tools tools = new Tools();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            ArrayList<String> keys = getKeys();
            if (keys == null) return;
            try {
                // Odczytujemy plik jako bajty
                byte[] content = ReadWriteFile.readOtherFile(file.getAbsolutePath());
                // Konwertujemy bajty na "bity" (ArrayList<byte[]>)
                ArrayList<byte[]> bits = tools.otherToBits(content);
                // Szyfrujemy lub deszyfrujemy dane (DES3 dla plików)
                ArrayList<byte[]> resultBits = isEncrypt ? des3.encryptDES3File(bits, keys) : des3.decryptDES3File(bits, keys);
                // Zamieniamy ArrayList<byte[]> na tablicę byte[] do zapisu
                byte[] resultBytes = tools.bitsToByteArray(resultBits);
                // Zachowujemy oryginalne rozszerzenie pliku
                String fileName = file.getName();
                int dotIndex = fileName.lastIndexOf('.');
                String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
                String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);
                String newFilePath = file.getAbsolutePath().replace(fileName, baseName + (isEncrypt ? "-encrypted" : "-decrypted") + extension);
                // Zapisujemy wynik do pliku
                ReadWriteFile.writeOtherFile(newFilePath, resultBytes);
                showAlert("Sukces", "Plik zapisano jako " + newFilePath);
            } catch (IOException e) {
                showAlert("Błąd", "Nie udało się przetworzyć pliku.");
            }
        }
    }






}
