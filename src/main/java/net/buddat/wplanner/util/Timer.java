package net.buddat.wplanner.util;

public class Timer
{

    private long startTime, endTime;
    private String name;
    
    public Timer(String timerName) 
    { 
        name = timerName;
    }
    
    public void start()
    {
        startTime = System.nanoTime();
    }
    
    public void end()
    {
        endTime = System.nanoTime();
    }

    public long getStartTime()
    {
        return startTime;
    }

    public long getEndTime()
    {
        return endTime;
    }

    public long getTimeDifference()
    {
        return (endTime - startTime) / 1000000L;
    }

    public String getName()
    {
        return name;
    }
    
    @Override
    public String toString() {
        return "[Timer] " + name + ": " + getTimeDifference(getTimeDifference());
    }
    
    public String getTimeDifference(long difference) {
        long hours = 0, minutes = 0, seconds = 0, ms = 0;
        
        seconds = difference / 1000;
        minutes = seconds / 60;
        hours = minutes / 60;
        
        ms = difference - (seconds * 1000);
        seconds -= minutes * 60;
        minutes -= hours * 60;
        
        String timeDiff = (hours < 10 ? "0" + hours : hours) + ":" +
                (minutes < 10 ? "0" + minutes : minutes) + ":" +
                (seconds < 10 ? "0" + seconds : seconds) + ":" +
                (ms < 100 ? "0" + (ms < 10 ? "0" + ms : ms) : ms);
        
        return timeDiff;
    }

}

