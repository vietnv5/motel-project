package com.slook.persistence;

import com.slook.model.Client;
import com.slook.model.V_CustomerCheckin;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("session")
@Service(value = "clientService")
public class ClientServiceImpl extends GenericDaoImplNewV2<Client, Long> {

    private static ClientServiceImpl instance;

    public static ClientServiceImpl getInstance() {
        if (instance == null) {
            instance = new ClientServiceImpl();
        }

        return instance;
    }
}
