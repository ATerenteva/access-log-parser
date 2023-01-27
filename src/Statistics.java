import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Statistics {
    long totalTraffic;
    LocalDateTime minTime;
    LocalDateTime maxTime;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = LocalDateTime.now();
        this.maxTime = LocalDateTime.of(1970,1,1,0,1);
    }

    public void addEntry(LogEntry logEntry){
        if (logEntry.getTime().isBefore(this.minTime)) this.minTime = logEntry.getTime();
        if (logEntry.getTime().isAfter(this.maxTime)) this.maxTime = logEntry.getTime();
        this.totalTraffic += logEntry.getResponseSize();
    }
    public long getTrafficRate (){
        return (long)(this.totalTraffic / ((double)ChronoUnit.MINUTES.between(this.minTime, this.maxTime)/60));
    }
}
