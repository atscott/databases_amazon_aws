package com.application.GUI;

import com.MySql.ResultTable;
import com.MySql.CannedQueries;
import com.application.GUI.searchCards.*;
import com.application.MyTableView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.*;

/**
 * User: atscott
 * Date: 10/19/13
 * Time: 10:29 AM
 */
public class SearchCardForm
{
  private JButton searchButton;
  private JPanel searchFormPanel;


  private JPanel mainPanel;
  private NameNumberMinWeeksForm nameNumberMinWeeks;
  private OwnerInfo ownerInfo;
  private OwnersByWeeksOwnedForm ownersByWeeksOwnedInBuilding;
  private ShareOfMaintenanceForm shareOfMaintenance;
  private ShareOfMaintenanceForm ownersOfUnit;
  private OwnersOnWeekForm ownersOnWeek;
  private Object currentCard = nameNumberMinWeeks;


  public SearchCardForm()
  {
    setupSearchButton();

    setupAutoCompletes();
  }

  public JPanel GetMainPanel()
  {
    return mainPanel;
  }

  private void setupSearchButton()
  {
    searchButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (currentCard == nameNumberMinWeeks)
        {
          performSearchByBuildingNameUnitNumberAndWeeks();
        } else if (currentCard == shareOfMaintenance)
        {
          performShareOfMaintenanceSearch();
        } else if (currentCard == ownersByWeeksOwnedInBuilding)
        {
          performOwnersByWeeksInBuildingSearch();
        }else if(currentCard == ownerInfo){
          performOwnerInfoSearch();
        }else if(currentCard == ownersOfUnit)
        {
          performOwnersOfUnitSearch();
        }else if(currentCard == ownersOnWeek)
        {
          performOwnersOnWeekSearch();
        }
        else
       {
          JOptionPane.showMessageDialog(null, "Programmer forgot to set up the button for the current card");
        }
      }
    });
  }



  private void setupAutoCompletes()
  {
    setupAutoCompleteForBuildings();
    setupAutoCompleteForNames();
  }

  private void setupAutoCompleteForNames()
  {
    CannedQueries cq = new CannedQueries();
    java.util.List<String> firstNames;
    try
    {
      firstNames = cq.GetOwnerFirstNames();
    } catch (SQLException e)
    {
      firstNames = new ArrayList<>();
    }
    ownerInfo.SetupAutoCompleteFirstNames(firstNames);

    java.util.List<String> lastNames;
    try
    {
      lastNames = cq.GetOwnerLastNames();
    } catch (SQLException e)
    {
      lastNames = new ArrayList<>();
    }
    ownerInfo.SetupAutoCompleteLastNames(lastNames);
  }

  private void setupAutoCompleteForBuildings()
  {
    CannedQueries cq = new CannedQueries();
    java.util.List<String> buildings;
    try
    {
      buildings = cq.GetBuildingNames();
    } catch (SQLException e)
    {
      buildings = new ArrayList<>();
    }
    shareOfMaintenance.SetupAutoCompleteBuildingNames(buildings);
    ownersByWeeksOwnedInBuilding.SetupAutoCompleteBuildingNames(buildings);
    nameNumberMinWeeks.SetupAutoCompleteBuildingNames(buildings);
    ownersOfUnit.SetupAutoCompleteBuildingNames(buildings);
  }


  private void performOwnersOnWeekSearch()
  {
    CannedQueries cq = new CannedQueries();
    try
    {
      ResultTable data = cq.GetOwnersOnWeek(ownersOnWeek.GetWeek());
      MyTableView tablePanel = getTablePanel(data);
      showResults(tablePanel);
    } catch (SQLException e)
    {
      JOptionPane.showMessageDialog(null, "Error while retrieving data. Check entries");
    }
  }

  private void performOwnerInfoSearch()
  {
    CannedQueries cq = new CannedQueries();
    try
    {
      ResultTable data = cq.GetOwnerShares(ownerInfo.GetFirstName(), ownerInfo.GetLastName());
      MyTableView tablePanel = getTablePanel(data);
      showResults(tablePanel);
    } catch (SQLException e)
    {
      JOptionPane.showMessageDialog(null, "Error while retrieving data. Check entries");
    }
  }

  private void performOwnersOfUnitSearch()
  {
    CannedQueries cq = new CannedQueries();
    try
    {
      ResultTable data = cq.GetOwnersForUnit(ownersOfUnit.GetBuildingName(), ownersOfUnit.GetUnitNumber());
      MyTableView tablePanel = getTablePanel(data);
      showResults(tablePanel);
    } catch (SQLException e)
    {
      JOptionPane.showMessageDialog(null, "Error while retrieving data. Check entries");
    }
  }

  private void performShareOfMaintenanceSearch()
  {
    CannedQueries cq = new CannedQueries();
    try
    {
      ResultTable data = cq.GetShareOfMaintenance(shareOfMaintenance.GetBuildingName(), shareOfMaintenance.GetUnitNumber());
      MyTableView tablePanel = getTablePanel(data);
      showResults(tablePanel);
    } catch (SQLException e)
    {
      JOptionPane.showMessageDialog(null, "Error while retrieving data. Check entries");
    }
  }

  private void performOwnersByWeeksInBuildingSearch()
  {
    CannedQueries cq = new CannedQueries();
    try
    {
      ResultTable data = cq.GetOwnersByWeeksOwnedInBuilding(ownersByWeeksOwnedInBuilding.GetBuildingName());
      MyTableView tablePanel = getTablePanel(data);
      showResults(tablePanel);
    } catch (SQLException e)
    {
      JOptionPane.showMessageDialog(null, "Error while retrieving data. Check entries");
    }
  }



  private void performSearchByBuildingNameUnitNumberAndWeeks()
  {
    CannedQueries cq = new CannedQueries();
    try
    {
      ResultTable data = cq.GetOwnersOfUnits(nameNumberMinWeeks.GetBuildingName(), nameNumberMinWeeks.GetUnitNumber(), nameNumberMinWeeks.GetMinimumWeeksOwned());
      MyTableView tablePanel = getTablePanel(data);
      showResults(tablePanel);
    } catch (SQLException e)
    {
      JOptionPane.showMessageDialog(null, "Error while retrieving data. Check entries");
    }
  }

  private MyTableView getTablePanel(ResultTable data)
  {
    MyTableView tablePanel = new MyTableView(data.GetHeader());
    tablePanel.SetData(data.GetRows());
    return tablePanel;
  }

  private void showResults(MyTableView tablePanel)
  {
    JFrame frame = new JFrame("Results");
    frame.setContentPane(tablePanel);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
    frame.setLocationRelativeTo(null);
  }

  public void ShowSearchByBuildingUnitNumAndMinWeeks()
  {
    currentCard = nameNumberMinWeeks;
    showCard("nameNumberMinWeeks");
  }

  public void ShowShareOfMaintenance()
  {
    currentCard = shareOfMaintenance;
    showCard("shareOfMaintenance");
  }


  public void ShowOwnersOfUnit()
  {
    currentCard = ownersOfUnit;
    showCard("ownersOfUnit");
  }

  public void ShowOwnersByWeeksOwned()
  {
    currentCard = ownersByWeeksOwnedInBuilding;
    showCard("ownersByWeeksOwnedInBuilding");
  }

  public void ShowOwnerInfo()
  {
    currentCard = ownerInfo;
    showCard("ownerInfo");
  }

  public void ShowOwnersOnWeek()
  {
    currentCard = ownersOnWeek;
    showCard("ownersOnWeek");
  }

  private void showCard(String cardName)
  {
    CardLayout layout = (CardLayout) searchFormPanel.getLayout();
    layout.show(searchFormPanel, cardName);
  }

}
