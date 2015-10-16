package Modelos;

public class CUIT {
    
    String cadena;
    
    public CUIT(String CUIT)
    {
        this.cadena = CUIT;
    }
    
    public boolean verificar()
    {
        
        return (int)(cadena.charAt(10) - '0') == calcularDigito();
        
    }
                             
    
    public int calcularDigito()
    {
        int sumador = 0, resultado = 0, resto = 0;
    
        for( int i = 0; i< cadena.length(); i++)
        {
            switch(i)
            {           
                case 0: sumador += (int)(cadena.charAt(i) - '0')* 5;
                        break;
                case 1: sumador += (int)(cadena.charAt(i) - '0')* 4;
                        break;
                case 2: sumador += (int)(cadena.charAt(i) - '0')* 3;
                        break;
                case 3: sumador += (int)(cadena.charAt(i) - '0')* 2;
                        break;
                case 4: sumador += (int)(cadena.charAt(i) - '0')* 7;
                        break;
                case 5: sumador += (int)(cadena.charAt(i) - '0')* 6;
                        break;
                case 6: sumador += (int)(cadena.charAt(i) - '0')* 5;
                        break;
                case 7: sumador += (int)(cadena.charAt(i) - '0')* 4;
                        break;
                case 8: sumador += (int)(cadena.charAt(i) - '0')* 3;
                        break;
                case 9: sumador += (int)(cadena.charAt(i) - '0')* 2;
                        break;
            }
        }
        resultado = sumador / 11;
        resto = sumador - (resultado * 11);
        if(resto == 0)
        {
            return 0;
        }
        else
        {
            if(resto == 1)
            {
                    if ((int)cadena.charAt(0) == 2 && (int)cadena.charAt(1) == 0)
                    {
                       return 9;
                    }
                    if ((int)cadena.charAt(0) == 2 && (int)cadena.charAt(1) == 3)
                    {
                      return 4;
                    }
		
            }
            else
            {
                    return 11 - resto;
            }
            
        }
      return 0;  
    }

}



