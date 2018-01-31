package com.purdue.a407.testbinding;


import com.kyleohanian.databinding.modelbindingforms.Annotations.BindingAttributes;
import com.kyleohanian.databinding.modelbindingforms.Annotations.BindingPositioning;
import com.kyleohanian.databinding.modelbindingforms.Annotations.ModelBindingAttributes;
import com.kyleohanian.databinding.modelbindingforms.Annotations.RadioIntegerAttributes;

/**
 * Created by kyleohanian on 11/24/17.
 */

@ModelBindingAttributes(createTitle = "Create User", updateTitle = "Update User",
        padding = 30, titleSize = 30)
public class User {


    @BindingAttributes(viewName = "First Name")
    @BindingPositioning(row = 1)
    private String firstName;


    @BindingAttributes(viewName = "Last Name")
    @BindingPositioning(row = 2)
    private String lastName;


    @BindingAttributes(viewName = "Age")
    @BindingPositioning(row = 3)
    @RadioIntegerAttributes(viewValues = {"0-12", "13-20", "21+"},
            internalValues = {0,1,2})
    private int age;

    @BindingAttributes(viewName = "Rating")
    @BindingPositioning(row = 3, column = 1)
    @RadioIntegerAttributes(viewValues = {"Poor", "Average", "Good"},
            internalValues = {0,1,2})
    private int rating;

    @BindingAttributes(viewName = "Taxes")
    @BindingPositioning(row = 4)
    private double taxes;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getTaxes() {
        return taxes;
    }

    public void setTaxes(double taxes) {
        this.taxes = taxes;
    }
}

