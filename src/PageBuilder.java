public class PageBuilder {

    private int pageNumber;
    private OMCW omcw;
    private Address address;
    private PageAttributes attributes;
    private TextLineFrame[] textFrames = new TextLineFrame[9]; // Max of 9 lines per page

    public PageBuilder(int pageNumber) {
        this.pageNumber = pageNumber;
        this.attributes = new PageAttributes();
        this.address = new Address();
    }

    /**
     * Set the attributes for this page
     * TODO: Maybe also put all the attributes here as setters
     */
    public PageBuilder setAttributes(PageAttributes attributes) {
        this.attributes = attributes;
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
     * Set the unit address for this page
     */
    public PageBuilder setAddress(Address address) {
        this.address = address;
        return this;
    }

    /***
     * Add a text line
     * @param lineNumber Line Number
     * @param text       Text
     * @param textSize   Text Size
     * @param attributes Line Attributes
     * TODO: figure out a cleaner way of adding attributes here, maybe a TextLineBuilder?
     */
    public PageBuilder addLine(int lineNumber, String text, byte textSize, TextLineAttributes attributes) {
        textFrames[lineNumber - 1] = new TextLineFrame(lineNumber, textSize, text);
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
        return addLine(lineNumber, text, textSize, new TextLineAttributes());
    }

    /**
     * Add a text line with the default size, attributes, and color
     *
     * @param lineNumber Line Number
     * @param text       Text
     */
    public PageBuilder addLine(int lineNumber, String text) {
        return addLine(lineNumber, text, (byte) 0b0100);
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

        // TODO: Allow configuring these
        TextLineAttributes lineAttributes = new TextLineAttributes();

        // Add the header to our output frames
        frames[0] = new PageHeaderFrame(this.pageNumber, lineCount, omcw, address, attributes, lineAttributes,
                lineAttributes, lineAttributes, lineAttributes, lineAttributes, lineAttributes, lineAttributes,
                lineAttributes);

        // Add all of the text lines
        for (TextLineFrame textFrame : textFrames) {
            if (textFrame == null) continue;
            frames[frameIndex] = textFrame;
            frameIndex++;
        }

        return frames;
    }

    /**
     * @return The number of text line frames that are not null
     */
    private int getLineCount() {
        int count = 0;
        for (TextLineFrame textFrame : textFrames) {
            if (textFrame != null) count++;
        }
        return count;
    }

}
