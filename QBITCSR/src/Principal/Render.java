package Principal;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class Render extends DefaultTableCellRenderer
{
	
	private static final long serialVersionUID = 1L;
		public Render() 
	    {
	        super();
	        setOpaque(true);
	    } 
	    public Component getTableCellRendererComponent(JTable table, Object value, 
	            boolean isSelected, boolean hasFocus, int row, int column) 
	    { 
	    	
	        if(table.getValueAt(row, 0).equals(Integer.parseInt("1")))
	        {
	                    
	            setBackground(Color.LIGHT_GRAY); 
	            setForeground(Color.black);  
	        }    
	        else
	        {    
	        	setBackground(Color.white);     
	                
	        } 
	        
	        return this;
	    }
	}
	
	
	
	

