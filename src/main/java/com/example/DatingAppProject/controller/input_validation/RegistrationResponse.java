package com.example.DatingAppProject.controller.input_validation;

public class RegistrationResponse {
    private boolean firstName;
    private boolean lastName;
    private boolean email;
    private boolean phone;
    private boolean aboutme;

    private String error = "";
    private boolean inputOK = false;

    public RegistrationResponse() {
    }

    public void inputResponse() {
        if (!firstName) {
            error+="First name must not contain numbers. ";
        }
        if (!lastName) {
            error+="Last name must not contain numbers. ";
        }
        if (!email) {
            error+="E-mail must be a valid address. ";
        }
        if (!phone) {
            error+="Phone must be a valid number. ";
        }
        if (!aboutme) {
            error+="About me must not contain more than 255 characters. ";
        }
        if (firstName && lastName && email && phone && aboutme) {
            inputOK = true;
            error="";
        }
    }

    public boolean isInputOK() {
        inputResponse();
        return inputOK;
    }

    public void setInputOK(boolean inputOK) {
        this.inputOK = inputOK;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isFirstName() {
        return firstName;
    }

    public void setFirstName(boolean firstName) {
        this.firstName = firstName;
    }

    public boolean isLastName() {
        return lastName;
    }

    public void setLastName(boolean lastName) {
        this.lastName = lastName;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isPhone() {
        return phone;
    }

    public void setPhone(boolean phone) {
        this.phone = phone;
    }

    public boolean isAboutme() {
        return aboutme;
    }

    public void setAboutme(boolean aboutme) {
        this.aboutme = aboutme;
    }
}
