package xyz.kyleohanian.modelformsbinding.UIObjects;

public interface UIObject {

    void setVarName(String varName);
    void setVarType(String varType);
    void setVarViewName(String varViewName);
    void setUiInputType(int uiInputType);
    String getValue();
    String getVarName();
    int getRow();
    int getCol();
    void setRow(int row);
    void setCol(int col);
}
