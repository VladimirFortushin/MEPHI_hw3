package ru.mephi.web;


import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import ru.mephi.entity.ShortenLinkRequest;
import ru.mephi.entity.ShortenLinkResponse;
import ru.mephi.util.DataStorageUtil;
import ru.mephi.util.LinkManager;

import java.awt.*;
import java.util.UUID;

@Path("/link")
public class WebController {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/create")
    public ShortenLinkResponse createLink(ShortenLinkRequest request){
        //Если нет юзера по имени в локальном кеше, заносим его туда, присваиваем рандомный UUID
        if(!DataStorageUtil.getUserSet().contains(request.getUser())){
            request.getUser().setUserId(UUID.randomUUID().toString());
            DataStorageUtil.getUserSet().add(request.getUser());
        }
        //Если юзер есть, работа передается LinkManager, который по UUID проверяет ссылку на кол-во использований
        return generateResponse(request);
    }

    private ShortenLinkResponse generateResponse(ShortenLinkRequest request) {
        String linkManagerResponse = LinkManager.getLink(request.getUser().getUserId(), request.getUrl());
        ShortenLinkResponse response = new ShortenLinkResponse();
        if(linkManagerResponse.contains("Error")){
            response.setResponseCode(-1);
            response.setResponseMessage(linkManagerResponse);
        }else{
            response.setLink(linkManagerResponse);
            String expiresAt = LinkManager.getUuidLinkMap().get(request.getUser().getUserId())[1];
            String repeats = LinkManager.getUuidLinkMap().get(request.getUser().getUserId())[2];
            response.setExpiresAt(expiresAt);
            response.setUsages(Integer.parseInt(repeats));
            response.setResponseCode(0);
            response.setResponseMessage("Успешно");
        }

        return response;
    }


}