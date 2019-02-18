public class TODBuilder {

    OMCW omcw;
    int timeZone;
    int dayOfWeek;
    int month;
    int dayOfMonth;
    int hours;
    int minutes;
    int seconds;
    int PM;

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
    public TODBuilder setMonth(int month) 
    {
        this.month = month;
        return this;
    }
    public TODBuilder setDayOfMonth(int dayOfMonth) 
    {
        this.dayOfMonth = dayOfMonth;
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
        this.minutes = minutes;
        return this;
    }
    public TODBuilder setSeconds(int seconds)
    {
        this.seconds = seconds;
        return this;
    }
    public TODBuilder setPM(int PM)
    {
        this.PM = PM;
        return this;
    }
    public DataFrame[] build() 
    {
        DataFrame[] todframes = new DataFrame[1];
        todframes[0] = new TimeOfDayFrame(omcw, timeZone, dayOfWeek, month,
            dayOfMonth, hours, minutes, seconds, PM);
        return todframes;
    }

}
