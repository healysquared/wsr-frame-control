public class PageBuilder {

    private int pageNumber;
    private byte pageAttributes;
    private OMCW omcw;
    private Address address;
    private TextLineFrame[] textFrames = new TextLineFrame[9]; // Max of 9 lines per page

    public PageBuilder(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * Set the attributes for this page
     */
    public PageBuilder setAttributes(byte pageAttributes) {
        this.pageAttributes = pageAttributes;
        return this;
    }

    /**
     * Set the OMCW for this page
     */
    public PageBuilder setOMCW(OMCW omcw) {
        this.omcw = omcw;
        return this;
    }

    /**
     * Set the address for this page
     */
    public PageBuilder setAddress(Address address) {
        this.address = address;
        return this;
    }

    /**
     * Add a text line with the default attributes, and color
     *
     * @param lineNumber Line Number
     * @param text       Text
     * @param textSize   Text Size
     */
    public PageBuilder addLine(int lineNumber, String text, byte textSize) {
        textFrames[lineNumber - 1] = new TextLineFrame(lineNumber, textSize, text);
        return this;
    }

    /**
     * Add a text line with the default size, attributes, and color
     *
     * @param lineNumber Line Number
     * @param text       Text
     */
    public PageBuilder addLine(int lineNumber, String text) {
        return addLine(lineNumber, text, (byte) 0b0010);
    }

    /**
     * Build the array of frames needed for this page
     *
     * @return Array of frames to send to the STAR
     */
    public DataFrame[] build() {
        int lineCount = getLineCount();
        int frameIndex = 1;
        DataFrame[] frames = new DataFrame[lineCount + 1];

        // Add the header to our output frames
        frames[0] = new PageHeaderFrame(this.pageNumber, lineCount);

        // Add all of the text lines
        for (int i = 0; i <= textFrames.length; i++) {
            if (textFrames[i] == null) continue;
            frames[frameIndex] = textFrames[i];
            frameIndex++;
        }

        return frames;
    }

    /**
     * @return The number of text line frames that are not null
     */
    private int getLineCount() {
        int count = 0;
        for (int i = 0; i <= textFrames.length; i++) {
            if (textFrames[i] != null) count++;
        }
        return count;
    }

}
