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
import java.util.Base64;
import java.util.Random;

import static Model.ReadWriteFile.readFileBytes;
import static Model.ReadWriteFile.writeFileBytes;

public class Controller {

    @FXML
    private TextField key1Field, key2Field, key3Field;

    @FXML
    private Button generateKeysButton, doEncrypt, doDecrypt, cleanLeftButton, cleanRightButton, openFileText, openFileCrypt, saveFileText, saveFileCrypt, pickFileToEncrypt, pickFileToDecrypt;

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

        cleanLeftButton.setOnAction(e -> cleanLeft());
        cleanRightButton.setOnAction(e -> cleanRight());
    }

    private void encryptText() {
        ArrayList<String> keys = getKeys();
        if (keys == null) return;
        String plainText = textInput.getText();
        textInput.setText("");

        String encryptedText = des3.encryptDES3(plainText, keys);

        String base64EncryptedText = Base64.getEncoder().encodeToString(encryptedText.getBytes());

        textOutput.setText(base64EncryptedText);

    }

    private void decryptText() {
        ArrayList<String> keys = getKeys();
        if (keys == null) return;

        String encryptedText = textOutput.getText();
        textInput.setText("");

        byte[] decodedBase64 = Base64.getDecoder().decode(encryptedText);

        String decryptedText = des3.decryptDES3(new String(decodedBase64), keys);
        textInput.setText(decryptedText);
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
                byte[] tekst_do_zmiany = readFileBytes(file.getPath());
                System.out.println(file.getPath());

                String base64EncryptedText = Base64.getEncoder().encodeToString(tekst_do_zmiany);
                String m = isEncrypt ? des3.encryptDES3(base64EncryptedText, keys) : des3.decryptDES3(base64EncryptedText, keys);
                byte[] encryptedBytes = m.getBytes();
                //String newFileName = (isEncrypt ? "encrypted" : "decrypted");
                writeFileBytes("D:\\IV sem\\cryptology\\src\\main\\java\\GUI\\plik2222.pdf", encryptedBytes);
                //showAlert("Sukces", "Plik zapisano jako " + newFileName);
            } catch (IOException e) {
                showAlert("Błąd", "Nie udało się przetworzyć pliku.");
            }
        }
    }

//    String m = DES31.encryptDES3(base64EncryptedText, keys);
//    byte[] encryptedBytes = m.getBytes();
//    writeFileBytes("C:\studia\kadzik\ml_ap_zad3\src\algorytm1.py", encryptedBytes);

//    byte[] test2 = readFileBytes("C:\studia\kadzik\ml_ap_zad3\src\algorytm1.py");
//    String decryptedText = DES31.decryptDES3(new String(test2), keys);
//    byte[] decryptedBytes = Base64.getDecoder().decode(decryptedText);
//    writeFileBytes("C:\studia\kadzik\ml_ap_zad3\src\algorytm11.py", decryptedBytes);


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
}
