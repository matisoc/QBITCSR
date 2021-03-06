package Principal;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.security.InvalidKeyException;

import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.sun.glass.events.KeyEvent;


import Modelos.Data;
//import Modelos.PKCS12;        

public class Ventana extends JFrame implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	private Data sql;
    private Dialogo dlg_Op1, dlg_Op2, dlg_Op3, details;
    private JToolBar toolBar;
    private JButton btn_Op1, btn_Op2,btn_Op3,btn_Exit;
    private JPopupMenu ppu_Table;
    private JMenuItem ppu_Table_Item;
	private JTable table;
	private JScrollPane scrollPane;
	
    public Ventana(String title) throws Exception 
    {
        super(title); 
        sql= new Data();
        sql.connect();
        initUI();
    	setLocationRelativeTo(null);  
    }
    
    private void initUI()throws Exception
    {
        setResizable(false);
        setDefaultCloseOperation(0);
    	setSize( 600, 300 );
    	setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    	setLayout(new BorderLayout());
  	   
	    //Barra de herramientas e items c/acciones
    	toolBar = new JToolBar("toolBar"); 
        toolBar.setFloatable(false); 
        
        btn_Op1 = new JButton("Generar CSR");
        btn_Op1.addActionListener(this);
        toolBar.add(btn_Op1); 
        toolBar.addSeparator();
        
        btn_Op2 = new JButton("Instructivo AFIP");
        btn_Op2.addActionListener(this);
        btn_Op2.setEnabled(false);
        toolBar.add(btn_Op2);
        toolBar.addSeparator();
        
        btn_Op3 = new JButton("Generar P12");
        btn_Op3.addActionListener(this);
        btn_Op3.setEnabled(false);
        toolBar.add(btn_Op3);
	    toolBar.addSeparator();
	    
	    btn_Exit = new JButton("Salir");
	    btn_Exit.addActionListener(this);
	    toolBar.add(btn_Exit);
        
	    getContentPane().add(toolBar, BorderLayout.NORTH);

	    //Menu contextual
	    ppu_Table = new JPopupMenu();
        ppu_Table_Item = new JMenuItem("Detalle");
        ppu_Table_Item.addActionListener(this);
        ppu_Table.add(ppu_Table_Item);
	   
	    //Tabla y ScrollPane
	    scrollPane = new JScrollPane();
	    table = new JTable();
	    table.setModel(actualizarTabla());
	    TableColumnModel columnModel = table.getColumnModel();
	    columnModel.getColumn(0).setPreferredWidth(20);
	    columnModel.getColumn(1).setPreferredWidth(220);
	    columnModel.getColumn(2).setPreferredWidth(65);
	    columnModel.getColumn(3).setPreferredWidth(30);
	    columnModel.getColumn(4).setPreferredWidth(30);
	    columnModel.getColumn(5).setPreferredWidth(30);
	
	    scrollPane.setViewportView(table);
	    getContentPane().add(scrollPane, BorderLayout.CENTER);
	    
	    
	    //Acciones de la tabla (Key & Mouse)
        table.addKeyListener(new java.awt.event.KeyAdapter() 
        {
            public void keyPressed(java.awt.event.KeyEvent evt) 
            {
            	if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_D) 
            	{
        			System.out.println("ID: " + table.getValueAt(table.getSelectedRow(), 0));
            	}
            }
        });
       	
        table.addMouseListener(new MouseAdapter()
        {
        	public void mousePressed(MouseEvent e)
        	{
        		if ( SwingUtilities.isRightMouseButton(e) )
        		{
        			java.awt.Point p = e.getPoint();
         			int rowNumber = table.rowAtPoint(p);
          			ListSelectionModel model = table.getSelectionModel();
        			model.setSelectionInterval( rowNumber, rowNumber );
        			ppu_Table.show(e.getComponent(), e.getX(), e.getY());
        			
        			
        		}
        	}
        });
        
    }
    
    private DefaultTableModel actualizarTabla () throws ClassNotFoundException, SQLException
    {
    	
		String[] columnNames = { 
								"ID",
								"Compa�ia",
								"Vencimiento",
								"CSR",
								"CRT",
								"P12"};
		
		DefaultTableModel aModel =  
				new DefaultTableModel(columnNames, 0)
				{
	    			private static final long serialVersionUID = 1L;
	    			public boolean isCellEditable(int row, int column) {return false;}
	    			
	    			 @Override
	    	            public Class<?> getColumnClass(int column) {
	    	                switch (column) 
	    	                {
	    	                	case 0: return Object.class;
	    	                	case 1: return Object.class;
	    	                	case 2: return Object.class;	
	    	                    case 3:	return Boolean.class;
	    	                    case 4:	return Boolean.class;
	    	                    case 5:	return Boolean.class;
	    	                    default: return Object.class;
	    	                }
	    	            }

				};
	
			
		LinkedList <Object[]> linkedlist = sql.refreshTable();
		for(Object [] row : linkedlist)aModel.addRow(row);
		return aModel;
    }
    

	public void actionPerformed(ActionEvent e) 
	{ 
    	if(e.getSource()==btn_Op1)
    	{
    		try 
    		{
				dlg_Op1 = new Dialogo(this, "Generar CSR", 1);
			} 
    		catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException | IOException e2) 
    		{
				e2.printStackTrace();
			}
    		
    		dlg_Op1.setVisible(true);
    		try
    		{
    			table.setModel(actualizarTabla());
    			
    		}
    		catch ( Exception e1)
    		{
    			System.out.println(e1.getMessage());
    		}
    	}
    	else if (e.getSource()==btn_Op2)
    	{
    		try 
    		{
				dlg_Op2 = new Dialogo(this, "Instructivo AFIP", 2);
			} 
    		catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException | IOException e1) 
    		{
				e1.printStackTrace();
			}
    		
    		dlg_Op2.setVisible(true);
    	}
    	else if (e.getSource()==btn_Op3)
  	  	{
    		try
    		{
				dlg_Op3 = new Dialogo(this, "Generar P12", 3);
			} 
    		catch (InvalidKeyException | SignatureException | NoSuchAlgorithmException | IOException e1) 
    		{
    			e1.printStackTrace();
			}
    	
    		dlg_Op3.setVisible(true);
    	}
    	else if (e.getSource()==btn_Exit)
    	{ 
    		System.exit(0);
    	}
    	
    	else if (e.getSource()==ppu_Table_Item)
    	{ 
    		
    		Object ID = (int) table.getValueAt(table.getSelectedRow(), 0);
    		try { 
				details = new Dialogo (this, "Detalle de registro (ID = " 
							+ String.valueOf(table.getValueAt(table.getSelectedRow(), 0)) +
							" | Compa�ia: " + table.getValueAt(table.getSelectedRow(), 1) +
							")"
							, ID);
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		details.setVisible(true);
    		
    		
    	}
    	
   
    	
    		else {}
    }  
       
    
}




