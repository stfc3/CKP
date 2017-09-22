/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author dmin
 */
@Entity
@Table(name = "prices")
@NamedQueries({
    @NamedQuery(name = "Price.fillAllPrice", query = "FROM Price p")
    ,
    @NamedQuery(name = "Price.fillPriceActive", query = "FROM Price p WHERE p.status = 1")
})
public class Price implements Serializable {

    private Long priceId;
    private Long contractId;
    private Long pumpId;
    private Integer pumpType;
    private Integer locationType;
    private Long locationMin;
    private Long locationMax;
    private Double priceM3;
    private Double priceShift;
    private Double priceWait;
    private Double priceLocation;
    private Integer convertType;
    private Integer convertValue;
    private Integer status;

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
    public Integer getPumpType() {
        return pumpType;
    }

    public void setPumpType(Integer pumpType) {
        this.pumpType = pumpType;
    }

    @Column(name = "location_type")
    public Integer getLocationType() {
        return locationType;
    }

    public void setLocationType(Integer locationType) {
        this.locationType = locationType;
    }

    @Column(name = "location_min")
    public Long getLocationMin() {
        return locationMin;
    }

    public void setLocationMin(Long locationMin) {
        this.locationMin = locationMin;
    }

    @Column(name = "location_max")
    public Long getLocationMax() {
        return locationMax;
    }

    public void setLocationMax(Long locationMax) {
        this.locationMax = locationMax;
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

    @Column(name = "price_location")
    public Double getPriceLocation() {
        return priceLocation;
    }

    public void setPriceLocation(Double priceLocation) {
        this.priceLocation = priceLocation;
    }

    @Column(name = "convert_type")
    public Integer getConvertType() {
        return convertType;
    }

    public void setConvertType(Integer convertType) {
        this.convertType = convertType;
    }

    @Column(name = "convert_value")
    public Integer getConvertValue() {
        return convertValue;
    }

    public void setConvertValue(Integer convertValue) {
        this.convertValue = convertValue;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
