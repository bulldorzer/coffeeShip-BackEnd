package com.teamproject.coffeeShop.exception;

/*
    * 토큰 예외 처리를 위함
*/
public class CustomJWTException extends RuntimeException{

  public CustomJWTException(String msg){
      super(msg);
  }
}
