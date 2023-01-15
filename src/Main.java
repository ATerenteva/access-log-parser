import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        int count = 0;
        double stringSum = 0;
        double yandex = 0;
        double google = 0;

        while (true) {
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (isDirectory) {
                System.out.println("Указанный путь является путём к папке, а не к файлу");
                continue;
            }
            if (!fileExists) {
                System.out.println("Указанный файл не существует");
                continue;
            }
            count++;
            System.out.println("Путь указан верно.");
            System.out.println("Это файл номер " + count);
            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader =
                        new BufferedReader(fileReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    if (length > 1024)
                        throw new ExceededTheLimit("The number of characters in the string should not be more than 1024");
                    stringSum += 1;
                    Pattern pattern = Pattern.compile("(?<remoteaddr>(?:^|\\b(?<!\\.))(?:1?\\d\\d?|2[0-4]\\d|25[0-5])(?:\\.(?:1?\\d\\d?|2[0-4]\\d|25[0-5])){3}(?=$|[^\\w.]))\\s-\\s(?<remoteusr>-|[a-z_][a-z0-9_]{0,30})\\s\\[(?<dateandtime>\\d{2}\\/[\\w]{3}\\/\\d{4}:\\d{2}:\\d{2}:\\d{2}\\s[\\+|\\-]\\d{4})\\]\\s(?<request>\\\"(?<reqmethod>[\\w]+)\\s(?<requri>\\/[^\\s]*)\\s(?<httpver>HTTP/\\d\\.\\d)\\\")\\s(?<status>\\d{3})\\s(?<bodybytesent>\\d+)\\s\\\"(?<httpreferer>[^\\s]+)\\\"\\s\\\"(?<useragent>[^\\\"]+)\\\"");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String usr = matcher.group("useragent");
                        Pattern subpattern = Pattern.compile("(compat.+ot[^\\)]+)");
                        Matcher submatcher = subpattern.matcher(usr);
                        if (submatcher.find()) {
                            String firstBrackets = submatcher.group();
                            String[] parts = firstBrackets.split(";");
                            if (parts.length >= 2) {
                                String fragment = parts[1];
                            }
                            String[] subParts = new String[10];
                            for (int i = 0; i < parts.length - 1; i++) {
                                subParts[i] = parts[i].replace(" ", "");
                            }
                            for (int i = 0; i < parts.length - 1; i++) {
                                Pattern botPattern = Pattern.compile("bot/", Pattern.CASE_INSENSITIVE);
                                Matcher botMatcher = botPattern.matcher(subParts[i]);
                                if (botMatcher.find()) {
                                    String botm = subParts[i];
                                    String bot = botm.substring(0, botm.indexOf("/"));
                                    Pattern googleP = Pattern.compile("Googlebot", Pattern.CASE_INSENSITIVE);
                                    Matcher googleM = googleP.matcher(bot);
                                    if (googleM.find()) {
                                        google++;
                                    }
                                    Pattern yandexP = Pattern.compile("YandexBot", Pattern.CASE_INSENSITIVE);
                                    Matcher yandexM = yandexP.matcher(bot);
                                    if (yandexM.find()) {
                                        yandex++;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("Количество строк: " + stringSum);
            System.out.println("Доля запросов от YandexBot: " + yandex / stringSum * 100 + "%");
            System.out.println("Доля запросов от GoogleBot: " + google / stringSum * 100 + "%");
        }
    }
}

