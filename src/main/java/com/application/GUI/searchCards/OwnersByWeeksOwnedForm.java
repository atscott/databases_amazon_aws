package com.application.GUI.searchCards;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

import javax.swing.*;
import java.util.List;

/**
 * User: atscott
 * Date: 10/19/13
 * Time: 6:48 PM
 */
public class OwnersByWeeksOwnedForm
{
  private JComboBox namesCombobox;
  private JPanel mainPanel;
  private JPanel p2;


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

  public String GetBuildingName()
  {
    return (String) namesCombobox.getSelectedItem();
  }
}
