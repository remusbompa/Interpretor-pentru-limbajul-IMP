import java.io.*;
import java.util.*;

public class Tema {
  public static void main (String[] args) throws IOException {
    TemaLexer l = new TemaLexer(new FileReader(args[0]));
 
	l.yylex();

	Expression ast = l.ast;

	BufferedWriter bw = new BufferedWriter(new FileWriter("arbore"));
	ast.explore(0, bw);
	bw.close();
	
	
	LinkedHashMap<String,Integer> variables = l.variables;
	bw = new BufferedWriter(new FileWriter("output"));	

	try{
		((BlockExpr)ast).interpret(variables, null);
	
		for( Map.Entry<String,Integer> entry : variables.entrySet()){
	  		String name = entry.getKey();
	  		Integer val = entry.getValue();
			bw.write(name + "=" + val + "\n");
		}
	}catch(TemaException e){
		bw.write(e.getMessage());
	}
	bw.close();
  }
}
