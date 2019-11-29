package com.zzzwb.myblog.domain;

import com.zzzwb.myblog.domain.base.BaseEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Resource extends BaseEntity {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资源类名称
     */
    @Column(name = "class_name")
    private String className;

    /**
     * 资源类描述
     */
    @Column(name = "class_description")
    private String classDescription;

    /**
     * 请求动作
     */
    private String action;

    /**
     * 路径
     */
    private String path;

    /**
     * 接口描述
     */
    private String description;

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
     * 获取资源类名称
     *
     * @return class_name - 资源类名称
     */
    public String getClassName() {
        return className;
    }

    /**
     * 设置资源类名称
     *
     * @param className 资源类名称
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 获取资源类描述
     *
     * @return class_description - 资源类描述
     */
    public String getClassDescription() {
        return classDescription;
    }

    /**
     * 设置资源类描述
     *
     * @param classDescription 资源类描述
     */
    public void setClassDescription(String classDescription) {
        this.classDescription = classDescription;
    }

    /**
     * 获取请求动作
     *
     * @return action - 请求动作
     */
    public String getAction() {
        return action;
    }

    /**
     * 设置请求动作
     *
     * @param action 请求动作
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * 获取路径
     *
     * @return path - 路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置路径
     *
     * @param path 路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取接口描述
     *
     * @return description - 接口描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置接口描述
     *
     * @param description 接口描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Resource resource = (Resource) o;

        return new EqualsBuilder().append(action, resource.action).append(path,
                resource.path).isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37).append(action).append(path).toHashCode();
    }

    @Override
    public String toString() {
        return "Resource{" + "id=" + id + ", className='" + className + '\'' + ", classDescription='" + classDescription + '\'' + ", action='" + action + '\'' + ", path='" + path + '\'' + ", description='" + description + '\'' + '}';
    }
}