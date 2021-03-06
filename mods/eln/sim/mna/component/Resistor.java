package mods.eln.sim.mna.component;

import java.util.ArrayList;

import mods.eln.sim.mna.RootSystem;
import mods.eln.sim.mna.SubSystem;
import mods.eln.sim.mna.state.State;

import org.apache.commons.math3.linear.RealMatrix;

public class Resistor extends Bipole {


	public Resistor() {
	}
	
	public Resistor(State aPin,State bPin) {
		super(aPin, bPin);
	}
	
	//public SubSystem interSystemA, interSystemB;
	
	
/*	public Line line = null;
	public boolean lineReversDir;
	public boolean isInLine() {
		
		return line != null;
	}*/
	
	private double r = 1000000000.0, rInv = 1/1000000000.0;


	//public boolean usedAsInterSystem = false;

	
	public double getRInv(){
		return rInv;
	}
	
	public double getR(){
		return r;
	}
	public double getI(){
		return getCurrent();
	}	
	public double getP(){
		return getU()*getCurrent();
	}
	public double getU(){
		return (aPin == null ? 0 : aPin.state) - (bPin == null ? 0 : bPin.state);
	}	
	public Resistor setR(double r) {
		if(this.r != r){
			this.r = r;
			this.rInv = 1 / r;
			dirty();
		}
		return this;
	}

	public void highImpedance(){
		setR(1000000000000.0);
	}
	
	/*@Override
	public void dirty() {
		if(line != null){
			line.recalculateR();
		}
		if(usedAsInterSystem){
			aPin.getSubSystem().breakSystem();
			if(aPin.getSubSystem() != bPin.getSubSystem()){
				bPin.getSubSystem().breakSystem();
			}
		}
		
		super.dirty();
	}*/
	
	boolean canBridge() {
		return false;
	}

	@Override
	public void applyTo(SubSystem s) {
		s.addToA(aPin, aPin, rInv);
		s.addToA(aPin, bPin, -rInv);
		s.addToA(bPin, bPin, rInv);
		s.addToA(bPin, aPin, -rInv);
	}

	
	@Override
	public double getCurrent() {
		return getU()*rInv;
		/*if(line == null)
			return getU()*rInv;
		else if(lineReversDir)
			return -line.getCurrent();
		else
			return line.getCurrent();*/
	}

}
