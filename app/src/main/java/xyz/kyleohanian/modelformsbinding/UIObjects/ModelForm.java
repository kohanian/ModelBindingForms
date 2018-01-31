package xyz.kyleohanian.modelformsbinding.UIObjects;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import xyz.kyleohanian.modelformsbinding.Annotations.BindingAttributes;
import xyz.kyleohanian.modelformsbinding.Annotations.BindingPositioning;
import xyz.kyleohanian.modelformsbinding.Annotations.ModelBindingAttributes;
import xyz.kyleohanian.modelformsbinding.Annotations.RadioIntegerAttributes;
import xyz.kyleohanian.modelformsbinding.Listeners.OnBindDialogCancelListener;
import xyz.kyleohanian.modelformsbinding.Listeners.OnBindDialogCreateListener;
import xyz.kyleohanian.modelformsbinding.Listeners.OnBindDialogUpdateListener;
import xyz.kyleohanian.modelformsbinding.R;

public class ModelForm<T> {

    private Class<T> thisClass;
    private ArrayList<UIObject> uiObjects;
    private UIObject title;
    private Context mContext;
    private OnBindDialogCreateListener createListener;
    private OnBindDialogCancelListener cancelListener;
    private OnBindDialogUpdateListener updateListener;
    private ModelBindingAttributes modelBindingAttributes;

    private static final int CREATE = 0;
    private static final int UPDATE = 1;

    public ModelForm(Context mContext, Class<T> thisClass) {
        this.mContext = mContext;
        this.thisClass = thisClass;
        uiObjects = new ArrayList<>();
        getClassInformation();
        getFieldsFromClass();
    }

    private void getClassInformation() {
        ModelBindingAttributes annotation = thisClass.getAnnotation(ModelBindingAttributes.class);
        TextViewBinding textViewBinding = new TextViewBinding(mContext);
        textViewBinding.setVarType(thisClass.getSimpleName());
        textViewBinding.setVarName(thisClass.getSimpleName());
        textViewBinding.setTextSize(annotation.titleSize());
        modelBindingAttributes = annotation;
        title = textViewBinding;
    }

    private void getFieldsFromClass() {
        Field[] fields = thisClass.getDeclaredFields();
        for(Field field: fields) {
            BindingAttributes attribute = field.getAnnotation(BindingAttributes.class);
            if(attribute != null) {
                RadioIntegerAttributes integerAttributes = field.getAnnotation(RadioIntegerAttributes.class);
                if(integerAttributes == null) {
                    UIObject object = fillFromField(field);
                    uiObjects.add(object);
                }
                else {
                    UIObject object = fillRadioGroup(field);
                    uiObjects.add(object);
                }
            }
        }
    }

    private UIObject fillRadioGroup(Field field) {
        RadioGroupBinding groupBinding = new RadioGroupBinding(mContext);
        RadioIntegerAttributes integerAttributes = field.getAnnotation(RadioIntegerAttributes.class);
        BindingAttributes bindingAttributes = field.getAnnotation(BindingAttributes.class);
        String varViewName = "";
        if(bindingAttributes == null || bindingAttributes.viewName().equals("")) {
            varViewName = field.getName();
        }
        else {
            varViewName = bindingAttributes.viewName();
        }
        groupBinding.setUpEverythingInt(field.getName(), field.getType().getSimpleName(),
                varViewName,integerAttributes.viewValues(), integerAttributes.internalValues());
        BindingPositioning positioning = field.getAnnotation(BindingPositioning.class);
        if(positioning != null) {
            groupBinding.setRow(positioning.row());
            groupBinding.setCol(positioning.column());
        }
        return groupBinding;
    }

    private void sortUIObjects() {
        Collections.sort(uiObjects, new Comparator<UIObject>() {
            @Override
            public int compare(UIObject uiObject1, UIObject uiObject2) {
                if(uiObject1.getRow() < uiObject2.getRow()) {
                    return -1;
                }
                else if(uiObject1.getRow() > uiObject2.getRow()) {
                    return 1;
                }
                else {
                    if(uiObject1.getCol() < uiObject2.getCol()) {
                        return -1;
                    }
                    else {
                        return 1;
                    }
                }
            }
        });
    }

    private UIObject fillFromField(Field field) {
        EditTextBinding editTextBinding = new EditTextBinding(mContext);
        editTextBinding.setVarName(field.getName());
        editTextBinding.setVarType(field.getType().getSimpleName());
        editTextBinding.setUiInputType(getInputType(field.getType().getSimpleName()));
        BindingAttributes attributes = field.getAnnotation(BindingAttributes.class);

        if(attributes == null || attributes.viewName().equals("")) {
            editTextBinding.setVarViewName(field.getName());
        }
        else
            editTextBinding.setVarViewName(attributes.viewName());
        BindingPositioning positioning = field.getAnnotation(BindingPositioning.class);
        if(positioning != null) {
            editTextBinding.setRow(positioning.row());
            editTextBinding.setCol(positioning.column());
        }
        return editTextBinding;
    }

    private T createObjectFromUIComponents() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n");
        for(int i = 0; i < uiObjects.size(); i++) {
            UIObject object = uiObjects.get(i);
            stringBuilder.append(String.format("\"%s\" : \"%s\"", object.getVarName(),
                    object.getValue()));
            if(i + 1 == uiObjects.size()) {
                stringBuilder.append("\n");
            }
            else {
                stringBuilder.append(",\n");
            }
        }
        stringBuilder.append("}");
        Log.d("Object", stringBuilder.toString());
        T newObj = new Gson().fromJson(stringBuilder.toString(), thisClass);
        return newObj;
    }

    private LinearLayout addBindingsToLayout(int type) {
        final LinearLayout linearLayout = initLayoutWithTitle(CREATE);
        int currentRow = Integer.MIN_VALUE;
        LinearLayout currentLinear = createNewLayout();
        for (UIObject object: uiObjects) {
            if(object.getRow() != currentRow) {
                linearLayout.addView(currentLinear);
                currentLinear = createNewLayout();
                currentRow = object.getRow();
            }
            if(object instanceof EditTextBinding) {
                EditTextBinding editTextBinding = (EditTextBinding)object;
                editTextBinding.setLayoutParams(new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                currentLinear.addView(editTextBinding);
            }
            else if(object instanceof TextViewBinding) {
                TextViewBinding textViewBinding = (TextViewBinding)object;
                textViewBinding.setLayoutParams(new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                currentLinear.addView(textViewBinding);
            }
            else if(object instanceof RadioGroupBinding) {
                RadioGroupBinding radioGroupBinding = (RadioGroupBinding)object;
                radioGroupBinding.setLayoutParams(new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                currentLinear.addView(radioGroupBinding);
            }
        }
        linearLayout.addView(currentLinear);
        return linearLayout;
    }

    public View setUpCreate() {
        sortUIObjects();
        final LinearLayout linearLayout = addBindingsToLayout(CREATE);
        View buttons = LayoutInflater.from(mContext).inflate(R.layout.cancel_create_buttons, null);
        buttons.findViewById(R.id.createButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T newObj = createObjectFromUIComponents();
                if(createListener == null) {
                    return;
                }
                createListener.onCreate(newObj, linearLayout);
            }
        });
        buttons.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cancelListener == null) {
                    return;
                }
                cancelListener.onCancel(linearLayout);
            }
        });
        linearLayout.addView(buttons,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return linearLayout;
    }

    private HashMap<String, Object> hashObject(T currentObj) {
        final Gson gson = new Gson();
        String jsonObj = gson.toJson(currentObj);
        HashMap<String, Object> keyResults = new HashMap<>();
        try {
            keyResults = new ObjectMapper().readValue(jsonObj, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final HashMap<String, Object> newKeySet = keyResults;
        return newKeySet;
    }

    private LinearLayout initLayoutWithTitle(int type) {
        final LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextViewBinding titleBinding = (TextViewBinding)title;
        if(type == CREATE) {
            titleBinding.setVarViewName(modelBindingAttributes.createTitle());
        }
        else if(type == UPDATE) {
            titleBinding.setVarViewName(modelBindingAttributes.updateTitle());
        }
        int padding = modelBindingAttributes.padding();
        linearLayout.setPadding(padding, padding, padding, padding);
        titleBinding.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(titleBinding);
        return linearLayout;
    }

    private LinearLayout createNewLayout() {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        return linearLayout;
    }

    public View setUpUpdate(T currentObj) {
        final HashMap<String, Object> newKeySet = hashObject(currentObj);
        sortUIObjects();
        final LinearLayout linearLayout = initLayoutWithTitle(UPDATE);
        int currentRow = Integer.MIN_VALUE;
        LinearLayout currentLinear = createNewLayout();
        for (UIObject object: uiObjects) {
            if(object.getRow() != currentRow) {
                linearLayout.addView(currentLinear);
                currentLinear = new LinearLayout(mContext);
                currentLinear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                currentLinear.setOrientation(LinearLayout.HORIZONTAL);
                currentRow = object.getRow();
            }
            if(object instanceof EditTextBinding) {
                EditTextBinding editTextBinding = (EditTextBinding)object;
                editTextBinding.setLayoutParams(new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));

                editTextBinding.setText(newKeySet.get(editTextBinding.getTag()).toString());
                currentLinear.addView(editTextBinding);
            }
            else if(object instanceof TextViewBinding) {
                TextViewBinding textViewBinding = (TextViewBinding)object;
                textViewBinding.setLayoutParams(new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                currentLinear.addView(textViewBinding);
            }
            else if(object instanceof RadioGroupBinding) {
                RadioGroupBinding radioGroupBinding = (RadioGroupBinding)object;
                radioGroupBinding.setLayoutParams(new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                currentLinear.addView(radioGroupBinding);
                radioGroupBinding.setCurrValue(newKeySet.get(radioGroupBinding.getTag()).toString());
            }
        }
        linearLayout.addView(currentLinear);
        View buttons = LayoutInflater.from(mContext).inflate(R.layout.cancel_create_buttons, null);
        ((AppCompatButton)buttons.findViewById(R.id.createButton)).setText("Update");
        buttons.findViewById(R.id.createButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < uiObjects.size(); i++) {
                    UIObject object = uiObjects.get(i);
                    newKeySet.put(object.getVarName(), object.getValue());
                }
                T newObj = null;
                try {
                    String newJSON = new ObjectMapper().writeValueAsString(newKeySet);
                    newObj = new Gson().fromJson(newJSON, thisClass);
                    if(updateListener == null) {
                        return;
                    }
                    updateListener.onUpdate(newObj, linearLayout);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        buttons.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cancelListener == null) {
                    return;
                }
                cancelListener.onCancel(linearLayout);
            }
        });
        linearLayout.addView(buttons,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        return linearLayout;
    }

    public void setOnBindDialogCreateListener(OnBindDialogCreateListener listener) {
        this.createListener = listener;
    }

    public void setOnBindDialogCancelListener(OnBindDialogCancelListener listener) {
        this.cancelListener = listener;
    }

    public void setOnBindDialogUpdateListener(OnBindDialogUpdateListener listener) {
        this.updateListener = listener;
    }


    public int getInputType(String type) {
        switch (type) {
            case "String":
                return InputType.TYPE_CLASS_TEXT;
            case "int":
                return InputType.TYPE_CLASS_NUMBER;
            case "double":
                return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
            case "float":
                return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
            case "long":
                return InputType.TYPE_CLASS_NUMBER;
            default:
                return InputType.TYPE_CLASS_TEXT;
        }
    }
}
