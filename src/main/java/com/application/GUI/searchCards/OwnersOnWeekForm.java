package com.application.GUI.searchCards;

import javax.swing.*;

/**
 * User: atscott
 * Date: 10/19/13
 * Time: 7:52 PM
 */
public class OwnersOnWeekForm
{
  private JSpinner weekSpinner;
  private JPanel mainPanel;

  public OwnersOnWeekForm()
  {
    setupWeeksOwnedSpinner();
  }

  private void setupWeeksOwnedSpinner()
  {
    SpinnerModel model = new SpinnerNumberModel(1,1,52,1);
    weekSpinner.setModel(model);
  }

  public int GetWeek()
  {
    return (int) weekSpinner.getValue();
  }
}
