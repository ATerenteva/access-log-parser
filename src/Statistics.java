import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Statistics {
    long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> pages = new HashSet<>();
    private HashSet<String> unavailablePages = new HashSet<>();
    private HashSet<String> ip = new HashSet<>();
    private HashSet<String> domains = new HashSet<>();
    private HashMap<String, Integer> osStatistic = new HashMap<>();
    private HashMap<String, Integer> browserStatistic = new HashMap<>();
    private HashMap<LocalDateTime, Integer> visitsPerSecond = new HashMap<>();
    private HashMap<String, Integer> visitsPerIp = new HashMap<>();
    private int numberOfVisits;
    private int numberOfFails;
    private int numberOfUsers;


    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = LocalDateTime.now();
        this.maxTime = LocalDateTime.of(1970, 1, 1, 0, 1);
    }

//метод получения вспомогательных переменных для сбора статистики
    public void addEntry(LogEntry logEntry) {
        if (logEntry.getTime().isBefore(this.minTime)) this.minTime = logEntry.getTime();
        if (logEntry.getTime().isAfter(this.maxTime)) this.maxTime = logEntry.getTime();

        this.totalTraffic += logEntry.getResponseSize();

        if (logEntry.getResponseCode() == 200) pages.add(logEntry.getPath());
        if (logEntry.getResponseCode() == 404) unavailablePages.add(logEntry.getPath());

        if (!(logEntry.getUserAgent().getOs() == null)) {
            if (osStatistic.containsKey(logEntry.getUserAgent().getOs()))
                osStatistic.put(logEntry.getUserAgent().getOs(), osStatistic.get(logEntry.getUserAgent().getOs()) + 1);
            else osStatistic.put(logEntry.getUserAgent().getOs(), 1);
        }

        if (!(logEntry.getUserAgent().getBrowser() == null)) {
            if (browserStatistic.containsKey(logEntry.getUserAgent().getBrowser()))
                browserStatistic.put(logEntry.getUserAgent().getBrowser(), browserStatistic.get(logEntry.getUserAgent().getBrowser()) + 1);
            else browserStatistic.put(logEntry.getUserAgent().getBrowser(), 1);
        }

        if (!(logEntry.getUserAgent().getBrowser() == null) && !(logEntry.getUserAgent().isBot()))
            numberOfVisits += 1;

        if(logEntry.getResponseCode()/100==5||logEntry.getResponseCode()/100==4) numberOfFails+=1;

        if(!logEntry.getUserAgent().isBot()) {
            ip.add(logEntry.getIpAddr());
            numberOfUsers+=1;
            if(!(logEntry.getIpAddr()==null)) {
                if (visitsPerIp.containsKey(logEntry.getIpAddr()))
                    visitsPerIp.put(logEntry.getIpAddr(), visitsPerIp.get(logEntry.getIpAddr()) + 1);
                else visitsPerIp.put(logEntry.getIpAddr(), 1);
            }
        }

        if(!logEntry.getUserAgent().isBot()){
            if (visitsPerSecond.containsKey(logEntry.getTime()))
                visitsPerSecond.put(logEntry.getTime(), visitsPerSecond.get(logEntry.getTime()) + 1);
            else visitsPerSecond.put(logEntry.getTime(), 1);
        }

        Pattern browserP = Pattern.compile("http[s]{0,1}\\:\\/\\/[w]{0,3}\\.([^\\/]+)");
        Matcher browserM = browserP.matcher(logEntry.getReferer());
        if (browserM.find()&&!(browserM.group(1).equals("-"))) {
            domains.add(browserM.group(1));
        }
    }

    public long getTrafficRate() {
        return (long) (this.totalTraffic / ((double) ChronoUnit.MINUTES.between(this.minTime, this.maxTime) / 60));
    }

    public HashSet<String> getExistingPages() {
        return pages;
    }

    public HashSet<String> getUnavailablePages() {
        return unavailablePages;
    }

    public HashMap<String, Double> getOsStatistic() {
        double count = 0;
        HashMap<String, Double> h = new HashMap<>();
        for (Integer s : osStatistic.values()) {
            count += s;
        }
        for (String s : osStatistic.keySet()) {
            h.put(s, (double) osStatistic.get(s) / count);
        }
        return h;
    }

    public HashMap<String, Double> getBrowserStatistic() {
        double count = 0;
        HashMap<String, Double> h = new HashMap<>();
        for (Integer s : browserStatistic.values()) {
            count += s;
        }
        for (String s : browserStatistic.keySet()) {
            h.put(s, (double) browserStatistic.get(s) / count);
        }
        return h;
    }

    public double visitsPerHour() {
        return ((double) ChronoUnit.MINUTES.between(this.minTime, this.maxTime) / 60) / (double) this.numberOfVisits;
    }

    public double failsPerHour() {
        return ((double) ChronoUnit.MINUTES.between(this.minTime, this.maxTime) / 60) / (double) this.numberOfFails;
    }

    public double visitsPerUser(){
        return (double)(numberOfUsers/ ip.size())/((double) ChronoUnit.MINUTES.between(this.minTime, this.maxTime) / 60);
    }

    public int getPeakVisits(){
        return Collections.max(visitsPerSecond.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();
    }

    public HashSet<String> getDomains(){
        return domains;
    }

    public int getPeakVisitsOfUsers(){
        return Collections.max(visitsPerIp.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getValue();
    }
}
