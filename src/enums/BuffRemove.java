package enums;

public enum BuffRemove
{
	// Constants
	NONE(0),
	ALL(1),
	SINGLE(2),
	MANUAL(3);

	// Fields
	private int ID;

	// Constructors
	private BuffRemove(int ID)
	{
		this.ID = ID;
	}

	// Public Methods
	public static BuffRemove getEnum(int ID)
	{
		for (BuffRemove b : values())
		{
			if (b.getID() == ID)
			{
				return b;
			}
		}
		return null;
	}

	// Getters
	public int getID()
	{
		return ID;
	}

}
