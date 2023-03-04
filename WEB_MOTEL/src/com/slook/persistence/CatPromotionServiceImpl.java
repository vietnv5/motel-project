/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.slook.model.CatPromotion;
import com.slook.model.V_CustomerCheckin;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("session")
@Service(value = "catPromotionService")
public class CatPromotionServiceImpl extends GenericDaoImplNewV2<CatPromotion, Long>
{

    private static CatPromotionServiceImpl instance;

    public static CatPromotionServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new CatPromotionServiceImpl();
        }

        return instance;
    }
}
