package com.slook.persistence;

import com.slook.model.CatService;
import com.slook.model.V_CustomerCheckin;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("session")
@Service(value = "catServiceImpl")
public class CatServiceImpl extends GenericDaoImplNewV2<CatService, Long> {

    private static CatServiceImpl instance;

    public static CatServiceImpl getInstance() {
        if (instance == null) {
            instance = new CatServiceImpl();
        }

        return instance;
    }
}
