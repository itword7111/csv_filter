import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Integer csvFileColumn;
        System.out.println("Введите строку для фильтрации");
        Scanner scanner = new Scanner(System.in);
        String pattern = scanner.nextLine();
        Date startDate = new Date();
        if (args.length == 0) {
            Properties properties = new Properties();
            String propFileName = "config.properties";
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(propFileName);
            properties.load(inputStream);
            csvFileColumn = Integer.parseInt(properties.getProperty("columnNumber"));
        } else {
            csvFileColumn = Integer.parseInt(args[0]);
        }

        if (csvFileColumn > 0) {
            pattern = "\"" + pattern;
        }

        Manager manager = new Manager(pattern, csvFileColumn);
        Map<String,String> foundLines=manager.call();
        foundLines.entrySet().forEach(n-> System.out.println((n.getValue())));
        System.out.println("Найдено "+foundLines.size()+" строк");

        Date endDate = new Date();
        System.out.println("Время поиска "+(endDate.getTime() - startDate.getTime()) + " ms");
    }
}
