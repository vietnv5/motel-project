/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.motel.model.GroupUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author VietNV Jan 17, 2018
 */
@Scope("session")
@Service(value = "groupUserService")
public class GroupUserServiceImpl extends GenericDaoImplNewV2<GroupUser, Long> {

    private static GroupUserServiceImpl instance;

    public static GroupUserServiceImpl getInstance() {
        if (instance == null) {
            instance = new GroupUserServiceImpl();
        }
        return instance;
    }
}
