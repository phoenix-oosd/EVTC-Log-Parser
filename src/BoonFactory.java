
public class BoonFactory {

	public Boon makeBoon(String boon) {
		if (boon == BoonEnum.MGHT.getName() || boon == BoonEnum.STAB.getName()) {
			return new BoonIntensity(BoonEnum.MGHT.getMaxStacks());
		}
		else if (boon == BoonEnum.GOTL.getName()) {
			return new BoonIntensity(BoonEnum.GOTL.getMaxStacks());
		}
		else if (boon == BoonEnum.FURY.getName() || boon == BoonEnum.SWFT.getName() || boon == BoonEnum.ALAC.getName()) {
			return new BoonDuration(BoonEnum.FURY.getMaxStacks());
		}
		else if (boon == BoonEnum.PROT.getName() || boon == BoonEnum.REGN.getName() || boon == BoonEnum.RSTC.getName() || boon == BoonEnum.RETL.getName() || boon == BoonEnum.QCKN.getName() || boon == BoonEnum.VIGR.getName()) {
			return new BoonDuration(BoonEnum.PROT.getMaxStacks());
		}
		else if (boon == BoonEnum.SPOT.getName() || boon == BoonEnum.FRST.getName() || boon == BoonEnum.SUNS.getName() || boon == BoonEnum.GOFE.getName() || boon == BoonEnum.EALL.getName() || boon == BoonEnum.BOFS.getName() || boon == BoonEnum.BOFD.getName()) {
			return new BoonDuration(BoonEnum.SPOT.getMaxStacks());
		}
		else {
			return null;
		}

	}
}