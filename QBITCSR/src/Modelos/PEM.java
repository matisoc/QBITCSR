package Modelos;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.sql.SQLException;
import java.util.Base64;
import sun.security.util.DerOutputStream;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;

public class PEM {
    
	private String dato, archivo, compania, csrPEMEncoded;
	private KeyPair keypair;
	private KEY clave;
	private X500Name x500Name;
	private final String algorithm = "SHA1WithRSA";
	private Signature signature;
	private byte[] certReqInfoSignature, csrDEREncoded, certReqInfo;
	private static DerOutputStream der2, der22;
	Data sql;
    
    public PEM(String CN, String O, String CUIT, String ruta)
    {
        this.dato = "C=AR,"+"CN="+CN+",O="+O+",serialNumber=CUIT "+CUIT;
        this.compania = CN;
        this.archivo = ruta.replace("\\","\\\\") + "\\\\"+CN.replace(" ","");
    }
    public boolean generarCertif() throws IOException, InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException 
    {	
    	
    	clave = new KEY();
        keypair = clave.generateKeyPair();
        x500Name = new X500Name(dato);
        certReqInfo = createCertificationRequestInfo(x500Name, keypair.getPublic());
        signature= Signature.getInstance(algorithm);
        signature.initSign(keypair.getPrivate());
        signature.update(certReqInfo);
        certReqInfoSignature = signature.sign();
        
        // create PKCS#10 Certificate Signing Request (CSR)
        csrDEREncoded= createCertificationRequestValue(certReqInfo, algorithm, certReqInfoSignature);
        csrPEMEncoded = createPEMFormat(csrDEREncoded);
        try
         {
            writeToFile(csrDEREncoded, archivo+".der");
            writeToFile(csrPEMEncoded.getBytes(), archivo+".pem");
            insertRow(compania, csrPEMEncoded.toString(), clave.getKey(1));            
            return true;
         } 
         catch (Exception e) 
         {
              return false;
         }    
    }
    
    private static String createPEMFormat(byte[] data) 
    {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(out);
        ps.println("-----BEGIN NEW CERTIFICATE REQUEST-----");
        ps.println(Base64.getMimeEncoder().encodeToString(data));
        ps.println("-----END NEW CERTIFICATE REQUEST-----");
        return out.toString();
    }
    
    private static byte[] createCertificationRequestInfo(X500Name x500Name, PublicKey publicKey) throws IOException 
    {
        final DerOutputStream der1 = new DerOutputStream();
        der1.putInteger(BigInteger.ZERO);
        x500Name.encode(der1);
        der1.write(publicKey.getEncoded());

        der2 = new DerOutputStream();
        der2.write((byte) 48, der1);
        return der2.toByteArray();
    }
    
    private static byte[] createCertificationRequestValue(byte[] certReqInfo, String signAlgo, byte[] signature) throws IOException, NoSuchAlgorithmException 
    {
        final DerOutputStream der1 = new DerOutputStream();
        der1.write(certReqInfo);

        // add signature algorithm identifier, and a digital signature on the certification request information
        AlgorithmId.get(signAlgo).encode(der1);
        der1.putBitString(signature);

        der22 = new DerOutputStream();
        der22.write((byte) 48, der1);
        return der22.toByteArray();
    }
    
    private void insertRow(final String compania, final String PEM, final String KPV) throws SQLException, ClassNotFoundException
    {
    	sql = new Data();
    	sql.grabar(compania, PEM, KPV);
    }

    private static void writeToFile(byte[] data, String file) throws FileNotFoundException, IOException 
    {
        try (FileOutputStream out = new FileOutputStream(file)) 
        {
            out.write(data);
        }
    }
 
}
    
    

