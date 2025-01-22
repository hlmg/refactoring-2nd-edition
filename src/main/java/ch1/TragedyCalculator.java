package ch1;

import ch1.data.Performance;
import ch1.data.Play;

public class TragedyCalculator extends PerformanceCalculator {

    public TragedyCalculator(Performance performance, Play play) {
        super(performance, play);
    }

    @Override
    public int amount() {
        var result = 40000;
        if (this.performance.getAudience() > 30) {
            result += 1000 * (performance.getAudience() - 30);
        }
        return result;
    }

}
