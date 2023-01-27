import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    final String line;
    final String ipAddr;
    final LocalDateTime time;
    final HttpMethod method;
    final String path;
    final int responseCode;
    final int responseSize;
    final String referer;
    final UserAgent userAgent;

    public LogEntry(String line) {
        this.line = line;
        int length = line.length();
        if (length > 1024)
            throw new ExceededTheLimit("The number of characters in the string should not be more than 1024");
        Pattern pattern = Pattern.compile("(?<remoteaddr>(?:^|\\b(?<!\\.))(?:1?\\d\\d?|2[0-4]\\d|25[0-5])(?:\\.(?:1?\\d\\d?|2[0-4]\\d|25[0-5])){3}(?=$|[^\\w.]))\\s-\\s(?<remoteusr>-|[a-z_][a-z0-9_]{0,30})\\s\\[(?<dateandtime>\\d{2}\\/[\\w]{3}\\/\\d{4}:\\d{2}:\\d{2}:\\d{2}\\s[\\+|\\-]\\d{4})\\]\\s(?<request>\\\"(?<reqmethod>[\\w]+)\\s(?<requri>\\/[^\\s]*)\\s(?<httpver>HTTP/\\d\\.\\d)\\\")\\s(?<status>\\d{3})\\s(?<bodybytesent>\\d+)\\s\\\"(?<httpreferer>[^\\s]+)\\\"\\s\\\"(?<useragent>[^\\\"]+)\\\"");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            this.ipAddr = matcher.group("remoteaddr");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss ZZZ", java.util.Locale.ENGLISH);
            this.time = LocalDateTime.parse(matcher.group("dateandtime"), formatter);
            this.method = HttpMethod.valueOf(matcher.group("reqmethod"));
            this.path = matcher.group("requri");
            this.responseCode = Integer.parseInt(matcher.group("status"));
            this.responseSize = Integer.parseInt(matcher.group("bodybytesent"));
            this.referer = matcher.group("httpreferer");
            this.userAgent = new UserAgent(matcher.group("useragent"));}
        else{
            this.ipAddr = null;
            this.time = null;
            this.method = null;
            this.path = null;
            this.responseCode = 0;
            this.responseSize = 0;
            this.referer = null;
            this.userAgent = null;
        }
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }

    @Override
    public String toString() {
        return line;
    }
}
