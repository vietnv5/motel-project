package com.slook.persistence;

import com.slook.model.Member;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by xuanh on 4/25/2017.
 */
@Service()
@Scope("session")
public class MemberService extends GenericDaoImplNewV2<Member, Long>
{

    private static MemberService instance;

    public static MemberService getInstance()
    {
        if (instance == null)
        {
            instance = new MemberService();
        }

        return instance;
    }
}
