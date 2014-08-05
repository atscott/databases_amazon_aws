package com.application.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: atscott
 * Date: 10/19/13
 * Time: 9:02 AM
 */
public class MainMenu
{
  private JFrame frame;
  SearchCardForm searchCardForm;
  private JPanel mainPanel;

  public MainMenu()
  {
    frame = new JFrame("Main");
    searchCardForm = new SearchCardForm();
    JPanel panel = searchCardForm.GetMainPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    frame.setContentPane(searchCardForm.GetMainPanel());
    createMenu();

  }

  private void createMenu()
  {
    MenuBar menuBar = new MenuBar();
    Menu menu = new Menu("Searches");
    menuBar.add(menu);

    MenuItem ownersItem = new MenuItem("Owners List");
    ownersItem.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        JFrame frame = OwnersListForm.CreateOwnersFrame();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
      }
    });
    menu.add(ownersItem);


    MenuItem nameNumberWeeksItem = new MenuItem("Search Owners By Building Name, Unit Number, and Weeks");
    nameNumberWeeksItem.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        searchCardForm.ShowSearchByBuildingUnitNumAndMinWeeks();
      }
    });
    menu.add(nameNumberWeeksItem);

    MenuItem maintShare = new MenuItem("Share of Maintenance");
    maintShare.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        searchCardForm.ShowShareOfMaintenance();
      }
    });
    menu.add(maintShare);

    MenuItem weeksOwned = new MenuItem("Owners By Weeks Owned In Building");
    weeksOwned.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        searchCardForm.ShowOwnersByWeeksOwned();
      }
    });
    menu.add(weeksOwned);

    MenuItem ownerInfo = new MenuItem("Owner Info");
    ownerInfo.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        searchCardForm.ShowOwnerInfo();
      }
    });
    menu.add(ownerInfo);

    MenuItem ownersOfUnit = new MenuItem("Owners Of Unit");
    ownersOfUnit.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        searchCardForm.ShowOwnersOfUnit();
      }
    });
    menu.add(ownersOfUnit);

    MenuItem ownersOnWeek = new MenuItem("Owners On Week");
    ownersOnWeek.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        searchCardForm.ShowOwnersOnWeek();
      }
    });
    menu.add(ownersOnWeek);

    Menu menuImport = new Menu("Import");
    menuBar.add(menuImport);

    MenuItem importCsvMenu = new MenuItem("Load CSV To Table");
    importCsvMenu.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        JFrame frame = LoadCsv.CreateCsvLoaderForm();
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
      }
    });
    menuImport.add(importCsvMenu);


    frame.setMenuBar(menuBar);
  }

  public JFrame GetFrame()
  {
    return frame;
  }

  public static void main(String[] args)
  {
    JFrame frame = new MainMenu().GetFrame();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
