/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guess.game;


import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author KAMAA
 */
public class GuessGame {
    
     
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
  Thread thread =new Thread (new Runnable (){
      @Override
      public void run() {
          for(int i=0; i<=30;i++){
              System .out .print("\nprinting"+ i*3 +"in a  thread");
              try {
                  Thread.sleep(1000);
              } catch (InterruptedException ex) {
                  Logger.getLogger(GuessGame.class.getName()).log(Level.SEVERE, null, ex);
              }
          }
      }
      
  }) ; 
thread.start();
 for(int i=0; i<=30;i++){
              System .out .print("\nprinting"+ i*3 +"in a  main thread");
              try {
                  Thread.sleep(1000);
              } catch (InterruptedException ex) {
                  Logger.getLogger(GuessGame.class.getName()).log(Level.SEVERE, null, ex);
              }
          
                  
    }
    
}
