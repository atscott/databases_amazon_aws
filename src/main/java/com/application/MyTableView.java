package com.application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

/**
 * User: atscott
 * Date: 10/18/13
 * Time: 10:11 PM
 */
public class MyTableView extends JPanel
{
  private String[] columnNames;
  JTable table;


  public MyTableView(String[] columnNames)
  {
    this.setLayout(new BorderLayout());
    this.columnNames = columnNames;
    table = new JTable();
    JScrollPane scrollPane = new JScrollPane(table);
    table.setFillsViewportHeight(true);
    this.add(scrollPane);
  }

  public void SetData(List<String[]> dataRows)
  {
    DefaultTableModel model = getDefaultTableModel();
    setModelColumns(model);
    populateModelRows(dataRows, model);
    table.setModel(model);
    setTableSorter();
  }

  private void setTableSorter()
  {
    TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
    table.setRowSorter(sorter);
  }

  private DefaultTableModel getDefaultTableModel()
  {
    return new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        //all cells false
        return false;
      }
    };
  }

  private void populateModelRows(List<String[]> dataRows, DefaultTableModel model)
  {
    for(String[] row : dataRows)
    {
      model.addRow(row);
    }
  }

  private void setModelColumns(DefaultTableModel model)
  {
    for(String column : columnNames)
    {
      model.addColumn(column);
    }
  }


}
