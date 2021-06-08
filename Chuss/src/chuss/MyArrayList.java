package chuss;

import java.util.ArrayList;

public class MyArrayList<T> {

    private final ArrayList<T> data;

    public MyArrayList() {

        data = new ArrayList<>();

    }

    public void add(T e) {

        data.add(e);

    }

    public void add(int index, T e) {

        data.add(index, e);

    }

    public void remove(T e) {

        data.remove(e);

    }

    public void remove(int index) {

        data.remove(index);

    }

    public T get(int index) {

        return data.get(index);

    }

    public int size() {

        return data.size();

    }

    public boolean contains(T e) {

        return data.contains(e);

    }

}