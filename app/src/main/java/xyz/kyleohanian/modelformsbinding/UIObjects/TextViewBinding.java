package xyz.kyleohanian.modelformsbinding.UIObjects;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class TextViewBinding extends AppCompatTextView implements UIObject {

    private String varType;
    private int row = 0;
    private int column = 0;


    public TextViewBinding(Context context) {
        super(context);
    }

    public TextViewBinding(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewBinding(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setVarName(String varName) {
        setTag(varName);
    }

    @Override
    public void setVarType(String varType) {
        this.varType = varType;
    }

    @Override
    public void setVarViewName(String varViewName) {
        this.setText(varViewName);
    }

    @Override
    public void setUiInputType(int uiInputType) {}

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getVarName() {
        return getTag().toString();
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return column;
    }

    @Override
    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public void setCol(int col) {
        this.column = col;
    }
}
