import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int count = 0;
        int stringSum = 0;
        int lenghtMax = 0;
        int lenghtMin = 1024;
        while (true) {
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (isDirectory == true) {
                System.out.println("Указанный путь является путём к папке, а не к файлу");
                continue;
            }
            if (fileExists == false) {
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
                    if(length>1024) throw new ExceededTheLimit("The number of characters in the string should not be more than 1024");
                    if(length>lenghtMax) lenghtMax = length;
                    if(length<lenghtMin) lenghtMin = length;
                    stringSum+=1;
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
            System.out.println("Количество строк: " + stringSum);
            System.out.println("Длина самой длинной строки: " + lenghtMax);
            System.out.println("Длина самой короткой строки: " + lenghtMin);
        }
    }
}

