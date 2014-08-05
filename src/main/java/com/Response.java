package com;

/**
 * User: atscott
 * Date: 10/15/13
 * Time: 7:04 PM
 */
public class Response
{
  private String message = "";
  private boolean success;
  public Response(boolean success, String message)
  {
    this.success = success;
    this.message = message;
  }

  public String GetMessage()
  {
    return message;
  }

  public boolean WasSuccessful()
  {
    return success;
  }
}
