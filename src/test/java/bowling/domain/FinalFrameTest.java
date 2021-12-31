package bowling.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

class FinalFrameTest {
    private static Frame frame;

    @BeforeEach
    public void before() {
        frame = FinalFrame.init();
    }

    @DisplayName("FinalFrame의 index는 Index의 Max다.")
    @Test
    void index() {
        assertThat(frame.getFrameIndex()).isEqualTo(FrameIndex.MAX);
    }

    @DisplayName("FinalFrame은 최소 2번 투구한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "5, 4, 5|4, 9",
            "5, 0, 5|-, 5",
            "0, 0, -|-, 0",
            "0, 3, -|3, 3"
    })
    void twoBowls(int bowl1, int bowl2, String symbol, int score) {
        List<Integer> pinNumbers = Arrays.asList(bowl1, bowl2);
        for (int pinNumber : pinNumbers) {
            frame.bowl(Ball.of(pinNumber, State.READY));
        }
        assertThat(frame.symbol()).isEqualTo(symbol);
        assertThat(frame.isEnd()).isTrue();
        assertThat(frame.score().getScore()).isEqualTo(score);
    }

    @DisplayName("FinalFrame은 첫 번째에 스트라이크이거나, 두 번째에서 스페어 처리한 경우 한 번 더 투구할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {
            "10, 10, 10, X|X|X, 30",
            "10, 10, 5, X|X|5, 25",
            "10, 3, 7, X|3|/, 20",
            "10, 5, 3, X|5|3, 18",
            "6, 4, 3, 6|/|3, 13"
    })
    void threeBowls(int bowl1, int bowl2, int bowl3, String symbol, int score) {
        List<Integer> pinNumbers = Arrays.asList(bowl1, bowl2, bowl3);
        for (int pinNumber : pinNumbers) {
            frame.bowl(Ball.of(pinNumber, State.READY));
        }
        assertThat(frame.symbol()).isEqualTo(symbol);
        assertThat(frame.isEnd()).isTrue();
        assertThat(frame.score().getScore()).isEqualTo(score);
    }

}