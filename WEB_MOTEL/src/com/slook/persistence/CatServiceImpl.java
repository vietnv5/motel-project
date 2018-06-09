package com.slook.persistence;

import com.slook.model.CatServiceOld;
import com.slook.model.V_CustomerCheckin;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("session")
@Service(value = "catServiceImpl")
public class CatServiceImpl extends GenericDaoImplNewV2<CatServiceOld, Long> {

    private static CatServiceImpl instance;

    public static CatServiceImpl getInstance() {
        if (instance == null) {
            instance = new CatServiceImpl();
        }

        return instance;
    }
}
