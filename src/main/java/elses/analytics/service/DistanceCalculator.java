package elses.analytics.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DistanceCalculator {

    /* Distance formula using log when n factor is 2 */
    public void logDistanceFormulaFactorTwo(Float rssi, int power){
        Double n = 2d;
        Double distance = Math.pow(10,(power-rssi)/(10*2*n));
        System.out.println(distance);
    }

    /* Distance formula using log when n factor is 3 */
    public void logDistanceFormulaFactorThree(Float rssi, int power){
        Double n = 3d;
        Double distance = Math.pow(10,(power-rssi)/(10*2*n));
        System.out.println(distance);
    }

    /* Distance formula using log when n factor is 4 */
    public void logDistanceFormulaFactorFour(Float rssi, int power){
        Double n = 4d;
        Double distance = Math.pow(10,(power-rssi)/(10*2*n));
        System.out.println(distance);
    }

    /*  https://forums.estimote.com/t/do-anyone-know-how-to-count-the-distance-between-beacon-and-beacons-phone/3615/3
    * */
    public void distanceEstimoteFormula(Float rssi, int power){
        if (rssi == 0 || power == 0)
            return;
        double ratio2 = power - rssi;
        double ratio2_linear = Math.pow(10, ratio2 / 10);
        double y = 0;
        double r = Math.sqrt(ratio2_linear);

        double ratio = rssi * 1.0 / power;
        if (ratio < 1.0)
        {
            y = Math.pow(ratio, 10);
        }
        else
        {
            y = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
        }
        System.out.println(y*10);
    }

    /*  https://gist.github.com/eklimcz/446b56c0cb9cfe61d575
     * */
    public void distanceGitGistFormula(Float rssi, int power){
        if (rssi == 0) {
            return;
        }

        double ratio = rssi*1.0/power;
        if (ratio < 1.0) {
            double distance =  Math.pow(ratio,10);
            System.out.println(distance);
        }
        else {
            double distance =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
            System.out.println(distance);
        }
    }

}
