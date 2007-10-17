package com.tech4d.tsm.web.member;

import com.tech4d.tsm.web.util.AuthForm;

public class SettingsForm extends AuthForm {    
    private String existingPassword;
    private Boolean deleteAccount; 
    
    public Boolean getDeleteAccount() {
        return deleteAccount;
    }
    public void setDeleteAccount(Boolean deleteAccount) {
        this.deleteAccount = deleteAccount;
    }
    public String getExistingPassword() {
        return existingPassword;
    }
    public void setExistingPassword(String existingPassword) {
        this.existingPassword = existingPassword;
    }
}
