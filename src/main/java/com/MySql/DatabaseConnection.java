package com.MySql;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.*;
import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User: atscott
 * Date: 10/9/13
 * Time: 2:46 PM
 */
public class DatabaseConnection
{
  public final static String timeShareDomain = "TimeShare";
  AmazonSimpleDB sdb;
  private Connection connection = null;
  private SqlQueryCreator queryCreator;

  public DatabaseConnection()
  {
    queryCreator = new SqlQueryCreator();
    sdb = new AmazonSimpleDBClient(new ClasspathPropertiesFileCredentialsProvider());
    Region usEast = Region.getRegion(Regions.US_EAST_1);
    sdb.setRegion(usEast);

    if (!sdb.listDomains().getDomainNames().contains(timeShareDomain))
    {
      sdb.createDomain(new CreateDomainRequest(timeShareDomain));
    }

  }

  public String Insert(String domainName, List<ReplaceableAttribute> attributes)
  {
    PutAttributesRequest request = new PutAttributesRequest().withDomainName(domainName);
    String uuid = UUID.randomUUID().toString();
    request.setItemName(uuid);
    request.withAttributes(attributes);
    sdb.putAttributes(request);
    return uuid;
  }

  public void BulkInsert(String domainName, List<ReplaceableItem> items){
    sdb.batchPutAttributes(new BatchPutAttributesRequest(domainName, items));
  }

  public void delete(String tableName, String itemName, List<Attribute> withAttributes) throws SQLException
  {
    sdb.deleteAttributes(new DeleteAttributesRequest(tableName, itemName)
        .withAttributes(withAttributes));
  }

  public CachedRowSet select(String[] selectionColumns, String[] tableNames, Comparison[] whereClauseComparisons) throws SQLException
  {
    String query = queryCreator.getSelectQuery(selectionColumns, tableNames, whereClauseComparisons);
    return getResultsForQuery(query);
  }

  public CachedRowSet select(String[] selectionColumns, String[] tableNames, Comparison[] whereClauseComparisons, String[] orderBy) throws SQLException
  {
    String query = queryCreator.getSelectQuery(selectionColumns, tableNames, whereClauseComparisons, orderBy);
    return getResultsForQuery(query);
  }

  private CachedRowSet getResultsForQuery(String query) throws SQLException
  {
    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery(query);
    CachedRowSet crs = new CachedRowSetImpl();
    crs.populate(rs);
    return crs;
  }

  public CachedRowSet rawQuery(String query) throws SQLException
  {
    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery(query);
    CachedRowSet crs = new CachedRowSetImpl();
    crs.populate(rs);
    return crs;
  }

  public List<Item> GetItemsWithSelect(String query)
  {
    SelectRequest selectRequest = new SelectRequest(query);
    return sdb.select(selectRequest).getItems();
  }

  public List<String> GetDomainNames()
  {
    return sdb.listDomains().getDomainNames();
  }
}
