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

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Site").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    ArrayList<ArrayList<String>> sites = new ArrayList<>();
    
    for (Entity site : results.asIterable()) {
      boolean toDisplay = (boolean) site.getProperty("display");
      if (toDisplay) {
        ArrayList<String> siteData = new ArrayList<>();
        siteData.add((String) site.getProperty("link"));
        siteData.add((String) site.getProperty("description"));
        siteData.add((String) site.getProperty("votes"));
        siteData.add((String) site.getProperty("image"));
        siteData.add((String) site.getProperty("name"));
        sites.add(siteData);
      }
    }

    String json = convertToJson(sites);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String rawText = getParameter(request, "description");
    String description = rawText.replace("\n", "").replace("\r", " ");
    String link = getParameter(request, "link");
    String name = getParameter(request, "name");
    long timestamp = System.currentTimeMillis();
    Entity site = new Entity("Site");

    site.setProperty("timestamp", timestamp);
    site.setProperty("description", description);
    site.setProperty("display", false);
    site.setProperty("image", "");
    site.setProperty("link", link);
    site.setProperty("votes", "10");
    site.setProperty("name", name);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(site);

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
  private String convertToJson(ArrayList<ArrayList<String>> comments) {
    Gson gson = new Gson();
    return gson.toJson(comments);
  }
}
