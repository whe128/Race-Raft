

/**
 * Enum representing different colors.
 */
public enum Color {
    BLUE, GREEN, RED, YELLOW,PURPLE, NONE;

    /**
     * Converts the color to a character representation.
     *
     * @param needUpperCase Whether to return the character in uppercase (true) or lowercase (false)
     * @return The character corresponding to the color
     */
    public char toChar( boolean needUpperCase ){
        char ch;
        switch (this){
            case BLUE   -> ch = 'b';
            case GREEN  -> ch = 'g';
            case RED    -> ch = 'r';
            case YELLOW -> ch = 'y';
            case PURPLE -> ch = 'p';
            default -> ch = 'n';
        }
        if(needUpperCase){
            ch = Character.toUpperCase(ch);
        }
        return ch;
    }

    /**
     * Converts a character to the corresponding Color enum value.
     *
     * @param ch The character representing the color ('b' for BLUE, 'g' for GREEN, 'r' for RED, 'y' for YELLOW, 'p' for PURPLE)
     * @return The Color enum corresponding to the character, or Color.NONE if the character does not match any known color
     */
    public static Color charToColor(char ch){
        Color color;
        switch (Character.toLowerCase(ch)){
            case 'b' -> color = Color.BLUE;
            case 'r' -> color = Color.RED;
            case 'y' -> color = Color.YELLOW;
            case 'p' -> color = Color.PURPLE;
            case 'g' -> color = Color.GREEN;
            default  -> color = Color.NONE;
        }
        return color;
    }

    /**
     * overwrite the toString Method to output the expected string
     *
     * @return the string of each color
     */
    @Override
    public String toString() {
        switch (this) {
            case BLUE -> {
                return "Blue";
            }
            case RED -> {
                return "Red";
            }
            case YELLOW -> {
                return "Yellow";
            }
            case PURPLE -> {
                return "Purple";
            }
            case GREEN -> {
                return "Green";
            }
            default -> {
                return "None";
            }
        }

    }
}
