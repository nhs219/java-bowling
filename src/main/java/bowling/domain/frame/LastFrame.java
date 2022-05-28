package bowling.domain.frame;

import bowling.domain.Score;
import bowling.domain.state.Ready;
import bowling.domain.state.State;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

public class LastFrame implements Frame {
    private static final String EXCLUDE_STRING = "[|, \\s]";
    private static final String SPACE = " ";
    private static final int LAST_FRAME_NUMBER = 10;

    private final LinkedList<State> states = new LinkedList<>();
    private boolean gameEnd = false;

    public LastFrame() {
        this(new Ready());
    }

    public LastFrame(State state) {
        this.states.add(state);
    }

    private void checkGameEnd() {
        if (canCalculateCurrentScore()) {
            gameEnd = true;
        }
    }

    private State firstState() {
        return states.getFirst();
    }

    private State lastState() {
        return states.getLast();
    }

    @Override
    public int number() {
        return LAST_FRAME_NUMBER;
    }

    @Override
    public Frame bowl(int pins) {
        State last = lastState();
        if (last.finish()) {
            states.add(new Ready().bowl(pins));
            checkGameEnd();
            return this;
        }

        states.removeLast();
        states.add(last.bowl(pins));
        checkGameEnd();
        return this;
    }

    @Override
    public boolean isGameEnd() {
        return gameEnd;
    }

    @Override
    public boolean canCalculateCurrentScore() {
        State first = firstState();
        if (!first.finish()) {
            return false;
        }

        Score beforeScore = first.score();
        if (beforeScore.canCalculate()) {
            return true;
        }

        return canCalculateAfterScore(beforeScore, 1);
    }

    @Override
    public boolean canCalculatePrefixSumScore(Score beforeScore) {
        State first = states.getFirst();
        if (!first.canCalculate(beforeScore)) {
            return false;
        }

        if (beforeScore.canCalculate()) {
            return true;
        }

        return canCalculateAfterScore(beforeScore, 1);
    }

    private boolean canCalculateAfterScore(Score beforeScore, int stateIndex) {
        if (stateIndex >= states.size()) {
            return false;
        }

        State state = states.get(stateIndex);
        if (!state.canCalculate(beforeScore)) {
            return false;
        }

        if (beforeScore.canCalculate()) {
            return true;
        }

        return canCalculateAfterScore(beforeScore, stateIndex + 1);
    }

    @Override
    public Score score() {
        Score score = firstState().score();
        if (score.canCalculate()) {
            return score;
        }
        return additionalScore(score, 1);
    }

    @Override
    public Score prefixSumScore(Score beforeScore) {
        Score score = firstState().sumBeforeScore(beforeScore);
        if (score.canCalculate()) {
            return score;
        }
        return additionalScore(score, 1);
    }

    private Score additionalScore(Score beforeScore, int stateIndex) {
        if (stateIndex >= states.size()) {
            return beforeScore;
        }
        State state = states.get(stateIndex);
        beforeScore = state.sumBeforeScore(beforeScore);
        if (beforeScore.canCalculate()) {
            return beforeScore;
        }
        return additionalScore(beforeScore, stateIndex + 1);
    }

    @Override
    public String mark() {
        StringBuilder scores = new StringBuilder(SPACE);
        String score = states.stream()
                .map(State::mark)
                .map(s -> s.replaceAll(EXCLUDE_STRING, ""))
                .map(s -> s.split(""))
                .flatMap(Arrays::stream)
                .collect(Collectors.joining("|"));

        scores.append(score);
        scores.append(SPACE.repeat(Math.max(0, 6 - scores.toString().length())));
        scores.append("|");
        return scores.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LastFrame lastFrame = (LastFrame) o;
        return Objects.equals(states, lastFrame.states);
    }

    @Override
    public int hashCode() {
        return Objects.hash(states);
    }
}