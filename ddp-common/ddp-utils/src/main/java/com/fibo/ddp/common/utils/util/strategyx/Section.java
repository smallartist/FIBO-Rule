package com.fibo.ddp.common.utils.util.strategyx;


public class Section {

	public float x;
	public float y;
	
	public Section(float low, float hight){
		 
		x=low;
		y=hight;
    }
	
    public String toString() {  
        return "[" + x + "," + y + "]";  
    }  
    
    boolean isSample(Section section) {  
        return x == section.x && y == section.y;  
    }  
}
