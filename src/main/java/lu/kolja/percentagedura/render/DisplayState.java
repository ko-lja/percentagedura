package lu.kolja.percentagedura.render;

public enum DisplayState {
    DISABLED,
    ENABLED_PERCENTAGE,
    ENABLED_NUMBER;

    private static int index = 0;

    public static DisplayState getNext() {
        var values = values();
        index = (index + 1) % values.length;
        return values[index];
    }
}
