package bowling.domain.state;

import bowling.domain.pin.Pins;
import bowling.domain.score.CommonScore;
import bowling.domain.score.Score;

import java.util.Collections;
import java.util.List;

public abstract class State {

    protected ResultState resultState = ResultState.NONE;
    protected ProgressState progressState = ProgressState.NONE;

    public abstract State hitPins(Pins pins);

    public abstract List<Integer> getHitPins();

    public List<State> getState() {
        return Collections.singletonList(this);
    }

    public Score score() {
        return CommonScore.ofBase();
    }

    public Score addScore(Score score) {
        if (score.isCompute()) {
            return score;
        }

        return addBonusScore(score);
    }

    public ResultState getResultState() {
        return resultState;
    }

    public ProgressState getProgressState() {
        return progressState;
    }

    protected Score addBonusScore(Score score) {
        return score;
    }

}