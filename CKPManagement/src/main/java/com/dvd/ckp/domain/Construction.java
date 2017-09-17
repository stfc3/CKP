/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author dmin
 */
@Entity
@Table(name = "construction")
@NamedQuery(name = "Construction.fillAllConstruction", query = "FROM Construction c WHERE status = 1")
public class Construction {

    private Long constructionId;
    private Long contractId;
    private Integer constructionFar;
    private Double constructionConvert;
    private String constructionCode;
    private String constructionName;
    private Integer status;

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

    @Column(name = "is_far")
    public Integer getConstructionFar() {
        return constructionFar;
    }

    public void setConstructionFar(Integer constructionFar) {
        this.constructionFar = constructionFar;
    }

    @Column(name = "convert_value")
    public Double getConstructionConvert() {
        return constructionConvert;
    }

    public void setConstructionConvert(Double constructionConvert) {
        this.constructionConvert = constructionConvert;
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

}
