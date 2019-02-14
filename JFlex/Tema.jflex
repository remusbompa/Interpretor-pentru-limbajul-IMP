import java.util.*;
 
%%
 
%class TemaLexer
%line
%standalone

%eof{
	generateRoot();
%eof}

%{
  	public LinkedHashMap<String,Integer> variables = new LinkedHashMap<String,Integer>();
	int nrVar = 0;
	Stack<Expression> stackAExpr = new Stack<>();
	public Expression ast = null;

	boolean putVariables = false;

	Expression get_nth_element_from_stack(int element_number) {
        Stack<Expression> temp_stack = new Stack<>();

        if (element_number > stackAExpr.size()) {
            return null;
        }

        for (int j = 0; j < element_number; ++j) {
            temp_stack.push(stackAExpr.pop());
        }

        Expression res = temp_stack.peek();

        for (int j = 0; j < element_number; ++j) {
            stackAExpr.push(temp_stack.pop());
        }
        return res;
   }

	Expression generateBlock() {

		Expression block;
		while(true){
			Expression expr = stackAExpr.pop();
			if(expr instanceof OpenA){
				block = new BlockExpr(null);
				break;
			}else{
				Expression expr2 = stackAExpr.pop();
				if(expr2 instanceof OpenA){
					block = new BlockExpr(expr);
					break;
				}else{
					stackAExpr.push(new SeqExpr(expr2, expr));
				}
			}
		}
		
		return block;
	}

	void generateRoot() {
		while(true){
			if(stackAExpr.isEmpty()){
				ast = new BlockExpr(null);
				break;
			}

			Expression expr = stackAExpr.pop();
			if(stackAExpr.isEmpty()){
				ast = new BlockExpr(expr);
				break;
			}
			Expression expr2 = stackAExpr.pop();
			stackAExpr.push(new SeqExpr(expr2, expr));
		}
	}

%}

Digit	=  [1-9]*
Number	=  {Digit} (  0  |  {Digit} )*  |  0
String	=	[a-z]*
Var 	=  {String}
AVal	=  {Number}
BVal	=  true | false
If = "if"
Else = "else"
While = "while"
Int = "int"

addOp = "+"
divOp = "/"
openR = "("
closeR = ")"
andOp = "&&"
greaterOp = ">"
notOp = "!"
openA = "{"
closeA = "}"
equalOp = "="
semicolonOp = ";"
commaOp = ","

whitespace = [\n\r\t ]+

%%   

{If} {
	stackAExpr.push(new If());
}

{Else} {
	stackAExpr.push(new Else());
}

{While} {
	stackAExpr.push(new While());
}

{Int} {
	putVariables = true;
	nrVar = 1;
}

{BVal} {
	stackAExpr.push(new BVal(yytext()));
}


{Var} {
	if(putVariables){
		variables.put(yytext(),null);
	}
	else{
		stackAExpr.push(new Var(yytext(), yyline, variables));
	}
}

{AVal} {
	stackAExpr.push(new AVal(yytext()));
}

{addOp} {
	while(true){
		Expression expr = get_nth_element_from_stack(2);
		if(expr == null) break;
		if(expr instanceof Plus){
			Expression right = stackAExpr.pop();
			stackAExpr.pop();
			Expression left = stackAExpr.pop();
			stackAExpr.push(new PlusExpr(left, right));
		}
		else if(expr instanceof Div){
			Expression right = stackAExpr.pop();
			stackAExpr.pop();
			Expression left = stackAExpr.pop();
			stackAExpr.push(new DivExpr(left, right, yyline));
		}
		else break;
	}
	stackAExpr.push(new Plus());
}

{divOp} {
	while(true){
		Expression expr = get_nth_element_from_stack(2);
		if(expr == null) break;
		if(expr instanceof Div){
			Expression right = stackAExpr.pop();
			stackAExpr.pop();
			Expression left = stackAExpr.pop();
			stackAExpr.push(new DivExpr(left, right, yyline));
		}
		else break;
	}
	stackAExpr.push(new Div());
}

{openR} {
	stackAExpr.push(new OpenR());
}

{closeR} {
	while(true){
			Expression expr = get_nth_element_from_stack(2);
			if(expr == null) break;
			if(expr instanceof Plus){
				Expression right = stackAExpr.pop();
				stackAExpr.pop();
				Expression left = stackAExpr.pop();
				stackAExpr.push(new PlusExpr(left, right));
			}
			else if(expr instanceof Div){
				Expression right = stackAExpr.pop();
				stackAExpr.pop();
				Expression left = stackAExpr.pop();
				stackAExpr.push(new DivExpr(left, right, yyline));
			}
			else if(expr instanceof Greater){
				Expression right = stackAExpr.pop();
				stackAExpr.pop();
				Expression left = stackAExpr.pop();
				stackAExpr.push(new GtExpr(left, right));
			}
			else if(expr instanceof And){
				Expression right = stackAExpr.pop();
				stackAExpr.pop();
				Expression left = stackAExpr.pop();
				stackAExpr.push(new AndExpr(left, right));
			}
			else if(expr instanceof Not){
				expr = stackAExpr.pop();
				stackAExpr.pop();
				stackAExpr.push(new NotExpr(expr));
			}
			else break;
	}
		Expression expr = stackAExpr.pop();
		Expression open = stackAExpr.pop();
		if(!(open instanceof OpenR)) System.out.println("Paranteza inchisa nedeschisa! ");
		if(expr != null){
				stackAExpr.push(new ParExpr(expr));
		}
		else System.out.println("Lipsa expresie aritmetica/ booleana in paranteze!"); 
}

{andOp} {
	while(true){
			Expression expr = get_nth_element_from_stack(2);
			if(expr == null) break;
			if(expr instanceof And){
				Expression right = stackAExpr.pop();
				stackAExpr.pop();
				Expression left = stackAExpr.pop();
				stackAExpr.push(new AndExpr(left, right));
			}
			else if(expr instanceof Not){
				expr = stackAExpr.pop();
				stackAExpr.pop();
				stackAExpr.push(new NotExpr(expr));
			}

			else if(expr instanceof Greater){
				Expression right = stackAExpr.pop();
				stackAExpr.pop();
				Expression left = stackAExpr.pop();
				stackAExpr.push(new GtExpr(left, right));
			}
			else break;
		}

	stackAExpr.push(new And());
}

{greaterOp} {
	while(true){
			Expression expr = get_nth_element_from_stack(2);
			if(expr == null) break;
			if(expr instanceof Plus){
				Expression right = stackAExpr.pop();
				stackAExpr.pop();
				Expression left = stackAExpr.pop();
				stackAExpr.push(new PlusExpr(left, right));
			}
			else if(expr instanceof Div){
				Expression right = stackAExpr.pop();
				stackAExpr.pop();
				Expression left = stackAExpr.pop();
				stackAExpr.push(new DivExpr(left, right, yyline));
			}
			else break;
		}
	stackAExpr.push(new Greater());
}

{notOp} {
	stackAExpr.push(new Not());
}

{equalOp} {
	stackAExpr.push(new Assignment());
}

{openA} {
	stackAExpr.push(new OpenA());
}

{closeA} {
	stackAExpr.push(generateBlock());
	Expression expr = get_nth_element_from_stack(2);
	Expression expr2 = get_nth_element_from_stack(3);
	if(expr != null && expr instanceof Else){
		Expression other = stackAExpr.pop();
		stackAExpr.pop();
		Expression then = stackAExpr.pop();
		Expression cond = stackAExpr.pop();
		stackAExpr.pop(); 
		stackAExpr.push(new IfExpr(cond, then, other));
	}
	else if(expr2 != null && expr2 instanceof While){
		Expression then = stackAExpr.pop();
		Expression cond = stackAExpr.pop();
		stackAExpr.pop(); 
		stackAExpr.push(new WhileExpr(cond, then));
	}

}

{commaOp} {
	nrVar ++;
}

{semicolonOp} {
	if(putVariables) putVariables = false;
	else{
		while(true){
			Expression expr = get_nth_element_from_stack(2);
			if(expr == null) break;
			if(expr instanceof Plus){
				Expression right = stackAExpr.pop();
				stackAExpr.pop();
				Expression left = stackAExpr.pop();
				stackAExpr.push(new PlusExpr(left, right));
			}
			else if(expr instanceof Div){
				Expression right = stackAExpr.pop();
				stackAExpr.pop();
				Expression left = stackAExpr.pop();
				stackAExpr.push(new DivExpr(left, right, yyline));
			}
			else break;
		}
		Expression expr = get_nth_element_from_stack(2);
		if(expr != null){
			if(expr instanceof Assignment){
				Expression right = stackAExpr.pop();
				stackAExpr.pop();
				Expression left = stackAExpr.pop();
				stackAExpr.push(new AssignExpr(left, right));
			}
		}

	}
}

{whitespace} {

}
