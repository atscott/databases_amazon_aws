package com.MySql;

import com.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * User: atscott
 * Date: 10/19/13
 * Time: 12:01 AM
 */
public class ResultTable
{
  private String[] header = {};
  private List<String[]> dataRows;

  public ResultTable()
  {
    dataRows = new ArrayList<>();
  }

  public Response setHeader(String[] header)
  {
    if(dataRows.size() > 0)
    {
      if(header.length != dataRows.get(0).length)
      {
        return new Response(false,"Header length does not match data row length");
      }
    }
    this.header = header;
    return new Response(true,"");
  }

  public Response AddRow(String[] row)
  {
    if(row.length != header.length)
    {
      return new Response(false,"Header length does not match data row length");
    }
    dataRows.add(row);
    return new Response(true,"");
  }


  public String[] GetHeader()
  {
    return header;
  }

  public List<String[]> GetRows()
  {
    return dataRows;
  }
}
