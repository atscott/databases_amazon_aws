package unit_tests;

import com.MySql.DatabaseConnection;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.NoSuchDomainException;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.*;

/**
 * User: atscott
 * Date: 10/9/13
 * Time: 2:24 PM
 */
public class databaseTests
{

  private DatabaseConnection db;

  @BeforeClass
  public void connectToDb()
  {
    db = new DatabaseConnection();
  }

  @Test
      (expectedExceptions = NoSuchDomainException.class)
  public void InsertIntoInvalidTableThrowsException() throws Exception
  {
    List<ReplaceableAttribute> list = new ArrayList<>();
    db.Insert("lskdjflksdjf", list);
  }

  @Test
  public void CanInsertIntoTable() throws Exception
  {
    List<ReplaceableAttribute> attributes = new ArrayList<>();
    attributes.add(new ReplaceableAttribute("building_name","Apple",true));
    attributes.add(new ReplaceableAttribute("unit_number","1",true));
    attributes.add(new ReplaceableAttribute("maintenance_cost","130000",true));
    attributes.add(new ReplaceableAttribute("first_name","Andrew",true));
    attributes.add(new ReplaceableAttribute("last_name","Scott",true));
    attributes.add(new ReplaceableAttribute("phone_number","6173948788",true));
    attributes.add(new ReplaceableAttribute("weeks","1",false));
    String uuid = db.Insert(DatabaseConnection.timeShareDomain, attributes);
    db.delete(DatabaseConnection.timeShareDomain, uuid,new ArrayList<Attribute>());
  }

  @Test
  public void TestCanSelectStarWithNoWhereClause() throws SQLException
  {
//    CachedRowSet crs = db.select(new String[]{"*"}, new String[]{DatabaseConnection.BUILDINGS_TABLE}, null);
//    assertTrue(crs.next());
  }

  @Test
  public void TestSelectWithOrderBy() throws SQLException
  {
//    CachedRowSet crs = db.select(new String[]{"*"}, new String[]{DatabaseConnection.BUILDINGS_TABLE}, null, new String[]{"name", "id"});
//    VerifyRowSetNamesAreInAscendingOrder(crs);
  }

  private void VerifyRowSetNamesAreInAscendingOrder(CachedRowSet crs) throws SQLException
  {
    String previousName = null;
    while (crs.next())
    {
      if (previousName != null)
      {
        assertTrue(crs.getString("name").compareTo(previousName) >= 0);
      }
      previousName = crs.getString("name");
    }
  }

  @Test
  public void TestCanSelectMultipleTables() throws SQLException
  {
//    db.select(new String[]{"*"}, new String[]{DatabaseConnection.BUILDINGS_TABLE, DatabaseConnection.OWNERS_TABLE}, null);
  }

  @Test
  public void TestCanAssignAliasesToTables() throws SQLException
  {
//    db.select(
//        new String[]{"b.*", "o.*"},
//        new String[]{DatabaseConnection.BUILDINGS_TABLE + " as b", DatabaseConnection.OWNERS_TABLE + " as o"},
//        null);
  }

}
