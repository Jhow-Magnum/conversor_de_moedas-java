package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class JsonUtils {

    private static final String JSON_FILE_PATH = "conversion_history.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static JsonObject readJsonFile() {
        File jsonFile = new File(JSON_FILE_PATH);
        if (!jsonFile.exists()) {
            System.out.println("Arquivo JSON não encontrado. Criando novo arquivo...");
            createNewJsonFile();
        }

        try (FileReader reader = new FileReader(jsonFile)) {
            return gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo JSON: " + e.getMessage());
            return null;
        }
    }

    private static void createNewJsonFile() {
        JsonObject jsonFile = new JsonObject();
        jsonFile.add("historico", new JsonArray());
        jsonFile.add("ultimaConversao", new JsonObject());

        try (FileWriter fileWriter = new FileWriter(JSON_FILE_PATH)) {
            gson.toJson(jsonFile, fileWriter);
            fileWriter.flush();
            System.out.println("Arquivo JSON criado com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao criar o arquivo JSON: " + e.getMessage());
        }
    }

    public static void writeConversionToJson(String mainCurrency, String toConvertCurrency, double amount, double convertedAmount) {
        JsonObject jsonFile = readJsonFile();
        if (jsonFile == null) {
            return;
        }

        JsonArray historico = jsonFile.getAsJsonArray("historico");
        JsonObject novaConversao = new JsonObject();
        novaConversao.addProperty("data", LocalDateTime.now().toString());
        novaConversao.addProperty("moedaOrigem", mainCurrency);
        novaConversao.addProperty("moedaDestino", toConvertCurrency);
        novaConversao.addProperty("valorOrigem", amount);
        novaConversao.addProperty("valorConvertido", convertedAmount);
        historico.add(novaConversao);
        jsonFile.add("historico", historico);
        jsonFile.add("ultimaConversao", novaConversao);

        try (FileWriter fileWriter = new FileWriter(JSON_FILE_PATH)) {
            gson.toJson(jsonFile, fileWriter);
            fileWriter.flush();
            System.out.println("Conversão registrada no arquivo JSON.");
        } catch (IOException e) {
            System.out.println("Erro ao escrever no arquivo JSON: " + e.getMessage());
        }
    }

    public static void printConversionHistory() {
        JsonObject jsonFile = readJsonFile();
        if (jsonFile == null) {
            return;
        }

        JsonArray historico = jsonFile.getAsJsonArray("historico");
        if (historico.size() == 0) {
            System.out.println("Nenhuma conversão registrada ainda.");
        } else {
            System.out.println("Histórico de Conversões:");
            for (int i = 0; i < historico.size(); i++) {
                JsonObject conversao = historico.get(i).getAsJsonObject();
                System.out.println("Conversão " + (i + 1) + ":");
                System.out.println("Data: " + conversao.get("data").getAsString());
                System.out.println("Moeda de Origem: " + conversao.get("moedaOrigem").getAsString());
                System.out.println("Moeda de Destino: " + conversao.get("moedaDestino").getAsString());
                System.out.println("Valor de Origem: " + conversao.get("valorOrigem").getAsDouble());
                System.out.println("Valor Convertido: " + conversao.get("valorConvertido").getAsDouble());
                System.out.println();
            }
        }
    }
}
