package com.ndm.ptit.enitities.signup;

public class SignUpResponse {
    private int result;
    private String msg;
    private User data;

    public SignUpResponse() {
    }

    public SignUpResponse(int result, String msg, User data) {
        this.result = result;
        this.msg = msg;
        this.data = data;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public class User {
        private String id;
        private String name;
        private String phone;

        public User() {
        }

        public User(String id, String name, String phone) {
            this.id = id;
            this.name = name;
            this.phone = phone;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
