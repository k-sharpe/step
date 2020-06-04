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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  @Override public void init() {
    Entity site = new Entity("Site");
    site.setProperty("link", "http://www.google.com/");
    site.setProperty("description", "Hi! This is my website.");
    site.setProperty("votes", 10);
    site.setProperty("Display", true);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(site);
  }

  // Comment number and size filters to prevent abuse.
  private static final int MAX_COMMENT_LENGTH = 300;
  private static final int MAX_COMMENT_COUNT = 10;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    ArrayList<String> comments = new ArrayList<>();

    Iterator<Entity> resultIterator = results.asIterable().iterator();
    while (comments.size() < MAX_COMMENT_COUNT && resultIterator.hasNext()) {
      String commentContents = (String) resultIterator.next().getProperty("Contents");
      comments.add(commentContents);
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
    if (text.length() > MAX_COMMENT_LENGTH) {
      text = text.substring(0, MAX_COMMENT_LENGTH);
    }
    
    Entity comment = new Entity("Comment");
    comment.setProperty("Contents", text);
    comment.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(comment);

    response.sendRedirect("/index.html");
  }

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
