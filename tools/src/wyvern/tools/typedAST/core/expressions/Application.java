package wyvern.tools.typedAST.core.expressions;

import wyvern.tools.errors.FileLocation;
import wyvern.tools.typedAST.abs.CachingTypedAST;
import wyvern.tools.typedAST.interfaces.*;
import wyvern.tools.types.ApplyableType;
import wyvern.tools.types.Environment;
import wyvern.tools.types.Type;
import wyvern.tools.types.extensions.Arrow;
import wyvern.tools.util.TreeWriter;

import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;

import static wyvern.tools.errors.ErrorMessage.TYPE_CANNOT_BE_APPLIED;
import static wyvern.tools.errors.ErrorMessage.VALUE_CANNOT_BE_APPLIED;
import static wyvern.tools.errors.ToolError.reportError;
import static wyvern.tools.errors.ToolError.reportEvalError;

public class Application extends CachingTypedAST implements CoreAST {
	private TypedAST function;
	private TypedAST argument;

	public Application(TypedAST function, TypedAST argument, FileLocation location) {
		this.function = function;
		this.argument = argument;
		this.location = location;
	}

	@Override
	public void writeArgsToTree(TreeWriter writer) {
		writer.writeArgs(function, argument);
	}

	@Override
	protected Type doTypecheck(Environment env, Optional<Type> expected) {
		Type fnType = function.typecheck(env, Optional.empty());
		
		if (this.argument != null)
			this.argument.typecheck(env, Optional.of(((Arrow)fnType).getArgument()));
		
		if (!(fnType instanceof ApplyableType))
			reportError(TYPE_CANNOT_BE_APPLIED, this, fnType.toString());
		
		return ((ApplyableType)fnType).checkApplication(this, env);
	}

	public TypedAST getArgument() {
		return argument;
	}

	public TypedAST getFunction() {
		return function;
	}

	@Override
	public Value evaluate(Environment env) {
		TypedAST lhs = function.evaluate(env);
		if (!(lhs instanceof ApplyableValue))
			reportEvalError(VALUE_CANNOT_BE_APPLIED, lhs.toString(), this);
		ApplyableValue fnValue = (ApplyableValue) lhs;
		return fnValue.evaluateApplication(this, env);
	}

	@Override
	public void accept(CoreASTVisitor visitor) {
		visitor.visit(this);
	}
	@Override
	public Map<String, TypedAST> getChildren() {
		Hashtable<String, TypedAST> children = new Hashtable<>();
		children.put("function", function);
		children.put("argument", argument);
		return children;
	}

	@Override
	public TypedAST cloneWithChildren(Map<String, TypedAST> nc) {
		return new Application(nc.get("function"), nc.get("argument"), location);
	}

	private FileLocation location = FileLocation.UNKNOWN;
	public FileLocation getLocation() {
		return this.location;
	}
}