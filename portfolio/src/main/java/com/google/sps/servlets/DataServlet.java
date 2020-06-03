// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  // Comment number and size filters to prevent abuse.
  private static final int maxCommentLength = 300;
  private static final int maxComments = 10;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    ArrayList<String> comments = new ArrayList<>();
    int foreachCounter = 0;

    for (Entity entity : results.asIterable()) {
      if (foreachCounter < maxComments) {
        String commentContents = (String) entity.getProperty("Contents");
        comments.add(commentContents);
        foreachCounter++;
      } else {
        break;
      }
    }

    String json = convertToJson(comments);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    long timestamp = System.currentTimeMillis();
    ArrayList<String> comments = new ArrayList<>();
    String rawText = getParameter(request, "text-input");
    String text = rawText.replace("\n", "").replace("\r", " ");

    // Cap on text length to prevent abuse.
    if (text.length() > maxCommentLength) {
      text = text.substring(0, maxCommentLength);
    }
    
    Entity comment = new Entity("Comment");
    comment.setProperty("Contents", text);
    comment.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(comment);

    response.sendRedirect("/index.html");
  }

  // Utility set
  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client (From the demo)
   */
  private String getParameter(HttpServletRequest request, String name) {
    String value = request.getParameter(name);
    if (value == null) {
      return "";
    }
    return value;
  }

  /**
   * Used to send json data to the client.
   */
  private String convertToJson(ArrayList<String> comments) {
    Gson gson = new Gson();
    return gson.toJson(comments);
  }
}
