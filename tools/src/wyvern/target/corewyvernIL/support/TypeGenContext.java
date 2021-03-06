package wyvern.target.corewyvernIL.support;

import java.util.LinkedList;
import java.util.List;

import wyvern.target.corewyvernIL.decl.Declaration;
import wyvern.target.corewyvernIL.decl.TypeDeclaration;
import wyvern.target.corewyvernIL.decltype.ConcreteTypeMember;
import wyvern.target.corewyvernIL.decltype.DeclType;
import wyvern.target.corewyvernIL.decltype.ValDeclType;
import wyvern.target.corewyvernIL.expression.Expression;
import wyvern.target.corewyvernIL.type.NominalType;
import wyvern.target.corewyvernIL.type.ValueType;
import wyvern.tools.errors.HasLocation;

public class TypeGenContext extends GenContext {

	String typeName;
	String objName;
	
	public TypeGenContext(String typeName, String newName, GenContext genContext) {
		super(genContext);
		this.typeName = typeName;
		this.objName = newName;
	}

	@Override
	public String getContainerForTypeAbbrev(String typeName) {
		if(this.typeName.equals(typeName)) return objName;
		else return getNext().getContainerForTypeAbbrev(typeName);
	}

	@Override
	public String toString() {
		return "GenContext[" + endToString();
	}
	
	@Override
	public String endToString() {
		return typeName + " : " + objName  + ", " + getNext().endToString();
	}

	@Override
	public ValueType lookup(String varName) {
		return getNext().lookup(varName);
	}

	@Override
	public CallableExprGenerator getCallableExprRec(String varName, GenContext origCtx) {
		return getNext().getCallableExprRec(varName, origCtx);
	}

}
