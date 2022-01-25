package com.example;


import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.Job;
import com.google.cloud.bigquery.JobId;
import com.google.cloud.bigquery.JobInfo;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;

import java.io.FileInputStream;
import java.util.UUID;

public class SimpleApp {
  public static void main(String... args) throws Exception {
	  //Conexión con BigQuery utilizando la clave JSON
      BigQuery bigquery = BigQueryOptions.newBuilder().setProjectId("proyecto-1234") //Especificar ID del proyecto
              .setCredentials(
                      ServiceAccountCredentials.fromStream(new FileInputStream("C:\\Users\\prueba.json"))//Dirección donde está alojado la clave JSON
              )
              .build().getService();
    QueryJobConfiguration queryConfig =
        QueryJobConfiguration.newBuilder(
                "SELECT commit, author, repo_name "
                    + "FROM `bigquery-public-data.github_repos.commits` "
                    + "WHERE subject like '%bigquery%' "
                    + "ORDER BY subject DESC LIMIT 10")
            .setUseLegacySql(false)
            .build();

    //Creación del Job de consulta
    JobId jobId = JobId.of(UUID.randomUUID().toString());
    Job queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

  //Esperar a que termine la consulta
    queryJob = queryJob.waitFor();

  //Informar errores
    if (queryJob == null) {
      throw new RuntimeException("Job no existe");
    } else if (queryJob.getStatus().getError() != null) {
      throw new RuntimeException(queryJob.getStatus().getError().toString());
    }

    //Obtener los resultados de la consulta
    TableResult result = queryJob.getQueryResults();

    //Imprimir en consola el resultado de la consulta
    for (FieldValueList row : result.iterateAll()) {
      String commit = row.get("commit").getStringValue();
      FieldValueList author = row.get("author").getRecordValue();
      String name = author.get("name").getStringValue();
      String email = author.get("email").getStringValue();
      String repoName = row.get("repo_name").getRecordValue().get(0).getStringValue();
      System.out.printf(
          "Repo name: %s Author name: %s email: %s commit: %s\n", repoName, name, email, commit);
    }
  }
}