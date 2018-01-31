package xyz.kyleohanian.modelformsbinding.UIObjects;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class RadioGroupBinding extends RadioGroup implements UIObject {

    private String value;
    private int row = 0;
    private int col = 0;
    private String varType;
    private String varViewName;
    private String[] viewValues;
    private int[] internalValues;
    OnCheckedChangeListener listener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            value = String.valueOf(internalValues[i]);
        }
    };

    public RadioGroupBinding(Context context) {
        super(context);
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public RadioGroupBinding(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        this.varViewName = varViewName;
    }

    public void setUpEverythingInt(String varName, String varType, String varViewName,
                                   String[] viewValues,
                                   int[] internalValues) {
        setVarName(varName);
        setVarType(varType);
        setVarViewName(varViewName);
        setViewOptions(viewValues);
        setInternalIntegerOptions(internalValues);
        setOnCheckedChangeListener(listener);
        if(viewValues.length != internalValues.length) {
            return;
        }
        value = "0";
        TextView textView = new TextView(getContext());
        textView.setText(varViewName);
        addView(textView);
        for(int i = 0; i < viewValues.length; i++) {
            RadioButton radioButton = new RadioButton(getContext());
            if(i == 0) {
                value = String.valueOf(internalValues[i]);
            }
            radioButton.setText(viewValues[i]);
            radioButton.setTag(i);
            radioButton.setId(internalValues[i]);
            addView(radioButton);
        }

    }



    @Override
    public void setUiInputType(int uiInputType) {}

    @Override
    public String getValue() {
        return value;
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

    public String[] getViewOptions() {
        return viewValues;
    }

    public void setViewOptions(String[] options) {
        viewValues = options;
    }

    public int[] getInternalIntegerOptions() {
        return internalValues;
    }

    public void setInternalIntegerOptions(int[] options) {
        internalValues = options;
    }

    public void setCurrValue(String checkedValue) {
        value = checkedValue;
        ((RadioButton)findViewById(Integer.parseInt(value))).setChecked(true);
    }
}
