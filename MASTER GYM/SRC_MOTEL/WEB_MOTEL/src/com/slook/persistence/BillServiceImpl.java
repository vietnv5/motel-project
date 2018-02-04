/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.slook.persistence;

import com.motel.model.Bill;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author VietNV Jan 26, 2018
 */
@Scope("session")
@Service(value = "billService")
public class BillServiceImpl  extends GenericDaoImplNewV2<Bill, Long> {

    private static BillServiceImpl instance;

    public static BillServiceImpl getInstance() {
        if (instance == null) {
            instance = new BillServiceImpl();
        }
        return instance;
    }
}
