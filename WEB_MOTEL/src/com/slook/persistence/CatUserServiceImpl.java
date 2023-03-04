package com.slook.persistence;

import com.slook.model.CatUser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by tuanpv14 on 24/03/2017.
 */
@Service(value = "catUserService")
@Scope("session")
public class CatUserServiceImpl extends GenericDaoImplNewV2<CatUser, Serializable>
{
}
