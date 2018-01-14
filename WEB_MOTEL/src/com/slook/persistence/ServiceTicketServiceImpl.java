/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.persistence;

import com.slook.model.ServiceTicket;
import com.slook.util.Constant;
import com.slook.util.DataUtil;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author VietNV on Nov 24, 2017
 */
@Scope("session")
@Service(value = "serviceTicketService")
public class ServiceTicketServiceImpl extends GenericDaoImplNewV2<ServiceTicket, Long> {

    private static ServiceTicketServiceImpl instance;

    public static ServiceTicketServiceImpl getInstance() {
        if (instance == null) {
            instance = new ServiceTicketServiceImpl();
        }

        return instance;
    }

    public static List<String> genericTicketCode(Long membershipId, int number) {
        List<String> lstRes = new ArrayList<>();
        String preCode = "0000";
        if (membershipId != null) {
            if (membershipId.toString().length() >= 4) {
                preCode = membershipId.toString();
            } else {
                preCode = (preCode + membershipId.toString());
                preCode=preCode.substring(preCode.length()-4, preCode.length());
            }
        }
        int lengthRandrom = 4;
        long minleng = Math.round(Math.log(number) / Math.log(26));//lay do dai toi thieu
        if (minleng > lengthRandrom) {
            lengthRandrom = (int) minleng;
        }
        for (int i = 0; i < number; i++) {
            String code = preCode + DataUtil.generateRandomStringUpper(lengthRandrom);
            while (lstRes.contains(code)) {
                code = preCode + DataUtil.generateRandomStringUpper(lengthRandrom);
            }
            lstRes.add(code);
        }
        return lstRes;
    }

    public static List<ServiceTicket> createListTicket(Long membershipId, int number) {
        List<ServiceTicket> lst = new ArrayList<>();
        List<String> lstCode = genericTicketCode(membershipId, number);
        for (int i = 0; i < number; i++) {
            ServiceTicket bo = new ServiceTicket(Constant.SERVICE_TICKET.STATUS_NEW, lstCode.get(i), membershipId);
            lst.add(bo);
        }
        return lst;
    }
    public static void main(String[] args){
        List<String> lst= genericTicketCode(122L, 1);
        System.out.println(lst.toArray().toString());
    }
}
