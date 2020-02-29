public class OMCWBuilder {

    boolean localProgram = false;
    boolean localPreroll = false;
    boolean auxAudio = false;
    boolean wxWarning = false;
    boolean radar = false;
    boolean regionSeparator = false;
    boolean topSolid = false;
    boolean botSolid = false;
    int topPageNum = 0;
    int ldlPage = 0;

    public OMCWBuilder setLocalProgramEnabled(boolean localProgram) {
        this.localProgram = localProgram;
        return this;
    }

    public OMCWBuilder setLocalPrerollEnabled(boolean localPreroll) {
        this.localPreroll = localPreroll;
        return this;
    }

    public OMCWBuilder setAuxAudioEnabled(boolean auxAudio) {
        this.auxAudio = auxAudio;
        return this;
    }

    public OMCWBuilder setWxWarningEnabled(boolean wxWarning) {
        this.wxWarning = wxWarning;
        return this;
    }

    public OMCWBuilder setRadarEnabled(boolean radar) {
        this.radar = radar;
        return this;
    }

    public OMCWBuilder setRegionSeparatorEnabled(boolean regionSeparator) {
        this.regionSeparator = regionSeparator;
        return this;
    }

    public OMCWBuilder setTopSolid(boolean topSolid) {
        this.topSolid = topSolid;
        return this;
    }

    public OMCWBuilder setBottomSolid(boolean botSolid) {
        this.botSolid = botSolid;
        return this;
    }

    public OMCWBuilder setTopPage(int topPageNum) {
        this.topPageNum = topPageNum;
        return this;
    }

    public OMCWBuilder setLdlPage(int ldlPage) {
        this.ldlPage = ldlPage;
        return this;
    }

    public OMCW build() {
        return new OMCW(localProgram, localPreroll, auxAudio, wxWarning, radar, regionSeparator, topSolid, botSolid,
                topPageNum, ldlPage);
    }

}
