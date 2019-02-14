import java.io.*;
import java.util.*;

public abstract class Expression{
	public void explore(int niv, BufferedWriter bw) throws IOException{
		System.out.print("");
	}

	public boolean interpret(LinkedHashMap<String,Integer> variables, boolean aux) throws TemaException{
		return true;
	}
	public void interpret(LinkedHashMap<String,Integer> variables, Object aux) throws TemaException{
	}
	public int interpret(LinkedHashMap<String,Integer> variables, int aux) throws TemaException{
		return 1;
	}
	public String interpret(LinkedHashMap<String,Integer> variables, char aux) throws TemaException{
		return null;
	}
}

class AVal extends Expression{
	public int val;
	public AVal(String text){
		val = Integer.parseInt(text);
	}
	@Override
	public void explore(int niv, BufferedWriter bw) throws IOException{
		for(int i =0; i < niv; i++)
			bw.write("\t");
		bw.write("<IntNode> " + val + "\n");
	}
	@Override
	public int interpret(LinkedHashMap<String,Integer> variables, int aux) throws TemaException{
		return val;
	}
}

class BVal extends Expression{
	public boolean val;
	public BVal(String text){
		val = Boolean.parseBoolean(text);
	}
	@Override
	public void explore(int niv, BufferedWriter bw) throws IOException{
		for(int i =0; i < niv; i++)
			bw.write("\t");
		bw.write("<BoolNode> " + val + "\n");
	}
	@Override
	public boolean interpret(LinkedHashMap<String,Integer> variables, boolean aux) throws TemaException{
		return val;
	}

}

class Var extends Expression{
	public String val;
	int line;

	static boolean exception = false;
	static int excLine;

	public Var(String text, int line, LinkedHashMap<String,Integer> variables){
		this.val = text;
		this.line = line + 1;

		if(!variables.containsKey(val) && exception == false){
			exception = true;
			excLine = line + 1;
		}
	}
	@Override
	public void explore(int niv, BufferedWriter bw) throws IOException{
		for(int i =0; i < niv; i++)
			bw.write("\t");
		bw.write("<VariableNode> " + val + "\n");
	}
	@Override
	public String interpret(LinkedHashMap<String,Integer> variables, char aux) throws TemaException{
		if(exception) throw new TemaException("UnassignedVar " + excLine + "\n");
		return val;
	}
	@Override
	public int interpret(LinkedHashMap<String,Integer> variables, int aux) throws TemaException{
		if(exception) throw new TemaException("UnassignedVar " + excLine + "\n");
		Integer res = variables.get(val);
		if(res == null) throw new TemaException("UnassignedVar " + line + "\n");
		return res;
	}

}

 class Plus extends Expression{
}

 class Div extends Expression{
}

 class OpenR extends Expression{
}

 class CloseR extends Expression{
}

 class And extends Expression{
}

 class Greater extends Expression{
}

 class Not extends Expression{
}

 class Assignment extends Expression{
}

 class OpenA extends Expression{
}

 class CloseA extends Expression{
}

 class If extends Expression{
}

 class While extends Expression{
}

 class Else extends Expression{
}

class PlusExpr extends Expression{
	Expression left, right;
	public PlusExpr(Expression left, Expression right){
		this.left = left;
		this.right = right;
	}
	@Override
	public void explore(int niv, BufferedWriter bw) throws IOException{
		for(int i =0; i < niv; i++)
			bw.write("\t");
		bw.write("<PlusNode> +\n");

		left.explore(niv + 1, bw);
		right.explore(niv + 1, bw);
	}
	@Override
	public int interpret(LinkedHashMap<String,Integer> variables, int aux) throws TemaException{
		int lterm = left.interpret(variables, 1);
		int rterm = right.interpret(variables, 1);
		return lterm + rterm;
	}
}

class DivExpr extends Expression{
	Expression left, right;
	int line;
	public DivExpr(Expression left, Expression right, int line){
		this.left = left;
		this.right = right;
		this.line = line + 1;
	}
	@Override
	public void explore(int niv, BufferedWriter bw) throws IOException{
		for(int i =0; i < niv; i++)
			bw.write("\t");
		bw.write("<DivNode> /\n");

		left.explore(niv + 1, bw);
		right.explore(niv + 1, bw);
	}
	@Override
	public int interpret(LinkedHashMap<String,Integer> variables, int aux) throws TemaException{
		int lterm = left.interpret(variables, 1);
		int rterm = right.interpret(variables, 1);

		if(rterm == 0) throw new TemaException("DivideByZero " + line + "\n");
		return lterm / rterm;
	}
}

class ParExpr extends Expression{
	Expression expr;
	public ParExpr(Expression expr){
		this.expr = expr;
	}
	@Override
	public void explore(int niv, BufferedWriter bw) throws IOException{
		for(int i =0; i < niv; i++)
			bw.write("\t");
		bw.write("<BracketNode> ()\n");

		expr.explore(niv + 1, bw);
	}
	@Override	
	public boolean interpret(LinkedHashMap<String,Integer> variables, boolean aux) throws TemaException{
		return expr.interpret(variables, true);
	}
	@Override
	public int interpret(LinkedHashMap<String,Integer> variables, int aux) throws TemaException{
		return expr.interpret(variables, 1);
	}
}

class AssignExpr extends Expression{
	Expression left, right;
	public AssignExpr(Expression left, Expression right){
		this.left = left;
		this.right = right;
	}
	@Override
	public void explore(int niv, BufferedWriter bw) throws IOException{
		for(int i =0; i < niv; i++)
			bw.write("\t");
		bw.write("<AssignmentNode> =\n");

		left.explore(niv + 1, bw);
		right.explore(niv + 1, bw);
	}
	@Override
	public void interpret(LinkedHashMap<String,Integer> variables, Object aux) throws TemaException{
		String name = left.interpret(variables, 'a');
		int val = right.interpret(variables, 1);
		variables.put(name, val);
	}

}

class AndExpr extends Expression{
	Expression left, right;
	public AndExpr(Expression left, Expression right){
		this.left = left;
		this.right = right;
	}
	@Override
	public void explore(int niv, BufferedWriter bw) throws IOException{
		for(int i =0; i < niv; i++)
			bw.write("\t");
		bw.write("<AndNode> &&\n");

		left.explore(niv + 1, bw);
		right.explore(niv + 1, bw);
	}
	@Override
	public boolean interpret(LinkedHashMap<String,Integer> variables, boolean aux) throws TemaException{
		boolean lterm = left.interpret(variables, true);
		boolean rterm = right.interpret(variables, true);
		return lterm && rterm;
	}
}

class NotExpr extends Expression{
	Expression expr;
	public NotExpr(Expression expr){
		this.expr = expr;
	}
	@Override
	public void explore(int niv, BufferedWriter bw) throws IOException{
		for(int i =0; i < niv; i++)
			bw.write("\t");
		bw.write("<NotNode> !\n");

		expr.explore(niv + 1, bw);
	}
	@Override
	public boolean interpret(LinkedHashMap<String,Integer> variables, boolean aux) throws TemaException{
		boolean res = expr.interpret(variables, true);
		return (! res);
	}
}

class GtExpr extends Expression{
	Expression left, right;
	public GtExpr(Expression left, Expression right){
		this.left = left;
		this.right = right;
	}
	@Override
	public void explore(int niv, BufferedWriter bw) throws IOException{
		for(int i =0; i < niv; i++)
			bw.write("\t");
		bw.write("<GreaterNode> >\n");

		left.explore(niv + 1, bw);
		right.explore(niv + 1, bw);
	}
	@Override
	public boolean interpret(LinkedHashMap<String,Integer> variables, boolean aux) throws TemaException{
		int lterm = left.interpret(variables, 1);
		int rterm = right.interpret(variables, 1);
		if(lterm > rterm) return true;
		else return false;
	}

}

class BlockExpr extends Expression{
	Expression expr;
	
	public BlockExpr(Expression expr){
		this.expr = expr;
	}
	@Override
	public void explore(int niv, BufferedWriter bw) throws IOException{
		if(niv == 0){
			bw.write("<MainNode>\n");
		}else{
			for(int i =0; i < niv; i++)
				bw.write("\t");
			bw.write("<BlockNode> {}\n");
		}

		if(expr != null){
			expr.explore(niv + 1, bw);
		}
	}
	@Override
	public void interpret(LinkedHashMap<String,Integer> variables, Object aux) throws TemaException{
		if(expr != null)
			expr.interpret(variables, null);
	}
}

class IfExpr extends Expression{
	Expression cond, then, other;
	public IfExpr(Expression cond, Expression then, Expression other){
		this.cond = cond;
		this.then = then;
		this.other = other;
	}
	@Override
	public void explore(int niv, BufferedWriter bw) throws IOException{
		for(int i =0; i < niv; i++)
			bw.write("\t");
		bw.write("<IfNode> if\n");

		cond.explore(niv + 1, bw);
		then.explore(niv + 1, bw);
		other.explore(niv + 1, bw);
	}
	@Override
	public void interpret(LinkedHashMap<String,Integer> variables, Object aux) throws TemaException{
		boolean conditie = cond.interpret(variables, true);
		if(conditie){
			then.interpret(variables, null);
		}else{
			other.interpret(variables, null);
		}
	}
}

class WhileExpr extends Expression{
	Expression cond, then;
	public WhileExpr(Expression cond, Expression then){
		this.cond = cond;
		this.then = then;
	}
	@Override
	public void explore(int niv, BufferedWriter bw) throws IOException{
		for(int i =0; i < niv; i++)
			bw.write("\t");
		bw.write("<WhileNode> while\n");

		cond.explore(niv + 1, bw);
		then.explore(niv + 1, bw);
	}
	@Override
	public void interpret(LinkedHashMap<String,Integer> variables, Object aux) throws TemaException{
		while(true){
			boolean conditie = cond.interpret(variables, true);
			if(conditie == false) break;
			then.interpret(variables, null);
		}
	}
}

class SeqExpr extends Expression{
	Expression left, right;
	public SeqExpr(Expression left, Expression right){
		this.left = left;
		this.right = right;
	}
	@Override
	public void explore(int niv, BufferedWriter bw) throws IOException{
		for(int i =0; i < niv; i++)
			bw.write("\t");
		bw.write("<SequenceNode>\n");

		left.explore(niv + 1, bw);
		right.explore(niv + 1, bw);
	}
	@Override
	public void interpret(LinkedHashMap<String,Integer> variables, Object aux) throws TemaException{
		left.interpret(variables, null);
		right.interpret(variables, null);
	}
}
