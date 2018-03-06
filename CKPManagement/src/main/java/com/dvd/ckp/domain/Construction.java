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
@Table(name = "construction")
@NamedQueries({
    @NamedQuery(name = "Construction.fillAllConstruction", query = "FROM Construction")
    ,
    @NamedQuery(name = "Construction.fillConstructionActive", query = "FROM Construction c WHERE c.status=1 ORDER BY c.createDate DESC"),
    
    @NamedQuery(name = "Construction.fillConstructionById", query = "FROM Construction c WHERE c.status=1 AND c.constructionId = :constructionId")
})
public class Construction implements Serializable {

    private Long constructionId;
    private Long contractId;
    private String constructionCode;
    private String constructionName;
    private String constructionAddress;
    private Integer isFar;
    private Integer status;
    private Date createDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "construction_id")
    public Long getConstructionId() {
        return constructionId;
    }

    public void setConstructionId(Long constructionId) {
        this.constructionId = constructionId;
    }

    @Column(name = "contract_id")
    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    @Column(name = "construction_code")
    public String getConstructionCode() {
        return constructionCode;
    }

    public void setConstructionCode(String constructionCode) {
        this.constructionCode = constructionCode;
    }

    @Column(name = "construction_name")
    public String getConstructionName() {
        return constructionName;
    }

    public void setConstructionName(String constructionName) {
        this.constructionName = constructionName;
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

    @Column(name = "construction_address")
    public String getConstructionAddress() {
        return constructionAddress;
    }

    public void setConstructionAddress(String constructionAddress) {
        this.constructionAddress = constructionAddress;
    }

    @Column(name = "is_far")
    public Integer getIsFar() {
        return isFar;
    }

    public void setIsFar(Integer isFar) {
        this.isFar = isFar;
    }
    
    

}
