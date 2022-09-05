package com.testDataBuilder.core.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class EnumList<V> extends ArrayList<EnumObj> {

    
    
    public EnumList() {
        super();
    }

    public EnumList(Collection<? extends EnumObj> c) {
        super(c);
    }

    public EnumList(int initialCapacity) {
        super(initialCapacity);
    }

    public int getEnumPercentSum(){
        int sum = 0;
        for(EnumObj enum_ : this){
            sum += enum_.getPercent();
        }
        return sum;
    }
    
    private Map<Double, Object> enumPercent = new TreeMap<Double, Object>();
    
    private void init(){
        this.enumPercent.clear();
        int sum =  getEnumPercentSum();
        if(this.size() > 0 && sum > 0){
            
            Integer oldPos = 0;
            for(EnumObj e : this){
                Integer percent = e.getPercent();
                if(percent != 0){
                    oldPos = oldPos + percent;
                    this.enumPercent.put(oldPos/(double)(sum), e.getValue());
                }
            }
        }
    }
    
    public Object randomByPercent(){
        if(this.size() > 0){
            if(this.enumPercent.isEmpty()){
                this.init();
            }
            
            double i = Math.random();
            for(Double key : this.enumPercent.keySet()){
                if(i < key){
                    return this.enumPercent.get(key);
                }
            }
            
        }

        return null;
    }
    
    public static void main(String[] args) {
        EnumList map = new EnumList();
//        map.put("1", 1);
//        map.put("2", 8);
//        map.put("3", 1);
//        map.put("4", 1);
//        map.put("5", 1);
        map.init();
        
        for(int i=0;i < 15; i++){
           System.out.println(map.randomByPercent());
        }
    }
}
