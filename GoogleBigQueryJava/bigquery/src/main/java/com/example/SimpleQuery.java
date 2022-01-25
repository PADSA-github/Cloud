package com.example;

import java.io.FileInputStream;

import com.google.auth.oauth2.ServiceAccountCredentials;

/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


// [START bigquery_query]
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryException;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;

public class SimpleQuery {

  public static void main(String[] args) {

    String query = "SELECT\r\n"
    		+ "  CONCAT(\r\n"
    		+ "    'https://stackoverflow.com/questions/',\r\n"
    		+ "    CAST(id as STRING)) as url,\r\n"
    		+ "  view_count\r\n"
    		+ "FROM `bigquery-public-data.stackoverflow.posts_questions`\r\n"
    		+ "WHERE tags like '%azure-blob-storage%'\r\n"
    		+ "ORDER BY view_count DESC\r\n"
    		+ "LIMIT 10";
    simpleQuery(query);
  }

  public static void simpleQuery(String query) {
    try {
          
    	BigQuery bigquery = BigQueryOptions.newBuilder().setProjectId("proyecto-1234")//Especificar ID del proyecto
              .setCredentials(
                      ServiceAccountCredentials.fromStream(new FileInputStream("C:\\Users\\bigquery.json"))//Dirección donde está alojado la clave JSON
              ).build().getService();

      QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

      TableResult result = bigquery.query(queryConfig);

      result.iterateAll().forEach(rows -> rows.forEach(row -> System.out.println(row.getValue())));

      System.out.println("Query ran successfully");
    } catch (Exception e) {
      System.out.println("Query did not run \n" + e.toString());
    }
  }
}