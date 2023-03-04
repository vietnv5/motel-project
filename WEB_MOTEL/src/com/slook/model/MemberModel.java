package com.slook.model;

import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

/**
 * Created by Antz01 on 06/06/2017.
 */
public class MemberModel extends ListDataModel<Member> implements SelectableDataModel<Member>
{
    protected GenericDaoServiceNewV2<Member, Long> daoService;

    public MemberModel()
    {
    }

    public MemberModel(List<Member> data)
    {
        super(data);
    }

    @Override
    public Object getRowKey(Member member)
    {
        return member.getMemberId();
    }

    @Override
    public Member getRowData(String s)
    {
        Member c = new Member();
        try
        {
            daoService = new GenericDaoImplNewV2<Member, Long>()
            {
            };
            c = daoService.findById(Long.parseLong(s));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return c;
    }
}
