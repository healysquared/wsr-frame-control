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

    int ldlPage = 0;

    public TODBuilder setOMCW(OMCW omcw) {
        this.omcw = omcw;
        return this;
    }
    public TimeOfDayFrame build() {
        return new TimeOfDayFrame(omcw, timeZone, dayOfWeek, month,
            dayOfMonth, hours, minutes, seconds, PM);
    }

}
