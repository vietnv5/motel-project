/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.slook.model.CatGroupPack;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author VietNV on Dec 22, 2017
 */
@Scope("session")
@Service(value = "catGroupPackService")
public class CatGroupPackServiceImpl extends GenericDaoImplNewV2<CatGroupPack, Long> {

    private static CatGroupPackServiceImpl instance;

    public static CatGroupPackServiceImpl getInstance() {
        if (instance == null) {
            instance = new CatGroupPackServiceImpl();
        }

        return instance;
    }
}
