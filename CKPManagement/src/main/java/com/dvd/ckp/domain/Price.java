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
@Table(name = "prices")
@NamedQueries({
    @NamedQuery(name = "Price.fillAllPrice", query = "FROM Price p")
    ,
    @NamedQuery(name = "Price.fillPriceActive", query = "FROM Price p WHERE p.status = 1 ORDER BY p.createDate DESC")
    ,
    @NamedQuery(name = "Price.fillPriceByContract", query = "FROM Price p WHERE p.status = 1 AND p.contractId = :contractId ORDER BY p.createDate DESC")
})
public class Price implements Serializable {

    private Long priceId;
    private Long contractId;
    private Long pumpId;
    private Long pumpType;
    private Double priceM3;
    private Double priceShift;
    private Double priceWait;
    private Long convertType;
    private Double convertValue;
    private Integer status;
    private Date createDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    @Column(name = "contract_id")
    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    @Column(name = "pump_id")
    public Long getPumpId() {
        return pumpId;
    }

    public void setPumpId(Long pumpId) {
        this.pumpId = pumpId;
    }

    @Column(name = "pump_type")
    public Long getPumpType() {
        return pumpType;
    }

    public void setPumpType(Long pumpType) {
        this.pumpType = pumpType;
    }

    @Column(name = "price_m3")
    public Double getPriceM3() {
        return priceM3;
    }

    public void setPriceM3(Double priceM3) {
        this.priceM3 = priceM3;
    }

    @Column(name = "price_shift")
    public Double getPriceShift() {
        return priceShift;
    }

    public void setPriceShift(Double priceShift) {
        this.priceShift = priceShift;
    }

    @Column(name = "price_wait")
    public Double getPriceWait() {
        return priceWait;
    }

    public void setPriceWait(Double priceWait) {
        this.priceWait = priceWait;
    }

    @Column(name = "convert_type")
    public Long getConvertType() {
        return convertType;
    }

    public void setConvertType(Long convertType) {
        this.convertType = convertType;
    }

    @Column(name = "convert_value")
    public Double getConvertValue() {
        return convertValue;
    }

    public void setConvertValue(Double convertValue) {
        this.convertValue = convertValue;
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
