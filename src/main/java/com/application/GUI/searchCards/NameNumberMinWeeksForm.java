package com.application.GUI.searchCards;

import javax.swing.*;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

import java.util.List;

/**
 * User: atscott
 * Date: 10/19/13
 * Time: 9:37 AM
 */
public class NameNumberMinWeeksForm
{

  private JComboBox namesCombobox;
  private JPanel mainPanel;
  private JSpinner unitNumberSpinner;
  private JSpinner weeksOwnedSpinner;

  public NameNumberMinWeeksForm()
  {
    setupUnitNumberSpinner();
    setupWeeksOwnedSpinner();
  }

  private void setupUnitNumberSpinner()
  {
    SpinnerModel model = new SpinnerNumberModel(1,1,null,1);
    unitNumberSpinner.setModel(model);
  }

  private void setupWeeksOwnedSpinner()
  {
    SpinnerModel model = new SpinnerNumberModel(1,1,52,1);
    weeksOwnedSpinner.setModel(model);
  }

  public void SetupAutoCompleteBuildingNames(final List<String> buildingsList)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        AutoCompleteSupport.install(namesCombobox, GlazedLists.eventList(buildingsList));
      }
    });
  }

  public int GetMinimumWeeksOwned()
  {
    return (int) weeksOwnedSpinner.getValue();
  }

  public int GetUnitNumber()
  {
    return (int) unitNumberSpinner.getValue();
  }

  public String GetBuildingName()
  {
    return (String) namesCombobox.getSelectedItem();
  }

}
