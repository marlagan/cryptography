package GUI;

import Model.DES3;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Random;

public class Controller {

    @FXML
    private TextField key1Field, key2Field, key3Field;

    @FXML
    private Button generateKeysButton, doEncrypt, doDecrypt;

    @FXML
    private TextArea textInput, textOutput;

    @FXML
    private RadioButton typeWindow, typeFile;  // Na razie nie używamy, ale są dostępne

    private DES3 des3;

    @FXML
    public void initialize() {
        // Inicjalizacja instancji klasy DES3 z pakietu Model
        des3 = new DES3();

        // Ustawienie obsługi przycisków
        generateKeysButton.setOnAction(e -> generateKeys());
        doEncrypt.setOnAction(e -> encryptText());
        doDecrypt.setOnAction(e -> decryptText());
    }

    // Metoda generująca trzy losowe 8-znakowe klucze (64 bitowe)
    private void generateKeys() {
        key1Field.setText(generateRandomKey());
        key2Field.setText(generateRandomKey());
        key3Field.setText(generateRandomKey());
    }

    // Prosta metoda generująca losowy 8-znakowy klucz z liter i cyfr
    private String generateRandomKey() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(characters.charAt(rand.nextInt(characters.length())));
        }
        return sb.toString();
    }

    // Metoda szyfrująca – pobiera tekst jawny i klucze, wywołuje metodę encryptDES3 z Model.DES3
    private void encryptText() {
        String plainText = textInput.getText();
        ArrayList<String> keys = new ArrayList<>();
        keys.add(key1Field.getText());
        keys.add(key2Field.getText());
        keys.add(key3Field.getText());

        String encrypted = des3.encryptDES3(plainText, keys);
        textOutput.setText(encrypted);
    }

    // Metoda deszyfrująca – pobiera szyfrogram i klucze, wywołuje metodę decryptDES3 z Model.DES3
    private void decryptText() {
        String cipherText = textOutput.getText();
        ArrayList<String> keys = new ArrayList<>();
        keys.add(key1Field.getText());
        keys.add(key2Field.getText());
        keys.add(key3Field.getText());

        String decrypted = des3.decryptDES3(cipherText, keys);
        textInput.setText(decrypted);
    }
}