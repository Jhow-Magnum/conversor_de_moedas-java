package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Scanner;

public class CurrencyConverter {

    private String apiKey = "";

    public CurrencyConverter() {
        File file = new File("src/resources/Key.txt");
        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextLine()) {
                apiKey = sc.nextLine().trim();
                System.out.println("Chave da API carregada com sucesso.");
            } else {
                throw new FileNotFoundException("Arquivo Key.txt está vazio.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo Key.txt não encontrado: " + e.getMessage());
        }
    }

    private String getExchangeRateData(String mainCurrency) throws IOException, InterruptedException {
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + mainCurrency;
        System.out.println("URL da solicitação: " + url);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Erro na API: Código de status HTTP " + response.statusCode());
        }

        return response.body();
    }

    public double getConversionRate(String mainCurrency, String toConvertCurrency) throws IOException, InterruptedException {
        String responseData = getExchangeRateData(mainCurrency);
        JsonObject jsonData = new Gson().fromJson(responseData, JsonObject.class);
        JsonObject dataObj = jsonData.getAsJsonObject("conversion_rates");

        if (dataObj != null && dataObj.has(toConvertCurrency)) {
            return dataObj.get(toConvertCurrency).getAsDouble();
        } else {
            System.out.println("Moeda " + toConvertCurrency + " não encontrada na resposta da API.");
            throw new RuntimeException("Erro ao conectar à API ou moeda não encontrada.");
        }
    }

    public double convertCurrency(double amount, double rate) {
        return amount * rate;
    }
}
