package Modelos;


import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.sql.SQLException;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import sun.misc.IOUtils;

public class PKCS12 {

	private String certFilename;
	private Data query;
	@SuppressWarnings("unused")
	private X509Certificate cert;
	
	
	public PKCS12(String archivo)
	{
		this.certFilename = "C:\\aTunes\\p2.crt";
		query = new Data();
	}

	public void hacer() throws CertificateException, KeyStoreException, NoSuchAlgorithmException, IOException
	{
		try {
			System.out.println(query.getKey(38));
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			//convertPEMToPKCS12(getKey("x"), certFilename, "Hola");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void getKey(String id) throws CertificateException,IOException, NumberFormatException, ClassNotFoundException, SQLException 
	{
	 	//Reader stringToReader = new StringReader(query.getKey(Integer.parseInt(id)));
	
	
	}
	/*
	
	public void chau ( final String cerFile,
	        final String password) 
	        
	{
			
		Security.addProvider(new BouncyCastleProvider());
        PEMParser pem = new PEMParser(keyFile);
        PEMKeyPair pemKeyPair = (PEMKeyPair) pem.readObject();
        KeyPair kp = new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
		PrivateKey key = kp.getPrivate();
		pem.close();
		
	    // Get the certificate
	    FileReader fileReader = new FileReader(cerFile);
		pem = new PEMParser(fileReader);
	    X509CertificateHolder certHolder = (X509CertificateHolder) pem.readObject();;
	    java.security.cert.Certificate X509Certificate = new JcaX509CertificateConverter().setProvider("SC")
                .getCertificate(certHolder);
	    pem.close();
	    fileReader.close();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(null);
        ks.setKeyEntry("BSP", (java.security.Key) key, password.toCharArray(),
	            new java.security.cert.Certificate[]{X509Certificate});
	        ks.store(bos, password.toCharArray());
	        bos.close();
	    FileOutputStream fos = new FileOutputStream (new File("p12file.p12")); 
        bos.writeTo(fos);
     */
	
	
}
