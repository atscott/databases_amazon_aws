package com.MySql;


import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: atscott
 * Date: 10/17/13
 * Time: 1:08 PM
 */
public class CannedQueries
{
  private DatabaseConnection con;
  AwsItemManipulator itemManipulator = new AwsItemManipulator();

  public CannedQueries()
  {
    con = new DatabaseConnection();
  }

  public List<String> GetTableList()
  {
    return con.GetDomainNames();
  }

  public List<String> GetOwnerFirstNames() throws SQLException
  {
    List<Item> items = con.GetItemsWithSelect("select first_name from " + DatabaseConnection.timeShareDomain);
    return getDistinctAttributeValues(items, "first_name");
  }

  public List<String> GetOwnerLastNames() throws SQLException
  {
    List<Item> items = con.GetItemsWithSelect("select last_name from " + DatabaseConnection.timeShareDomain);
    return getDistinctAttributeValues(items, "last_name");
  }

  public List<String> GetBuildingNames() throws SQLException
  {
    List<Item> items = con.GetItemsWithSelect("select building_name from " + DatabaseConnection.timeShareDomain);
    return getDistinctAttributeValues(items, "building_name");
  }


  private List<String> getDistinctAttributeValues(List<Item> items, String attributeKey) throws SQLException
  {
    List<String> stringList = new ArrayList<>();
    for (Item item : items)
    {
      for (Attribute attribute : item.getAttributes())
      {
        if (attribute.getName().trim().equals(attributeKey))
        {
          stringList.add(attribute.getValue());
        }
      }
    }
    removeDuplicatesFromStringList(stringList);
    return stringList;
  }

  private void removeDuplicatesFromStringList(List<String> stringList)
  {
    HashSet hs = new HashSet();
    hs.addAll(stringList);
    stringList.clear();
    stringList.addAll(hs);
  }


  public ResultTable GetOwnersOfUnits() throws SQLException
  {
    List<Item> items = con.GetItemsWithSelect("select first_name, last_name, phone_number from " +
        DatabaseConnection.timeShareDomain);
    itemManipulator.removeDuplicatesFromItemList(items);
    return GetResultTableFromItems(items);
  }

  public ResultTable GetOwnersOfUnits(String buildingName, int unitNumber, int minimumWeeksOwned) throws SQLException
  {
    String query = "select first_name, last_name, phone_number, weeks from " + DatabaseConnection.timeShareDomain +
        " where building_name = '" + buildingName + "' and unit_number = '" + unitNumber + "'";
    List<Item> items = con.GetItemsWithSelect(query);
    itemManipulator.removeDuplicatesFromItemList(items);
    items = itemManipulator.convertAttributeToCount(items, "weeks");
    items = itemManipulator.renameAttribute(items, "weeks", "weeks count");
    items = itemManipulator.getItemsWithAttributeValueGreaterThan(items, "weeks count", minimumWeeksOwned - 1);
    return GetResultTableFromItems(items);
  }


  public ResultTable GetShareOfMaintenance(String buildingName, int unitNumber) throws SQLException
  {
    String query = "select first_name, maintenance_cost, last_name, phone_number, weeks from " + DatabaseConnection.timeShareDomain +
        " where building_name = '" + buildingName + "' and unit_number = '" + unitNumber + "'";
    List<Item> items = con.GetItemsWithSelect(query);
    items = addMaintenanceShareToItems(items);
    items = itemManipulator.removeAttributesFromItems(items, "weeks", "maintenance_cost");
    return GetResultTableFromItems(items);
  }

  private List<Item> addMaintenanceShareToItems(List<Item> items)
  {
    List<Item> newList = new ArrayList<>();
    for (Item item : items)
    {
      double maintenance = 0.0;
      int weeksOwned = itemManipulator.countValues(item, "weeks");
      List<Attribute> newAttributes = new ArrayList<>();
      for (Attribute attribute : item.getAttributes())
      {
        if (attribute.getName().equals("maintenance_cost"))
        {
          maintenance = Double.parseDouble(attribute.getValue());
        }
        newAttributes.add(attribute);
      }
      String shareOfMaintenance = Double.toString(maintenance / 52 * weeksOwned);
      newAttributes.add(new Attribute().withName("maintenance share").withValue(shareOfMaintenance));
      Item newItem = new Item().withAttributes(newAttributes).withName(item.getName());
      newList.add(newItem);
    }
    return newList;
  }

  public ResultTable GetOwnersByWeeksOwnedInBuilding(String buildingName) throws SQLException
  {
    String query = "select first_name, last_name, phone_number, weeks from " + DatabaseConnection.timeShareDomain +
        " where building_name = '" + buildingName + "'";
    List<Item> items = con.GetItemsWithSelect(query);
    items = itemManipulator.groupItemsBy(items, "phone_number");
    items = itemManipulator.renameAttribute(items, "weeks", "weeks_owned_in_building");
    items = itemManipulator.convertAttributeToCount(items, "weeks_owned_in_building");
    items = itemManipulator.removeAttributesFromItems(items, "phone_number");
    items = itemManipulator.removeAttributesWithSameValueAndGroupThoseWithDifferent(items);
    return GetResultTableFromItems(items);
  }

  public ResultTable GetOwnerShares(String first_name, String last_name) throws SQLException
  {
    if (first_name == null)
    {
      first_name = "";
    }

    if (last_name == null)
    {
      last_name = "";
    }
    String query = "select first_name, last_name, building_name, unit_number, weeks from " + DatabaseConnection.timeShareDomain +
        " where first_name like '%" + first_name + "%' and last_name like '%" + last_name + "%'";
    List<Item> items = con.GetItemsWithSelect(query);
    return GetResultTableFromItems(items);

  }

  public ResultTable GetOwnersForUnit(String buildingName, int unitNumber) throws SQLException
  {
    String sql = "SELECT o.first_name,\n" +
        "  o.last_name,\n" +
        "  uo.week\n" +
        "FROM owners AS o,\n" +
        "  unit_ownerships AS uo,\n" +
        "  time_share_units AS u,\n" +
        "  time_share_buildings b\n" +
        "WHERE o.id = uo.owner_id\n" +
        "      AND uo.unit_id = u.id\n" +
        "      AND b.id = u.building_id\n" +
        "      AND b.name = '" + buildingName + "'\n" +
        "      AND u.unit_number = " + unitNumber + "\n" +
        "ORDER BY uo.week;";
    CachedRowSet crs = con.rawQuery(sql);
    return GetResultsTableFromRowSet(crs);
  }

  public ResultTable GetOwnersOnWeek(int week) throws SQLException
  {
    String sql = "SELECT o.first_name,\n" +
        "  o.last_name,\n" +
        "  b.name as building_name,\n" +
        "  u.unit_number\n" +
        "FROM owners AS o,\n" +
        "  unit_ownerships AS uo,\n" +
        "  time_share_units AS u,\n" +
        "  time_share_buildings b\n" +
        "WHERE o.id = uo.owner_id\n" +
        "      AND uo.unit_id = u.id\n" +
        "      AND b.id = u.building_id\n" +
        "      AND uo.week = " + week + " \n" +
        "ORDER BY uo.week;";
    CachedRowSet crs = con.rawQuery(sql);
    return GetResultsTableFromRowSet(crs);
  }

  private ResultTable GetResultsTableFromRowSet(CachedRowSet crs) throws SQLException
  {
    ResultTable results = new ResultTable();
    results.setHeader(GetColumnNames(crs));
    AddRowsToResultTable(crs, results);
    return results;
  }

  private ResultTable GetResultTableFromItems(List<Item> items)
  {

    ResultTable results = new ResultTable();
    if (!items.isEmpty())
    {

      List<String> header = new ArrayList<>();
      for (Attribute attribute : items.get(0).getAttributes())
      {
        if (!header.contains(attribute.getName()))
        {
          header.add(attribute.getName());
        }
      }
      results.setHeader(header.toArray(new String[header.size()]));
      AddRowsToResultTableFromItems(items, results);
    }
    return results;
  }

  private ResultTable AddRowsToResultTableFromItems(List<Item> items, ResultTable results)
  {
    for (Item item : items)
    {
      Map<String, String> attributeList = new LinkedHashMap<>();
      for (Attribute attribute : item.getAttributes())
      {
        if (attributeList.containsKey(attribute.getName()))
        {
          String oldValue = attributeList.get(attribute.getName());
          attributeList.remove(attribute.getName());
          attributeList.put(attribute.getName(), oldValue + "&&" + attribute.getValue());
        }
        else
        {
          attributeList.put(attribute.getName(), attribute.getValue());
        }
      }
      results.AddRow(attributeList.values().toArray(new String[attributeList.size()]));
    }
    return results;
  }

  private String[] GetColumnNames(CachedRowSet crs) throws SQLException
  {
    String[] columnNames = new String[crs.getMetaData().getColumnCount()];
    for (int i = 1; i <= columnNames.length; i++)
    {
      columnNames[i - 1] = crs.getMetaData().getColumnName(i);
    }
    return columnNames;
  }

  public void AddRowsToResultTable(CachedRowSet crs, ResultTable table) throws SQLException
  {
    int columns = crs.getMetaData().getColumnCount();
    while (crs.next())
    {
      String[] row = new String[columns];
      for (int i = 1; i <= columns; i++)
      {
        row[i - 1] = crs.getString(i);
      }
      table.AddRow(row);
    }
  }

}
