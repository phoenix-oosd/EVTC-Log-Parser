
public class BoonFactory {

	public Boon makeBoon(String boon) {
		if (boon.equals(BoonEnum.MGHT.getName()) || boon.equals(BoonEnum.STAB.getName())) {
			return new BoonIntensity(BoonEnum.MGHT.getMaxStacks(), "Intensity");
		}
		else if (boon.equals(BoonEnum.GOTL.getName())) {
			return new BoonIntensity(BoonEnum.GOTL.getMaxStacks(), "Intensity");
		}
		else if (boon.equals(BoonEnum.FURY.getName()) || boon.equals(BoonEnum.SWFT.getName()) || boon.equals(BoonEnum.ALAC.getName())) {
			return new BoonDuration(BoonEnum.FURY.getMaxStacks(), "Duration");
		}
		else if (boon.equals(BoonEnum.PROT.getName()) || boon.equals(BoonEnum.REGN.getName()) || boon.equals(BoonEnum.RSTC.getName()) || boon.equals(BoonEnum.RETL.getName()) || boon.equals(BoonEnum.QCKN.getName()) || boon.equals(BoonEnum.VIGR.getName())) {
			return new BoonDuration(BoonEnum.PROT.getMaxStacks(), "Duration");
		}
		else if (boon.equals(BoonEnum.SPOT.getName()) || boon.equals(BoonEnum.FRST.getName()) || boon.equals(BoonEnum.SUNS.getName()) || boon.equals(BoonEnum.GOFE.getName()) || boon.equals(BoonEnum.EALL.getName()) || boon.equals(BoonEnum.BOFS.getName()) || boon.equals(BoonEnum.BOFD.getName())) {
			return new BoonDuration(BoonEnum.SPOT.getMaxStacks(), "Duration");
		}
		else {
			return null;
		}

	}
}