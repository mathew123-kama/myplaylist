/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guess.game;

/**
 *
 * @author KAMAA
 */
 public class animal implements bird {

    public animal() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
     String name;
     

    @Override
    public void fly() {
        System.out.print("i am above the sea level");
    }

    
    
    

    @Override
    public void eat(String food) {
        System.out.print("am eating"+food);
    }
    
    }

    

