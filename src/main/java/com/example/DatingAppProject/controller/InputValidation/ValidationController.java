package com.example.DatingAppProject.controller.InputValidation;

import java.util.regex.Pattern;

public class ValidationController {

    private final String namePattern = "^[a-zA-Z]*$";
    private final String emailPattern = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private final String phonePattern = "^[0-9]+$";
    private final String birthDatePattern = ""; // Not yet implemented
    private final String aboutmePattern = "^.{0,255}$";  // All characters, max 255 long
    private final String tagPattern = ""; // Not yet implemented
    private RegistrationData regData;
    private RegistrationResponse regResp;

    public ValidationController() {
    }

    private void checkInput() {
        regResp = new RegistrationResponse();
        regResp.setFirstName(regData.getFirstName().matches(namePattern));
        regResp.setLastName(regData.getLastName().matches(namePattern));
        regResp.setEmail(regData.getEmail().matches(emailPattern));
        regResp.setPhone(regData.getPhone().matches(phonePattern));
        regResp.setAboutme(regData.getAboutme().matches(aboutmePattern));
    }

    public RegistrationResponse validate() {
        checkInput();
        return regResp;
    }

    public RegistrationData getRegData() {
        return regData;
    }

    public void setRegData(RegistrationData regData) {
        this.regData = regData;
    }

    public RegistrationResponse getRegResp() {
        return regResp;
    }

    public void setRegResp(RegistrationResponse regResp) {
        this.regResp = regResp;
    }
}