package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ApiKeyReader {

    public static String readApiKey() {
        String apiKey = "";
        File file = new File("src/resources/Key.txt");
        try (Scanner scanner = new Scanner(file)) {
            apiKey = scanner.nextLine().trim(); // Lê a chave e remove espaços em branco
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        }
        return apiKey;
    }
}
