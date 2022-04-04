package com.JvWorld.jcaller.contacts;

public class ContactsModel {

    private String contacts_name,contacts_number;

    public ContactsModel(String contacts_name, String contacts_number) {
        this.contacts_name = contacts_name;
        this.contacts_number = contacts_number;
    }

    public String getContacts_name() {
        return contacts_name;
    }

    public void setContacts_name(String contacts_name) {
        this.contacts_name = contacts_name;
    }

    public String getContacts_number() {
        return contacts_number;
    }

    public void setContacts_number(String contacts_number) {
        this.contacts_number = contacts_number;
    }

    //    public ContactsModel(String contacts_name) {
//        this.contacts_name = contacts_name;
//    }
//
//    public String getContacts_name() {
//        return contacts_name;
//    }
//
//    public void setContacts_name(String contacts_name) {
//        this.contacts_name = contacts_name;
//    }
}
