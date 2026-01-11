package com.unittest.dependencia;

public class Dependency {
private final SubDependency subDependency;
@SuppressWarnings("unused") //Se uso para pasar por alto el hecho de que no se usa la variable
	private double ultimoResultado;
	public double suma(double operando1, double operando2) {
		return ultimoResultado = operando1 + operando2;
	}
	public Dependency(SubDependency subDependency) {
		super();
		this.subDependency = subDependency;
	}
	
	public String getClassName() {
		return this.getClass().getSimpleName();
	}
	
	public String getSubdependencyClassName() {
		return subDependency.getClassName();
	}
	
	public int addTwo(int i) {
		return i + 2;
	}
	public String getClassNameUpperCase() {
		return getClassName().toUpperCase();
	}
	
}
