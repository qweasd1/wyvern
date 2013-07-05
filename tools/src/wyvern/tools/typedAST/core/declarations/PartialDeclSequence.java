package wyvern.tools.typedAST.core.declarations;

import java.util.Iterator;
import java.util.LinkedList;

import javax.management.RuntimeErrorException;

import wyvern.tools.errors.FileLocation;
import wyvern.tools.parsing.LineParser;
import wyvern.tools.parsing.LineSequenceParser;
import wyvern.tools.typedAST.abs.Declaration;
import wyvern.tools.typedAST.interfaces.EnvironmentExtender;
import wyvern.tools.typedAST.interfaces.TypedAST;
import wyvern.tools.typedAST.interfaces.Value;
import wyvern.tools.types.Environment;
import wyvern.tools.types.Type;
import wyvern.tools.util.Pair;
import wyvern.tools.util.TreeWriter;

public class PartialDeclSequence {
	
	private LinkedList<PartialDecl> decls = new LinkedList<PartialDecl>();
	
	private LinkedList<EnvironmentExtender> fullDecls = new LinkedList<>();
	
	public PartialDeclSequence () {
	}
	
	public Environment add(PartialDecl decl, Environment env) {
		if (!isResolved()) {
			decls.push(decl);
			return decl.extend(env);
		} else
			throw new RuntimeException("Tried to add to resolved decl sequence");
	}
	
	public Pair<TypedAST,Environment> resolve(Environment env) {
        LinkedList<PartialDecl> declsI = (LinkedList<PartialDecl>)decls.clone();
        decls = null;
        for (PartialDecl decl : declsI) {
            decl.preParse(env);
        }
		for (PartialDecl decl : declsI) {
			TypedAST ast = decl.getAST(env);
			fullDecls.addFirst((EnvironmentExtender) ast);
		}
		DeclSequence declseq = new DeclSequence(fullDecls);
		
		return new Pair<TypedAST, Environment>(declseq,env);
	}
	
	public boolean isResolved() {
		return decls == null;
	}
	
	public boolean isEmpty() {
		return decls.isEmpty();
	}

}
