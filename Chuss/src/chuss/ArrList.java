package chuss;

import java.util.Iterator;

public class ArrList<T> implements Iterable<T> {

    private Object[] data;
    private int lastElement;
    private int extendFactor;
    private final boolean customExtendFactor;

    public ArrList() {

        data = new Object[0];
        extendFactor = 1;
        customExtendFactor = false;
        lastElement = 0;

    }

    public ArrList(int extendFactor) {

        data = new Object[0];
        this.extendFactor = extendFactor;
        customExtendFactor = true;

    }

    public Object get(int index) {

       return data[index];

    }

    public int size() {

        return lastElement;

    }

    public void add(T element) {

        if(lastElement < data.length - 1) { data[lastElement] = element; lastElement++; }
        else {

            Object[] extendedData = new Object[data.length + extendFactor];

            System.arraycopy(data, 0, extendedData, 0, data.length);

            extendedData[lastElement] = element;
            lastElement++;

            if(!customExtendFactor) extendFactor = (int) Math.ceil((extendedData.length / 5.0));

            data = extendedData;

        }

    }

    public void set(int index, T element) {

        data[index] = element;

    }

    public void removeAll(T element) {

        int count = 0;

        for(int i = 0; i < lastElement; i++) {

            if(data[i] == element) count++;
            else data[i - count] = data[i];

        }

        lastElement -= count;

    }

    public void remove(T element) {

        int count = 0;

        for(int i = 0; i < lastElement; i++) {

            if(data[i] == element && count == 0) count++;
            else data[i - count] = data[i];

        }

        lastElement -= count;

    }

    public String toString() {

        StringBuilder listString = new StringBuilder();

        listString.append("[");

        if(size() > 0) {

            for(int i = 0; i < lastElement - 1; i++) {

                listString.append(get(i).toString()).append(", ");
            }

            listString.append(get(lastElement - 1).toString());

        }

        listString.append("]");

        return listString.toString();

    }

    @Override
    public Iterator<T> iterator() {

        return new ArrListIterator<>();

    }

    class ArrListIterator<T> implements Iterator<T> {

        private int index = 0;

        @Override
        public boolean hasNext() {

            return index < size();

        }

        @Override
        public T next() {

            return (T) get(index++);

        }

    }

}