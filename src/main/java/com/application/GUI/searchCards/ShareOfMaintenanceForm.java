package com.application.GUI.searchCards;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

import javax.swing.*;
import java.util.List;

/**
 * User: atscott
 * Date: 10/19/13
 * Time: 6:33 PM
 */
public class ShareOfMaintenanceForm
{
  private JComboBox namesCombobox;
  private JPanel shareMainPanel;
  private JSpinner unitNumberSpinner;

  public ShareOfMaintenanceForm()
  {
    setupUnitNumberSpinner();
  }

  private void setupUnitNumberSpinner()
  {
    SpinnerModel model = new SpinnerNumberModel(1, 1, null, 1);
    unitNumberSpinner.setModel(model);
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

  public int GetUnitNumber()
  {
    return (int) unitNumberSpinner.getValue();
  }

  public String GetBuildingName()
  {
    return (String) namesCombobox.getSelectedItem();
  }
}
