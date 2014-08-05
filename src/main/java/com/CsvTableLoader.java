package com;

import com.MySql.DatabaseConnection;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.sun.deploy.util.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: atscott
 * Date: 10/15/13
 * Time: 9:17 PM
 */
public class CsvTableLoader
{
  public List<Response> LoadFileIntoTable(String file, String table, DatabaseConnection con)
  {
    List<Response> responses = new ArrayList<>();
    CsvLoader loader = new CsvLoader();
    loader.LoadCsv(file);
    String[] nextLine;
    while ((nextLine = loader.NextLine()) != null)
    {
      String info = "table: " + table + "\n" +
          " keys: " + StringUtils.join(Arrays.asList(loader.GetHeader()), ",") + "\n" +
          " values: " + StringUtils.join(Arrays.asList(nextLine), ",") + "\n";
      try
      {
        List<ReplaceableAttribute> attributes = new ArrayList<>();

        for (int i = 0; i < loader.GetHeader().length; i++)
        {
          attributes.add(new ReplaceableAttribute(loader.GetHeader()[i], nextLine[i], true));
        }
        con.Insert(table, attributes);
        responses.add(new Response(true, info));
      } catch (Exception e)
      {
        responses.add(new Response(false, e.getMessage() + "\n" + info));
      }

    }
    return responses;
  }
}
