package chuss;

public class MinMaxer<T> {

    private T maxKey;
    private T minKey;
    private Integer maxVal;
    private Integer minVal;

    public MinMaxer() {

        maxVal = null;
        minVal = null;

    }

    public void add(T key, Integer value) {

        if(maxVal == null) {

            maxVal = value;
            minVal = value;
            maxKey = key;
            minKey = key;

        } else if(value >= maxVal) {

            maxVal = value;
            maxKey = key;

        } else if(value <= minVal) {

            minVal = value;
            minKey = key;

        }

    }

    public T getMax() {

        return maxKey;

    }

    public T getMin() {

        return minKey;

    }

}