package com.application.GUI;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import com.CsvTableLoader;
import com.MySql.CannedQueries;
import com.MySql.DatabaseConnection;
import com.Response;
import com.application.MyTableView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: atscott
 * Date: 10/19/13
 * Time: 8:21 PM
 */
public class LoadCsv
{
  private JTextField fileLocationTextbox;
  private JButton browseButton;
  private JComboBox tablesCombo;
  private JButton importButton;
  private JPanel mainPanel;

  public LoadCsv()
  {
    setupAutoComplete();
    setupBrowseButton();
    importButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        CsvTableLoader tableLoader = new CsvTableLoader();
        DatabaseConnection con = new DatabaseConnection();
        List<Response> responses = tableLoader.LoadFileIntoTable(fileLocationTextbox.getText(),(String) tablesCombo.getSelectedItem(), con);
        MyTableView resultsTable = getTableFromResults(responses);
        showResults(resultsTable);
      }
    });
  }

  public static JFrame CreateCsvLoaderForm()
  {
    JFrame frame = new JFrame("Load Csv To Table");
    frame.setContentPane(new LoadCsv().mainPanel);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    frame.pack();
    return frame;
  }

  private MyTableView getTableFromResults(List<Response> responses)
  {
    MyTableView resultsTable = new MyTableView(new String[] {"success","message"});
    List<String[]> resultsList = new ArrayList<>();
    for(Response response : responses)
    {
      resultsList.add(new String[] {response.WasSuccessful() ? "yes": "no",response.GetMessage() });
    }
    resultsTable.SetData(resultsList);
    return resultsTable;
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

  private void setupBrowseButton()
  {
    browseButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
          fileLocationTextbox.setText(chooser.getSelectedFile().getAbsolutePath());
        }
      }
    });
  }

  private void setupAutoComplete()
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        final List<String> tables = getTables();
        AutoCompleteSupport.install(tablesCombo, GlazedLists.eventList(tables));
      }
    });
  }

  private List<String> getTables()
  {
    CannedQueries cq = new CannedQueries();
    List<String> tables;
    try
    {
      tables = cq.GetTableList();
    } catch (Exception e)
    {
      tables = new ArrayList<>();
    }
    return tables;
  }


}
