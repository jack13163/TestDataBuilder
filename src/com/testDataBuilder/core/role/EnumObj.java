package com.testDataBuilder.core.role;

public class EnumObj {


    public EnumObj(Object value, Integer percent) {
        super();
        this.value = value;
        this.percent = percent;
    }
    
    private Object value;
    
    private Integer percent;

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    public EnumObj() {
        super();
        this.percent = 1;
    }
    
    public EnumObj(EnumObj other){
        this.setPercent(other.getPercent());
        this.setValue(other.getValue());
    }
    
    @Override
    public EnumObj clone(){
        return new EnumObj(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final EnumObj other = (EnumObj) obj;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }

    @Override
    public String toString(){
        if(value == null){
            return null;
        }
        return this.value.toString();
    }
    
}
