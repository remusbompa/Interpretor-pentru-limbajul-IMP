grammar Prog;

prog : 'int' varList ';' stmt ;
varList : var | var ',' varList ;

block : '{''}' | '{' stmt '}' ;

stmt 	: var '=' aExpr ';' 	#assignment		 
		| block  #stBlock
		| 'if' bExpr block 'else' block #if 
		| 'while' bExpr block #while	
		| <assoc=right> stmt stmt # seq	 
		;


aExpr : var #varAExpr
		| aval #avalAExpr
      | aExpr '/' aExpr #div
		| aExpr '+' aExpr #plus
		| '(' aExpr ')'  #bracketA
		;

bExpr : bval #bvalBExpr
      | '!' bExpr 			 #not
		| bExpr '&&' bExpr 	 #and
 		| aExpr '>' aExpr	 #greater
		| '(' bExpr ')' 	#bracketB
		;

var : STRING ;
bval : BOOLEAN ;
aval : NUMBER ;

BOOLEAN : 'true' | 'false' ;
NUMBER :  [1-9]+ ( '0' | [1-9]+ )* | '0' ;
STRING : [a-z]+ ;
WS : [ \t\r\n]+ -> skip ; 
