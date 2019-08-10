package cn.selinx.cloud.server.bms.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 * @author cjp
 * @since 2019-08-07
 */
@Entity
@Table(name = "bms_user")
public class BmsUser implements Serializable {

    @Id
    @Column(name = "pkid")
    private long pkid;

    @Column(name = "account")
    private String account;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "photo")
    private String photo;

    @Column(name = "status")
    private long status;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "create_host")
    private String createHost;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "update_host")
    private String updateHost;

    public long getPkid() {
        return pkid;
    }

    public void setPkid(long pkid) {
        this.pkid = pkid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateHost() {
        return createHost;
    }

    public void setCreateHost(String createHost) {
        this.createHost = createHost;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateHost() {
        return updateHost;
    }

    public void setUpdateHost(String updateHost) {
        this.updateHost = updateHost;
    }
}
