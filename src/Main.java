import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int count = 0;
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
        }
    }
}

