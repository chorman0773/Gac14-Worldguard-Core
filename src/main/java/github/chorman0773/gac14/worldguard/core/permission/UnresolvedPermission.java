package github.chorman0773.gac14.worldguard.core.permission;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event.Result;

public class UnresolvedPermission implements IWorldguardPermission {

	private ResourceLocation name;

	public UnresolvedPermission(ResourceLocation res) {
		this.name = res;
	}

	@Override
	public ResourceLocation getPermissionName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public Result getDefaultResult() {
		// TODO Auto-generated method stub
		return Result.DEFAULT;
	}

}
