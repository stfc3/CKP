/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author dmin
 */
@Entity
@Table(name = "distribute")
@NamedQueries({
    @NamedQuery(name = "Distribute.fillAllDistribute", query = "FROM Distribute")
    ,
    @NamedQuery(name = "Distribute.fillDistributeActive", query = "FROM Distribute d WHERE d.status=1 ORDER BY d.createDate DESC")
})
public class Distribute implements Serializable {

    private Long distributeId;
    private String distributeCode;
    private String distributeName;
    private Date distributeYear;
    private Integer distributeRemote;
    private Integer distributeHandheld;
    private Integer status;
    private Date createDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "distribute_id")
    public Long getDistributeId() {
        return distributeId;
    }

    public void setDistributeId(Long distributeId) {
        this.distributeId = distributeId;
    }

    @Column(name = "distribute_code")
    public String getDistributeCode() {
        return distributeCode;
    }

    public void setDistributeCode(String distributeCode) {
        this.distributeCode = distributeCode;
    }

    @Column(name = "distribute_name")
    public String getDistributeName() {
        return distributeName;
    }

    public void setDistributeName(String distributeName) {
        this.distributeName = distributeName;
    }

    @Column(name = "distribute_year")
    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getDistributeYear() {
        return distributeYear;
    }

    public void setDistributeYear(Date distributeYear) {
        this.distributeYear = distributeYear;
    }

    @Column(name = "distribute_remote")
    public Integer getDistributeRemote() {
        return distributeRemote;
    }

    public void setDistributeRemote(Integer distributeRemote) {
        this.distributeRemote = distributeRemote;
    }

    @Column(name = "distribute_handheld")
    public Integer getDistributeHandheld() {
        return distributeHandheld;
    }

    public void setDistributeHandheld(Integer distributeHandheld) {
        this.distributeHandheld = distributeHandheld;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "create_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    

}
