package wyvern.target.corewyvernIL;

import java.io.IOException;

import wyvern.target.corewyvernIL.astvisitor.ASTVisitor;
import wyvern.target.corewyvernIL.astvisitor.EmitOIRVisitor;
import wyvern.target.corewyvernIL.type.ValueType;
import wyvern.target.oir.OIRAST;
import wyvern.target.oir.OIREnvironment;

public class FormalArg extends ASTNode implements EmitOIR {

	private String name;
	private ValueType type;
	
	public FormalArg(String name, ValueType type) {
		this.name = name;
		this.type = type;
	}
	
	@Override
	public void doPrettyPrint(Appendable dest, String indent) throws IOException {
		dest.append(name).append(':');
		type.doPrettyPrint(dest, indent);
	}

	public String getName() {
		return name;
	}
	
	public ValueType getType() {
		return type;
	}
	
	@Override
	public <T> T acceptVisitor(ASTVisitor<T> emitILVisitor,
			Environment env, OIREnvironment oirenv) {
		return emitILVisitor.visit(env, oirenv, this);
	}
}
