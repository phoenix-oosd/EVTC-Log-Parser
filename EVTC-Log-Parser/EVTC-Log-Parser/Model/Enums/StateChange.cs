namespace EVTC_Log_Parser.Model
{
    public enum StateChange
    {
        None,
        EnterCombat,
        ExitCombat,
        ChangeUp,
        ChangeDead,
        ChangeDown,
        Spawn,
        Despawn,
        HealthUpdate,
        LogStart,
        LogEnd,
        WeaponSwap,
        MaxHealthUpdate,
        PointOfView,
        Language,
        GWBuild,
        ShardID,
        Reward
    }
}
