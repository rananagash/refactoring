package theater;

/**
 * Represents a performance of a play.
 */
public final class Performance {
    private final String playID;
    private final int audience;

    /**
     * Constructs a Performance with play ID and audience size.
     *
     * @param playID the play ID
     * @param audience the audience size
     */
    public Performance(final String playID, final int audience) {
        this.playID = playID;
        this.audience = audience;
    }

    public String getPlayID() {
        return playID;
    }

    public int getAudience() {
        return audience;
    }
}
