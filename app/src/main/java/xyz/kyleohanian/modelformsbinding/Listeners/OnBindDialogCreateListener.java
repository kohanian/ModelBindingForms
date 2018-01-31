package xyz.kyleohanian.modelformsbinding.Listeners;

import android.view.View;

public interface OnBindDialogCreateListener<T> {
    void onCreate(T obj, View view);
}