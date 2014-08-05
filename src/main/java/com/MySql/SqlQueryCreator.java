package com.MySql;

import com.sun.deploy.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: atscott
 * Date: 10/16/13
 * Time: 3:45 PM
 */
public class SqlQueryCreator
{
  String SQL_INSERT = "INSERT INTO ${table} (${keys}) VALUES(${values})";
  String SQL_DELETE = "DELETE FROM ${table} ${where}";
  String SQL_SELECT = "SELECT ${columns} FROM ${tables} ${where}";
  private static final String WHERE_REGEX = "\\$\\{where\\}";
  private static final String COLUMNS_REGEX = "\\$\\{columns\\}";
  private static final String TABLE_REGEX = "\\$\\{table\\}";
  private static final String TABLES_REGEX = "\\$\\{tables\\}";
  private static final String KEYS_REGEX = "\\$\\{keys\\}";
  private static final String VALUES_REGEX = "\\$\\{values\\}";


  private String whereClauseCombiner(Comparison[] comparisons)
  {
    List<String> whereStatements = new ArrayList<>();
    if (comparisons != null)
    {
      for (int i = 0; i < comparisons.length; i++)
      {
          whereStatements.add(comparisons[i].toString());
      }
      return " where " + StringUtils.join(whereStatements, " and ");
    }
    return "";
  }

  public String getSelectQuery(String[] selectionColumns, String[] tableNames, Comparison[] whereClauseComparisons)
  {
    String columns = StringUtils.join(Arrays.asList(selectionColumns), ", ");
    String query = SQL_SELECT.replaceFirst(COLUMNS_REGEX, columns);
    query = query.replaceFirst(TABLES_REGEX, StringUtils.join(Arrays.asList(tableNames), ", "));
    query = query.replaceFirst(WHERE_REGEX, whereClauseCombiner(whereClauseComparisons));
    return query;
  }

  public String getSelectQuery(String[] selectionColumns, String[] tableNames, Comparison[] whereClauseComparisons, String[] orderBy)
  {
    String query = getSelectQuery(selectionColumns, tableNames, whereClauseComparisons);
    if(orderBy != null)
    {
      query += " order by " + StringUtils.join(Arrays.asList(orderBy), ",");
    }
    return query;
  }

    public String getInsertQuery(String tableName, String[] columnKeys, String[] values)
  {
    String query = SQL_INSERT.replaceFirst(TABLE_REGEX, tableName);
    query = query.replaceFirst(KEYS_REGEX, StringUtils.join(Arrays.asList(columnKeys), ","));
    query = query.replaceFirst(VALUES_REGEX, StringUtils.join(Arrays.asList(values), ","));
    return query;
  }

  public String getDeleteQuery(String tableName, Comparison[] whereClauseComparisons)
  {
    String query = SQL_DELETE.replaceFirst(TABLE_REGEX, tableName);
    query = query.replaceFirst(WHERE_REGEX, whereClauseCombiner(whereClauseComparisons));
    return query;
  }
}
