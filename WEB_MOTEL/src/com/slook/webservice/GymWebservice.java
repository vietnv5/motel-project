package com.slook.webservice;

import com.slook.webservice.object.AuthorityBO;
import com.slook.webservice.object.JsonResponseBO;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by xuanh on 6/10/2017.
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface GymWebservice
{

    @WebMethod(operationName = "checkDataStatus")
    public JsonResponseBO checkDataStatus(
            @WebParam(name = "authorityBO") AuthorityBO authorityBO,
            @WebParam(name = "type") String type,
            @WebParam(name = "requestId") Long requestId
    );

    @WebMethod(operationName = "saveDataToDatabase")
    public JsonResponseBO saveDataToDatabase(
            @WebParam(name = "authorityBO") AuthorityBO authorityBO,
            @WebParam(name = "type") String type,
            @WebParam(name = "requestId") Long requestId,
            @WebParam(name = "content") String content
    );

    @WebMethod(operationName = "getSaveDataTimekeeper")
    public JsonResponseBO getSaveDataTimekeeper(
            @WebParam(name = "authorityBO") AuthorityBO authorityBO,
            @WebParam(name = "requestId") Long requestId
    );

    @WebMethod(operationName = "checkSaveDataTimekeeper")
    public JsonResponseBO checkSaveDataTimekeeper(
            @WebParam(name = "authorityBO") AuthorityBO authorityBO,
            @WebParam(name = "requestId") Long requestId,
            @WebParam(name = "status") Long status,
            @WebParam(name = "description") String description
    );

}