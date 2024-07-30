package com.application.url_shortner.servlets;

import java.io.*;

import com.application.url_shortner.dao.UrlProviderDao;
import com.application.url_shortner.models.ShortenedUrl;
import com.application.url_shortner.service.Base62Converter;
import com.application.url_shortner.util.Helper;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "UrlShortenerServlet", value = "/*")
public class UrlShortenerServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        String url = request.getParameter("request_url");
        if (url != null && Helper.validateURl(url)) {
            String shortenedUrl = Base62Converter.encode(url);
            if (UrlProviderDao.getUrl(shortenedUrl).getOriginalUrl() == null) {
                UrlProviderDao.saveUrl(url, shortenedUrl);
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(200);
            printWriter.write("{\"success\": \"true\",\"message\":\"Created Successfully\", \"TinyURL\":" + shortenedUrl + "}");
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(422);
            printWriter.write("{\"success\": \"false\",\"message\":\"Enter Valid URL\"}");
            return;
        }
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = request.getRequestURI();
        String shortValue = url.substring(url.lastIndexOf("/") + 1);
        ShortenedUrl shortenedUrl = UrlProviderDao.getUrl(shortValue);
        if (shortenedUrl.getOriginalUrl() != null) {
            response.setStatus(200);
            UrlProviderDao.updateAccessCount(shortValue);
            response.sendRedirect(shortenedUrl.getOriginalUrl());
        } else {
            response.setStatus(404);
        }
    }
}