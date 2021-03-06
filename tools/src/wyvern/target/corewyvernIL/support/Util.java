package wyvern.target.corewyvernIL.support;

import java.util.LinkedList;

import wyvern.target.corewyvernIL.decltype.DeclType;
import wyvern.target.corewyvernIL.type.NominalType;
import wyvern.target.corewyvernIL.type.StructuralType;
import wyvern.target.corewyvernIL.type.ValueType;

public class Util {
	public static ValueType intType() { return theIntType; }
	public static ValueType unitType() { return theUnitType; }
	private static ValueType theIntType = new NominalType("system","Int");
	private static ValueType theStringType = new NominalType("system","String");
	private static ValueType theUnitType = new StructuralType("unitSelf", new LinkedList<DeclType>());
	public static ValueType stringType() {
		return theStringType;
	}
}
