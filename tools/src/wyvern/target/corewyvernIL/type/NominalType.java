package wyvern.target.corewyvernIL.type;

import java.io.IOException;
import java.util.Arrays;

import wyvern.target.corewyvernIL.Environment;
import wyvern.target.corewyvernIL.FormalArg;
import wyvern.target.corewyvernIL.astvisitor.ASTVisitor;
import wyvern.target.corewyvernIL.astvisitor.EmitOIRVisitor;
import wyvern.target.corewyvernIL.decltype.ConcreteTypeMember;
import wyvern.target.corewyvernIL.decltype.DeclType;
import wyvern.target.corewyvernIL.expression.Path;
import wyvern.target.corewyvernIL.expression.Variable;
import wyvern.target.corewyvernIL.support.TypeContext;
import wyvern.target.corewyvernIL.support.View;
import wyvern.target.oir.OIRAST;
import wyvern.target.oir.OIREnvironment;

public class NominalType extends ValueType{
	
	@Override
	public StructuralType getStructuralType(TypeContext ctx, StructuralType theDefault) {
		DeclType dt = path.typeCheck(ctx).getStructuralType(ctx).findDecl(typeMember, ctx);
		if (dt instanceof ConcreteTypeMember) {
			ValueType vt = ((ConcreteTypeMember)dt).getResultType(View.from(path, ctx));
			return vt.getStructuralType(ctx, theDefault);
		} else {
			return super.getStructuralType(ctx, theDefault);
		}
	}

	@Override
	public void doPrettyPrint(Appendable dest, String indent) throws IOException {
		path.doPrettyPrint(dest, indent);
		dest.append('.').append(typeMember);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(new Object[] {
		           path,
		           typeMember,
		    });
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NominalType))
			return false;
		NominalType other = (NominalType)obj;
		return path.equals(other.path) && typeMember.equals(other.typeMember);
	}

	private Path path;
	private String typeMember;
	
	
	public NominalType(String pathVariable, String typeMember) {
		super();
		this.path = new Variable(pathVariable);
		this.typeMember = typeMember;
	}

	public NominalType(Path path, String typeMember) {
		super();
		this.path = path;
		this.typeMember = typeMember;
	}

	public Path getPath() {
		return path;
	}
	
	public String getTypeMember() {
		return typeMember;
	}
	
	public boolean isSubtypeOf(Type t, TypeContext ctx) {
		if (equals(t))
			return true;
		DeclType dt = path.typeCheck(ctx).getStructuralType(ctx).findDecl(typeMember, ctx);
		if (dt instanceof ConcreteTypeMember) {
			ValueType vt = ((ConcreteTypeMember)dt).getResultType(View.from(path, ctx));
			return vt.isSubtypeOf(t, ctx);
		} else {
			return false; // nominal equality was the best we can do 
		}
	}

	@Override
	public <T> T acceptVisitor(ASTVisitor <T> emitILVisitor,
			Environment env, OIREnvironment oirenv) {
		return emitILVisitor.visit(env, oirenv, this);
	}

	@Override
	public ValueType adapt(View v) {
		return new NominalType(path.adapt(v), typeMember);
	}
	
}
