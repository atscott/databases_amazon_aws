package com.MySql;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;

import java.util.*;

/**
 * User: atscott
 * Date: 11/8/13
 * Time: 6:37 PM
 */
public class AwsItemManipulator
{

  public void removeDuplicatesFromItemList(List<Item> itemList)
  {
    Map<String, Item> map = new LinkedHashMap<>();
    for (Item item : itemList)
    {
      String combinedAttributes = "";
      for (Attribute attribute : item.getAttributes())
      {
        combinedAttributes += attribute.getValue();
      }
      map.put(combinedAttributes, item);
    }
    itemList.clear();
    itemList.addAll(map.values());
  }

  public List<Item> groupItemsBy(List<Item> items, String attributeToGroupBy)
  {
    Map<String, Item> itemList = new LinkedHashMap<>();
    for (Item item : items)
    {
      for (Attribute attribute : item.getAttributes())
      {
        if (attribute.getName().equals(attributeToGroupBy))
        {
          if (itemList.containsKey(attribute.getValue()))
          {
            Item oldItem = itemList.get(attribute.getValue());
            List<Attribute> attributes = oldItem.getAttributes();
            attributes.addAll(item.getAttributes());
            oldItem.setAttributes(attributes);
            itemList.remove(attribute.getValue());
            itemList.put(attribute.getValue(), oldItem);
          }
          else
          {
            itemList.put(attribute.getValue(), item);
          }
        }
      }
    }
    List<Item> results = new ArrayList<>();
    results.addAll(itemList.values());
    return results;
  }

  public List<Item> removeAttributesWithSameValueAndGroupThoseWithDifferent(List<Item> items)
  {
    List<Item> newItems = new ArrayList<>();
    for (Item item : items)
    {
      Map<String, Attribute> attributeList = new LinkedHashMap<>();
      for (Attribute attribute : item.getAttributes())
      {
        if (attributeList.containsKey(attribute.getName()))
        {
          Attribute oldValue = attributeList.get(attribute.getName());
          if (!oldValue.getValue().equals(attribute.getValue()))
          {
            attributeList.remove(attribute.getName());
            attributeList.put(attribute.getName(), new Attribute().withName(attribute.getName()).withValue(oldValue.getValue() + "&&" + attribute.getValue()));
          }
        }
        else
        {
          attributeList.put(attribute.getName(), attribute);
        }
      }
      newItems.add(new Item().withName(item.getName()).withAttributes(attributeList.values()));
    }
    return newItems;
  }

  public List<Item> convertAttributeToCount(List<Item> items, String attributeName)
  {
    List<Item> newList = new ArrayList<>();
    for (Item item : items)
    {
      List<Attribute> newAttributes = new ArrayList<>();
      for (Attribute attribute : item.getAttributes())
      {
        if (!attribute.getName().equals(attributeName))
        {
          newAttributes.add(attribute);
        }
      }
      newAttributes.add(new Attribute(attributeName, countValues(item, attributeName).toString()));
      Item newItem = new Item().withAttributes(newAttributes).withName(item.getName());
      newList.add(newItem);
    }
    return newList;
  }

  public List<Item> removeAttributesFromItems(List<Item> items, String... attributeNames)
  {

    List<Item> newList = new ArrayList<>();
    for (Item item : items)
    {
      Item newItem = new Item().withName(item.getName());
      List<Attribute> newAttributes = new ArrayList<>();
      for (Attribute attribute : item.getAttributes())
      {
        if (!Arrays.asList(attributeNames).contains(attribute.getName()))
        {
          newAttributes.add(attribute);
        }
      }
      newItem.setAttributes(newAttributes);
      newList.add(newItem);
    }
    return newList;
  }

  public Integer countValues(Item forItem, String attributeName)
  {
    int count = 0;
    for (Attribute attribute : forItem.getAttributes())
    {
      if (attribute.getName().equals(attributeName))
      {
        count++;
      }
    }
    return count;
  }

  public List<Item> getItemsWithAttributeValueGreaterThan(List<Item> items, String attributeName, Integer boundary)
  {
    List<Item> newList = new ArrayList<>();
    for (Item item : items)
    {
      Item newItem = new Item().withName(item.getName()).withAttributes(item.getAttributes());
      Attribute attr = getAttributeWithNameOrNull(attributeName, newItem);
      if (attr != null && Integer.parseInt(attr.getValue()) > boundary)
      {
        newList.add(newItem);
      }
    }
    return newList;
  }

  public Attribute getAttributeWithNameOrNull(String name, Item item)
  {
    for (Attribute attribute : item.getAttributes())
    {
      if (attribute.getName().equals(name))
      {
        return attribute;
      }
    }
    return null;
  }

  public List<Item> renameAttribute(List<Item> items, String oldName, String newName)
  {
    List<Item> newList = new ArrayList<>();
    for (Item item : items)
    {
      Item newItem = new Item().withName(item.getName());
      List<Attribute> newAttributes = new ArrayList<>();
      for (Attribute attribute : item.getAttributes())
      {
        if (attribute.getName().equals(oldName))
        {
          newAttributes.add(new Attribute().withName(newName).withValue(attribute.getValue()));
        }
        else
        {
          newAttributes.add(attribute);
        }
      }
      newItem.setAttributes(newAttributes);
      newList.add(newItem);
    }
    return newList;
  }
}
