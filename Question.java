public class Question {
    private final int number;
    private final String difficulty;
    private final String text;
    private final String[] options;
    private final char correctAnswer;

    public Question(int number, String difficulty, String text, String[] options, char correctAnswer) {
        this.number = number;
        this.difficulty = difficulty;
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getDifficulty() { return difficulty; }
    public String getText() { return text; }
    public String[] getOptions() { return options; }
    public char getCorrectAnswer() { return correctAnswer; }
}
