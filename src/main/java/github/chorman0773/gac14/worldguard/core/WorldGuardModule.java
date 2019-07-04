package github.chorman0773.gac14.worldguard.core;

import github.chorman0773.gac14.Gac14Module;
import github.chorman0773.gac14.Version;
import net.minecraft.util.ResourceLocation;

public final class WorldGuardModule extends Gac14Module<WorldGuardModule> {

	public WorldGuardModule() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ResourceLocation getModuleName() {
		// TODO Auto-generated method stub
		return new ResourceLocation("gac14:worldguard/core");
	}

	@Override
	public Version getModuleVersion() {
		// TODO Auto-generated method stub
		return new Version("1.0");
	}

}
