package com.zzzwb.myblog.domain;

import com.zzzwb.myblog.domain.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import javax.persistence.*;

@Setter
@Getter
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
     * 对应的角色id，多个id使用,隔开
     */
    @Transient
    private String roles;
}