/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.slook.model.CatDictionaryBO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author vietnv14
 */
@Scope("session")
@Service(value = "catDictionaryService")
public class CatDictionaryServiceImpl extends GenericDaoImplNewV2<CatDictionaryBO, Long>
{

}
