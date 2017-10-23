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
@Table(name = "price_location")
@NamedQueries({
    @NamedQuery(name = "PriceLocation.fillPriceLocationActive", query = "FROM PriceLocation p WHERE p.status = 1 ORDER BY p.createDate DESC")
    ,
    @NamedQuery(name = "PriceLocation.fillPriceLocationByPrice", query = "FROM PriceLocation p WHERE p.status = 1 AND p.priceId = :priceId ORDER BY p.createDate DESC")
})
public class PriceLocation implements Serializable {

    private Long priceLocationId;
    private Long priceId;
    private Long locationType;
    private Long locationMin;
    private Long locationMax;
    private Double priceLocation;
    private Double priceLocationShift;
    private Integer status;
    private Date createDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_location_id")
    public Long getPriceLocationId() {
        return priceLocationId;
    }

    public void setPriceLocationId(Long priceLocationId) {
        this.priceLocationId = priceLocationId;
    }

    @Column(name = "price_id")
    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    @Column(name = "location_type")
    public Long getLocationType() {
        return locationType;
    }

    public void setLocationType(Long locationType) {
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

    @Column(name = "price_location")
    public Double getPriceLocation() {
        return priceLocation;
    }

    public void setPriceLocation(Double priceLocation) {
        this.priceLocation = priceLocation;
    }
    @Column(name = "price_location_shift")
    public Double getPriceLocationShift() {
        return priceLocationShift;
    }

    public void setPriceLocationShift(Double priceLocationShift) {
        this.priceLocationShift = priceLocationShift;
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
