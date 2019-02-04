public class ControlFrame extends DataFrame {

    OMCW omcw;

    public void setOmcw(OMCW omcw) {
        this.omcw = omcw;
    }

    void setOmcwBytes() {
        // OMCW is always at bytes 4-7
        byte[] omcwBytes = omcw.getOmcwBytes();
        frame[4] = omcwBytes[0];
        frame[5] = omcwBytes[1];
        frame[6] = omcwBytes[2];
        frame[7] = omcwBytes[4];
    }

}
