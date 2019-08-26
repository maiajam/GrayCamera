package com.maiajam.graycamera.Helper;

import android.util.Size;

import java.util.Comparator;

public class CompareSizesByArea implements Comparator<Size> {
    @Override
    public int compare(Size o1, Size o2) {
        return Long.signum((long) o1.getWidth() * o1.getHeight() -
                (long) o2.getWidth() * o2.getHeight());
    }
}
