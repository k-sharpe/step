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
@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
  
  // Comment number and size filters to prevent abuse.
  private static final int MAX_COMMENT_LENGTH = 300;
  private static final int MAX_COMMENT_COUNT = 10;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    ArrayList<String[]> comments = new ArrayList<>();

    Iterator<Entity> resultIterator = results.asIterable().iterator();
    while (comments.size() < MAX_COMMENT_COUNT && resultIterator.hasNext()) {
      Entity result = resultIterator.next();
      String commentContents = (String) result.getProperty("Contents");
      String commentName = (String) result.getProperty("name");
      String[] resultingData = {commentName, commentContents};

      comments.add(resultingData);
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
    String text = stringCleaner(rawText);
    String rawName = getParameter(request, "name");
    String name = stringCleaner(rawName);
    
    Entity comment = new Entity("Comment");
    comment.setProperty("Contents", text);
    comment.setProperty("timestamp", timestamp);
    comment.setProperty("name", name);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(comment);
    response.sendRedirect("/index.html");
  }

  // limit abuse on the forms.
  private static String stringCleaner(String raw) {
    if (raw.length() > MAX_COMMENT_LENGTH) {
      raw = raw.substring(0, MAX_COMMENT_LENGTH);
    }
    String clean = raw.replace("\n", "").replace("\r", " ").replace(";", "").replace("<", "").replace(">", "")
    return clean;
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
  private String convertToJson(ArrayList<String[]> comments) {
    Gson gson = new Gson();
    return gson.toJson(comments);
  }
}
