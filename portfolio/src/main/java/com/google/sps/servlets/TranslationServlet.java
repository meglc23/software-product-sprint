package com.google.sps.servlets;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.gson.Gson;
import java.io.IOException;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet responsible for translating all comments. */
@WebServlet("/translate")
public class TranslationServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int commentLength = Integer.parseInt(request.getParameter("commentLength"));
    List<String> translatedComments = new ArrayList<>();
    String languageCode = request.getParameter("languageCode");

    // Do the translation.
    Translate translate = TranslateOptions.getDefaultInstance().getService();
    Translation translation;
    for (int i = 0; i < commentLength; i++) {
        translation = translate.translate(
            request.getParameter(String.valueOf(i)),
            Translate.TranslateOption.targetLanguage(languageCode));
        translatedComments.add(translation.getTranslatedText());
    }

    // Output the translation.
    Gson gson = new Gson();
    response.setContentType("text/html; charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(gson.toJson(translatedComments));
  }
}
