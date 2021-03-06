package practicalities.gui;

import practicalities.Logger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ItemHelper {

	private static ItemHelper instance;
	public static ItemHelper instance() {
		if(instance == null)
			instance = new ItemHelper();
		return instance;
	}
	
	public ItemStack parseItemStack(String str) {
		String[] parts = str.split("\\|");
		
		Item item = null;
		int meta = 0;
		int quantity = 1;
		NBTTagCompound tag = null;
		
		item = parseItem(parts[0]);
		
		if(parts.length > 1)
			quantity = parseNum(parts[1]);
		if(parts.length > 2)
			meta = parseNum(parts[2]);
		if(parts.length > 3)
			tag = parseTag(parts[3]);
		
		if(item == null)
			return null;
		
		ItemStack stack = new ItemStack(item, quantity, meta);
		stack.setTagCompound(tag);
		
		return stack;
	}
	
	public Item parseItem(String str) {
		ResourceLocation resourcelocation = new ResourceLocation(str);
        Item item = (Item)Item.itemRegistry.getObject(resourcelocation);
		if(item == null) {
			Logger.warning("ITEM NOT FOUND, RETURNING NULL! '%s'", str);
		}
		return item;
	}
	
	public int parseNum(String str) {
		int meta = 0;
		try {
			meta = Integer.parseInt(str);
		} catch (NumberFormatException numberformatexception) {
			;
		}
		return meta;
	}
	
	public NBTTagCompound parseTag(String str) {
		NBTBase nbtbase;
		try {
			nbtbase = JsonToNBT.getTagFromJson(str);
		} catch (NBTException e) {
			
			Logger.warning("ERROR LOADING NBT TAG! Error: '%s' Json: '%s'", e.getMessage(), str);
			return null;
		}

		if (!(nbtbase instanceof NBTTagCompound)) {
			Logger.warning("INVALID NBT TAG! Json: '%s'", str);
			return null;
		}
		
		return (NBTTagCompound)nbtbase;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean isPlayerHolding(Class c, EntityPlayer p) {
		return p.getHeldItem() != null && p.getHeldItem().getItem() != null &&
				c.isAssignableFrom(p.getHeldItem().getItem().getClass());
	}
}
