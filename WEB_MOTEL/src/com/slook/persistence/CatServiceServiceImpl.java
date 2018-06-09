/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slook.persistence;

import com.motel.model.CatService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author VietNV Jun 9, 2018
 */
@Scope("session")
@Service(value = "catServiceService")
public class CatServiceServiceImpl  extends GenericDaoImplNewV2<CatService, Long>  {

    private static CatServiceServiceImpl instance;

    public static CatServiceServiceImpl getInstance() {
        if (instance == null) {
            instance = new CatServiceServiceImpl();
        }
        return instance;
    }

}
