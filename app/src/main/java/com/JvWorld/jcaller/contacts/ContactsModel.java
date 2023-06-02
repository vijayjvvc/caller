package com.JvWorld.jcaller.contacts;

public class ContactsModel {

    private String contacts_name, contacts_number, contacts_photo;

    public ContactsModel(String contacts_name, String contacts_number, String contacts_photo) {
        this.contacts_name = contacts_name;
        this.contacts_number = contacts_number;
        this.contacts_photo = contacts_photo;
    }

    public String getContacts_name() {
        return contacts_name;
    }

    public String getContacts_number() {
        return contacts_number;
    }

    public String getContacts_photo() {
        return contacts_photo;
    }
}
