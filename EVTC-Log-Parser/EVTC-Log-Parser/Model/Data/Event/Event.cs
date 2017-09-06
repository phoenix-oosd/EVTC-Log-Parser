namespace EVTC_Log_Parser.Model
{
    public class Event
    {
        #region Properties
        public int Time { get; set; }
        public string SrcAgent { get; set; }
        public string DstAgent { get; set; }
        public int Value { get; set; }
        public int BuffDmg { get; set; }
        public int OverstackValue { get; set; }
        public int SkillId { get; set; }
        public int SrcInstid { get; set; }
        public int DstInstid { get; set; }
        public int SrcMasterInstid { get; set; }
        public IFF IFF { get; set; }
        public bool IsBuff { get; set; }
        public Result Result { get; set; }
        public Activation Activation { get; set; }
        public BuffRemove BuffRemove { get; set; }
        public bool IsNinety { get; set; }
        public bool IsFifty { get; set; }
        public bool IsMoving { get; set; }
        public StateChange StateChange { get; set; }
        public bool IsFlanking { get; set; }
        public bool IsShield { get; set; }
        #endregion
    }
}
