package Principal;

import java.awt.*;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.glass.ui.Clipboard;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import static javax.swing.JOptionPane.*;
import Modelos.CUIT;
import Modelos.Data;
import Modelos.PEM;

public class Dialogo extends JDialog  
{
	
	private static final long serialVersionUID = 1L;

	public Dialogo(JFrame owner, String title, int tipo) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, IOException 
	{
		super(owner, title, true);
	    setResizable(false);
	    setTitle(title);
	    switch(tipo)
	    {
	    	case 1: generarCSR();
	    	case 2: instructivoAFIP();
	    	case 3: generarP12();
	    }
	}
	public Dialogo(JFrame owner, String title, Object ID) throws ClassNotFoundException, SQLException  
	{
		super(owner, title, true);
	    setResizable(false);
	    setTitle(title);
	    getDetails(ID);
	
	}
	
	private void generarCSR() throws IOException, InvalidKeyException, SignatureException, NoSuchAlgorithmException
	{
		setSize(530,250);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		
		JPanel p = new JPanel();
		setContentPane(p);
		
        p.setLayout(null);
        JLabel companyLabel = new JLabel("Razon Social");
        companyLabel.setBounds(10, 10, 80, 25);
        p.add(companyLabel);
        
        JTextField companyField = new JTextField(45);
        companyField.setBounds(100, 10, 300, 25);
        p.add(companyField);
        
        JLabel clientLabel = new JLabel("Cliente");
        clientLabel.setBounds(10, 40, 80, 25);
        p.add(clientLabel);

        JTextField clientField = new JTextField(45);
        clientField.setBounds(100, 40, 300, 25);
        clientField.setEditable(false);
        clientField.setText("QBit SA");
        p.add(clientField);

        JLabel cuitLabel = new JLabel("CUIT");
        cuitLabel.setBounds(10, 70, 80, 25);
        p.add(cuitLabel);

        JTextField cuitField = new JTextField(11);
        cuitField.setBounds(100, 70, 160, 25);
        p.add(cuitField);

        JButton destinyButton = new JButton("Seleccionar destino");
        destinyButton.setBounds(134, 110, 250, 25);
        p.add(destinyButton);
        
        JLabel destinyLabel = new JLabel("Destino CSR");
        destinyLabel.setBounds(10, 140, 80, 25);
        p.add(destinyLabel);
      
        final JTextField destinyField = new JTextField(100);
        destinyField.setBounds(100, 140, 400, 25);
        destinyField.setEditable(false);
        p.add(destinyField);
        
        JButton genButton = new JButton("Generar Certificado");
        genButton.setBounds(75, 190, 170, 25);
        p.add(genButton);
        
        JButton exitButton = new JButton("Cerrar");
        exitButton.setBounds(290, 190, 170, 25);
        p.add(exitButton);
   
        destinyButton.addActionListener((ActionEvent e) -> 
        {  
            {
                JFileChooser fc=new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                FileNameExtensionFilter filtro = new FileNameExtensionFilter("Folders only", "Folders Only");
                fc.setFileFilter(filtro);
                int seleccion=fc.showOpenDialog(p);     
                if(seleccion==JFileChooser.APPROVE_OPTION)
                {            
                    File fichero=fc.getSelectedFile();
                    destinyField.setText(fichero.getAbsolutePath());
                    try(FileReader fr=new FileReader(fichero))
                    {
                        String cadena="";
                        int valor=fr.read();
                        while(valor!=-1)
                        {
                            cadena=cadena+(char)valor;
                            valor=fr.read();
                        }
                        	destinyField.setText(cadena);
                    } 
                    catch (IOException e1) 
                    {
                        e1.printStackTrace();
                    }
                }
            }
        });
        
        exitButton.addActionListener((ActionEvent e) -> {
            this.dispose();   
        }); 
        
        genButton.addActionListener((ActionEvent e) -> 
        {
            if(cuitField.getText().trim().length() == 0) 
            {
            	JOptionPane.showMessageDialog(p, "El campo del CUIT esta vacio", "Error de validación", ERROR_MESSAGE);
            } 
            else if(cuitField.getText().trim().length() != 11) 
            {
            	JOptionPane.showMessageDialog(p, "El campo CUIT no tiene 11 caracteres","Error de validación", ERROR_MESSAGE);
            } 
            else 
            {
            	if (cuitField.getText().trim().length() == 11) 
                {
            		CUIT valid1 = new CUIT (cuitField.getText().trim().toString());
                    if (valid1.verificar()) 
                    {  
                    	PEM certificado = new PEM(companyField.getText().toString(), clientField.getText().toString(), cuitField.getText().trim().toString(), destinyField.getText().toString());
                        try {
							if(certificado.generarCertif())
							 {
							        JOptionPane.showMessageDialog(p, "Se genero correctamente el certificado","Qbit SA", INFORMATION_MESSAGE);
							        this.dispose();
							 }
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    } 
                    else 
                    {
                    	JOptionPane.showMessageDialog(p, "El CUIT no es valido", "Error de Validación", ERROR_MESSAGE);
                    }
                 }   
            }
        });
	}

	
	private void instructivoAFIP ()
	{
			
	}
	
	private void generarP12()
	{
		
	}
	
	private void getDetails(Object data) throws ClassNotFoundException, SQLException
	{
		Data sql = new Data();
		setSize(530,270);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		JTabbedPane jtp_detail = new JTabbedPane();
		getContentPane().add(jtp_detail);
		
		JPanel panel_KPV = new JPanel();
		jtp_detail.addTab("Private Key", panel_KPV);
		
		JTextArea KPV = new JTextArea(10,45);
		KPV.setLineWrap(true);
		KPV.setEditable(false);
		KPV.setWrapStyleWord(true);
		JScrollPane scroll_KPV = new JScrollPane(KPV);
	    KPV.setText(sql.blobToString((int)data,0));
	    panel_KPV.add(scroll_KPV);
	    
	    JButton btn_KPV = new JButton("Exportar Private Key");
        panel_KPV.add(btn_KPV);
	   
		
		
        JPanel panel_KPB = new JPanel();
        jtp_detail.addTab("Public Key", panel_KPB);
    	JTextArea KPB = new JTextArea(10,45);
    	KPB.setLineWrap(true);
    	KPB.setEditable(false);
    	KPB.setWrapStyleWord(true);
		JScrollPane scroll_KPB = new JScrollPane(KPB);
		KPB.setText(sql.blobToString((int)data,1));
	    panel_KPB.add(scroll_KPB);
	    
	    JButton btn_KPB = new JButton("Exportar Public Key");
        panel_KPB.add(btn_KPB);


        JPanel panel_PEM = new JPanel();
        jtp_detail.addTab("Peticion de cert.", panel_PEM);
        
    	JTextArea PEM = new JTextArea(10,45);
    	PEM.setLineWrap(true);
    	PEM.setEditable(false);
    	PEM.setWrapStyleWord(true);
		JScrollPane scroll_PEM = new JScrollPane(PEM);
		PEM.setText(sql.blobToString((int)data,2));
		panel_PEM.add(scroll_PEM);
		
		JButton btn_PEM = new JButton("Exportar CSR");
        panel_PEM.add(btn_PEM);
        
  
        JPanel panel_CRT = new JPanel();
        jtp_detail.addTab("Certificado", panel_CRT);
        JPanel panel_P12 = new JPanel();
        jtp_detail.addTab("Certificado PKCS12", panel_P12);
        
      
        
        
        
//        jp1.add(label1);
  //      jp2.add(label2);
        
		
		
		/*JPanel p = new JPanel();
		setContentPane(p);
		
			
        p.setLayout(null);
        JLabel companyLabel = new JLabel("Razon Social");
        companyLabel.setBounds(10, 10, 80, 25);
        p.add(companyLabel);
        
     
        JButton KPV = new JButton("Exportar Private Key");
        KPV.setBounds(10, 40, 300, 25);
        p.add(KPV);
        JButton KPB = new JButton("Exportar Public Key");
        KPB.setBounds(10, 70, 300, 25);
        p.add(KPB);
        JButton CSR = new JButton("Exportar CSR");
        CSR.setBounds(10, 100, 300, 25);
        p.add(CSR);
        JButton CRT = new JButton("Exportar CRT");
        CRT.setBounds(10, 130, 300, 25);
        p.add(CRT);
        JButton P12 = new JButton("Exportar P12");
        P12.setBounds(10, 160, 300, 25);
        p.add(P12);
   */
		
	}
	   
	

}
