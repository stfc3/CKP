/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dvd.ckp.business.dao;

import com.dvd.ckp.domain.Param;
import java.util.List;

/**
 *
 * @author dmin
 */
public interface UtilsDAO {

    List<Param> getParamByKey(String key);


}
