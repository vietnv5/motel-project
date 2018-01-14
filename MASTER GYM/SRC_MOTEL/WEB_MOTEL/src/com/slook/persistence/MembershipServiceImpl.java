/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.slook.model.Membership;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author SLOOK. JSC on Nov 1, 2017 3:15:44 PM
 */
@Scope("session")
@Service(value = "membershipService")
public class MembershipServiceImpl extends GenericDaoImplNewV2<Membership, Long> {

    private static MembershipServiceImpl instance;

    public static MembershipServiceImpl getInstance() {
        if (instance == null) {
            instance = new MembershipServiceImpl();
        }
        return instance;
    }
}
