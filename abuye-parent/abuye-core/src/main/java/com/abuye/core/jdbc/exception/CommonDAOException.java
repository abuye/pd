 package com.abuye.core.jdbc.exception;
 
 public class CommonDAOException extends Exception
 {
   private static final long serialVersionUID = 1L;
 
   public CommonDAOException()
   {
   }
 
   public CommonDAOException(String message)
   {
     super(message);
   }
 
   public CommonDAOException(Throwable cause) {
     super(cause);
   }
   public CommonDAOException(String message, Throwable cause) {
     super(message, cause);
   }
 }

