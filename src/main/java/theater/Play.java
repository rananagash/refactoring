package theater;

/**
 * Represents a play with name and type.
 * @author Your Name
 * @version 1.0
 */
public final class Play {
    private final String name;
    private final String type;

    /**
     * Constructs a Play with name and type.
     *
     * @param name the play name
     * @param type the play type
     */
    public Play(final String name, final String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
