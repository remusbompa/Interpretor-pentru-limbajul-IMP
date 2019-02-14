import java.lang.Integer;

class MyVisitor extends ProgBaseVisitor<Void> {
   private int nr = 0;
	public String str = "<MainNode>\n";
	boolean ok = false; 
   
    private String addTabs() {
		String tabs = "";
      for (int i = 0; i < this.nr; i++) {
           tabs += "\t";
       }

		return tabs;
    }

	@Override public Void visitProg(ProgParser.ProgContext ctx) {

		visitChildren(ctx); 
		return null;

	}

	@Override public Void visitVarList(ProgParser.VarListContext ctx) {
		visitChildren(ctx);
		ok = true; 
		return null;
	}
	
	@Override public Void visitBlock(ProgParser.BlockContext ctx) { 

		nr += 1;
      str += addTabs();
      str += "<BlockNode> {}\n"; 
		visitChildren(ctx); 
		nr -= 1;
		return null;
	}
	
	@Override public Void visitAssignment(ProgParser.AssignmentContext ctx) {
		nr += 1;
      str += addTabs();
      str += "<AssignmentNode> =\n"; 
		visitChildren(ctx); 
		nr -= 1;
		return null;
	}
	
	@Override public Void visitWhile(ProgParser.WhileContext ctx) {
		nr += 1;
      str += addTabs();
      str += "<WhileNode> while\n"; 
		visitChildren(ctx); 
		nr -= 1;
		return null;
	}

	@Override public Void visitIf(ProgParser.IfContext ctx) {
		nr += 1;
      str += addTabs();
      str += "<IfNode> if\n";
		visitChildren(ctx);
		nr -= 1; 
		return null;
	}
	
	@Override public Void visitSeq(ProgParser.SeqContext ctx) {
		nr += 1;
      str += addTabs();
      str += "<SequenceNode>\n" ;
		visitChildren(ctx);
		nr -= 1;
		return null;
	}
	
	
	@Override public Void visitDiv(ProgParser.DivContext ctx) {
		nr += 1;
      str += addTabs();
      str += "<DivNode> /\n" ;
		visitChildren(ctx);
		nr -= 1;
		return null;
	}
	
	@Override public Void visitBracketA(ProgParser.BracketAContext ctx) {
		nr += 1;
      str += addTabs();
      str += "<BracketNode> ()\n";
		visitChildren(ctx);
		nr -= 1;
		return null;
	}
	
	@Override public Void visitPlus(ProgParser.PlusContext ctx) {
		nr += 1;
      str += addTabs();
      str += "<PlusNode> +\n"; 
		visitChildren(ctx);
		nr -= 1;
		return null;
	}
	
	@Override public Void visitNot(ProgParser.NotContext ctx) {
	   nr += 1;
      str += addTabs();
      str += "<NotNode> !\n";
 		visitChildren(ctx);
		nr -= 1;
		return null;
	}
	
	@Override public Void visitAnd(ProgParser.AndContext ctx) {
		nr += 1;
      str += addTabs();
      str += "<AndNode> &&\n"; 
		visitChildren(ctx);
		nr -= 1;
		return null;
	}
	
	@Override public Void visitGreater(ProgParser.GreaterContext ctx) {
		nr += 1;
      str += addTabs();
      str += "<GreaterNode> >\n"; 
		visitChildren(ctx); 
		nr -= 1;
		return null;
	}
	
	@Override public Void visitBracketB(ProgParser.BracketBContext ctx) {
		nr += 1;
      str += addTabs();
      str += "<BracketNode> ()\n"; 
		visitChildren(ctx);
		nr -= 1; 
		return null;
	}
	
	@Override public Void visitVar(ProgParser.VarContext ctx) {
		nr += 1;
      if(ok){
         str += addTabs();
         str += "<VariableNode> " + ctx.STRING() + "\n";
		}
		visitChildren(ctx);
		nr -= 1;
		return null;
	}
	
	@Override public Void visitBval(ProgParser.BvalContext ctx) {
		nr += 1;
      str += addTabs();
      str += "<BoolNode> " + ctx.BOOLEAN() + "\n"; 
		visitChildren(ctx); 
		nr -= 1;
		return null;
	}
	
	@Override public Void visitAval(ProgParser.AvalContext ctx) {
		nr += 1;
      str += addTabs();
      str += "<IntNode> " + ctx.NUMBER() + "\n";
		visitChildren(ctx);
		nr -= 1;
		return null;
	}
}
