/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;


import com.slook.model.CatItemBO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 *
 * @author vietnv14
 */
@Scope("session")
@Service(value = "catItemService")
public class CatItemServiceImpl extends GenericDaoImplNewV2<CatItemBO, Long> implements GenericDaoServiceNewV2<CatItemBO, Long>, Serializable {
    private static Logger logger = LogManager.getLogger(CatItemServiceImpl.class);
    
}
