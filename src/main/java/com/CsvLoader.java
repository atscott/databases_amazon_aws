package com;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

/**
 * User: atscott
 * Date: 10/15/13
 * Time: 6:58 PM
 */
public class CsvLoader
{
  String[] headerRow;
  public final static String UNABLE_TO_LOAD_FILE = "Unable to load file";
  public static String NO_HEADER_ROW = "No header row No columns defined in given CSV file. Please check the CSV file format.";
  CSVReader reader;

  public Response LoadCsv(String csvFile)
  {
    Response response;
    response = tryOpenCsvReader(csvFile);
    if(!response.WasSuccessful())
      return response;
    response = tryGetHeaderRow();
    return response;
  }

  private Response tryOpenCsvReader(String csvFile)
  {
    try
    {
      reader = new CSVReader(new FileReader(csvFile), ',');
    } catch (FileNotFoundException e)
    {
      return new Response(false, UNABLE_TO_LOAD_FILE);
    }
    return new Response(true,"");
  }

  private Response tryGetHeaderRow()
  {
    try
    {
      headerRow = reader.readNext();
    } catch (IOException e)
    {
      return new Response(false, UNABLE_TO_LOAD_FILE);
    }

    if (headerRow == null)
      return new Response(false, NO_HEADER_ROW);

    return new Response(true, "");
  }

  public String[] GetHeader()
  {
    return headerRow;
  }

  public String[] NextLine()
  {
    try
    {
      return reader.readNext();
    } catch (IOException e)
    {
      return null;
    }
  }
}
