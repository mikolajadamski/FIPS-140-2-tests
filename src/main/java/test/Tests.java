package test;

import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

public class Tests {

    public static int monobitTest(String data)
    {
        int minCount = (int)((0.5 - 0.01375) * data.length());
        int maxCount = (int)((0.5 + 0.01375) * data.length());
        int counter = (int) data.chars().filter(bit -> bit == 49).count();
        return counter;
    }

    public static Map<Integer, Integer> seriesTest(String data)
    {

        Map<Integer, Integer> serieses = new HashMap<>();
        IntStream.range(1,7)
                .forEach(val -> serieses.put(val, 0));
        int seriesCounter = 0;
        for(int i = 0; i < data.length(); i++)
        {
            if(data.charAt(i) == 49){
                seriesCounter++;
            }
            else{
                if(seriesCounter > 0){
                    if(seriesCounter>6) {
                        seriesCounter = 6;
                    }
                    serieses.replace(seriesCounter, serieses.get(seriesCounter)+1);
                    seriesCounter = 0;
                }
            }
        }
        return serieses;
    }

    public static int longSeriesTest(String data){
        int seriesCounter = 0;
        int longSeriesCounter = 0;
        for(int i = 0; i < data.length(); i++)
        {
            if(data.charAt(i) == 49){
                seriesCounter++;
            }
            else{
                if(seriesCounter > 0){
                    if(seriesCounter >= 26) {
                        longSeriesCounter++;
                    }
                    seriesCounter = 0;
                }
            }
        }
        return longSeriesCounter;
    }

    public static double pokerTest(String data) {

        Map<Integer, Integer> values = new HashMap<>();
        IntStream.range(0,16)
                .forEach(val -> values.put(val, 0));

        IntStream.range(0, 5000).map(i -> convert(data.substring(i * 4, i * 4 + 4))).forEach(val -> values.replace(val, values.get(val) + 1));
        values.forEach((k,v) -> {
            values.put(k,v*v);
        });

        double x  = 16.0/5000.0 * (double)values.values().stream().reduce(0, Integer::sum) - 5000;
        return x;
    }
    private static int convert(String bits)
    {
        return getbit(bits.substring(0,1)) * 8 + getbit(bits.substring(1,2)) * 4 +
                getbit(bits.substring(2,3)) * 2 + getbit(bits.substring(3,4)) * 1;
    }
    private static int getbit(String bit)
    {
        return Integer.parseInt(bit);
    }
}
