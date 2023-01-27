import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgent {
    final String useragent;
    final String os;
    final String browser;
    public UserAgent(String useragent) {
        String temp = "";
        String tempBrowser = "";
        String tempOS = "";
        this.useragent = useragent;
        Pattern browserP = Pattern.compile("([^\\/\\(\\;]+)");
        Matcher browserM = browserP.matcher(useragent);
        if (browserM.find()) {
            tempBrowser=browserM.group(1).equals("-")?null:browserM.group(1);
        }
        else{
            tempBrowser = null;
        }
        if (!useragent.contains("(")||useragent.equals("-")) tempOS = null;
        else {
            Pattern osP = Pattern.compile("\\(([^)]+)");
            Matcher osM = osP.matcher(useragent);
            if (!osM.find()) tempOS = null;
            else {
                temp = osM.group(1);
                if (temp.contains("Windows")) tempOS = "Windows";
                else {
                    if (temp.contains("X11")) {tempOS = "Linux";}
                    else {
                        if (temp.contains("Mac OS")) {tempOS = "MacOS";}
                        else {
                            if (temp.contains("compatible")) tempOS = null;
                            else {
                                if (!temp.contains(";")) tempOS = temp;
                                else {
                                    String[] words = temp.split(";");
                                    tempOS = words[0];
                                }
                            }
                        }
                    }
                }
            }
        }
        if (tempOS!=null&&(tempOS.contains("http")||tempOS.contains(".com")||tempOS.contains(".org"))){this.os = null; this.browser = null;}
        else {
            if (tempOS!=null&&tempOS.contains("Mac")){this.browser = tempBrowser; os = "MacOS";}
            else if (tempOS!=null&&tempBrowser!=null&&(tempBrowser.contains("Bot")|tempOS.contains("crawler")|tempBrowser.equals("-")|tempBrowser.contains("itoid")|tempOS.contains("Java"))){this.os = null; this.browser = null;}
            else if (tempBrowser!=null&&tempBrowser.contains("iOS")){this.browser = tempBrowser; os = "iOS";}
            else {browser = tempBrowser; os = tempOS;}}

    }

    public String getUseragent() {
        return useragent;
    }

    public String getOs() {
        return os;
    }

    public String getBrowser() {
        return browser;
    }


    @Override
    public String toString() {
    return useragent;
        }

    }