package hello;

public class HelloWorld {
    public String printTitle(String msg)
    {
    	String texto = "<h1>"+msg+"</h1>";
    	
        System.out.println(texto);
        
        return texto;
        //out.println ("<h1>"+msg+"</h1>");
    }
    
    public String printText(String msg)
    {
    	String texto = "<p>"+msg+"</p>";
    	
        System.out.println(texto);
        
        return texto;
    }
}
