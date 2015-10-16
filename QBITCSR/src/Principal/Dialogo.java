package Principal;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import static javax.swing.JOptionPane.*;
import Modelos.CUIT;
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

}
