public class TODBuilder {

    OMCW omcw;
    int timeZone;
    int dayOfWeek;
    int month;
    int dayOfMonth;
    int hours;
    int minutes;
    int seconds;
    boolean PM;

    public TODBuilder setOMCW(OMCW omcw) 
    {
        this.omcw = omcw;
        return this;
    }
    public TODBuilder setTimeZone(int timeZone) 
    {
        this.timeZone = timeZone;
        return this;
    }
    public TODBuilder setDayOfWeek(int dayOfWeek) 
    {
        this.dayOfWeek = dayOfWeek;
        return this;
    }
    public TODBuilder setHours(int hours) 
    {
        this.hours = hours;
        return this;
    }
    public TODBuilder setMinutes(int minutes)
    {
        this.timeZone = minutes;
        return this;
    }
    public TODBuilder setSeconds(int seconds)
    {
        this.seconds = seconds;
        return this;
    }
    public TODBuilder setPM(boolean PM)
    {
        this.PM = PM;
        return this;
    }
    public TimeOfDayFrame build() 
    {
        return new TimeOfDayFrame(omcw, timeZone, dayOfWeek, month,
            dayOfMonth, hours, minutes, seconds, PM);
    }

}
