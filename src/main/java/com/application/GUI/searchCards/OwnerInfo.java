package com.application.GUI.searchCards;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

import javax.swing.*;
import java.util.List;

/**
 * User: atscott
 * Date: 10/19/13
 * Time: 7:17 PM
 */
public class OwnerInfo
{
  private JComboBox firstNameCombo;
  private JComboBox lastNameCombo;
  private JPanel mainPanel;

  public void SetupAutoCompleteFirstNames(final List<String> firstNames)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        AutoCompleteSupport.install(firstNameCombo, GlazedLists.eventList(firstNames));
      }
    });
  }

  public void SetupAutoCompleteLastNames(final List<String> lastNames)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        AutoCompleteSupport.install(lastNameCombo, GlazedLists.eventList(lastNames));
      }
    });
  }

  public String GetFirstName()
  {
    return (String) firstNameCombo.getSelectedItem();
  }

  public String GetLastName()
  {
    return (String) lastNameCombo.getSelectedItem();
  }
}
