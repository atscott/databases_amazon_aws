package com.application.GUI;

import com.MySql.ResultTable;
import com.MySql.CannedQueries;
import com.application.MyTableView;

import javax.swing.*;
import java.sql.SQLException;

/**
 * User: atscott
 * Date: 10/18/13
 * Time: 10:23 PM
 */
public class OwnersListForm
{
  private MyTableView ownersView1;
  private JPanel panel1;

  public static JFrame CreateOwnersFrame()
  {
    JFrame frame = new JFrame("OwnersListForm");
    frame.setContentPane(new OwnersListForm().panel1);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.pack();
    return frame;
  }


  private void createUIComponents() throws SQLException
  {
    CannedQueries cq = new CannedQueries();
    ResultTable data = cq.GetOwnersOfUnits();
    ownersView1 = new MyTableView(data.GetHeader());
    ownersView1.SetData(data.GetRows());
  }

}
