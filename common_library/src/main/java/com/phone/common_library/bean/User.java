package com.phone.common_library.bean;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User implements Cloneable {

    @Id(autoincrement = true)
    private Long id;
    private String userId;
    private String userName;
    private String date;
    private int age;
    private Double salary;
    @Convert(columnType = String.class, converter = AddressBeanListConverter.class)
    private List<AddressBean> addressBeanList = new ArrayList<>();

    @Generated(hash = 1992107914)
    public User(Long id, String userId, String userName, String date, int age,
                Double salary, List<AddressBean> addressBeanList) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.date = date;
        this.age = age;
        this.salary = salary;
        this.addressBeanList = addressBeanList;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Double getSalary() {
        return this.salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public List<AddressBean> getAddressBeanList() {
        return this.addressBeanList;
    }

    public void setAddressBeanList(List<AddressBean> addressBeanList) {
        this.addressBeanList = addressBeanList;
    }

    @Override
    protected User clone() throws CloneNotSupportedException {
        List<AddressBean> addressBeanList = new ArrayList<>();
        User user = (User) super.clone();
        for (int i = 0; i < user.getAddressBeanList().size(); i++) {
            addressBeanList.add(user.getAddressBeanList().get(i).clone());
        }
        user.setAddressBeanList(addressBeanList);
        return user;
    }

}
