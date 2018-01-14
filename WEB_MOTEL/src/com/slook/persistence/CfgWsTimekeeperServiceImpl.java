package com.slook.persistence;

import com.slook.model.CfgWsTimekeeper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by dungvv8 on 12/21/2016.
 */
@Service(value = "cfgWsTimekeeperService")
@Scope("session")
public class CfgWsTimekeeperServiceImpl extends GenericDaoImplNewV2<CfgWsTimekeeper, Serializable> {

    private static CfgWsTimekeeperServiceImpl instance;

    public static CfgWsTimekeeperServiceImpl getInstance() {
        if (instance == null) {
            instance = new CfgWsTimekeeperServiceImpl();
        }
        return instance;
    }
}
