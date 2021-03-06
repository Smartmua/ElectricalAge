package mods.eln.item;

import java.util.List;

import mods.eln.generic.GenericItemUsingDamageDescriptor;
import mods.eln.misc.Utils;
import mods.eln.sim.mna.component.Resistor;
import mods.eln.sixnode.lampsocket.LampSocketType;
import mods.eln.wiki.Data;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;



public class LampDescriptor  extends GenericItemUsingDamageDescriptorUpgrade
{


	public enum Type {Incandescent,eco}
	public double nominalP,nominalLight,nominalLife;
	public String name,description;
	public Type type;
	public LampSocketType socket;
	
	public int textureId;
	
	
	public double nominalU,minimalU;
	public double stableU,stableUNormalised,stableTime,vegetableGrowRate;
	
	public LampDescriptor(	
			String name,String iconName,
			Type type,LampSocketType socket,
			double nominalU,double nominalP,double nominalLight,double nominalLife,
			double vegetableGrowRate
			)
	{
		super( name);
		changeDefaultIcon(iconName);
		this.type = type;
		this.socket = socket;
		this.nominalU = nominalU;
		this.nominalP = nominalP;
		this.nominalLight = nominalLight;
		this.nominalLife = nominalLife;
		//this.description = description;
		this.vegetableGrowRate = vegetableGrowRate;

		switch (type) {
		case Incandescent:
			minimalU = nominalU*0.5; 
			break;
		case eco:
			stableUNormalised = 0.75;
			minimalU = nominalU * 0.5;
			stableU = nominalU * stableUNormalised;
			stableTime = 4;
			break;

		default:
			break;
		}
		
		
	}
	
	
	@Override
	public void setParent(Item item, int damage) {
		
		super.setParent(item, damage);
		Data.addLight(newItemStack());
	}
	
	public double getR()
	{
		return nominalU*nominalU/nominalP;
	}
	
	
	double getLifeInTag(ItemStack stack)
	{
		if(stack.hasTagCompound() == false)
			stack.setTagCompound(getDefaultNBT());
		return stack.getTagCompound().getDouble("life");
	}
	
	@Override
	public NBTTagCompound getDefaultNBT() {
		
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setDouble("life", 1.0);
		return nbt;
	}
	
	@Override
	public ItemStack newItemStack(int size) {
		

		return super.newItemStack(size);
	}
	
	public void applyTo(Resistor resistor)
	{
		resistor.setR(getR());
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer,
			List list, boolean par4) {
		
		super.addInformation(itemStack, entityPlayer, list, par4);
		
		//list.add("Socket : " + socket);
		list.add("Techn  : " + type);
		list.add("Light   : " + (int)(nominalLight*15) + " blocks");
		list.add("Power : " + (int)nominalP + " W");
		//list.add(Utils.plotOhm("Resistance", this.getR()));
		list.add(Utils.plotTime("Life    : ",getLifeInTag(itemStack)*nominalLife));
		
	}
}
