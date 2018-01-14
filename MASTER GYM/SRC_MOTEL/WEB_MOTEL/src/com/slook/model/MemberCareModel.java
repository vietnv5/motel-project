package com.slook.model;

import com.slook.persistence.GenericDaoImplNewV2;
import com.slook.persistence.GenericDaoServiceNewV2;
import org.apache.poi.ss.formula.functions.T;
import org.primefaces.model.SelectableDataModel;

import javax.faces.model.ListDataModel;
import java.util.List;

/**
 * Created by Antz01 on 06/06/2017.
 */
public class MemberCareModel extends ListDataModel<MemberCare> implements SelectableDataModel<MemberCare> {
    protected GenericDaoServiceNewV2<MemberCare, Long> daoService;

    public MemberCareModel() {
    }

    public MemberCareModel(List<MemberCare> data) {
        super(data);
    }

    @Override
    public Object getRowKey(MemberCare memberCare) {
        return memberCare.getMemberCareId();
    }

    @Override
    public MemberCare getRowData(String s) {
        MemberCare c = new MemberCare();
        try {
            daoService = new GenericDaoImplNewV2<MemberCare, Long>() {};
            c = daoService.findById(Long.parseLong(s));
        }catch (Exception e){
            e.printStackTrace();
        }
        return c;
    }
}
