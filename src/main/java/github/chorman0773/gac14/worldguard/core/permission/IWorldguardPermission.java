package github.chorman0773.gac14.worldguard.core.permission;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IWorldguardPermission extends IForgeRegistryEntry<IWorldguardPermission> {
	ResourceLocation getPermissionName();
	Result getDefaultResult();
	@Override
	default IWorldguardPermission setRegistryName(ResourceLocation name) {
		throw new UnsupportedOperationException("Cannot modify the name of a Principal Provider");
	}
	@Override
	default ResourceLocation getRegistryName() {
		// TODO Auto-generated method stub
		return getPermissionName();
	}
	@Override
	default Class<IWorldguardPermission> getRegistryType() {
		// TODO Auto-generated method stub
		return IWorldguardPermission.class;
	}
}
