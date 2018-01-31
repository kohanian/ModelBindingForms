package xyz.kyleohanian.modelformsbinding.UIObjects;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class EditTextBinding extends AppCompatEditText implements UIObject {

    private String varType;
    private int row;
    private int col;

    public EditTextBinding(Context context) {
        super(context);
    }

    public EditTextBinding(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextBinding(Context context, AttributeSet attrs, int defStyleAttr) {
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
        setHint(varViewName);
    }

    @Override
    public void setUiInputType(int uiInputType) {
        setInputType(uiInputType);
    }

    @Override
    public String getValue() {
        return getText().toString();
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
        return col;
    }

    @Override
    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public void setCol(int col) {
        this.col = col;
    }
}
