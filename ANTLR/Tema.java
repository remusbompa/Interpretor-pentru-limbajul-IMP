import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.*;
import java.util.*;

import java.io.IOException;

public class Tema {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.err.println("No file given");
            return;
        }

        ProgLexer lexer = null;
        CommonTokenStream tokenStream = null;
        ProgParser parser = null;
        ParserRuleContext globalTree = null;

        // True if any lexical or syntax errors occur.
        boolean lexicalSyntaxErrors = false;

        // Deschidem fisierul input pentru a incepe parsarea
        String fileName = args[0];
        CharStream input = CharStreams.fromFileName(fileName);

        // Definim Lexer-ul
        lexer = new ProgLexer(input);

        // Obtinem tokenii din input
        tokenStream = new CommonTokenStream(lexer);

        // Definim Parser-ul
        parser = new ProgParser(tokenStream);

        // Incepem parsarea
        ParserRuleContext tree = parser.prog();

        // Vizitam AST-ul
        MyVisitor visitor = new MyVisitor();
        visitor.visit(tree);

		//scriem in fisierul de iesire
		String str = visitor.str;
		BufferedWriter bw = new BufferedWriter(new FileWriter("arbore"));
		bw.write(str);
		bw.close();
    }
}
