import model.CurrencyConverter;
import util.JsonUtils;

import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CurrencyConverter converter = new CurrencyConverter();

        while (true) {
            exibirMenu();

            System.out.print("Escolha a moeda de origem (ou digite 'sair' para encerrar): ");
            String mainCurrency = scanner.nextLine().toUpperCase();

            if (mainCurrency.equalsIgnoreCase("sair")) {
                System.out.println("Encerrando o programa...");
                break;
            }

            System.out.print("Escolha a moeda de destino: ");
            String toConvertCurrency = scanner.nextLine().toUpperCase();

            System.out.print("Informe o valor a ser convertido: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consumir a quebra de linha após o número

            try {
                double rate = converter.getConversionRate(mainCurrency, toConvertCurrency);
                double convertedAmount = converter.convertCurrency(amount, rate);

                System.out.println("\nCotação de " + mainCurrency + " para " + toConvertCurrency + " é de " + rate);
                System.out.printf("O valor da conversão para %s é de %.2f %n%n", toConvertCurrency, convertedAmount);

                JsonUtils.writeConversionToJson(mainCurrency, toConvertCurrency, amount, convertedAmount);

                //JsonUtils.printConversionHistory();
            } catch (Exception e) {
                System.out.println("Erro ao realizar a conversão: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\nMoedas disponíveis:");
        System.out.println("USD - Dólar Americano");
        System.out.println("BRL - Real Brasileiro");
        System.out.println("EUR - Euro");
        System.out.println("GBP - Libra Esterlina");
        System.out.println("ARS - Peso Argentino");
        System.out.println("CHF - Franco Suíço");
        System.out.println("AUD - Dólar Australiano");
        System.out.println("CAD - Dólar Canadense");
        System.out.println("JPY - Iene Japonês");
        System.out.println("CNY - Yuan Chinês");
    }
}
