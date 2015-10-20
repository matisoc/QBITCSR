package Modelos;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import sun.misc.BASE64Encoder;

public class pKey 
{
	private String privateKey;
	private String publicKey;
	
	public pKey()
	{
		privateKey = "";
		publicKey = "";
		
	}
	
	public KeyPair generateKeyPair () 
    {
		/*
	    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024, new SecureRandom());
        KeyPair keypair = keyGen.generateKeyPair(); 


		 */

		KeyPair pair = null;
		try
		{   
    	
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
			BASE64Encoder b64 = new BASE64Encoder();

			SecureRandom random = createFixedRandom();
			generator.initialize(1024, random);

			pair = generator.generateKeyPair();
			Key pubKey = pair.getPublic();
			Key privKey = pair.getPrivate();
			
			
			this.privateKey = b64.encode(privKey.getEncoded());
			this.publicKey = b64.encode(pubKey.getEncoded());
			return pair;

		}
		catch (Exception e) 
		{
			System.out.println(e);
		}
		return pair;
    }
 
	public String getKey (int tipo)
	{
		return (tipo == 1) ? this.privateKey : (tipo == 2) ? this.publicKey : null ;
			
				//this.publicKey;
		
		/*if(tipo == 1)return this.privateKey;
		else if(tipo == 2) return this.publicKey;
		else return null;	*/
	}
	
    private static SecureRandom createFixedRandom()
    {
        return new FixedRand();
    }
 
    private static class FixedRand extends SecureRandom 
    {
 		private static final long serialVersionUID = 1L;
		MessageDigest sha;
        byte[] state;
 
        FixedRand() {
            try
            {
                this.sha = MessageDigest.getInstance("SHA-1");
                this.state = sha.digest();
            }
            catch (NoSuchAlgorithmException e)
            {
                throw new RuntimeException("can't find SHA-1!");
            }
        }
 
        public void nextBytes(byte[] bytes){
 
            int    off = 0;
 
            sha.update(state);
 
            while (off < bytes.length)
            {                
                state = sha.digest();
 
                if (bytes.length - off > state.length)
                {
                    System.arraycopy(state, 0, bytes, off, state.length);
                }
                else
                {
                    System.arraycopy(state, 0, bytes, off, bytes.length - off);
                }
 
                off += state.length;
 
                sha.update(state);
            }
        }
    }

}
