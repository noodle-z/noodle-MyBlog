package com.zzzwb.myblog.domain;

import com.zzzwb.myblog.domain.base.BaseEntity;

import java.util.Date;
import javax.persistence.*;

public class User extends BaseEntity {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 性别，1代表男，0代表女
     */
    private String sex;

    /**
     * 个人简介
     */
    @Column(name = "personal_profile")
    private String personalProfile;

    /**
     * 星座
     */
    private String constellation;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取电话
     *
     * @return phone - 电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置电话
     *
     * @param phone 电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取性别，1代表男，0代表女
     *
     * @return sex - 性别，1代表男，0代表女
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置性别，1代表男，0代表女
     *
     * @param sex 性别，1代表男，0代表女
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取个人简介
     *
     * @return personal_profile - 个人简介
     */
    public String getPersonalProfile() {
        return personalProfile;
    }

    /**
     * 设置个人简介
     *
     * @param personalProfile 个人简介
     */
    public void setPersonalProfile(String personalProfile) {
        this.personalProfile = personalProfile;
    }

    /**
     * 获取星座
     *
     * @return constellation - 星座
     */
    public String getConstellation() {
        return constellation;
    }

    /**
     * 设置星座
     *
     * @param constellation 星座
     */
    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    /**
     * 获取生日
     *
     * @return birthday - 生日
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 设置生日
     *
     * @param birthday 生日
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}