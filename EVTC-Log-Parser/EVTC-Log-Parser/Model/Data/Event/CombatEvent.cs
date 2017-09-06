namespace EVTC_Log_Parser.Model
{
    public class CombatEvent
    {
        #region Properties
        public int Time { get; set; }
        public int Damage { get; set; }
        public int SkillId { get; set; }
        public bool IsBuff { get; set; }
        public Result Result { get; set; }
        public bool IsNinety { get; set; }
        public bool IsMoving { get; set; }
        public bool IsFlanking { get; set; }
        #endregion
    }
}
