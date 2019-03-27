package com.android.babbler.DataClasses;



public class User {

    static private User m_instance;
    private int m_id;
    private String m_role;
    private String m_name;
    private String m_email;
    private String m_birthday;
    private String m_password;
    private String m_phone_number;
    private String m_address;
    private int m_image;
    private boolean isConnected=false;

    private User(){}

    public User(int m_id, String m_role, String m_name, String m_email, String m_birthday, String m_phone_number, String m_address, int m_image) {
        this.m_id = m_id;
        this.m_role = m_role;
        this.m_name = m_name;
        this.m_email = m_email;
        this.m_birthday = m_birthday;
        this.m_phone_number = m_phone_number;
        this.m_address = m_address;
        this.m_image = m_image;
    }

    public static User getInstance() {
      if(m_instance == null)
          m_instance = new User();
      return m_instance;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public String getM_role() {
        return m_role;
    }

    public void setM_role(String m_role) {
        this.m_role = m_role;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getM_email() {
        return m_email;
    }

    public void setM_email(String m_emal) {
        this.m_email = m_emal;
    }

    public String getM_birthday() {
        return m_birthday;
    }

    public void setM_birthday(String m_birthday) {
        this.m_birthday = m_birthday;
    }

    public String getM_password() {
        return m_password;
    }

    public void setM_password(String m_password) {
        this.m_password = m_password;
    }

    public String getM_phone_number() {
        return m_phone_number;
    }

    public void setM_phone_number(String m_phone_number) {
        this.m_phone_number = m_phone_number;
    }

    public String getM_address() {
        return m_address;
    }

    public void setM_address(String m_address) {
        this.m_address = m_address;
    }

    public void reset()
    {
        m_instance = null;
    }

    public boolean isConnected() {return isConnected;}

    public void setConnected(boolean connected) {isConnected = connected;}

    public int getM_image() {
        return m_image;
    }

    public void setM_image(int m_image) {
        this.m_image = m_image;
    }

}

