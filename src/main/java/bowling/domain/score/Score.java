package bowling.domain.score;

public class Score {
    private static final int DEFAULT_LEFT_COUNT = 0;
    private static final int SPARE_LEFT_COUNT = 1;
    private static final int STRIKE_LEFT_COUNT = 2;

    private static final int GUTTER_POINT = 10;
    private static final int SPARE_POINT = 10;
    private static final int STRIKE_POINT = 10;
    private static final int CAN_CALCULATE_LEFT_COUNT = 0;

    private int score;
    private int leftCount;

    public static Score ofGutter() {
        return new Score(GUTTER_POINT, DEFAULT_LEFT_COUNT);
    }

    public static Score ofMiss(int point) {
        return new Score(point, DEFAULT_LEFT_COUNT);
    }

    public static Score ofSpare() {
        return new Score(SPARE_POINT, SPARE_LEFT_COUNT);
    }

    public static Score ofStrike() {
        return new Score(STRIKE_POINT, STRIKE_LEFT_COUNT);
    }

    private Score(int score, int leftCount) {
        this.score = score;
        this.leftCount = leftCount;
    }

    public Score calculate(int point) {
        if (leftCount != CAN_CALCULATE_LEFT_COUNT) {
            this.score += point;
            this.leftCount--;
        }
        return this;
    }

    public Integer getScore() {
        if (!canCalcucateScore()) {
            throw new ScroeAccessDenyException("아직 스코어에 접근 할 수 없습니다.");
        }
        return this.score;
    }

    public boolean canCalcucateScore() {
        return leftCount == CAN_CALCULATE_LEFT_COUNT;
    }
}
