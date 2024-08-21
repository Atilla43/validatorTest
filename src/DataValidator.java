import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.regex.*;

public class DataValidator {
    public static void main(String[] args) {
        try {
            // Парсим ApplicationData
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document applicationData = builder.parse(new File("src/resurces/ApplicationData.xml"));
            Document systemData = builder.parse(new File("src/resurces/SystemData.xml"));

            // Получаем данные по продуктам
            NodeList loanProducts = systemData.getElementsByTagName("LoanProduct");
            if (loanProducts.getLength() > 0) {
                // Приводим к Element для получения дочерних элементов
                Element loanProduct = (Element) loanProducts.item(0);

                int minAmount = Integer.parseInt(loanProduct.getElementsByTagName("MinAmount")
                        .item(0).getTextContent());
                int maxAmount = Integer.parseInt(loanProduct.getElementsByTagName("MaxAmount")
                        .item(0).getTextContent());
                int minTerm = Integer.parseInt(loanProduct.getElementsByTagName("MinTerm")
                        .item(0).getTextContent());
                int maxTerm = Integer.parseInt(loanProduct.getElementsByTagName("MaxTerm")
                        .item(0).getTextContent());

                // Проверяем каждого заемщика
                NodeList loaners = applicationData.getElementsByTagName("Loaner");
                for (int i = 0; i < loaners.getLength(); i++) {
                    Element loaner = (Element) loaners.item(i);

                    // Проверка возраста
                    int age = Integer.parseInt(loaner.getElementsByTagName("Age").item(0).getTextContent());
                    if (age > 85) {
                        System.out.println("Ошибка: Возраст заемщика не должен превышать 85 лет.");
                    }

                    // Проверка суммы и срока кредита
                    int loanAmount = Integer.parseInt(loaner.getElementsByTagName("LoanAmount").item(0).getTextContent());
                    int loanTerm = Integer.parseInt(loaner.getElementsByTagName("LoanTerm").item(0).getTextContent());

                    if (loanAmount <= minAmount || loanAmount >= maxAmount) {
                        System.out.println("Ошибка: Сумма кредита должна быть больше минимальной (" + minAmount + ") и меньше максимальной (" + maxAmount + ").");
                    }

                    if (loanTerm < minTerm || loanTerm > maxTerm) {
                        System.out.println("Ошибка: Срок кредита должен быть от " + minTerm + " до " + maxTerm + " месяцев.");
                    }

                    String inn = loaner.getElementsByTagName("INN").item(0).getTextContent();
                    if (!isValidINN(inn)) {
                        System.out.println("Ошибка: Неверный ИНН: " + inn);
                    }
                }
            } else {
                System.out.println("Ошибка: Не найдены условия по кредитам.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isValidINN(String inn) {
        // Пример проверки: ИНН должен быть 10 или 12 цифр
        Pattern pattern = Pattern.compile("\\d{10}|\\d{12}");
        Matcher matcher = pattern.matcher(inn);
        return matcher.matches();
    }
}
