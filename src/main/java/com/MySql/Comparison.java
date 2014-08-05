package com.MySql;

/**
 * User: atscott
 * Date: 10/16/13
 * Time: 5:16 PM
 */
public class Comparison
{
  public String LeftHandSide;
  public String RightHandSide;
  public String Comparator;

  public Comparison(String leftHandSide, String comparator, String rightHandSide)
  {
    LeftHandSide = leftHandSide;
    RightHandSide = rightHandSide;
    Comparator = comparator;
  }

  @Override
  public String toString()
  {
    return LeftHandSide + " " + Comparator + " " + RightHandSide;
  }
}
